package ba.unsa.etf.nbp.DonationPlatform.mapper;

import ba.unsa.etf.nbp.DonationPlatform.dto.AddressDTO;
import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO mapToAddressDto(Address address) {
     return new AddressDTO(address.getStreet(), address.getCity(), address.getPostalCode(), address.getCountry());
    }
    public Address mapToAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountry(addressDTO.getCountry());
        return address;
    }
}
