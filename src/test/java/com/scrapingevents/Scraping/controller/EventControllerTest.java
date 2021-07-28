package com.scrapingevents.Scraping.controller;
import com.scrapingevents.Scraping.dao.EventsDao;
import com.scrapingevents.Scraping.model.Events;

import com.scrapingevents.Scraping.services.EventService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {
    @MockBean
    private EventService eventService;
    @MockBean
    private EventsDao eventsDao;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllEvents() throws Exception {
        Events event = new Events();
        event.setId(00);
        event.setEventName("Test Event");
        event.setEventStartDate(new Date());
        event.setLocation("Test Location");
        event.setWebsiteName("test.com");

        List<Events> events = new ArrayList<>();
        events.add(event);

        given(eventsDao.getAllEvents()).willReturn(events);
        this.mockMvc.perform(get("/getAllEvents"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Location")));
    }
}