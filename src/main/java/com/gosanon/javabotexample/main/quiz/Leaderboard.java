package com.gosanon.javabotexample.main.quiz;

import java.util.LinkedList;

import static com.gosanon.javabotexample.main.quiz.QuizHandlers.quizDB;

public class Leaderboard extends LinkedList<String> {

    @Override
    public String toString(){
        var result = new StringBuilder("Список лидеров:\n");
        var count = 1;
        for (var e : this){
            var stats = quizDB.getOverallStats(e);
            result.append(String.format("%d. %s - %d%n", count, e, stats.score));
            count++;
        }

        return result.toString();
    }
}
