package com.scrapingevents.Scraping.dao;


import com.scrapingevents.Scraping.model.Events;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class EventsDao {

    @Autowired
    private SessionFactory factory;

    public void saveEvent(Events event) {

        Session session = factory.openSession();

        session.save(event);
        session.close();
    }

    public void saveEventList(List<Events> event) {
        Session session = factory.openSession();

        for(int i=0;i<event.size();i++){
            session.save(event.get(i));
            if(i%20==0) {
                session.clear();
            }
        }
        session.close();
    }


    @SuppressWarnings("deprecation")
    public List<Events> getEvents() {
        Session session = factory.openSession();
        List<Events> response = session.createCriteria(Events.class).list();
        session.close();
        return response;
    }




}
