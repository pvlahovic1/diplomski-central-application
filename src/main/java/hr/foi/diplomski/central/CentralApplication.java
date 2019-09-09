package hr.foi.diplomski.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class CentralApplication {

	//Angular client
	public static void main(String[] args) {
		SpringApplication.run(CentralApplication.class, args);
	}

	@PostConstruct
	void configOnStart() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zagreb"));
	}

}
