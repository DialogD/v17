package com.hgz.v17userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IUserService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.constant.MQConstant;
import com.hgz.commons.utils.CodeUtils;
import com.hgz.entity.TUser;
import com.hgz.mapper.TUserMapper;
import com.hgz.v17userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/11
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private TUserMapper userMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResultBean checkUserNameIsExists(String username) {
        int count = userMapper.checkUserNameIsExists(username);
        if (count>0){
            return new ResultBean("200","success");
        }
        return new ResultBean("500","fail");
    }

    @Override
    public ResultBean checkPhoneIsExists(String phone) {
        int count = userMapper.checkPhoneIsExists(phone);
        if (count>0){
            return new ResultBean("200","success");
        }
        return new ResultBean("500","fail");
    }

    @Override
    public ResultBean checkEmailIsExists(String email) {
        int count = userMapper.checkEmailIsExists(email);
        if (count>0){
            return new ResultBean("200","success");
        }
        return new ResultBean("500","fail");
    }

    //生成验证码   identification--手机或邮箱
    @Override
    public ResultBean generateCode(String identification) {
        //1.生成验证码6位数数字
        String code = CodeUtils.generateCode(6);
        //2.保存一个凭证-验证码的关系存入redis,key-value
        redisTemplate.opsForValue().set(identification,code,5, TimeUnit.MINUTES);
        //3.发送短信验证码到相应的手机号(通过aliyun短信服务)
        //3.1 调通aliyun短信服务的demo
        //3.2 发送短信变成一个公共短信服务
        //3.3 异步发送短信（MQ消息中间件）
        Map<String,String> params = new HashMap<>();
        params.put("identification",identification);
        params.put("code",code);
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.SMS_EXCHANGE,"sms.code",params);

        //TODO 此处是不需要发送任何邮件，仅做测试使用
        Map<String,String> params2 = new HashMap<>();
        params2.put("to","1228516160@qq.com");
        params2.put("username","张老三");

        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.EMAIL_EXCHANGE,"email.birthday",params2);

        return new ResultBean("200","ok");
    }

    @Override
    public ResultBean checkLogin(TUser user) {
        //1.根据用户账号（手机号或者邮箱）去查询用户信息
        TUser currentUser = userMapper.selectByIdentification(user.getUsername());
        //2.根据查询出来的用户密码比较
        if (currentUser != null){
            //if (currentUser.getPassword().equals(user.getPassword())){
            if (passwordEncoder.matches(user.getPassword(),currentUser.getPassword())){
                // 将唯一凭证保存到redis中(有状态的)
                //1.生成唯一凭证uuid
                //String uuid = UUID.randomUUID().toString();
                //2.将凭证存储到redis中key-value,并设置有效期为30分钟
                //redisTemplate.opsForValue().set("user:token:"+uuid,user.getUsername(),30,TimeUnit.MINUTES);

                //使用JwtUtils生成token(无状态的)
                JwtUtils jwtUtils = new JwtUtils();
                jwtUtils.setSecretKey("java1907");
                //30分钟
                jwtUtils.setTtl(30*60*1000);
                String jwtToken = jwtUtils.createJwtToken(currentUser.getId().toString(), currentUser.getUsername());

                //TODO 构建map返回两个(存入cookie标识jwt和redis用户唯一令牌username)
                Map<String,String> params = new HashMap<>();
                params.put("jwtToken",jwtToken);
                params.put("username",currentUser.getUsername());
                return new ResultBean("200",params);
            }
        }
        return new ResultBean("404",null);
    }

    //判断是否登录状态--根据key到redis中查询
    @Override
    public ResultBean checkIsLogin(String uuid) {

        //1.拼接得到key
        //String token = new StringBuilder("user:token:").append(uuid).toString();
        //2.查询数据库redis
        //String username = (String) redisTemplate.opsForValue().get(token);
       /* if (username != null){
            //刷新凭证的有效期30分钟
            redisTemplate.expire(token,30,TimeUnit.MINUTES);
            return new ResultBean("200",username);
        }*/

        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey("java1907");
        //解析令牌会有异常//ExpiredJwtException-时间过期
        //一般框架的异常都是业务(逻辑)异常继承于RuntimeException
        try {
            Claims claims = jwtUtils.parseJwtToken(uuid);
            String username = claims.getSubject();
            return new ResultBean("200",username);
        }catch (RuntimeException e){
            //1.如果针对不同的异常区分对待，则需要写多个catch分别处理
            //2.如果一致对待，那么就可以统一处理
            return new ResultBean("404",null);
        }

    }


    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
