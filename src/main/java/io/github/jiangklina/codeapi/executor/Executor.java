package io.github.jiangklina.codeapi.executor;

import io.github.jiangklina.codeapi.api.Api;
import io.github.jiangklina.codeapi.api.ApiException;
import io.github.jiangklina.codeapi.api.Param;
import io.github.jiangklina.codeapi.api.ParamException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CodeApi.Executor
 * 执行者的作用是,通过[请求参数]以及Api执行后得到结果交给Factory
 *
 * @author jiangklijna
 */
public interface Executor<Result> {

    /**
     * 根据具体执行者不同需求,获取相对应的code执行
     * 从otherCode获取最准确的code,没有则默认commonCode
     *
     * @param api Api对象
     * @return code
     */
    default String getCode(Api api) {
        return api.getCommon();
    }

    /**
     * 把code和执行参数交给执行者
     *
     * @param code       执行代码
     * @param execParams 执行参数
     * @return 执行结果
     * @throws ApiException 执行异常
     */
    Result execCode(String code, Map<String, Object> execParams) throws Exception;

    /**
     * 解析参数,根据类型转换为需要的对象
     *
     * @param param         ->type int long double date string Array<int> Array<long> Array<double> Array<date> Array<string>
     * @param value         如果为数组则为String[],其他类型均String.valueOf处理
     * @param isSplitSingle 为true且value为string类型时,对字符串进行split(",")
     * @return 转换后的对象
     */
    default Object parseParam(Param param, Object value, boolean isSplitSingle) throws ParamException {
        String type = param.getType();
        final SimpleDateFormat simpleDateFormat = type.contains("date") ? new SimpleDateFormat(param.getFormat()) : null;

        if (type.startsWith("Array")) { // 数组类型 Array<int> Array<long> Array<double> Array<date> Array<string>
            final List<String> list;
            if (value instanceof String) {
                String string = String.valueOf(value);
                if (isSplitSingle) {
                    list = Arrays.asList(string.split(","));
                } else {
                    list = Collections.singletonList(string);
                }
            } else if (value instanceof String[]) {
                list = Arrays.asList((String[]) value);
            } else {
                throw new ParamException(param, "wrong param type! value: " + value);
            }
            switch (type) {
                case "Array<string>":
                    return list.toArray();
                case "Array<int>":
                    try {
                        return list.stream().map(Integer::parseInt).toArray();
                    } catch (NumberFormatException e) {
                        throw new ParamException(param, "parseInt error! array is " + list);
                    }
                case "Array<long>":
                    try {
                        return list.stream().map(Long::parseLong).toArray();
                    } catch (NumberFormatException e) {
                        throw new ParamException(param, "parseLong error! array is " + list);
                    }
                case "Array<date>":
                    return list.stream().map((s) -> {
                        try {
                            return simpleDateFormat.parse(s);
                        } catch (ParseException e) {
                            throw new ParamException(param, "dateFormat error! format is " + param.getFormat() + " text is " + s);
                        }
                    }).toArray();
                default:
                    throw new ParamException(param, "wrong param type(" + type + ")");
            }
        } else { // 基础类型 int long double date string
            if (value.getClass().isArray()) {
                throw new ParamException(param, "wrong param type(Array), the expected type is " + type);
            }
            String string = String.valueOf(value);
            switch (type) {
                case "":
                case "string":
                    return string;
                case "int":
                    try {
                        return Integer.parseInt(string);
                    } catch (NumberFormatException e) {
                        throw new ParamException(param, "parseInt error! text is " + string);
                    }
                case "long":
                    try {
                        return Long.parseLong(string);
                    } catch (NumberFormatException e) {
                        throw new ParamException(param, "parseLong error! text is " + string);
                    }
                case "date":
                    try {
                        return simpleDateFormat.parse(string);
                    } catch (ParseException e) {
                        throw new ParamException(param, "dateFormat error! format is " + param.getFormat() + " text is " + string);
                    }
                default:
                    throw new ParamException(param, "wrong param type(" + type + ")");
            }
        }
    }

    /**
     * 判断参数是否为null "" []
     *
     * @param o 类型只有 String|String[] 两种
     */
    default boolean isEmptyParam(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (((String) o).isEmpty()) {
                return true;
            }
        }
        if (o instanceof String[]) {
            if (((String[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据api.params以及请求参数,对参数进行类型转换等包装处理
     *
     * @param api    Api对象
     * @param params 请求参数
     * @return 处理后的执行参数
     * @throws ParamException 可能会有参数异常
     */
    default Map<String, Object> execParams(Api api, Map<String, Object> params) throws ParamException {
        Map<String, Object> resultMap = new LinkedHashMap<>();

        for (Param p : api.getParams()) {
            String key = p.getKey();
            if (!isEmptyParam(params.get(key))) {
                resultMap.put(key, parseParam(p, params.get(key), false));
                continue;
            }
            if (!isEmptyParam(p.getDefaultValue())) {
                resultMap.put(key, parseParam(p, p.getDefaultValue(), true));
                continue;
            }
            if (p.isRequire()) {
                throw new ParamException("param [" + key + "] is required, but not found!");
            }
        }
        return resultMap;
    }

    /**
     * 首先获取要执行的code,其次获取执行参数,最后把code和执行参数交给真正的执行者
     *
     * @param api    Api对象
     * @param params 请求参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    default Result execApi(Api api, Map<String, Object> params) throws Exception {
        String code = getCode(api);
        Map<String, Object> execParams = execParams(api, params);
        return execCode(code, execParams);
    }
}
