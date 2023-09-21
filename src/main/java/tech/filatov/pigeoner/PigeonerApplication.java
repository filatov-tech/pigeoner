package tech.filatov.pigeoner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PigeonerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigeonerApplication.class, args);
	}

}
