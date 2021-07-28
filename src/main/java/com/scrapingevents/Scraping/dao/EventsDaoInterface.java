package com.scrapingevents.Scraping.dao;

import com.scrapingevents.Scraping.model.Events;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventsDaoInterface extends PagingAndSortingRepository<Events, Integer> {
    Page<Events> findAll(Pageable pageable);
    List<Events> findByEventNameAndEventStartDateAndLocation(String eventName, Date eventStartDate, String location);
    List<Events> findAllByEventStartDateGreaterThanEqualAndEventStartDateLessThanEqual(Date startDate1, Date startDate2);
    Page<Events> findAllByEventStartDateGreaterThanEqualAndEventStartDateLessThanEqual(Date startDate1, Date startDate2, Pageable pageable);

}
