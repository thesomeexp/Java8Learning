package com.someexp.modules.test.service;

import com.someexp.modules.test.domain.dto.UserDTO;
import com.someexp.modules.test.domain.entity.User;

public interface UserService {

    User get(long id);

    User update(UserDTO userDTO);

}
