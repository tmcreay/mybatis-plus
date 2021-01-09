package com.example.springbootmybatisplus.controller;

import com.example.springbootmybatisplus.bean.User;
import com.example.springbootmybatisplus.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper uesrMapper;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        List<User> list = uesrMapper.selectList(null);
        return list;
    }
}
