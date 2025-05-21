package ba.unsa.etf.nbp.DonationPlatform.controller;

import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address created = addressService.createAddress(address);
        return ResponseEntity.ok(created);
    }

    // READ (all)
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // READ (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        try {
            Address address = addressService.updateAddress(id, updatedAddress);
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
