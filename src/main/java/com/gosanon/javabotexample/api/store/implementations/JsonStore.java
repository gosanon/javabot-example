package com.gosanon.javabotexample.api.store.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonStore<TUserData> extends RuntimeStore<TUserData> {
    protected String filePath;

    JsonStore(String filePath, String defaultState, TUserData defaultRecord) {
        super(defaultState, defaultRecord);

        this.filePath = filePath;

        var cwd = Paths.get(".").toAbsolutePath().normalize();
        var dbFile = cwd.resolve(filePath);
        if (Files.exists(dbFile)) {
            String content = null;
            try {
                content = Files.readString(dbFile, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }

            var gson = new Gson();
            Type type = new TypeToken<RuntimeStore<TUserData>>(){}.getType();
            this.db.putAll(gson.fromJson(content, type));
        }
    }

    void sync() {
        var gson = new Gson();
        var content = gson.toJson(this.db);
        try {
            Files.writeString(Path.of(filePath), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
