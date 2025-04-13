package ba.unsa.etf.nbp.DonationPlatform.seed;

import ba.unsa.etf.nbp.DonationPlatform.model.Address;
import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.AddressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public UserSeeder(UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder,
                      AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) {
        List<Address> addresses = createAddresses();
        seedUsers(addresses);
    }

    private List<Address> createAddresses() {
        List<Address> result = new ArrayList<>();

        result.add(findOrCreateAddress("Titova 17", "Sarajevo", 71000L, "Bosna i Hercegovina"));
        result.add(findOrCreateAddress("Ferhadija 12", "Sarajevo", 71000L, "Bosna i Hercegovina"));
        result.add(findOrCreateAddress("Čekaluša 11", "Sarajevo", 71000L, "Bosna i Hercegovina"));

        return result;
    }

    private Address findOrCreateAddress(String street, String city, Long postalCode, String country) {
        return addressRepository
                .findByStreetAndCityAndPostalCodeAndCountry(street, city, postalCode, country)
                .orElseGet(() -> {
                    Address newAddress = new Address(street, city, postalCode, country);
                    return addressRepository.save(newAddress);
                });
    }

    private void seedUsers(List<Address> addresses) {
        Optional<Role> orgRoleOpt = roleRepository.findByName("ORGANIZACIJA");
        Optional<Role> volRoleOpt = roleRepository.findByName("VOLONTER");

        orgRoleOpt.ifPresent(role -> {
            if (!userRepository.existsByUsername("org1")) {
                User organizacija = createUser(
                        "Organizacija", "1", "org1@gmail.com", "org1",
                        "Org123!?", addresses.get(0), role
                );
                userRepository.save(organizacija);
                System.out.println("Seeded user org1.");
            } else {
                System.out.println("User org1 already exists, skipping.");
            }
        });

        volRoleOpt.ifPresent(role -> {
            if (!userRepository.existsByUsername("vol1")) {
                User volonter1 = createUser(
                        "Volonter", "1", "vol1@gmail.com", "vol1",
                        "Vol1123!", addresses.get(1), role
                );
                userRepository.save(volonter1);
                System.out.println("Seeded user vol1.");
            } else {
                System.out.println("User vol1 already exists, skipping.");
            }

            if (!userRepository.existsByUsername("vol2")) {
                User volonter2 = createUser(
                        "Volonter", "2", "vol2@gmail.com", "vol2",
                        "Vol2123!", addresses.get(2), role
                );
                userRepository.save(volonter2);
                System.out.println("Seeded user vol2.");
            } else {
                System.out.println("User vol2 already exists, skipping.");
            }
        });
    }

    private User createUser(String firstName, String lastName, String email,
                            String username, String rawPassword, Address address, Role role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setAddressId(address.getId());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return user;
    }
}
