package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by avishek on 1/4/16.
 */
public class ShowUserResponse {

    /**
     * id : 1
     * name : Nguyen Tien Manh
     * email : example@railstutorial.org
     * avatar : https://manh-nt.herokuapp.com/uploads/user/avatar/1/Screenshot_from_2015-12-14_14_47_00.png
     * admin : true
     * created_at : 2015-12-11T03:30:31.000Z
     * updated_at : 2015-12-11T04:09:51.000Z
     * learned_words : 1
     * activities : [{"id":1,"content":"Logout","created_at":"2015-12-11T03:37:06.000Z"},{"id":2,"content":"Login","created_at":"2015-12-11T03:37:08.000Z"}]
     */

    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public static class UserEntity {
        private int id;
        private String name;
        private String email;
        private String avatar;
        private boolean admin;
        private String created_at;
        private String updated_at;
        private int learned_words;
        /**
         * id : 1
         * content : Logout
         * created_at : 2015-12-11T03:37:06.000Z
         */

        private List<ActivitiesEntity> activities;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public void setLearned_words(int learned_words) {
            this.learned_words = learned_words;
        }

        public void setActivities(List<ActivitiesEntity> activities) {
            this.activities = activities;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getAvatar() {
            return avatar;
        }

        public boolean isAdmin() {
            return admin;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public int getLearned_words() {
            return learned_words;
        }

        public List<ActivitiesEntity> getActivities() {
            return activities;
        }

        public static class ActivitiesEntity {
            private int id;
            private String content;
            private String created_at;

            public void setId(int id) {
                this.id = id;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getId() {
                return id;
            }

            public String getContent() {
                return content;
            }

            public String getCreated_at() {
                return created_at;
            }
        }
    }
}
