package top.lishaobo.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lishaobo.example.service.UserService;
import top.lishaobo.framework.util.ResponseBean;
import top.lishaobo.framework.util.ResultUtil;

import java.util.Date;

@RequestMapping(value = "/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register")
    public ResponseBean register(String accountNumber, String passWord, String name, Integer age, Date birthday) {

        userService.register(accountNumber, passWord, name, age, birthday);

        return ResultUtil.success();

    }
}
