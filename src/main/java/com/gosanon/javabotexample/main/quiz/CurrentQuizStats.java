package com.gosanon.javabotexample.main.quiz;

public class CurrentQuizStats extends UserQuizStats {
    public final int questionsInQuiz;
    public final Question currentQuestion;

    public CurrentQuizStats(){
        super();
        this.questionsInQuiz = 0;
        this.currentQuestion = null;
    }

    public CurrentQuizStats(int questionsInQuiz, int answeredQuestionsNumber,
                            int correctAnswerNumber, int score, Question currentQuestion) {
        super(answeredQuestionsNumber, correctAnswerNumber, score);
        this.questionsInQuiz = questionsInQuiz;
        this.currentQuestion = currentQuestion;
    }
}
