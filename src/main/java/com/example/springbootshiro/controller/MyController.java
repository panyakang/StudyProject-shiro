package com.example.springbootshiro.controller;

import com.example.springbootshiro.pojo.User;
import com.example.springbootshiro.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Security;

@Controller
public class MyController {
    @Autowired
    UserService userService;

    @RequestMapping({"/","/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello,Shiro");
        return "index";
    }

    @RequestMapping({"/user/add"})
    public String add(Model model){
//        model.addAttribute("msg","hello,Add");
        return "user/add";
    }

    @RequestMapping({"/user/update"})
    public String update(Model model){
//        model.addAttribute("msg","hello,Update");
        return "user/update";
    }

    @RequestMapping({"/toLogin"})
    public String toLogin(Model model){

        return "login";
    }

    @RequestMapping({"/login"})
    public String login(String username,String password,Model model){
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

        try {
            subject.login(usernamePasswordToken);
            return "index";
        } catch (AuthenticationException e) {
            model.addAttribute("msg",e.getMessage());
            return "login";
        }

    }

    @RequestMapping({"/register"})
    public String register(String username,String password,Model model){

        if("".equals(username)){
            model.addAttribute("msg", "用户名错误");
            return "user/add";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        int hashIterations=1024;
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        SimpleHash md5 = new SimpleHash("MD5", password, credentialsSalt, hashIterations);
        user.setPassword(md5.toHex());
        user.setSalt(credentialsSalt.toString());
        userService.AddUser(user);

        System.out.println(md5);

        return "login";

    }

    @RequestMapping({"/noauth"})
    @ResponseBody
    public String noauth(Model model){
        return "未经授权无法访问此页面";
    }
}
