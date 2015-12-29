package com.framgia.elsytem.model;

import java.util.List;

/**
 * Created by ahsan on 12/15/15.
 */
public class Lesson {

    /**
     * id : 3
     * name : #3
     * words : [{"id":20,"result_id":123,"content":"v922p","answers":[{"id":77,"content":"dck8v","is_correct":false},{"id":78,"content":"mm9hf","is_correct":true},{"id":79,"content":"mcmwn","is_correct":false},{"id":80,"content":"lopus","is_correct":false}]}]
     */

    private LessonEntity lesson;

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
    }

    public LessonEntity getLesson() {
        return lesson;
    }

    public static class LessonEntity {
        private int id;
        private String name;
        /**
         * id : 20
         * result_id : 123
         * content : v922p
         * answers : [{"id":77,"content":"dck8v","is_correct":false},{"id":78,"content":"mm9hf","is_correct":true},{"id":79,"content":"mcmwn","is_correct":false},{"id":80,"content":"lopus","is_correct":false}]
         */

        private List<WordsEntity> words;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setWords(List<WordsEntity> words) {
            this.words = words;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<WordsEntity> getWords() {
            return words;
        }

        public static class WordsEntity {
            private int id;
            private int result_id;
            private String content;
            /**
             * id : 77
             * content : dck8v
             * is_correct : false
             */

            private List<AnswersEntity> answers;

            public void setId(int id) {
                this.id = id;
            }

            public void setResult_id(int result_id) {
                this.result_id = result_id;
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

            public int getResult_id() {
                return result_id;
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
}