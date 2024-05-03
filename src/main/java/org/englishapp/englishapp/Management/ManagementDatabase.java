package org.englishapp.englishapp.Management;

import org.englishapp.englishapp.CustomObject.Word;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class ManagementDatabase {
    protected Connection sqlConnection;

    protected List<Word> searchResultList;

    public ManagementDatabase() {
        this.searchResultList = null;
    }

    public Connection getSqlConnection() {
        return this.sqlConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public List<Word> getSearchResultList() {
        return this.searchResultList;
    }

    public void setSearchResultList(List<Word> wordList) {
        this.searchResultList = wordList;
    }

    public abstract Word findWord(String wordType);

    public abstract void deleteWord(String wordType);

    public abstract void findMatchestWord(String wordType);

    public void addWord(String wordType, String wordExplain, String shortDescrip, String prounciation) {
    }


}
