package ba.unsa.etf.nbp.DonationPlatform;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DonationPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationPlatformApplication.class, args);
	}

}
