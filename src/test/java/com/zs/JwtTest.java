package com.zs;

import com.auth0.jwt.JWTVerifyException;
import com.zs.bean.User;
import com.zs.security.JWT;
import com.zs.type.Date4LongType;
import com.zs.type.ListStrType;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author: zsq-1186
 * Date: 2017-08-22-11:28
 */
public class JwtTest {

    @Test
    public void testEncode(){
        ListStrType type=new ListStrType();
        List<String> array=new ArrayList<>();
        array.add("football");
        array.add("basketball");
        type.setValues(array);

        Date4LongType dType=new Date4LongType();
        dType.setCurrentTime(new Date());
        User user=new User(1l,"tom",20,"guangdong sz",type,dType);

        String token=JWT.sign(user,1000*60*60);
        System.out.println(token);

    }


    @Test
    public void testDecode() throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException {
        String token_msg="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MDMzNzY4NTI0MjksInBheWxvYWQiOiJ7XCJpZFwiOjEsXCJ1c2VyTmFtZVwiOlwidG9tXCIsXCJhZ2VcIjoyMCxcImFkZHJlc3NcIjpcImd1YW5nZG9uZyBzelwiLFwiaW50ZXJlc3RcIjp7XCJ2YWx1ZXNcIjpbXCJmb290YmFsbFwiLFwiYmFza2V0YmFsbFwiXX0sXCJyZWdUaW1lXCI6e1wiY3VycmVudFRpbWVcIjoxNTAzMzczMjUxOTYyfX0ifQ.xu7T6fy2k2SsqvBKpqtiPLr2g7G0yckBA-jivR71rZw";
        User user=JWT.unsign(token_msg,User.class);
        System.out.println(user.toString());
    }
}
