package com.example.main.entity;

public class ResponseResult {

    /**
     * 表明该请求被成功地完成，所请求的资源发送到客户端
     */
    public static final Integer OK = 200;
    /**
     * 请求要求身份验证，常见对于需要登录而用户未登录的情况。
     */
    public static final Integer UNAUTHORIZED = 401;
    /**
     * 服务器拒绝请求，常见于机密信息或复制其它登录用户链接访问服务器的情况。
     */
    public static final Integer FORBIDDEN = 403;
    /**
     * 服务器无法取得所请求的网页，请求资源不存在。
     */
    public static final Integer NOT_FOUND = 404;
    /**
     * 服务器内部错误。
     */
    public static final Integer SERVER_ERROR = 500;

    private Integer code;
    private String msg = "";
    private Object data = new int[0];

    public ResponseResult() {
    }

    public ResponseResult(Integer code) {
        this.code = code;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
