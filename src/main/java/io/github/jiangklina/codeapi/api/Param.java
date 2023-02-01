package io.github.jiangklina.codeapi.api;

import java.io.Serializable;

/**
 * CodeApi.Param
 * 接口参数的作用是,描述了此Api需要的参数
 *
 * @author jiangklijna
 */
public class Param implements Serializable {

    private String key;
    private String defaultValue;
    private String type;
    private String format;
    private String note;
    private boolean require;

    public Param() {
    }

    public Param(String key, String defaultValue, String type, boolean require) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.require = require;
    }

    public Param(String key, String defaultValue, String type, boolean require, String format) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.require = require;
        this.format = format;
    }

    public Param(String key, String defaultValue, String type, boolean require, String format, String note) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.require = require;
        this.format = format;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Param{" +
                "key='" + key + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", type='" + type + '\'' +
                ", format='" + format + '\'' +
                ", require=" + require +
                ", note=" + note +
                '}';
    }
}
