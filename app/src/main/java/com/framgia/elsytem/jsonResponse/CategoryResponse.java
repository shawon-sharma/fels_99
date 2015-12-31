package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by sharma on 12/24/15.
 */
public class CategoryResponse {

    /**
     * categories : [{"id":5,"name":"Senior Operations Officer","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/5/upload.png"},{"id":4,"name":"District Integration Facilitator","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/4/upload.png"},{"id":3,"name":"District Optimization Administrator","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/3/upload.png"},{"id":2,"name":"Human Division Developer","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/2/upload.png"},{"id":1,"name":"Global Applications Manager","photo":"https://herokuupload.s3.amazonaws.com/uploads/category/photo/1/upload.png"}]
     * total_pages : 2
     */

    private int total_pages;
    /**
     * id : 5
     * name : Senior Operations Officer
     * photo : https://herokuupload.s3.amazonaws.com/uploads/category/photo/5/upload.png
     */

    private List<CategoriesEntity> categories;

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setCategories(List<CategoriesEntity> categories) {
        this.categories = categories;
    }

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

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }
    }
}
