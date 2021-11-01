package com.example.springbootshiro.config;

import com.example.springbootshiro.pojo.User;
import com.example.springbootshiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权doGetAuthorizationInfo()");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

//        info.addStringPermission("user:add");

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        User currentUser = (User) subject.getPrincipal();

        session.setAttribute("loginUser", currentUser);

        info.addStringPermission(currentUser.getPerms());


        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了doGetAuthorizationInfo()");


        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        User user = userService.queryUserByName(token.getUsername());
        if(user == null){
            return null;
        }

        ByteSource bytes = ByteSource.Util.bytes(user.getUsername());

        return new SimpleAuthenticationInfo(user, token.getPassword(), bytes , "");
    }
}
