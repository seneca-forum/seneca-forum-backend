package com.seneca.senecaforum.service.utils;

import java.util.ArrayList;
import java.util.List;

public class ContentConverterUtils {
    public static String extractContentWithKeywords(String text, String keyword){
        List<Integer> idxs = new ArrayList<>();
        String result = null;
        String[] tokens = keyword.split(" ");
        // using different search patterns to get the start and end position of keyword in the text
        if(tokens.length==1){
            idxs = getIdxsShortKeywords(text, keyword);
        }
        else{
            idxs = getIdxsLongKeywords(text, keyword);
        }
        // check if there are any results
        if(idxs.size()>0){
            result = extractP(text, idxs);
        }
        return result;
    }

    public static List<Integer>getIdxsShortKeywords(String text, String keyword){
        List<Integer>idxs = new ArrayList<>();
        if(text.indexOf(keyword)!=-1){
            idxs.addAll(List.of(text.indexOf(keyword),text.indexOf(keyword)+keyword.length()));
        }
        return idxs;
    }

    public static List<Integer>getIdxsLongKeywords(String text, String keyword){
        List<Integer>maxIdxs = new ArrayList<>();
        String[] splitArr = null;
        String[] tokens = keyword.split(" ");
        int lastIdx = tokens.length - 1;
        while(lastIdx!=0){
            String first = keyword.split(" ")[0];
            String last = keyword.split(" ")[lastIdx];
            String pattern = "\\b(?:"+first+"\\W+(?:\\w+\\W+){0,5}?"+last+"|"+last+"\\W+(?:\\w+\\W+){0,5}?"+first+")\\b";
            splitArr = text.split(pattern);
            // if found nothing, decrement lastIdx to the previous last and keep finding, if found, stop the loop
            if(splitArr[0].length() == text.length()){
                lastIdx--;
            }
            else{
                lastIdx = 0;
                // set the exact keyword that matches with content. For ex: small beautiful company -> small company, small company beautiful -> small company
                keyword = first+" "+last;
            }
        }
        // if found the pattern
        if(splitArr[0].length() != text.length()){
            // get the max substring keywords found: small comapny, small beautifyl company, e.g
            int max = 0;
            int[]temp = new int[2];
            int sumLength = 0;
            for(int i = 0; i < splitArr.length-1; i++){
                int startIdx = sumLength+splitArr[i].length();
                int endIdx = sumLength+splitArr[i].length()+keyword.length();
                String extracted = text.substring(startIdx,endIdx);
                sumLength+=splitArr[i].length()+keyword.length();

                if(max<extracted.length()){
                    temp[0] = startIdx;
                    temp[1] = endIdx;
                }
                max = Math.max(extracted.length(), max);
            }
            maxIdxs.addAll(List.of(temp[0], temp[1]));
        }
        return maxIdxs;
    }

    public static String extractP(String text, List<Integer>idxs){
        String firstBlock = text.substring(0,idxs.get(0));
        String secondBlock = text.substring(idxs.get(1));
        int pElem = firstBlock.lastIndexOf("<p>");
        int pendElem = text.indexOf(secondBlock)+ secondBlock.indexOf("</p")+4;
        String result = text.substring(pElem, pendElem);
        return result;
    }
}
