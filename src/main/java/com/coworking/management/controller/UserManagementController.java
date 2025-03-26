package com.coworking.management.controller;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.RoleEnum;
import com.coworking.management.entity.User;
import com.coworking.management.service.UsersManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping(value = "/auth/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReqRes> register(
        @RequestPart("user") String userJson,
        @RequestPart("photoFile") MultipartFile photoFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class); 
            ReqRes response = usersManagementService.register(user, photoFile);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatusCode(400);
            errorResponse.setMessage("Invalid JSON format: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }
    
    @GetMapping("/admin/users")
    public ResponseEntity<ReqRes> getAllUsers() {
        ReqRes response = usersManagementService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-users-by-role/{role}")
    public ResponseEntity<ReqRes> getUsersByRole(@PathVariable String role) {
        RoleEnum userRole = RoleEnum.valueOf(role.toUpperCase());
        return ResponseEntity.ok(usersManagementService.getAllUsersByRole(userRole));
    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersManagementService.getUsersById(userId));
    }

    @PutMapping(value = "/admin/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReqRes> updateUser(
        @PathVariable Long userId,
        @RequestPart("user") String userJson,
        @RequestPart(value = "photo", required = false) MultipartFile photoFile
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User updatedUser = objectMapper.readValue(userJson, User.class);
            ReqRes response = usersManagementService.updateUser(userId, updatedUser, photoFile);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatusCode(400);
            errorResponse.setMessage("Invalid JSON format: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(usersManagementService.getProfile(email));
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @PutMapping("/adminuser/update-profile-photo")
    public ResponseEntity<ReqRes> updateProfilePhoto(@RequestPart("photo") MultipartFile photoFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(usersManagementService.updateProfilePhoto(email, photoFile));
    }
    
    @GetMapping("/admin/profiles")
    public ResponseEntity<ReqRes> getAllProfiles() {
        return ResponseEntity.ok(usersManagementService.getAllProfiles());
    }
   
}