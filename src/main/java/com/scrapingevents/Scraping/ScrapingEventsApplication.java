package com.scrapingevents.Scraping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication()
public class ScrapingEventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapingEventsApplication.class, args);
	}

}
