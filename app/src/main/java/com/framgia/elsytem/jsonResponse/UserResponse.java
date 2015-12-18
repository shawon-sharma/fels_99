package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by avishek on 12/17/15.
 */
public class UserResponse {

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
        private String remember_token;
        private String created_at;
        private String updated_at;

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

        public void setRemember_token(String remember_token) {
            this.remember_token = remember_token;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
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

        public String getRemember_token() {
            return remember_token;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
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
