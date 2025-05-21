package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.dto.ValidateTokenRequestDTO;
import ba.unsa.etf.nbp.DonationPlatform.mapper.UserMapper;
import ba.unsa.etf.nbp.DonationPlatform.model.RevokedToken;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.repository.RevokedTokenRepository;
import ba.unsa.etf.nbp.DonationPlatform.request.LoginUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.request.RegisterUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.response.LoginResponse;
import ba.unsa.etf.nbp.DonationPlatform.security.JwtTokenHelper;
import ba.unsa.etf.nbp.DonationPlatform.service.RoleService;
import ba.unsa.etf.nbp.DonationPlatform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired

    private  JwtTokenHelper tokenHelper;
    @Autowired
    private RoleService roleService;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        return ResponseEntity.ok(userDTOList);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest user) {
        try {
            User saved = userService.registerUser(user);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserRequest loginRequestDto) {
        // Fetch user by email
        UserDTO user = userService.getUserByEmail(loginRequestDto.getEmail());
        if (user != null && passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            // If user exists and password matches, fetch role and generate token
            Optional<RoleDTO> userRole = roleService.getRoleByName(user.getRole());
            String token = tokenHelper.generateToken(user, userRole.orElse(null));
            return ResponseEntity.ok(new LoginResponse(token));
        }

        // Return unauthorized if authentication fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ValidateTokenRequestDTO validateTokenRequestDto) {

        String token = authorizationHeader.replace("Bearer ", "");
        boolean isValid = tokenHelper.validateTokenAndItsClaims(token, validateTokenRequestDto.getRoles());

        return isValid ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (!revokedTokenRepository.existsByToken(token)) {
                RevokedToken revokedToken = new RevokedToken();
                revokedToken.setToken(token);
                revokedTokenRepository.save(revokedToken);
            }
        }
        return ResponseEntity.ok().build();
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Map<String, String> updateRequest) {
        try {

            String token = authHeader.replace("Bearer ", "");
            String emailFromToken = tokenHelper.getClaimsFromToken(token).get("email", String.class);


            UserDTO existingUser = userService.getUserByEmail(emailFromToken);

            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            String newEmail = updateRequest.get("email");
            String newUsername = updateRequest.get("username");
            //String newAddress = updateRequest.get("address");


            if (newEmail != null && !newEmail.isBlank()) {
                existingUser.setEmail(newEmail);
            }
            if (newUsername != null && !newUsername.isBlank()) {
                existingUser.setUsername(newUsername);
            }
            /*if (newAddress != null && !newAddress.isBlank()) {
               existingUser.setAddress(newAddress);
            }*/


            UserDTO updatedUser = userService.updateUserProfile(existingUser);


            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.replace("Bearer ", "");

            // Get email from token
            String email = tokenHelper.getClaimsFromToken(token).get("email", String.class);
            System.out.println("Email from token: " + email); // Debug log

            try {
                // Get user by email using the existing method
                UserDTO user = userService.getUserByEmail(email);

                // Create a response object with only the necessary user information
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("email", user.getEmail());
                response.put("username", user.getUsername());
                response.put("addressId", user.getAddress());

                System.out.println("Found user: " + user.getEmail()); // Debug log
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                System.out.println("User not found for email: " + email); // Debug log
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            System.out.println("Error in getCurrentUserProfile: " + e.getMessage()); // Debug log
            e.printStackTrace(); // Print full stack trace
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }


    }


}