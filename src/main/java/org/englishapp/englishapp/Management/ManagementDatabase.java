package org.englishapp.englishapp.Management;

import org.englishapp.englishapp.CustomObject.Word;

import java.sql.Connection;
import java.util.List;

public abstract class ManagementDatabase {
    private Connection sqlConnection;

    private final List<Word> searchResultList;

    public ManagementDatabase() {
        this.searchResultList = null;
    }

    public abstract Word findWord(String wordType);

    public abstract void deleteWord(String wordType);

    public abstract void findMatchestWord(String wordType);

    public abstract void addWord(String wordType,String wordExplain,String shortDescrip,String prounciation);

}
