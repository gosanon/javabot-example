package com.gosanon.javabotexample.main.quiz.questions;

import java.io.*;
import java.net.URL;
import com.google.gson.Gson;
import com.gosanon.javabotexample.main.quiz.questions.Question;

public class QuestionsProvider {
    protected static Question parseData(String data){
        var arrayOfOneToObject = data.substring(1, data.length() - 1);
        var gson = new Gson();
        return gson.fromJson(arrayOfOneToObject, Question.class);
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
