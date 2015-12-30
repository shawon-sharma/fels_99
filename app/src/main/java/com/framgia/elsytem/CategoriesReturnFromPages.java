package com.framgia.elsytem;

/**
 * Created by sharma on 12/30/15.
 */
public class CategoriesReturnFromPages{
    private String CategoriesName;
    private Integer categoriesId;

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
}
