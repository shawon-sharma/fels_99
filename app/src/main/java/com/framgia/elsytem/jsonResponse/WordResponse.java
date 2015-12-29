package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by sharma on 12/28/15.
 */
public class WordResponse{

    /**
     * id : 1
     * content : fe9kk
     * answers : [{"id":1,"content":"p2jh8","is_correct":true},{"id":2,"content":"dg0st","is_correct":false},{"id":3,"content":"2vq33","is_correct":false},{"id":4,"content":"l8t2b","is_correct":false}]
     */

    private List<WordsEntity> words;

    public void setWords(List<WordsEntity> words) {
        this.words = words;
    }

    public List<WordsEntity> getWords() {
        return words;
    }

    public static class WordsEntity {
        private int id;
        private String content;
        /**
         * id : 1
         * content : p2jh8
         * is_correct : true
         */

        private List<AnswersEntity> answers;

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setAnswers(List<AnswersEntity> answers) {
            this.answers = answers;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public List<AnswersEntity> getAnswers() {
            return answers;
        }

        public static class AnswersEntity {
            private int id;
            private String content;
            private boolean is_correct;

            public void setId(int id) {
                this.id = id;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setIs_correct(boolean is_correct) {
                this.is_correct = is_correct;
            }

            public int getId() {
                return id;
            }

            public String getContent() {
                return content;
            }

            public boolean isIs_correct() {
                return is_correct;
            }
        }
    }
}