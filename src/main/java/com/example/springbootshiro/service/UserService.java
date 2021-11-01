package com.example.springbootshiro.service;


import com.example.springbootshiro.pojo.User;

public interface UserService {

    public User queryUserByName(String name);

    public void AddUser(User user);
}
