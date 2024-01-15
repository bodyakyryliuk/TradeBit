package com.tradebit.services;

import com.tradebit.exceptions.FailedToParseException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateConverter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date convertStringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new FailedToParseException("Failed to parse date: " + dateString + ".\n" + e);
        }
    }
}
