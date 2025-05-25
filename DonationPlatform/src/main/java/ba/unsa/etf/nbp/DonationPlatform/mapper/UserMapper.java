package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final RoleRepository roleRepository;

    public UserMapper(AddressRepository addressRepository, AddressMapper addressMapper, RoleRepository roleRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.roleRepository = roleRepository;
    }

    public UserDTO mapToUserDto(User user) {
        if (user == null) {
            return null;
        }

        Address address = null;
        if (user.getAddressId() != null) {
            address = addressRepository.findById(user.getAddressId()).orElse(null);
        }

        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                address != null ? addressMapper.mapToAddressDto(address) : null,
                user.getRole().getName()
        );
    }
    public User mapToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setBirthDate(userDTO.getBirthDate());
        if (userDTO.getAddress() != null) {
            Address address = addressMapper.mapToAddress(userDTO.getAddress());

            // Optional: check for an existing address before saving (e.g. by street)
            Address existing = (Address) addressRepository.findAddressesByStreet(address.getStreet()).stream().findFirst()
                    .orElse(null);

            if (existing == null) {
                existing = addressRepository.save(address);
            }

            user.setAddressId(existing.getId());
        }
        user.setRole(roleRepository.findByName(userDTO.getRole()).orElse(null));
        return user;
    }

    private String formatAddress(Address address) {
        if (address == null) return null;

        return String.format("%s, %s %s, %s",
                address.getStreet(),
                address.getPostalCode(),
                address.getCity(),
                address.getCountry());
    }
    private Address parseAddress(String addressString) {
        try {
            // Očekuje format: "ulica, 71000 grad, država"
            String[] parts = addressString.split(",");
            if (parts.length < 3) return null;

            String street = parts[0].trim();
            String[] postalAndCity = parts[1].trim().split(" ");
            String postalCode = postalAndCity[0];
            String city = String.join(" ", java.util.Arrays.copyOfRange(postalAndCity, 1, postalAndCity.length));
            String country = parts[2].trim();

            Address address = new Address();
            address.setStreet(street);
            address.setPostalCode(Long.valueOf(postalCode));
            address.setCity(city);
            address.setCountry(country);

            return address;
        } catch (Exception e) {
            return null; // Loš format
        }
    }

}