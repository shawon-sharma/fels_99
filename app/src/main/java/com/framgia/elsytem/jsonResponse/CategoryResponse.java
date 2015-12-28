package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by sharma on 12/24/15.
 */
public class CategoryResponse {

    /**
     * id : 15
     * name : International Mobility Liason
     */

    private List<CategoriesEntity> categories;

    public void setCategories(List<CategoriesEntity> categories) {
        this.categories = categories;
    }

    public List<CategoriesEntity> getCategories() {
        return categories;
    }

    public static class CategoriesEntity {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
