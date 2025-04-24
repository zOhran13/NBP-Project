package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.dto.RoleDTO;
import ba.unsa.etf.nbp.DonationPlatform.mapper.UserMapper;
import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import ba.unsa.etf.nbp.DonationPlatform.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ba.unsa.etf.nbp.DonationPlatform.mapper.UserMapper;

import java.util.List;
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

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole().getName(),
                        user.getAddressId() != null
                                ? addressRepository.findById(user.getAddressId())
                                .map(Address::getStreet)
                                .orElse("No address")
                                : "No address"


                ))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().getName(),
                        user.getPassword(),
                        user.getAddressId() != null
                                ? addressRepository.findById(user.getAddressId())
                                .map(Address::getStreet)
                                .orElse("No address")
                                : "No address"
                ));
    }

    public User registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken.");
        }

        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() -> new RuntimeException("Address ID is not valid."));

        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role Id is not valid."));

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());

// Address handling
        String street = Optional.ofNullable(user.getAddressId())
                .flatMap(addressRepository::findById)
                .map(Address::getStreet)
                .orElse("No address");

        user.setRole(role);
        return userRepository.save(user);
    }
    public User changeUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);
        return userRepository.save(user);
    }



    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::mapToUserDto)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}