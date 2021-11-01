package com.example.springbootshiro.service;

import com.example.springbootshiro.mapper.UserMapper;
import com.example.springbootshiro.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }

    @Override
    public void AddUser(User user) {
        userMapper.AddUser(user);
    }
}
