package com.someexp.modules.test.controller;

import com.someexp.modules.test.domain.dto.UserDTO;
import com.someexp.modules.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    @Resource
    private UserService userService;

    @GetMapping("/hello/{id}")
    public ResponseEntity<?> hello(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/update/{name}")
    public ResponseEntity<?> update(@PathVariable("name") String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        return ResponseEntity.ok(userService.update(userDTO));
    }

}
