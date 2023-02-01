package io.github.jiangklina.codeapi.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CodeApi.Api
 * 接口的作用是,代表了一段code|多段code,以及执行参数
 *
 * @author jiangklijna
 */
public class Api implements Serializable {

    private String common;
    private List<Param> params = Collections.emptyList();
    private Map<String, String> other = Collections.emptyMap();
    private String note = "";

    public Api() {
    }

    public Api(String common) {
        this.common = common;
    }

    public Api(String common, List<Param> params, Map<String, String> other) {
        this.common = common;
        this.params = params;
        this.other = other;
    }

    public Api(String common, List<Param> params, Map<String, String> other, String note) {
        this.common = common;
        this.params = params;
        this.other = other;
        this.note = note;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public Map<String, String> getOther() {
        return other;
    }

    public void setOther(Map<String, String> other) {
        this.other = other;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Api{" +
                "common='" + common + '\'' +
                ", params=" + params +
                ", other=" + other +
                ", note=" + note +
                '}';
    }
}
