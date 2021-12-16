package com.gosanon.javabotexample.main.quiz;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

public class QuizDB {
    protected static String dbFileName = "src/main/java/com/gosanon/javabotexample/main/quiz/quizDB.json";
    protected boolean isTemporal;
    protected HashMap<String, StatsList> quizDB = new HashMap<>();
    protected LinkedList<String> leaderboard = new LinkedList<>();
    public UserQuizStats overallStats(String id){ return quizDB.get(id).overallStats; }
    public UserQuizStats currentStats(String id){ return quizDB.get(id).currentStats; }

    public QuizDB(boolean isTemporal){
        this.isTemporal = isTemporal;
        if (!isTemporal){
            var cwd = Paths.get(".").toAbsolutePath().normalize();
            var dbFile = cwd.resolve(dbFileName);
            if (Files.exists(dbFile)) {
                try {
                    var content = Files.readString(dbFile, StandardCharsets.UTF_8);
                    var gson = new Gson();
                    Type type = new TypeToken<HashMap<String, StatsList>>(){}.getType();
                    quizDB = gson.fromJson(content, type);
                    for (var id : quizDB.keySet()) {
                        updateLeaderboard(id);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void putRecord(String id, UserQuizStats stats){
        if (quizDB.containsKey(id))
            quizDB.get(id).currentStats = stats;
        else {
            var statsList = new StatsList(stats);
            quizDB.put(id, statsList);
        }
    }

    public void updateUserData(String id){
        var overall = quizDB.get(id).overallStats;
        var current = quizDB.get(id).currentStats;
        overall.questionNumber += current.questionNumber;
        overall.answeredQuestionsNumber += current.answeredQuestionsNumber;
        overall.correctAnswerNumber += current.correctAnswerNumber;
        overall.score += current.score;
        overall.currentQuestion = current.currentQuestion;
        quizDB.get(id).currentStats = new UserQuizStats(0);
        updateLeaderboard(id);
        if (!isTemporal) {
            var gson = new Gson();
            var content = gson.toJson(quizDB);
            try {
                Files.writeString(Path.of(dbFileName), content, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateLeaderboard(String id){
        if (leaderboard.isEmpty()){
            leaderboard.add(id);
            return;
        }
        if (leaderboard.contains(id))
            leaderboard.remove(id);
        int count = 0;
        for (var e : leaderboard){
            if (quizDB.get(e).overallStats.score < quizDB.get(id).overallStats.score) {
                leaderboard.add(count, id);
                return;
            }
            count++;
        }
        leaderboard.addLast(id);
    }

    public String printLeaderboard(){
        var result = new StringBuilder("Список лидеров:\n");
        var count = 1;
        for (var e : leaderboard){
            var stats = quizDB.get(e).overallStats;
            result.append(String.format("%d. %s - %d%n", count, e, stats.score));
            count++;
        }

        return result.toString();
    }

}
