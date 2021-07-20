package com.scrapingevents.Scraping.services;

import com.scrapingevents.Scraping.model.Events;
import com.scrapingevents.Scraping.services.helper.CWHelper;
import com.scrapingevents.Scraping.services.helper.Common;
import com.scrapingevents.Scraping.services.helper.TMHelper;

import java.text.ParseException;
import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


@Repository
@Service
public class EventService {

    private CWHelper cwhelper = new CWHelper();
    private TMHelper tmHelper = new TMHelper();
    private Common common = new Common();


    @Transactional
    public List<Events> scrapingEvents(String[] urls){
        List<String> urlList = Arrays.asList(urls);
        List<Events> eventList = urlList.parallelStream()
                .map(url -> {
                        String websiteName = common.getWebsiteName(url);
                        try {
                            if(websiteName.equals("https://www.computerworld.com")) {
                                return cwhelper.scrapeCW(url, websiteName);
                            } else if(websiteName.equals("https://www.techmeme.com")){
                                return tmHelper.scrapeTM(url, websiteName);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                .filter(Objects::nonNull)
                .peek(it -> System.out.println(Thread.currentThread().getName() + ": " + it))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return eventList;

    }

}
