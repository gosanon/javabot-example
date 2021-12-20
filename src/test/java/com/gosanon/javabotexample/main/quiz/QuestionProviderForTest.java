package com.gosanon.javabotexample.main.quiz;

import com.gosanon.javabotexample.main.quiz.questions.Question;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionProviderForTest {
    public static ArrayList<Question> questions = new ArrayList<>(Arrays.asList(
            new Question(
                    "А и б сидели на трубе, а упало, б пропало, что осталось на трубе?",
                    "и",
                    10),
            new Question(
                    "В каком городе жил Сократ?",
                    "Афины",
                    10),
            new Question(
                    "Какая высота у горы Эверест, в метрах?",
                    "8848",
                    25),
            new Question(
                    "Сколько колонн у входа в Большой театр в Москве?",
                    "8",
                    40),
            new Question(
                    "Как называется первый серийный спорткар, который мог разогнаться до 400 км/ч?",
                    "Bugatti Veyron",
                    40),
            new Question(
                    "На каком месте в мире по числу носителей немецкий язык?",
                    "12",
                    40)
    ));
    private static int usedQuestionsCount = 0;

    public static void refresh(){
        usedQuestionsCount = 0;
    }

    public static Question nextQuestion(){
        usedQuestionsCount++;
        return questions.get(usedQuestionsCount - 1);
    }
}