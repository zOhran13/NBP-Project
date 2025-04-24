package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.request.LoginUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.request.RegisterUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.response.LoginResponse;
import ba.unsa.etf.nbp.DonationPlatform.security.JwtTokenHelper;
import ba.unsa.etf.nbp.DonationPlatform.service.RoleService;
import ba.unsa.etf.nbp.DonationPlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

}
