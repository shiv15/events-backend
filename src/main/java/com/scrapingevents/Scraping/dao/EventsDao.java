package com.scrapingevents.Scraping.dao;


import com.scrapingevents.Scraping.model.Events;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("deprecation")
@Repository
@Transactional
public class EventsDao {

    @Autowired
    private SessionFactory factory;

    /**
     * Save a single event to the database
     */
    public void saveEvent(Events event) {

        Session session = factory.openSession();
        List<Events> response = session.createCriteria(Events.class).add(Restrictions.eq("eventName", event.getEventName())).list();
        if(response.isEmpty())
            session.save(event);
        session.close();
    }

    /**
     * Save a list of events to the database.
     * @param event
     */

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

    /**
     * Get all the records from the database table.
     *
     * @return a list of Events.
     */


    public List<Events> getEvents() {
        Session session = factory.openSession();
        List<Events> response = session.createCriteria(Events.class).list();
        session.close();
        return response;
    }


}
