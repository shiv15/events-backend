package com.scrapingevents.Scraping.events;

import com.scrapingevents.Scraping.controller.EventController;
import com.scrapingevents.Scraping.model.Events;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Entity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ScrapingEventsApplicationTests {


	@Autowired
	private EventController eventController;

	@Test
	void contextLoads() throws Exception {
		assertThat(eventController.getAllEvents().isEmpty());
	}

//	void contextLoads() throws Exception {
//		assertThat(eventController.save(Events).isEmpty());
//	}

}
