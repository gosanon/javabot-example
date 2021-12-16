package com.gosanon.javabotexample.main.quiz;

public class UserQuizStats {
    public int questionNumber;
    public int answeredQuestionsNumber = 0;
    public int correctAnswerNumber = 0;
    public int score = 0;
    public Question currentQuestion;

    public UserQuizStats(int questionNumber){
        this.questionNumber = questionNumber;
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
