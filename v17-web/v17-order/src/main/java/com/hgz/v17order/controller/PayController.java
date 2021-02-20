package com.hgz.v17order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.v17order.config.AlipayClientConfig;
import com.hgz.v17order.pojo.AlipayBizContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/18
 */
@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    private AlipayClient alipayClient;

    @RequestMapping("generate")
    public void generate(String orderNo,
                         HttpServletResponse response) throws IOException {
        //1.创建AlipayClient对象
       /* AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                "2016101700706609",
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCFwnLElz9BtRIoWsI9s+/ZhwF4zdkXptmYfUufzXkbXH2MM3BTGrUO2dJuDg6L8vjBvZ6XmdqUgDBaRj47wy2dDJ9ya8o9trGHqCuMblyKvA0h0vWfmT9zR4mgdXD2aPXbX69dLMrKYvhf7LF3CRv10pcMwgk41GMulHQpsBJDUbJQVN9TzSMMPSMLNGA/k/+Isl1gLMko5+8lxBBkxymsB38Pbj9f5Iz2tAZComFhLiUtAe2kPl7pSZPswoEqig1UzY7Vz0lu0mXqxfz0UTkvt4txFpX649CKF0Yg5tXVwIKDFUtgeESgqvr81r95AMvTB2h3dGJUfNlz2b34LtS5AgMBAAECggEAYREeBioYHo/PUHekc/CLQxlk6QzA2Rgc2js60QnkNAXnmgGOJtC/yboqqfELf2XgPYBkgXGGHLP14t0MtoPKKykI4Jj2V9indmGE0NEZ2Rfkk8IquxoT4I1ug4IWasr6d6s1cHFb7Ijr43i5xD9ZCgg702dQF+yQ2TGkJsh+SFkHZ5yu2w+LZf+H+wxBAwpp1E8gvXOeV9hVYzo4m26o8zwrBBDd95cKydWfKzpjiil/n1/ONWXrf8alft1Y49/AE6ZfJTHw12XSSsU4bnutqIpoJppsKPnyY6INRHnjEtNsQFrIJuBaE1fmNuxkG4AU30NvT9Qzt3/xpIqlVrN9LQKBgQDRS3t9l62Sd5dAwQXxEphWojjngVK0dLO3DfBPBLS+t35YCcfZDC36B4P9v6JhdJGZx+oJuQmYChdn7+u8GSjgavqdwRGPZeqbc+cjt/g9rmF3HbU6qyb3oWfmPHsA0UkaBRc3J5puzGfJyVHue4iUrvCAH77OzJuuxVFy4Di9RwKBgQCjm9B4gsR9rGLu1XyWLuWjtRfOPfP6g6BFnfPDJ0mYPb20Bz/TPAdjnAmZoeptPudCPlW/BMdylZzMSindfmjuq4guaNHeb89cLbiWLbYQ9FbS6fzUOm/6p+f9xD9Z5gNqACMp5oSYI39LHAvt91vupix1CVgJYhVc48nDy5zd/wKBgQCnlYUgH1+AM/oMg4I6ceVBGUvvMwICNJ/iJ2sQGniPbWXhIJPBG5uE5Jevis5+slQTipu/hUZFU29Cbj2Xs52jdIWP45Qn+jdMmhWt6BUBbqvkMQl4HKtrBPaoyCzFjs614vWf2Do0Fb/VNzo/dvL5+sngDYs/E15lJuH81cUY6QKBgQCG9QSmI9hJgmWpdVjr2Yt72unkKp6Syvjt9DkbhdDjUCqn0kMHz8CYniIdJL7nlrfUoO7CPwL2tgs9SP/3wUp1dfFDtxtHTRvTgbN9zf8dRTBJktIHjpzIOrDXV4aadW3jhnF7IrfFd6KNJnmAtkaTOWf6stZ4BvxwP0YzyEWBqQKBgEEbBF2hbF2L17wfFFDtqRcWueRPZnwzbMKe4MOTixmHOV1JWi4IRW3lTRIXvZq8QzF1QeM8qAhyeJmiPnGNhhhlYEBGRJlMvBilm/FUWpVrRm1wBkKnt2lUkW0AUQaU9NMqoVjSI3y1Ot887qSpKKqGjPgHcN8CxbXLBeYNDrXG",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr98iOFPcthrEKEr8OlYzj7q1Iz2hkJN/1MmlAIaGJUZrKGUKhr/Ut50+2wY5CB1jur+Fs4UAcxt3A76P26hDOC9+aFX/3SQoAhMFyJq3IQI3qBuWqcXyevyv7su1YMENPdug34PCDiiJbMmIgDWbOyPsMyuB0gMClOymtg+Cbk4N3sFzZBWT7TLBIB3QQ7uzzBaEzSnrJTITgAvDpSaWsgmdnE17hdV11jKjFkd0fdwTxav36czON0bMs1Q/Sbb0E+WeTlI5B6kTD/kVf8Pbzu3URwhdroSF+kr3EP5rd6tsHKZn4Kvv9mc79ks80UhFTmj25TS3aEPz6v5KCPK+RwIDAQAB",
                "RSA2"); //获得初始化的AlipayClient*/
        //2.创建交易请求对象，用于封装业务相关的参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");  //TODO 设置为自己的地址,给用户看的
        alipayRequest.setNotifyUrl("http://nskmcv.natappfree.cc/pay/notifyPayResult");//  TODO 自己对结果验签回调
        /*
        关键参数：  out_trade_no 订单编号
        * */
        AlipayBizContent bizContent = new AlipayBizContent(orderNo,"FAST_INSTANT_TRADE_PAY",
                "9998.9","笔记本电脑","游戏本");
        ObjectMapper objectMapper = new ObjectMapper();
        //对象-->json
        String json = objectMapper.writeValueAsString(bizContent);
        alipayRequest.setBizContent(json);

       /* alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orderNo+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":9988.88," +
                "    \"subject\":\"IphoneX 128G\"," +
                "    \"body\":\"IphoneX 128G\"" +
                "  }");//填充业务参数*/

        //3.通过client发送请求
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();

    }

    //对异步返回结果进行验签
    //说明：因为此时我们是在内网做实验，所以需要通过内网穿透工具来进行映射
    @RequestMapping("notifyPayResult")
    @ResponseBody
    public void notifyPayResult(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        System.out.println("支付宝系统调用回调了...");
        //将异步通知中收到的所有参数都存放到 paramsMap 中
        Map<String, String> paramsMap = new HashMap<>();
        //所有请求参数都会封装到request中
        Map<String, String[]> parameterMap = request.getParameterMap();
        //value不用，由String[]-->String
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < values.length - 1; i++) {
                value.append(values[i]+",");
            }
            value.append(values[values.length-1]);
            paramsMap.put(entry.getKey(),value.toString());
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr98iOFPcthrEKEr8OlYzj7q1Iz2hkJN/1MmlAIaGJUZrKGUKhr/Ut50+2wY5CB1jur+Fs4UAcxt3A76P26hDOC9+aFX/3SQoAhMFyJq3IQI3qBuWqcXyevyv7su1YMENPdug34PCDiiJbMmIgDWbOyPsMyuB0gMClOymtg+Cbk4N3sFzZBWT7TLBIB3QQ7uzzBaEzSnrJTITgAvDpSaWsgmdnE17hdV11jKjFkd0fdwTxav36czON0bMs1Q/Sbb0E+WeTlI5B6kTD/kVf8Pbzu3URwhdroSF+kr3EP5rd6tsHKZn4Kvv9mc79ks80UhFTmj25TS3aEPz6v5KCPK+RwIDAQAB",
                "utf-8",
                "RSA2"); //调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验
            //  校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("支付宝回调...验签成功");
            //重要的是核对业务数据是否正确
            String trade_status = request.getParameter("trade_status");
            //判断支付状态是否成功
            if("TRADE_SUCCESS".equals(trade_status)){
                //支付成功后进一步核对业务数据
                String out_trade_no= request.getParameter("out_trade_no");
                String total_amount = request.getParameter("total_amount");
                String receipt_amount = request.getParameter("receipt_amount");
                //比较以上的订单编号，金额，如果匹配成功，则更新订单状态为已支付

                //相关的流水支付数据
                String trade_no = request.getParameter("trade_no");
                String app_id = request.getParameter("app_id");
                String buyer_id = request.getParameter("buyer_id");
                String seller_id = request.getParameter("seller_id");

                response.getWriter().write("success");
                response.getWriter().flush();
                response.getWriter().close();
            }

        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("验签失败,不是支付宝发来的");
            response.getWriter().write("failure");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
