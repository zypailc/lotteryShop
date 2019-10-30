package com.didu.lotteryshop.common.utils;

import com.didu.lotteryshop.common.enumeration.ResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * API接口返回json格式封装类
 * @author CHJ
 * @date 2019-10-23
 *
 */
public class Result {

    private static final long serialVersionUID = 1348172152215944560L;

    /**
     * 请求成功返回码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 请求失败返回码
     */
    public static final int ERROR_CODE = 500;


    /**
     * 返回状态码，200为正确，500为失败
     */
    private int code;

    /**
     * 返回处理信息，成功或者失败
     */
    private String msg;

    /**
     * 成功返回true，失败返回false
     */
    private Boolean success;

    /**
     * 返回给前端的数据
     */
    private Map<String, Object> extend = new HashMap<String ,Object>();

    /**
     * 成功返回的json封装体
     * @param value 原始数据
     * @return json格式
     */
    public static Result successJson(Object value){
        Result results = new Result();
        results.setCode(ResultCode.SUCCESS.getCode());
        results.setMsg(ResultCode.SUCCESS.getMessage());
        results.setSuccess(true);
        results.getExtend().put("data",value);
        return results;
    }

    /**
     * 失败返回的json封装体
     * @return json格式
     */
    public static Result errorJson(){
        Result results = new Result();
        results.setCode(ResultCode.FAILED.getCode());
        results.setSuccess(false);
        results.setMsg(ResultCode.FAILED.getMessage());
        return results;
    }

    /**
     * 失败返回的json封装体
     * @return json格式
     */
    public static Result errorJson(String msg){
        Result results = new Result();
        results.setCode(ResultCode.FAILED.getCode());
        results.setSuccess(false);
        results.setMsg(msg);
        return results;
    }
    public static Result errorJson(String msg,Object value){
        Result results = new Result();
        results.setCode(ResultCode.FAILED.getCode());
        results.setSuccess(false);
        results.setMsg(msg);
        results.getExtend().put("data",value);
        return results;
    }
    public static Result errorJson(Object value){
        Result results = new Result();
        results.setCode(ResultCode.FAILED.getCode());
        results.setSuccess(false);
        results.setMsg(ResultCode.FAILED.getMessage());
        results.getExtend().put("data",value);
        return results;
    }

    /**
     * 失败返回的json封装体
     * @return json格式
     */
    public static Result errorJson(String msg,Integer code){
        Result results = new Result();
        results.setCode(code);
        results.setSuccess(false);
        results.setMsg(msg);
        return results;
    }

    public static Result jsonObject(String msg,Integer code){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

}
