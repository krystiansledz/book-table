package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.UserDTO;
import com.krystiansledz.booktable.security.jwt.JwtUtils;
import com.krystiansledz.booktable.security.services.UserDetailsServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(@RequestHeader("Authorization") String token) {
        String email = jwtUtils.getUserNameFromJwtToken(token.replace("Bearer ", ""));
        UserDetails user = userService.loadUserByUsername(email);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return ResponseEntity.ok(userDTO);
    }
}
