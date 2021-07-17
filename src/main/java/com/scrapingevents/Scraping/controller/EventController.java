package com.scrapingevents.Scraping.controller;


import com.scrapingevents.Scraping.dao.EventsDao;
import com.scrapingevents.Scraping.model.Events;

import com.scrapingevents.Scraping.model.Urls;
import com.scrapingevents.Scraping.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("")
public class EventController {
    @Autowired
    private EventsDao dao;

    @Autowired
    private EventService service;

    @PostMapping("/saveEvent")
    public String save(@RequestBody Events event) {
        dao.saveEvent(event);
        return "saved successfully";
    }

    @GetMapping("/getAllEvents")
    public List<Events> getAllEvents() {
        return dao.getEvents();
    }

    @PostMapping ("/scrapeEvents")

    // todo model to accept the urls
    public List<Events> scrapeEvents(@RequestBody Urls urls) {
        List<Events> eventList= service.scrapingEvents(urls.getUrls());
            dao.saveEventList(eventList);

        return eventList;
    }



}
