package com.scrapingevents.Scraping.services.helper;

import com.scrapingevents.Scraping.model.Events;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    private String pattern = ".*?\\.(.*?\\.[a-zA-Z]+)";
    private Pattern regExp = Pattern.compile(pattern);


    public String getWebsiteName(String url) {
        Matcher m = regExp.matcher(url);
        m.find();
        return m.group(0);
    }

    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }

    public List<Events> getUniqueRecords(List<Events> toSave,List<Events> fromDB){
        return new ArrayList<>();
    }
}
