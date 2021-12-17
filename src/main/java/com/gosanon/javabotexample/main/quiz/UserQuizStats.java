package com.gosanon.javabotexample.main.quiz;

public class UserQuizStats {
    public final int questionNumber;
    public final int answeredQuestionsNumber;
    public final int correctAnswerNumber;
    public final int score;
    public final Question currentQuestion;

    public UserQuizStats(int questionNumber, int answeredQuestionsNumber,
                         int correctAnswerNumber, int score, Question currentQuestion){
        this.questionNumber = questionNumber;
        this.answeredQuestionsNumber = answeredQuestionsNumber;
        this.correctAnswerNumber = correctAnswerNumber;
        this.score = score;
        this.currentQuestion = currentQuestion;
    }

    public String toString(){
        return String.format(
                """
                        Вопросов сыграно: %d
                        Правильных ответов: %d
                        Очков набрано: %d""",
                this.answeredQuestionsNumber, this.correctAnswerNumber, this.score);
    }
}
