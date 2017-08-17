package com.zs.handler;


import com.zs.dto.ApiErrorResponse;
import com.zs.exception.NotFoundException;
import com.zs.exception.NotLoginException;
import com.zs.exception.ServiceException;
import com.zs.util.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

import static com.zs.dto.ApiErrorResponse.fail;


/**
 * 处理全局异常的控制类, 只处理一些需要全局处理的就好<br><br>
 * <span style="color:red">其他未特殊处理的异常将会由 BasicErrorController 处理.</span>
 * <ul>
 * <li>请求 html 由 errorHtml 方法处理, json 请求由 error 方法处理</li>
 * <li>error 页面的上下文信息值在 DefaultErrorAttributes 中可查看</li>
 * </ul>
 *
 * @see org.springframework.boot.autoconfigure.web.BasicErrorController
 * @see org.springframework.boot.autoconfigure.web.DefaultErrorAttributes
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


    @ExceptionHandler(NotLoginException.class)
    public void noLogin(NotLoginException e, HttpServletResponse response) throws IOException {

    }

    @ExceptionHandler(NotFoundException.class)
    public void notFound(NotFoundException e, HttpServletResponse response) throws IOException {

    }

    @ExceptionHandler(ServiceException.class)
    public ApiErrorResponse service(ServiceException e, HttpServletResponse response) {
       // RequestUtils.toJson(fail(500,9999,e.getMessage()).toString(), response);
        return fail(500,9999,e.getMessage());
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse constraintViolationException(ConstraintViolationException ex) {
        return new ApiErrorResponse(500, 5001, ex.getMessage());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse IllegalArgumentException(IllegalArgumentException ex) {
        return new ApiErrorResponse(501, 5002, ex.getMessage());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse noHandlerFoundException(Exception ex) {
        return new ApiErrorResponse(404, 4041, ex.getMessage());
    }


    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse unknownException(Exception ex) {
        return new ApiErrorResponse(500, 5002, ex.getMessage());
    }



    /**
     * 使用 @Validated  hibernate validate校验异常获取
     * **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void validException(MethodArgumentNotValidException e, HttpServletResponse response) throws Exception{
        List<ObjectError> errorList=e.getBindingResult().getAllErrors();
        StringBuilder sb=new StringBuilder();
        for(ObjectError objError:errorList){
            FieldError fieldError= (FieldError) objError;
            String fieldName=fieldError.getField();
            String errorMsg=fieldError.getDefaultMessage();
            sb.append(errorMsg+" ");
        }
//        ResultObject resultObject=new ResultObject();
//        resultObject.fail(sb.toString());
//        RequestUtils.toJson(resultObject, response);

    }


}
