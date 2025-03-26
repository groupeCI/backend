package com.coworking.management.dto;

import com.coworking.management.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
    }
}