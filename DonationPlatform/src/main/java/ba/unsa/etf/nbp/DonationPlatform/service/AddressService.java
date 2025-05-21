package ba.unsa.etf.nbp.DonationPlatform.service;

import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Address createAddress(Address address) {
        return repository.save(address);
    }

    // READ (all)
    public List<Address> getAllAddresses() {
        return repository.findAll();
    }

    // READ (by ID)
    public Optional<Address> getAddressById(Long id) {
        return repository.findById(id);
    }

    // UPDATE
    public Address updateAddress(Long id, Address updatedAddress) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setCity(updatedAddress.getCity());
                    existing.setStreet(updatedAddress.getStreet());
                    existing.setPostalCode(updatedAddress.getPostalCode());
                    // Dodaj još polja ako ih ima u Address modelu
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Address not found with id " + id));
    }

    // DELETE
    public void deleteAddress(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Address not found with id " + id);
        }
        repository.deleteById(id);
    }
}
