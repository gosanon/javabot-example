package com.gosanon.javabotexample.main.quiz;

public class StatsList {
    public UserQuizStats overallStats;
    public UserQuizStats currentStats;

    public StatsList(UserQuizStats stats){
        this.overallStats = new UserQuizStats(0);
        this.currentStats = stats;
    }
}
