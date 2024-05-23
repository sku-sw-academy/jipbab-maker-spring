package sku.splim.jipbapmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatetestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatetestApplication.class, args);
	}

}
