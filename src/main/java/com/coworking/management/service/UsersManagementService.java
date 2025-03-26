package com.coworking.management.service;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.RoleEnum;
import com.coworking.management.entity.User;
import com.coworking.management.repository.UserRepo;
import com.coworking.management.util.JWTUtils;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersManagementService {

    @Autowired
    private UserRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create upload folder: " + uploadDir, e);
        }
    }

    
    public ReqRes register(User user, MultipartFile photoFile) {
        ReqRes reqRes = new ReqRes();
       
        try {
            // Check if the photo file is empty
            if (photoFile.isEmpty()) {
                reqRes.setStatusCode(400);
                reqRes.setMessage("The file is empty.");
                return reqRes;
            }
           
            // Save the photo file
            String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            Files.copy(photoFile.getInputStream(), this.rootLocation.resolve(filename));
            user.setPhoto(filename);

            // Encode the password before saving the user
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save the user
            User savedUser = usersRepo.save(user);

            // Prepare the response
            reqRes.setUsers(savedUser);
            reqRes.setStatusCode(200);
            reqRes.setMessage("User saved successfully.");
            
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return reqRes;
    }

    private String savePhoto(MultipartFile photoFile) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(photoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName; // Return the relative path for the photo
    }

   
    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            User user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken((UserDetails) user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), (UserDetails) user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
           
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = usersRepo.findByEmail(email).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), (UserDetails) user)) {
                var jwt = jwtUtils.generateToken((UserDetails) user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully Refreshed Token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    
    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();
        try {
            List<User> users = usersRepo.findAll(); 
            if (!users.isEmpty()) {
                reqRes.setUsersList(users);
                reqRes.setStatusCode(200);
                reqRes.setMessage("All users retrieved successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getAllUsersByRole(RoleEnum role) {
        ReqRes reqRes = new ReqRes();
        try {
            List<User> users = usersRepo.findByRole(role);
            if (!users.isEmpty()) {
                reqRes.setUsersList(users);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found with role " + role.name());
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getUsersById(Long id) {
        ReqRes reqRes = new ReqRes();
        try {
            User user = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUsers(user);
            reqRes.setStatusCode(200);
            reqRes.setMessage("User with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Long userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Long userId, User updatedUser, MultipartFile photoFile) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                
                // Ajoutez cette vérification avant de modifier l'email
                if(!existingUser.getEmail().equals(updatedUser.getEmail())) {
                    if(usersRepo.existsByEmail(updatedUser.getEmail())) {
                        reqRes.setStatusCode(400);
                        reqRes.setMessage("Email already exists");
                        return reqRes;
                    }
                    existingUser.setEmail(updatedUser.getEmail());
                }
                
                // Update basic fields
                existingUser.setFullName(updatedUser.getFullName());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                existingUser.setRole(updatedUser.getRole());

                // Update password (only if provided)
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                // Update photo (only if provided)
                if (photoFile != null && !photoFile.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                    Files.copy(photoFile.getInputStream(), this.rootLocation.resolve(filename));
                    existingUser.setPhoto(filename);
                }

                // Save the updated user
                User savedUser = usersRepo.save(existingUser);
                
                // Prepare response
                reqRes.setUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
             
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getProfile(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Profile retrieved successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while retrieving profile: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateProfilePhoto(String email, MultipartFile photoFile) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                if (photoFile != null && !photoFile.isEmpty()) {
                    String photoUrl = savePhoto(photoFile);
                    existingUser.setPhoto(photoUrl);
                }
                User savedUser = usersRepo.save(existingUser);
                reqRes.setUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Profile photo updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating profile photo: " + e.getMessage());
        }
        return reqRes;
    }
    
    public ReqRes getAllProfiles() {
        ReqRes reqRes = new ReqRes();
        try {
            List<User> users = usersRepo.findAll();
            if (!users.isEmpty()) {
                // Ne retourner que les données nécessaires pour le profil
                List<Map<String, Object>> profiles = users.stream()
                    .map(user -> {
                        Map<String, Object> profile = new HashMap<>();
                        profile.put("id", user.getId());
                        profile.put("fullName", user.getFullName());
                        profile.put("email", user.getEmail());
                        profile.put("phoneNumber", user.getPhoneNumber());
                        profile.put("role", user.getRole());
                        profile.put("photo", user.getPhoto());
                        return profile;
                    })
                    .collect(Collectors.toList());
                
                reqRes.setProfilesList(profiles);
                reqRes.setStatusCode(200);
                reqRes.setMessage("All profiles retrieved successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }
}