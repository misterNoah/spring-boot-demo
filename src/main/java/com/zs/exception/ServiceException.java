package com.zs.exception;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-17-10:52
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {super("业务异常");}

    public ServiceException(String msg) {super(msg);}

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
