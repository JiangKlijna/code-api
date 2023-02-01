package io.github.jiangklina.codeapi.router;

import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.executor.Executor;
import io.github.jiangklina.codeapi.filesystem.FileSystem;

import java.util.Map;

/**
 * CodeApi.Router
 * 路由的作用是,通过不同的Request&Response获取到[请求路径]以及[请求参数]
 * 将[请求路径]交给FileSystem从而获取到Api,最后将[请求参数]和Api一起交给Executor执行
 * 最后执行结果返回给Response
 *
 * @author jiangklijna
 */
public interface Router<Request, Response> {

    /**
     * 通过req获取本次请求参数
     *
     * @param req Request
     * @return Map<String, Object>
     */
    Map<String, Object> getParamsByReq(Request req);

    /**
     * 通过req获取本次请求要查找的接口路径
     * 示例: uri: /ctx/api/v2/code/api/**
     * path即为/**的路径,requestPrefix为/ctx/api/v2/code/api
     *
     * @param req           Request
     * @param requestPrefix 请求完整前缀
     * @return path
     */
    String getPathByReq(Request req, String requestPrefix);

    /**
     * 获取指定要执行的apiType类型 带.
     *
     * @param req Request
     * @return 默认可以返回null, 依次查找
     */
    default String getApiType(Request req) {
        return null;
    }

    /**
     * 将result交给res,返回给客户端
     *
     * @param result 返回结果
     * @param res    Response 响应对象
     */
    void printSuccessByRes(Object result, Response res);

    /**
     * 将result交给res,返回给客户端
     *
     * @param e   返回异常结果
     * @param res Response 响应对象
     */
    void printFailureByRes(Exception e, Response res);

    /**
     * 通过Request&Response获取到[请求路径]以及[请求参数]
     * 将[请求路径]交给FileSystem从而获取到Api,最后将[请求参数]和Api一起交给Executor执行
     * 最后执行结果返回给Response
     *
     * @param req           请求对象
     * @param res           响应对象
     * @param requestPrefix 请求完整前缀
     * @param fs            文件系统
     * @param exe           执行者
     */
    default void codeApi(Request req, Response res, String requestPrefix, FileSystem fs, Executor<?> exe) {
        final Api api;
        try {
            String path = getPathByReq(req, requestPrefix);
            String apiType = getApiType(req);
            api = fs.readApi(path, apiType);
        } catch (Exception e) {
            printFailureByRes(e, res);
            return;
        }
        try {
            Map<String, Object> params = getParamsByReq(req);
            Object result = exe.execApi(api, params);
            printSuccessByRes(result, res);
        } catch (Exception e) {
            printFailureByRes(e, res);
        }
    }
}
