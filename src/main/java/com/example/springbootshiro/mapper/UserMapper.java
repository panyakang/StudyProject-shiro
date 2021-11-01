package com.example.springbootshiro.mapper;

import com.example.springbootshiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    public User queryUserByName(String name);

    public void AddUser(User user);
}
