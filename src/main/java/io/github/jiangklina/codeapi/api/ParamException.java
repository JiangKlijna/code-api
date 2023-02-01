package io.github.jiangklina.codeapi.api;

/**
 * CodeApi.ParamException
 * 接口参数的异常
 *
 * @author jiangklijna
 */
public class ParamException extends ApiException {

    public ParamException(String msg) {
        super(msg);
    }

    public ParamException(Param param, String msg) {
        super(param.toString() + ". " + msg);
    }
}
