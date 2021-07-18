package com.scrapingevents.Scraping.dao;


import com.scrapingevents.Scraping.model.Events;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
        List<Events> response = session.createCriteria(Events.class).add(Restrictions.eq("eventName", event.getEventName())).list();
        if(response.isEmpty())
            session.save(event);
        session.close();
    }

    public void saveEventList(List<Events> event) {
        Session session = factory.openSession();

        for(int i=0;i<event.size();i++){
            List<Events> response = session.createCriteria(Events.class).add(Restrictions.eq("eventName", event.get(i).getEventName())).list();
            if(response.isEmpty())
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
