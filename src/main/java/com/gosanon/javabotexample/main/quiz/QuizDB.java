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

public class QuizDB {
    private final static String dbFileName = "src/main/java/com/gosanon/javabotexample/main/quiz/quizDB.json";
    private HashMap<String, StatsList> quizDB = new HashMap<>();
    public Leaderboard leaderboard = new Leaderboard();
    public UserQuizStats getOverallStats(String id){ return quizDB.get(id).overallStats; }
    public CurrentQuizStats getCurrentQuizStats(String id){ return quizDB.get(id).currentQuizStats; }

    public QuizDB(){
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

    public void putRecord(String id, CurrentQuizStats stats){
        if (quizDB.containsKey(id))
            quizDB.get(id).currentQuizStats = stats;
        else {
            var statsList = new StatsList(stats);
            quizDB.put(id, statsList);
        }
        saveQuizDB();
    }

    public void updateUserStats(String id){
        quizDB.get(id).overallStats = quizDB.get(id).overallStats.
                updateStatsWith(quizDB.get(id).currentQuizStats);
        quizDB.get(id).currentQuizStats = new CurrentQuizStats();
        updateLeaderboard(id);
        saveQuizDB();
    }

    public void saveQuizDB(){
        var gson = new Gson();
        var content = gson.toJson(quizDB);
        try {
            Files.writeString(Path.of(dbFileName), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void updateLeaderboard(String id){
        if (leaderboard.isEmpty()){
            leaderboard.add(id);
            return;
        }
        leaderboard.remove(id);
        var count = 0;
        var scoreIsMinimal = true;
        for (var e : leaderboard){
            if (quizDB.get(e).overallStats.score < quizDB.get(id).overallStats.score) {
                scoreIsMinimal = false;
                break;
            }
            count++;
        }
        if (!scoreIsMinimal)
            leaderboard.add(count, id);
        else
            leaderboard.addLast(id);
    }
}
