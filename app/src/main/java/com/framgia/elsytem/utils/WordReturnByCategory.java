package com.framgia.elsytem.utils;

import java.util.ArrayList;

/**
 * Created by sharma on 12/30/15.
 */
public class WordReturnByCategory {
    private ArrayList<String> word;
    private ArrayList<String> ans;
    private String nWord;
    private String nAns;

    public WordReturnByCategory(ArrayList<String> word, ArrayList<String>
            ans) {
        this.word = word;
        this.ans = ans;
    }

    public WordReturnByCategory(String nWord, String nAns) {
        this.nWord = nWord;
        this.nAns = nAns;
    }

    public ArrayList<String> getWordList() {
        return word;
    }

    public ArrayList<String> getAnswerList() {
        return ans;
    }

    public String getSingleWord() {
        return nWord;
    }

    public String getSingleAnswer() {
        return nAns;
    }
}
