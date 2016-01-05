package com.framgia.elsytem;

/**
 * Created by sharma on 12/30/15.
 */
public class CategoriesReturnFromPages{
    private String CategoriesName;
    private Integer categoriesId;
    private String mCategoryImage;
    public CategoriesReturnFromPages(String categoriesName,Integer categoriesId,String mCategoryImage)
    {
        this.CategoriesName=categoriesName;
        this.categoriesId=categoriesId;
        this.mCategoryImage=mCategoryImage;
    }

    public CategoriesReturnFromPages(String categoriesName,Integer categoriesId)
    {
        this.CategoriesName=categoriesName;
        this.categoriesId=categoriesId;
    }
    public String getCategoriesName() {
        return CategoriesName;
    }

    public Integer getCategoriesId() {
        return categoriesId;
    }
    public String getmCategoryImage(){
        return mCategoryImage;
    }
}