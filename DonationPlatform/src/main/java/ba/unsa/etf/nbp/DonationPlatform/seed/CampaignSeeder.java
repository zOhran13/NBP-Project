

package ba.unsa.etf.nbp.DonationPlatform.seed;

import ba.unsa.etf.nbp.DonationPlatform.model.Campaign;
import ba.unsa.etf.nbp.DonationPlatform.model.CampaignProgress;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignProgressRepository;
import ba.unsa.etf.nbp.DonationPlatform.repository.CampaignRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
            if (campaignRepository.count() == 0) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    List<Campaign> campaigns = Arrays.asList(
                            new Campaign("Pomoć poplavljenim područjima u Krajini",
                                    loadImage("kampanja1.jpg"),
                                    formatter.parse("2025-03-10"),
                                    formatter.parse("2025-06-20"),
                                    25000.0),
                            new Campaign("Donacije za renovaciju doma za djecu bez roditeljskog staranja",
                                    loadImage("kampanja2.jpg"),
                                    formatter.parse("2025-02-01"),
                                    formatter.parse("2025-07-20"),
                                    15000.0),
                            new Campaign("Zajedno pošumljavamo",
                                    loadImage("kampanja3.jpg"),
                                    formatter.parse("2025-04-01"),
                                    formatter.parse("2025-09-01"),
                                    10000.0),
                            new Campaign("Podrška obrazovanju u ruralnim područjima",
                                    loadImage("kampanja4.jpg"),
                                    formatter.parse("2025-01-15"),
                                    formatter.parse("2025-08-30"),
                                    20000.0)
                    );

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
                    System.err.println("Error occurred when seeding campaigns.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Campaigns already present, skipping seeding.");
            }
        }
    }

    private byte[] loadImage(String fileName) throws IOException {
        ClassPathResource imgFile = new ClassPathResource("images/" + fileName);
        return imgFile.getInputStream().readAllBytes();
    }
}
