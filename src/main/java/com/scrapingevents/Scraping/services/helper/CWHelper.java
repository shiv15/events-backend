package com.scrapingevents.Scraping.services.helper;

import com.scrapingevents.Scraping.model.Events;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CWHelper {

    private Common common = new Common();
    private DateFormat formatCW = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);


    Events setEventDataCW(String[] tableRow, String websiteName) throws ParseException {
        Events event = new Events();
        event.setEventName(tableRow[0]);
        event.setEventStartDate(formatCW.parse(tableRow[2].replace("-","/")));
        event.setEventEndDate(formatCW.parse(tableRow[3].replace("-","/")));
        event.setLocation(tableRow[4]);
        event.setWebsiteName(common.getWebsiteName(websiteName));
        return event;
    }


    public List<Events> scrapeCW(String url, String websiteName) throws ParseException {

        List<Events> eventList = new ArrayList<>();
        Document doc = common.getDocument(url);
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

}
