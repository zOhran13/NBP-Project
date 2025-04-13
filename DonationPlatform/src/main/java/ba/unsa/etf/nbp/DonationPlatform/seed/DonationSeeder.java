package ba.unsa.etf.nbp.DonationPlatform.seed;

import java.util.Date;
import java.util.List;
import java.util.Random;

import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.Donation;
import ba.unsa.etf.nbp.DonationPlatform.model.User;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.DonationRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
@Order(4)
public class DonationSeeder implements CommandLineRunner {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public void run(String... args) {
        if (donationRepository.count() == 0) {
            List<User> users = userRepository.findAll();
            List<Campaign> campaigns = campaignRepository.findAll();

            if (users.isEmpty() || campaigns.isEmpty()) {
                System.out.println("No users or campaigns found. Skipping donation seeding.");
                return;
            }

            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                Donation donation = new Donation();
                donation.setAmount(10 + (1000 - 10) * random.nextDouble());
                donation.setDonationDate(new Date());
                donation.setUser(users.get(random.nextInt(users.size())));
                donation.setCampaign(campaigns.get(random.nextInt(campaigns.size())));
                donationRepository.save(donation);
            }

            System.out.println("Seeded donations.");
        } else {
            System.out.println("Donations already present, skipping seeding.");
        }
    }
}

