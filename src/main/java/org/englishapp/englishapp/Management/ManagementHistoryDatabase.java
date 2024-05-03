package org.englishapp.englishapp.Management;

import org.englishapp.englishapp.CustomObject.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagementHistoryDatabase extends ManagementDatabase {
    private final String PATH_DATABASE = "jdbc:sqlite:src/main/resources/org/englishapp/englishapp/Database/history.db";

    public ManagementHistoryDatabase() {
        this.searchResultList = new ArrayList<Word>();
        try {
            this.sqlConnection = DriverManager.getConnection(PATH_DATABASE);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Word findWord(String wordType) {
        String sqlQuery = "SELECT html FROM av WHERE word = '?' COLLATE NOCASE";
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
        String sqlQuery = "Delete FROM history WHERE Word = ? COLLATE NOCASE";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addWord(String wordType) {
        this.deleteWord(wordType);
        String sqlQuery = "INSERT INTO history(Id,Word) VALUES(NULL,?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Word> showHistory() {
        String sqlQuery = "SELECT * FROM history";
        String word = "";
        PreparedStatement preparedStatement;
        this.searchResultList.clear();
        ResultSet resultSet;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.searchResultList.add(new Word(resultSet.getString("Word"), null));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return this.searchResultList;
    }

    public void findMatchestWord(String wordType) {
        this.searchResultList.clear();
        String sqlQuery = "SELECT Word FROM history WHERE Word LIKE ? ORDER BY Word ASC LIMIT 100";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = sqlConnection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, wordType + "%");
            System.out.print(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                this.searchResultList.add(new Word(resultSet.getString("Word"), null));
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
