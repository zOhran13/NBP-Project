package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.UserDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AddressRepository addressRepository;

    public UserMapper(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
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
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole() != null ? user.getRole().getName() : null,
                formatAddress(address)
        );
    }

    private String formatAddress(Address address) {
        if (address == null) return null;

        return String.format("%s, %s %s, %s",
                address.getStreet(),
                address.getPostalCode(),
                address.getCity(),
                address.getCountry());
    }
}
