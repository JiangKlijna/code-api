package io.github.jiangklina.codeapi.router.servlet;


import cn.hutool.json.JSONUtil;
import io.github.jiangklina.codeapi.router.Router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * CodeApi.Router.ServletRouter
 * 适合于Servlet容器内使用
 *
 * @author jiangklijna
 */
public class ServletRouter implements Router<HttpServletRequest, HttpServletResponse> {

    @Override
    public Map<String, Object> getParamsByReq(HttpServletRequest req) {
        Map<String, Object> params = new HashMap<>();

        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] values = req.getParameterValues(key);
            if (values.length == 1) {
                params.put(key, values[0]);
            } else if (values.length > 1) {
                params.put(key, values);
            }
        }
        return params;
    }

    @Override
    public String getPathByReq(HttpServletRequest req, String requestPrefix) {
        String requestURI = req.getRequestURI().replaceAll("//", "/");
        return requestURI.replaceFirst(requestPrefix, "");
    }

    @Override
    public String getApiType(HttpServletRequest req) {
        return req.getHeader("X-API-TYPE");
    }

    @Override
    public void printSuccessByRes(Object result, HttpServletResponse res) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            map.put("code", 1);
            map.put("data", result);
            PrintWriter writer = res.getWriter();
            writer.print(JSONUtil.toJsonStr(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printFailureByRes(Exception e, HttpServletResponse res) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            map.put("code", 0);
            map.put("msg", e.getMessage());
            PrintWriter writer = res.getWriter();
            writer.print(JSONUtil.toJsonStr(map));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

}
