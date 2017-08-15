package com.zs.bean;

import com.zs.type.Date4LongType;
import com.zs.type.ListStrType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;



/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zsq-1186
 * Date: 2017-08-04
 * Time: 17:40
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private Long id;
    private String userName;
    private int age;
    private String address;
    private ListStrType interest;
    private Date4LongType regTime;


}
