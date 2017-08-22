package com.zs.security.Exception;

import com.auth0.jwt.JWTVerifyException;

/**
 * Description:
 *  时间过期异常
 * @author: zsq-1186
 * Date: 2017-08-22-11:42
 */
public class JWTExpiredException extends JWTVerifyException {

    private long expiration;

    public JWTExpiredException(long expiration) {
        this.expiration = expiration;
    }

    public JWTExpiredException(String message, long expiration) {
        super(message);
        this.expiration = expiration;
    }

    public long getExpiration() {
        return expiration;
    };
}
