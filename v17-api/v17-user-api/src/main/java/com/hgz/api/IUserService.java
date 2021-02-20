package com.hgz.api;

import com.hgz.commons.base.IBaseService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;

/**
 * @Author: DJA
 * @Date: 2019/11/11
 */
public interface IUserService extends IBaseService<TUser> {
    //验证用户名唯一性
    public ResultBean checkUserNameIsExists(String username);

    public ResultBean checkPhoneIsExists(String phone);

    public ResultBean checkEmailIsExists(String email);

    //生成验证码
    public ResultBean generateCode(String identification);

    public ResultBean checkLogin(TUser user);

    ResultBean checkIsLogin(String uuid);

//    ResultBean logout(String uuid);   //不需要，cookie.setMaxAge(0)

    //注册  register使用现成  insert

    //激活用户--update改变用户的状态值使用现成的
}
