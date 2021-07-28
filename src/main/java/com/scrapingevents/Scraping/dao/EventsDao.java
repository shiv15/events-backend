package com.scrapingevents.Scraping.dao;


import com.scrapingevents.Scraping.model.Events;
import com.scrapingevents.Scraping.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
@Repository
@Transactional
public class EventsDao {

    @Autowired
    private EventsDaoInterface eventsDaoInterface;
    private EventService service;

    /**
     * Save a single event to the database
//     */

    public String saveEvent(Events event) {
        List<Events> response = eventsDaoInterface.findByEventNameAndEventStartDateAndLocation(event.getEventName(), event.getEventStartDate(), event.getLocation());
        if(response.isEmpty()) {
            eventsDaoInterface.save(event);
            return "saved successfully.";
        }
        return "record exists.";
    }

    /**
     * Save a list of events to the database.
     * @param events
     */

    public String saveEventList(List<Events> events) {
        Date startDateRange = events.get(0).getEventStartDate();
        Date EndDateRange = events.get(events.size()-1).getEventStartDate();    //todo replace the logic with iterator - better practice
        List<Events> responseEvents = eventsDaoInterface.findAllByEventStartDateGreaterThanEqualAndEventStartDateLessThanEqual(startDateRange, EndDateRange);

        //todo move to a different method
        List<Events> uniqueEvents = new ArrayList<>();

        for(Events responseEvent : responseEvents) {
            Iterator<Events> eventIterator = events.iterator();
            while (eventIterator.hasNext()){

                Events events1 = eventIterator.next();
                //todo add date check for the event. the date types are different
                if (events1.getEventName().equals(responseEvent.getEventName()) && events1.getLocation().equals(responseEvent.getLocation())){
                    eventIterator.remove();      //Mutation
                }
            }

        }
        if(events.isEmpty()) return "No unique events";
        eventsDaoInterface.saveAll(events);
        return "Saved successfully";
    }

    /**
     * Get all the records from the database table.
     *
     * @return a list of Events.
     */


    public List<Events> getAllEvents() {
        Iterable<Events> response = eventsDaoInterface.findAll();
        return StreamSupport
                .stream(response.spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Events> getEvents(int pageNo, int pageSize, String eventStartDate, String eventEndDate, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Events> pagedResult = eventsDaoInterface.findAllByEventStartDateGreaterThanEqualAndEventStartDateLessThanEqual(new Date(eventStartDate), new Date(eventEndDate), paging);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Events>();
        }
    }

    public List<Events> getEvents(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        try {
            Page<Events> pagedResult = eventsDaoInterface.findAll(paging);
            if(pagedResult.hasContent()) {
                return pagedResult.getContent();
            } else {
                return new ArrayList<Events>();
            }
        } catch (Exception e){
            System.out.println("Something went wrong.");
            return new ArrayList<Events>();
        }
    }

}

