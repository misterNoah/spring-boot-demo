package com.zs.dto;

import lombok.*;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-17-11:36
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private int status;
    private int code;
    private String message;

   public static ApiErrorResponse fail(int status, int code, String message) {
        return new ApiErrorResponse(status,code,message);
    }
}
