package com.gosanon.javabotexample.main;

import java.io.*;
import java.net.URL;
import java.util.regex.Pattern;

public class Questions {
    protected static Question parseData(String data){
        data = data.substring(2, data.length() - 2);
        var questionRegex = "\"question\":\"(.+?[^\\\\])\"";
        var answerRegex = "\"answer\":\"(.+?[^\\\\])\"";
        var valueRegex = "\"value\":(\\d+)";
        var question = findMatchByRegex(data, questionRegex);
        var answer = findMatchByRegex(data, answerRegex);
        var questionValue = Integer.parseInt(findMatchByRegex(data, valueRegex));
        return new Question(question,answer, questionValue);
    }

    protected static String findMatchByRegex(String text, String regex){
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static Question nextQuestion(){
        var urlAddress = "https://jservice.io/api/random";
        var data = "";
        try {
            data = new String(
                    new URL(urlAddress)
                            .openStream()
                            .readAllBytes()
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return parseData(data);
    }
}
