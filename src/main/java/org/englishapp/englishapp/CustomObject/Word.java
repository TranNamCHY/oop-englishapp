package org.englishapp.englishapp.CustomObject;

public class Word  {
    private String wordType;

    private String htmlType;

    public Word(String wordType, String htmlType ){
        this.wordType = wordType;
        this.htmlType = htmlType;
    }

    public String getWordType(){
        return this.wordType;
    }

    public void setWordType(String wordType){
        this.wordType = wordType;
    }

    public String getHtmlType(){
        return this.htmlType;
    }

    public void setHtmlType(String htmlType){
        this.htmlType = htmlType;
    }

}
