package com.framgia.elsytem.model;

/**
 * Created by ahsan on 12/21/15.
 */
public class Result {
    public String word;
    public String language;
    public int state;
    int id;
    public Result(int id,String word, String language, int state) {
        this.id=id;
        this.word = word;
        this.language = language;
        this.state = state;
    }
}
