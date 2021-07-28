package com.scrapingevents.Scraping.controller;


import com.scrapingevents.Scraping.dao.EventsDao;
import com.scrapingevents.Scraping.model.Events;

import com.scrapingevents.Scraping.model.Urls;
import com.scrapingevents.Scraping.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PropertySource(value = {"classpath:application.properties"})
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://scraping-events.web.app"})
@RequestMapping("")
public class EventController {
    @Autowired
    private EventsDao dao;

    @Autowired
    private EventService service;

    @PostMapping("/saveEvent")
    public String save(@RequestBody Events event) {
        return dao.saveEvent(event);
    }

    @GetMapping("/getAllEvents")
    public List<Events> getAllEvents() {
        return dao.getAllEvents();
    }


    @GetMapping("/getEvents")
    public List<Events> getEvents(@RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam(defaultValue = "empty") String eventStartDate,
                                  @RequestParam(defaultValue = "empty") String eventEndDate,
                                  @RequestParam(defaultValue = "eventName") String sortBy) {
        if(eventStartDate.equals("empty")){
            return dao.getEvents(pageNo, pageSize, sortBy);
        }
        return dao.getEvents(pageNo, pageSize, eventStartDate, eventEndDate, sortBy);
    }


    @PostMapping ("/scrapeEvents")
    public String scrapeEvents(@RequestBody Urls urls) {
        List<Events> eventList= service.scrapingEvents(urls.getUrls());
        return dao.saveEventList(eventList);
    }



}
