package com.hgz.api;

import com.hgz.commons.base.ResultBean;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 */
public interface IEmailService {
    /**
     * 发生日祝福
     * @param to   收件人
     * @param username  收件人姓名
     * @return
     */
    public ResultBean sendBirthday(String to, String username);

    /**
     * 发激活邮件
     * @param to
     * @param username
     * @return
     */
    public ResultBean sendActivate(String to,String username);
}
