package com.gosanon.javabotexample.main.quiz.stats;

public class StatsList {
    public UserQuizStats overallStats;
    public CurrentQuizStats currentQuizStats;

    public StatsList(CurrentQuizStats stats){
        this.overallStats = new UserQuizStats();
        this.currentQuizStats = stats;
    }
}
