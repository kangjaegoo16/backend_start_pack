package com.example.demo.repository;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRepository {

    User findByUserName(String username);

    void save(User user);
}
