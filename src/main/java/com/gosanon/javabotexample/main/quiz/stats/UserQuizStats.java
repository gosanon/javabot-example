package com.gosanon.javabotexample.main.quiz.stats;

public class UserQuizStats {
    public final int answeredQuestionsNumber;
    public final int correctAnswerNumber;
    public final int score;


    public UserQuizStats(){
        this.answeredQuestionsNumber = 0;
        this.correctAnswerNumber = 0;
        this.score = 0;
    }

    public UserQuizStats(int answeredQuestionsNumber, int correctAnswerNumber, int score) {
        this.answeredQuestionsNumber = answeredQuestionsNumber;
        this.correctAnswerNumber = correctAnswerNumber;
        this.score = score;
    }

    public String toString(){
        return String.format(
            """
                Вопросов сыграно: %d
                Правильных ответов: %d
                Очков набрано: %d""",
            this.answeredQuestionsNumber, this.correctAnswerNumber, this.score);
    }

    public UserQuizStats updateStatsWith(UserQuizStats statsSource){
        return new UserQuizStats(
            this.answeredQuestionsNumber + statsSource.answeredQuestionsNumber,
            this.correctAnswerNumber + statsSource.correctAnswerNumber,
            this.score + statsSource.score);
    }
}
