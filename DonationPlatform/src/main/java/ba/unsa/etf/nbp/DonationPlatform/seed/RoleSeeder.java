package ba.unsa.etf.nbp.DonationPlatform.seed;


import ba.unsa.etf.nbp.DonationPlatform.model.Role;
import ba.unsa.etf.nbp.DonationPlatform.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<Role> roles = Arrays.asList(
                new Role("VOLONTER"),
                new Role("ORGANIZACIJA"),
                new Role("DONATOR")
        );

        for (Role role : roles) {
            roleRepository.findByName(role.getName()).ifPresent(existingRole -> {
                System.out.println("Role with name '" + role.getName() + "' already exists. Skipping...");
            });

            if (roleRepository.findByName(role.getName()).isEmpty()) {
                roleRepository.save(role);
                System.out.println("Seeded role: " + role.getName());
            }
        }

        System.out.println("Role seeding process completed.");
    }}