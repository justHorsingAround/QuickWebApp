package com.codecool.web.services;


import Doggo.api.Doggo;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class UserService {
    private final Doggo doggo;

    public UserService(Doggo dog) {
        this.doggo = dog;
    }

    public Map<String,String> loginUser(String id, String targetTable, String columnName) throws IllegalArgumentException, IOException {
        String sql = "SELECT * FROM " + targetTable;
        doggo.autoSend(false);
        doggo.executeSelect(sql);
        List<Map<String, String>> users = doggo.getResult();
        for (Map<String,String> user: users) {
            if(user.get(columnName).equals(id)){
                return user;
            }
        }
        return null;
    }

}
