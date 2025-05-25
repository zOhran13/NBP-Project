package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.mapper.AddressMapper;
import ba.unsa.etf.nbp.DonationPlatform.mapper.RoleMapper;
import ba.unsa.etf.nbp.DonationPlatform.mapper.UserMapper;
import ba.unsa.etf.nbp.DonationPlatform.model.*;
import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.PasswordTokenRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import ba.unsa.etf.nbp.DonationPlatform.request.LoginUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.request.RegisterUserRequest;
import ba.unsa.etf.nbp.DonationPlatform.response.LoginResponse;
import ba.unsa.etf.nbp.DonationPlatform.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private JwtTokenHelper tokenHelper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private RoleMapper roleMapper;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public LoginResponse login(LoginUserRequest loginRequestDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequestDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                RoleDTO roleDto = roleService.getRoleByName(user.getRole().getName()).orElse(null);
                String token = tokenHelper.generateToken(user, roleDto);
                return new LoginResponse(token);
            }
        }

        return null;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getBirthDate(),
                        addressRepository.findById(user.getAddressId()).map(addressMapper::mapToAddressDto).orElse(null),
                        user.getRole().getName()


                ))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(userMapper::mapToUserDto);
    }

    public LoginResponse updateUserProfile(String originalEmail, UserDTO updateRequest) {
        // 1. Find user by original email
        User user = userRepository.findByEmail(originalEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // 2. Check if new username exists (and belongs to someone else)
        if (!updateRequest.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPhoneNumber(updateRequest.getPhoneNumber());
        user.setBirthDate(updateRequest.getBirthDate());
        if (updateRequest.getAddress() != null) {
            Address address = addressMapper.mapToAddress(updateRequest.getAddress());

            // Optional: check for an existing address before saving (e.g. by street)
            Address existing = addressRepository.findAddressesByStreet(address.getStreet())
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                existing = addressRepository.save(address);
            }

            user.setAddressId(existing.getId());


            if (existing == null) {
                existing = addressRepository.save(address);
            }

            user.setAddressId(existing.getId());
        }
        // 4. Save user
        userRepository.save(user);

        // 5. Return new LoginResponse (you may want to regenerate token here)
        return new LoginResponse(tokenHelper.generateToken(user, roleMapper.mapToRoleDTO(user.getRole())));
    }




    public UserDTO registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken.");
        }
        Address address = new Address();
        address.setCountry(request.getAddress().getCountry());
        address.setCity(request.getAddress().getCity());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setStreet(request.getAddress().getStreet());
        addressRepository.save(address);

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role Id is not valid."));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        user.setAddressId(address.getId());
        user.setRole(role);
        userRepository.save(user);
        return userMapper.mapToUserDto(user);
    }
    public UserDTO changeUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        userRepository.save(user);
        return userMapper.mapToUserDto(user);
    }



    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::mapToUserDto)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }



}