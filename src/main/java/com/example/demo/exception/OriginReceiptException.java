package com.example.demo.exception;

/**
 * Created with IntelliJ IDEA.
 * Description: 原始水单文件异常
 * User: jun.lei@counect.com
 * Date: 2018-01-11
 * Time: 15:21
 */
public class OriginReceiptException extends RuntimeException {

    public OriginReceiptException(String msg){
        super(msg);
    }
}
