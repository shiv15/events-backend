package com.scrapingevents.Scraping.services.helper;

import com.scrapingevents.Scraping.model.Events;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TMHelper {

    private Common common = new Common();

    DateFormat formatTM = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

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


    Date getDateTM(String dateString) throws ParseException {
        if(dateString.isEmpty()) return null;
        Date dateObj = new Date();

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
        Date endDateOpt = getEndDateTM(startDate, endDate);
        if(Objects.isNull(endDateOpt)) event.setEventEndDate(getDateTM(startDate));
        else event.setEventEndDate(getEndDateTM(startDate, endDate));
        event.setLocation(row.select("div:nth-child(3)").text());
        event.setWebsiteName(common.getWebsiteName(websiteName));
        System.out.println(event);
        return event;
    }

    public List<Events> scrapeTM (String url, String websiteName) throws ParseException {

        List<Events> eventList = new ArrayList<>();
        Document doc = common.getDocument(url);
        Elements tables = doc.select("#events").select("div.rhov");


        for (Element table : tables) {
            Elements trs = table.select("a");
            eventList.add(setEventDataTM(trs, websiteName));
        }
        return eventList;
    }
}
