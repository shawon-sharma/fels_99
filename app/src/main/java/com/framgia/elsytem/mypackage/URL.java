package com.framgia.elsytem.mypackage;

/**
 * Created by sharma on 12/29/15.
 */
public class URL {

    String wordFetchURL="https://manh-nt.herokuapp.com/words.json?";
    String categoryFetchURL="https://manh-nt.herokuapp.com/categories.json?";
    public String getWordfetchurl()
    {
        return wordFetchURL;
    }
    public String getCategoryFetchURL(){return categoryFetchURL;}

}
