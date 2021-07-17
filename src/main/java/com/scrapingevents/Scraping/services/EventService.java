package com.scrapingevents.Scraping.services;

import com.scrapingevents.Scraping.controller.EventController;
import com.scrapingevents.Scraping.dao.EventsDao;
import com.scrapingevents.Scraping.model.Events;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Repository
@Service
public class EventService {

    DateFormat formatCW = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
    DateFormat formatTM = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

    List<String> urls = Arrays.asList("https://www.computerworld.com/article/3313417/tech-event-calendar-2020-upcoming-shows-conferences-and-it-expos.html", "https://www.techmeme.com/events");  // todo: do i need to declare it as a private here or final?

    String pattern = ".*?\\.(.*?\\.[a-zA-Z]+)";
    Pattern regExp = Pattern.compile(pattern);


    String getWebsiteName(String url) {
        Matcher m = regExp.matcher(url);
        m.find();
        return m.group(0);
    }

    Document getDocument(String url) {
        try {
           return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }
    Events setEventDataCW(String[] tableRow, String websiteName) throws ParseException {
        Events event = new Events();
        event.setEventName(tableRow[0]);
        event.setEventStartDate(formatCW.parse(tableRow[2].replace("-","/")));
        event.setEventEndDate(formatCW.parse(tableRow[3].replace("-","/")));
        event.setLocation(tableRow[4]);
        event.setWebsiteName(getWebsiteName(websiteName));
        return event;
    }

    @SuppressWarnings("deprecation")
    Date getDateTM(String dateString) throws ParseException {
        if(dateString.isEmpty()) return null;
        Date dateObj = new Date();
        // @todo extra work here
//        String dateString = "Jul 19";
        Date date = formatTM.parse(dateString);
        int year = dateObj.getYear();
        date.setYear(year);
        System.out.println(date);
        return date;
    }


    Date getEndDateTM(String startDate, String endDate) throws ParseException {
        String endDateStr = getEndDateStrTM(startDate, endDate);

        return getDateTM(endDateStr);
    }

    String getEndDateStrTM(String startDate, String endDate) {
        String[] dateStrArr = endDate.split("\\s+");
        if(endDate.equals("")){
            return "";
        } else if(dateStrArr.length == 1){
            String month = startDate.split(" ")[0];
            return month+" "+endDate;
        } else if (dateStrArr.length == 2) {
            return endDate;
        } else {
            return "Invalid Date String Techmeme";
        }
    }

    Events setEventDataTM(Elements row, String websiteName) throws ParseException {
        Events event = new Events();
        String endDate = "";

        String dateString = row.select("div:nth-child(1)").text();
        String[] dateArr = dateString.split("-");

        String startDate = dateArr[0];
        if(dateArr.length>1){
            endDate = dateArr[1];
        }


        event.setEventName(row.select("div:nth-child(2)").text());
        event.setEventStartDate(getDateTM(startDate));

        event.setEventEndDate(getEndDateTM(startDate, endDate));
        event.setLocation(row.select("div:nth-child(3)").text());
        event.setWebsiteName(getWebsiteName(websiteName));
        System.out.println(event);
        return event;
    }

    List<Events>scrapeTM (String url, String websiteName) throws ParseException {

        List<Events> eventList = new ArrayList<>();
        Document doc = getDocument(url);
        Elements tables = doc.select("#events").select("div.rhov");


        for (Element table : tables) {
            Elements trs = table.select("a");


            eventList.add(setEventDataTM(trs, websiteName));

        }

        return eventList;
    }

    List<Events> scrapeCW(String url, String websiteName) throws ParseException {

        List<Events> eventList = new ArrayList<>();
        Document doc = getDocument(url);
        Elements tables = doc.select("\n" +
                "#cwsearchabletable").select("tbody");
        for (Element table : tables) {
            Elements trs = table.select("tr");
            String[][] trtd = new String[trs.size()][];
            for (int i = 0; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                trtd[i] = new String[tds.size()+1];
                trtd[i][0] = trs.get(i).select("th").text();
                for (int j = 1; j <= tds.size(); j++) {
                    trtd[i][j] = tds.get(j-1).text();

                }
                eventList.add(setEventDataCW(trtd[i], websiteName));
            }

        }

        return eventList;


    }
    @Transactional
    public List<Events> scrapingEvents(String[] urls){
        List<String> urlList = Arrays.asList(urls);
        List<Events> eventList = urlList.parallelStream()
                .map(url -> {
                        String websiteName = getWebsiteName(url);
                        try {
                            if(websiteName.equals("https://www.computerworld.com")) {
                                return scrapeCW(url, websiteName);
                            } else if(websiteName.equals("https://www.techmeme.com")){
                                return scrapeTM(url, websiteName);
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
