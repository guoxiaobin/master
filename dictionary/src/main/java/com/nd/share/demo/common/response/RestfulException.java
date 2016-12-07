package com.nd.share.demo.common.response;

import com.nd.gaea.WafException;
import com.nd.gaea.client.exception.ErrorMessage;
import com.nd.share.demo.util.I18nUtil;
import org.springframework.http.HttpStatus;


/**
 * 错误信息格式
 *
 * @author 王其彬(163931)
 * @version 2015年12月16日 新开发
 * @note 错误信息
 */
@SuppressWarnings("serial")
public class RestfulException extends WafException {
    public static final String APP_FLAG = "DIC/";

    public RestfulException(ErrorCode errorCode) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage("MESSAGE." + errorCode.getReasonPhrase()),
                HttpStatus.valueOf(errorCode.getValue()));
    }

    public RestfulException(ErrorCode errorCode, String message) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage(message), HttpStatus.valueOf(errorCode
                .getValue()));
    }

    public RestfulException(ErrorCode errorCode, Object... args) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage("MESSAGE." + errorCode.getReasonPhrase(),
                args), HttpStatus.valueOf(errorCode.getValue()));
    }

    public RestfulException(ErrorCode errorCode, String message, Object... args) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage(message,
                args), HttpStatus.valueOf(errorCode.getValue()));
    }

    public RestfulException(ErrorCode errorCode, Throwable cause) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage("MESSAGE." + errorCode.getReasonPhrase()),
                HttpStatus.valueOf(errorCode.getValue()), cause);
    }

    public RestfulException(ErrorCode errorCode, Throwable cause, String message) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage(message), HttpStatus.valueOf(errorCode
                .getValue()), cause);
    }

    public RestfulException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage("MESSAGE." + errorCode.getReasonPhrase(),
                args), HttpStatus.valueOf(errorCode.getValue()), cause);
    }

    public RestfulException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
        super(APP_FLAG + errorCode.getReasonPhrase(), I18nUtil.getMessage(message,
                args), HttpStatus.valueOf(errorCode.getValue()), cause);
    }

    public RestfulException(ErrorCode errorCode, String detail, Throwable cause, Object... args) {
        super(
                new ErrorMessage(APP_FLAG + errorCode.getReasonPhrase(),
                        I18nUtil.getMessage("MESSAGE." + errorCode.getReasonPhrase(), args),
                        detail),
                HttpStatus.valueOf(errorCode.getValue()), cause);
    }

    /**
     * 由于函数重载冲突，所以写成静态方法
     *
     * @param errorCode errorCode
     * @param detail    detail
     */
    public static void throwWithDetail(ErrorCode errorCode, String detail) {
        throw new RestfulException(errorCode, detail, null, null);
    }

    /**
     * 快捷抛出
     *
     * @param detail detail
     */
    public static void throwInternalServiceErrorWithDetail(String detail) {
        throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, detail, null, null);
    }
}
