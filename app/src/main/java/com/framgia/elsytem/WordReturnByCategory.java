package com.framgia.elsytem;

/**
 * Created by sharma on 12/30/15.
 */
public class WordReturnByCategory {
    private String wordname;
    private Integer wordid;

    public WordReturnByCategory(String wordname, Integer wordid) {
        this.wordname = wordname;
        this.wordid = wordid;
    }

    public String getWordname() {
        return wordname;
    }

    public Integer getWordid() {
        return wordid;
    }
}
