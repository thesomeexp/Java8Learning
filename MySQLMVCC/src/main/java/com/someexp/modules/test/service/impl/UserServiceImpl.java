package com.someexp.modules.test.service.impl;

import com.someexp.modules.test.domain.dto.UserDTO;
import com.someexp.modules.test.domain.entity.User;
import com.someexp.modules.test.mapper.UserMapper;
import com.someexp.modules.test.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User get(long id) {
        return userMapper.get(id);
    }

    @Override
    @Transactional
    public User update(UserDTO userDTO) {
        User user = new User();
        user.setId(1L);
        user.setName(userDTO.getName());
        // 观测
        userMapper.get(1L);
        userMapper.update(user);
        System.out.println("finished");
        return null;
    }
}
