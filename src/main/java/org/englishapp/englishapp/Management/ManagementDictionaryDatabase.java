package org.englishapp.englishapp.Management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.englishapp.englishapp.CustomObject.Word;

public class ManagementDictionaryDatabase extends ManagementDatabase {

    private final String PATH_DATABASE = "jdbc:sqlite:src/main/resources/org/englishapp/englishapp/Database/dict_hh.db";

    public ManagementDictionaryDatabase() {
        this.searchResultList = new ArrayList<Word>();
        try {
            this.sqlConnection = DriverManager.getConnection(PATH_DATABASE);
        } catch (SQLException exception) {
            //throw new SQLException(exception);
            System.out.print(exception.getMessage());
        }
    }


    public Word findWord(String wordType) {
        String sqlQuery = "SELECT html FROM av WHERE word = ? COLLATE NOCASE";
        String htmlText = null;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType);
            ResultSet resultSet = preparedStatement.executeQuery();
            htmlText = resultSet.getString("html");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (htmlText == null) {
            return null;
        }
        return new Word(wordType, htmlText);
    }

    public void deleteWord(String wordType) {
        String sqlQuery = "Delete FROM av WHERE word = ? COLLATE NOCASE";
        String htmlText = null;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void findMatchestWord(String wordType) {
        this.searchResultList.clear();
        String sqlQuery = "SELECT word, html FROM av WHERE word LIKE ? ORDER BY word ASC LIMIT 5000";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.searchResultList.add(new Word(resultSet.getString("word"), resultSet.getString("html")));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean isExist(String wordType) {
        Word checkedWord = this.findWord(wordType);
        if (checkedWord != null) {
            return true;
        } else {
            return false;
        }
    }

    public void addWord(String wordType, String wordExplain, String shortDescrip, String prounciation) {
        String sql = "INSERT INTO av(word,html,description,pronounce) VALUES(?,?,?,?)";

        PreparedStatement preparedStatement;
        try {
            preparedStatement = this.sqlConnection.prepareStatement(sql);
            preparedStatement.setString(1, wordType);
            preparedStatement.setString(2, wordExplain);
            preparedStatement.setString(3, shortDescrip);
            preparedStatement.setString(4, prounciation);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
