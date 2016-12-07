package com.nd.share.demo.common.response;


/**
 * 错误码
 *
 * @author 王其彬(163931)
 * @version 2015年12月16日 新开发
 * @note 错误码
 */
public enum ErrorCode {
    DB_SHARD_NOT_EXIST(404, "DB_SHARD_NOT_EXIST"),
    // 无效请求
    BAD_REQUEST(400, "BAD_REQUEST"),
    // 缺少参数
    REQUIRE_ARGUMENT(400, "REQUIRE_ARGUMENT"),
    // 无效参数(格式不对,长度过长或过短等)
    INVALID_ARGUMENT(400, "INVALID_ARGUMENT"),
    //参数越界
    OUT_OF_RANGE_ARGUMENT(400, "OUT_OF_RANGE_ARGUMENT"),
    //参数不在列举范围内
    OUT_OF_ENUM(400, "OUT_OF_ENUM"),
    //类型转换错误
    CLASS_CAST_ARGUMENT(400, "CLASS_CAST_ARGUMENT"),
    // 未授权(默认)
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    // 授权受限（无权限或IP地址受限等）
    AUTH_DENIED(403, "AUTH_DENIED"),
    // 不允许访问
    ACCESS_DENIED(403, "ACCESS_DENIED"),
    // 不允许访问
    OPTION_DENIED(403, "OPTION_DENIED"),
    // 数据不允许操作
    DATA_PERMISSION_DENIED(403, "DATA_PERMISSION_DENIED"),
    // 系统错误
    SERVICE_ERROR_SYSTEM(400, "SERVICE_ERROR_SYSTEM"),
    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
    // 资源未找到
    NOT_FOUND(404, "NOT_FOUND"),
    // 使用APP本地缓存
    USE_APP_CACHE(405, "USE_APP_CACHE"),
    // 无权限
    HAS_NO_ROLE(400, "HAS_NO_ROLE"),
    //不能修改
    CAN_NOT_MODIFY(400, "CAN_NOT_MODIFY");

    private final int value;

    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public int getValue() {
        return this.value;
    }

}
