package com.scrapingevents.Scraping.controller;


import com.scrapingevents.Scraping.dao.EventsDao;
import com.scrapingevents.Scraping.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class EventController {
    @Autowired
    private EventsDao dao;

    @PostMapping("/saveEvent")
    public String save(@RequestBody Events event) {
        dao.saveEvent(event);
        return "saved successfully";
    }

    @GetMapping("/getAllEvents")
    public List<Events> getAllEvents() {
        return dao.getEvents();

    }

}
