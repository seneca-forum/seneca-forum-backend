package com.seneca.senecaforum.service.utils;

import com.seneca.senecaforum.service.constants.ApplicationConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationUtils {

    public static Date convertToDate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        return df.parse(date);
    }
}
