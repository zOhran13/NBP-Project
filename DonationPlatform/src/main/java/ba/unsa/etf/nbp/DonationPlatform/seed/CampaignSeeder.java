package ba.unsa.etf.nbp.DonationPlatform.seed;

import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.CampaignProgress;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignProgressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Order(3)
public class CampaignSeeder implements CommandLineRunner {

    private final CampaignRepository campaignRepository;
    private final CampaignProgressRepository campaignProgressRepository;

    public CampaignSeeder(CampaignRepository campaignRepository, CampaignProgressRepository campaignProgressRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignProgressRepository = campaignProgressRepository;
    }

    @Override
    public void run(String... args) {
        if (campaignRepository.count() == 0) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                List<Campaign> campaigns = Arrays.asList(new Campaign("Pomoć poplavljenim područjima u Krajini",
                        "https://balkans.aljazeera.net/wp-content/uploads/2025/03/82f28eea1de88859c2a1f9aa5176c3a3-1-1743075068.jpg?resize=770%2C513&quality=80",
                        formatter.parse("2025-03-10"),
                        formatter.parse("2025-06-20"),
                        25000.0),
                        new Campaign(
                                "Donacije za renovaciju doma za djecu bez roditeljskog staranja",
                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEA-rUuC0ss1I3u-zZtqfNjOsR5-ChCAZsVA&s",
                                formatter.parse("2025-02-01"),
                                formatter.parse("2025-07-20"),
                                15000.0
                        ),
                        new Campaign(
                                "Zajedno pošumljavamo",
                                "https://hr.izzi.digital/DOS/54720/datastore/10/publication/54720/pictures/2021/01/04/1609774720_suma_vazdazelena_1.jpg?v=1738764882",
                                formatter.parse("2025-04-01"),
                                formatter.parse("2025-09-01"),
                                10000.0
                        ),
                        new Campaign(
                                "Podrška obrazovanju u ruralnim područjima",
                                "https://www.srednja.hr/app/uploads/2024/03/Kolocep-lead.jpg",
                                formatter.parse("2025-01-15"),
                                formatter.parse("2025-08-30"),
                                20000.0
                        ));


                campaignRepository.saveAll(campaigns);
                for (Campaign campaign : campaigns) {
                    CampaignProgress progress = new CampaignProgress();
                    progress.setCampaign(campaign);
                    progress.setCurrentAmount(0.0);
                    progress.setLastUpdate(new Date());

                    campaignProgressRepository.save(progress);
                }

                System.out.println("Seeded campaigns.");
            } catch (Exception e) {
                System.err.println("Error occured when seeding campaigns.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Campaigns already present, skipping seeding.");
        }
    }
}
