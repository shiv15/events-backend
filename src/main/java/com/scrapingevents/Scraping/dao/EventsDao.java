package com.scrapingevents.Scraping.dao;


import com.scrapingevents.Scraping.model.Events;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class EventsDao {

    @Autowired
    private SessionFactory factory;

    public Session getSession() {
        Session session = factory.getCurrentSession();
        if (session == null) {
            session = factory.openSession();
        }

        return session;
    }

    public void saveEvent(Events event) {
        getSession().save(event);
    }

    @SuppressWarnings("deprecation")
    public List<Events> getEvents() {
        return getSession().createCriteria(Events.class).list();
    }

}
