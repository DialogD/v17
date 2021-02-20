package com.hgz.mapper;

import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser> {

    int checkUserNameIsExists(String username);

    int checkPhoneIsExists(String phone);

    int checkEmailIsExists(String email);

    TUser selectByIdentification(String username);
}