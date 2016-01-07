package com.framgia.elsytem;

/**
 * Created by sharma on 12/30/15.
 */
public class CategoriesReturnFromPages {
    private String CategoriesName;
    private Integer categoriesId;
    private String mCategoryImage;
    private int mLearnedWords;

    public CategoriesReturnFromPages(String categoriesName, Integer categoriesId, String
            mCategoryImage, int mLearnedWords) {
        this.CategoriesName = categoriesName;
        this.categoriesId = categoriesId;
        this.mCategoryImage = mCategoryImage;
        this.mLearnedWords = mLearnedWords;
    }

    public CategoriesReturnFromPages(String categoriesName, Integer categoriesId) {
        this.CategoriesName = categoriesName;
        this.categoriesId = categoriesId;
    }

    public int getmLearnedWords() {
        return mLearnedWords;
    }

    public String getCategoriesName() {
        return CategoriesName;
    }

    public Integer getCategoriesId() {
        return categoriesId;
    }

    public String getmCategoryImage() {
        return mCategoryImage;
    }
}