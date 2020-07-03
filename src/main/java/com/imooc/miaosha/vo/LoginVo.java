package com.imooc.miaosha.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class LoginVo implements Serializable {

    @NotNull
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式错误")
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

}
