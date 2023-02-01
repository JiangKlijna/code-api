package io.github.jiangklina.codeapi.api;

/**
 * CodeApi.ApiException
 * 接口的异常
 *
 * @author jiangklijna
 */
public class ApiException extends RuntimeException {

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(Api api, String msg) {
        super(api.toString() + ". " + msg);
    }
}
