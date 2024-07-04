package com.plexpt.chatgpt.constant;

import lombok.Data;

@Data
public class Result<T> {
    private String status;
    private String code;
    private T result;


    public static <T> Result<T>OK(){
        Result<T> r = new Result<>();
        r.setCode("200");
        r.setStatus("success");
        return r ;
    }
    public static <T> Result<T>OK(T data){
        Result<T> r = new Result<>();
        r.setCode("200");
        r.setStatus("success");
        r.setResult(data);
        return r ;
    }
    public static <T> Result<T>OK(T data,String status){
        Result<T> r = new Result<>();
        r.setCode("200");
        r.setStatus(status);
        r.setResult(data);
        return r ;
    }
    public static <T> Result<T>OK(T data,String status,String code){
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setStatus(status);
        r.setResult(data);
        return r ;
    }


}
