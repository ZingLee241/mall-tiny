package com.macro.mall.tiny.common.result;

import lombok.Getter;

public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    BIZ_ERROR(1000, "通用业务异常"),
    FILE_OUT_MAX(9000, "文件超出最大限制"),
    FILE_FORMAT_ERROR(9001, "文件格式不正确"),
    PARAM_ERROR(9050, "参数错误"),
    JSON_FORMAT_ERROR(9051, "Json解析异常"),
    SQL_ERROR(9052, "Sql解析异常"),
    NETWORK_TIMEOUT(9510, "网络超时"),
    UNKNOWN_INTERFACE(9520, "未知的接口"),
    REQ_MODE_NOT_SUPPORTED(9530, "请求方式不支持"),
    SYS_ERROR(9999, "系统异常");

    /**
     * 状态码
     */
    @Getter
    private final int code;

    /**
     * 状态信息
     */
    @Getter
    private final String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
