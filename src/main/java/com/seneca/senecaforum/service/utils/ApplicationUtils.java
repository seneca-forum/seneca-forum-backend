package com.seneca.senecaforum.service.utils;

import com.seneca.senecaforum.service.constants.ApplicationConstants;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.SerializationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationUtils {

    //convert String to Date
    public static Date convertToDate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        return df.parse(date);
    }

    //encode
    public static String serializeData(String original){
        byte[] input = SerializationUtils.serialize((original));
        return Base64.encodeBase64String(input);
    }

    //decode
    public static String deserializeData(String encrypted){
        try{
            if (Base64.isBase64(encrypted)){
                byte[]decodeOutput = Base64.decodeBase64(encrypted);
                return (String) SerializationUtils.deserialize(decodeOutput);
            }
            return encrypted;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
