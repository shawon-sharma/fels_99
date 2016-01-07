package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by sharma on 12/24/15.
 */
public class CategoryResponse {
    /**
     * categories : [{"id":15,"name":"Internal Interactions Supervisor","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/15/upload.png","learned_words":0},{"id":14,"name":"Senior Metrics Director","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/14/upload.png","learned_words":0}]
     * total_pages : 2
     */

    private int total_pages;
    /**
     * id : 15
     * name : Internal Interactions Supervisor
     * photo : https://herokuupload.s3.amazonaws.com/uploads/category/photo/15/upload.png
     * learned_words : 0
     */

    private List<CategoriesEntity> categories;

    public int getTotal_pages() {
        return total_pages;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }

    public static class CategoriesEntity {
        private int id;
        private String name;
        private String photo;
        private int learned_words;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public int getLearned_words() {
            return learned_words;
        }
    }
}
