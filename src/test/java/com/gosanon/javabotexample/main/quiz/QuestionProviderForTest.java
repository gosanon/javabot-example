package com.gosanon.javabotexample.main.quiz;

import java.util.ArrayList;

public class QuestionProviderForTest {
    public static ArrayList<Question> questions;
    public static int usedQuestionsCount = 0;

    public static void prepareQuestions(int count){
        questions = new ArrayList<>();
        usedQuestionsCount = 0;
        for(int i = 0; i < count; i++)
            questions.add(QuestionsProvider.nextQuestion());
    }
    public static Question nextQuestion(){
        usedQuestionsCount++;
        return questions.get(usedQuestionsCount - 1);
    }
}
