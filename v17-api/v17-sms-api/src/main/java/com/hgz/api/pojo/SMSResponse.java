package com.hgz.api.pojo;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 */
public class SMSResponse {
    //{"Message":"OK","RequestId":"311FA96A-9772-4C5F-95E0-80D8404FC577","BizId":"900222073557056962^0","Code":"OK"}
    private String Message;   //需要一一对应，大写
    private String RequestId;
    private String BizId;
    private String Code;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getBizId() {
        return BizId;
    }

    public void setBizId(String bizId) {
        BizId = bizId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    @Override
    public String toString() {
        return "SMSResponse{" +
                "Message='" + Message + '\'' +
                ", RequestId='" + RequestId + '\'' +
                ", BizId='" + BizId + '\'' +
                ", Code='" + Code + '\'' +
                '}';
    }
}
