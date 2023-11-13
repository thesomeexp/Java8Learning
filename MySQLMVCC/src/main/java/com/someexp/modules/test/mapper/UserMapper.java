package com.someexp.modules.test.mapper;

import com.someexp.modules.test.domain.entity.User;

public interface UserMapper {

    User get(long id);

    int update(User user);
}
