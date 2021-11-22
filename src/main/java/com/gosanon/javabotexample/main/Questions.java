package com.gosanon.javabotexample.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Questions {
    private final String urlAddress;
    private ArrayList<Question> questions = new ArrayList<>();
    private int questionsSent = 0;

    public Questions(int count) {
        urlAddress = "https://jservice.io/api/random?count=" + count;
        try {
            var data = new String(
                    new URL(urlAddress)
                            .openStream()
                            .readAllBytes()
            );
            dataParsing(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dataParsing(String data){
        data = data.substring(2, data.length() - 2);
        for(var element : data.split("},\\{")){
            var questionRegex = "\"question\":\"(.+?[^\\\\])\"";
            var answerRegex = "\"answer\":\"(.+?[^\\\\])\"";
            var valueRegex = "\"value\":(\\d+)";
            var question = findMatchByRegex(element, questionRegex);
            var answer = findMatchByRegex(element, answerRegex);
            var questionValue = Integer.parseInt(findMatchByRegex(element, valueRegex));
            questions.add(new Question(question, answer, questionValue));
        }
    }

    private String findMatchByRegex(String text, String regex){
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    public Question nextQuestion(){
        if (questionsSent == questions.size())
            throw new ArrayIndexOutOfBoundsException();
        var nextQuestion = questions.get(questionsSent);
        questionsSent++;
        return nextQuestion;
    }
}
