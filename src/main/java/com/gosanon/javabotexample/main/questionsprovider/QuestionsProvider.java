package com.gosanon.javabotexample.main.questionsprovider;

import com.gosanon.javabotexample.main.Main;

import java.io.*;
import java.net.URL;
import java.util.regex.Pattern;

public class QuestionsProvider {
    protected static Question parseData(String data){
        data = data.substring(2, data.length() - 2);
        var questionRegex = "\"question\":\"(.+?[^\\\\])\"";
        var answerRegex = "\"answer\":\"(.+?[^\\\\])\"";
        var valueRegex = "\"value\":(\\d+)";
        var question = Main.findMatchByRegex(data, questionRegex);
        var answer = Main.findMatchByRegex(data, answerRegex);
        var questionValue = Integer.parseInt(Main.findMatchByRegex(data, valueRegex));
        return new Question(question,answer, questionValue);
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
