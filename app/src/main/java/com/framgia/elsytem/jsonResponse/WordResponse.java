package com.framgia.elsytem.jsonResponse;

import java.util.List;

/**
 * Created by sharma on 12/28/15.
 */
public class WordResponse {
    /**
     * words : [{"id":560,"content":"fwunf","result_id":null,"answers":[{"id":2240,"content":"sa77w","is_correct":false},{"id":2239,"content":"p9et3","is_correct":false},{"id":2238,"content":"rrm85","is_correct":false},{"id":2237,"content":"gj3z3","is_correct":true}]},{"id":559,"content":"2ulab","result_id":null,"answers":[{"id":2236,"content":"kigh6","is_correct":false},{"id":2235,"content":"fkohx","is_correct":true},{"id":2234,"content":"ydnp1","is_correct":false},{"id":2233,"content":"3852r","is_correct":false}]},{"id":558,"content":"n4te4","result_id":null,"answers":[{"id":2232,"content":"rp18c","is_correct":true},{"id":2231,"content":"0ovnl","is_correct":false},{"id":2230,"content":"redju","is_correct":false},{"id":2229,"content":"f40ij","is_correct":false}]},{"id":557,"content":"3v5te","result_id":null,"answers":[{"id":2228,"content":"rz4hd","is_correct":true},{"id":2227,"content":"d8115","is_correct":false},{"id":2226,"content":"awkht","is_correct":false},{"id":2225,"content":"msg19","is_correct":false}]},{"id":556,"content":"vp752","result_id":null,"answers":[{"id":2224,"content":"t1epl","is_correct":false},{"id":2223,"content":"a8mwr","is_correct":true},{"id":2222,"content":"8mkh9","is_correct":false},{"id":2221,"content":"h7mbt","is_correct":false}]},{"id":555,"content":"kyayt","result_id":null,"answers":[{"id":2220,"content":"3mi9l","is_correct":false},{"id":2219,"content":"zbbak","is_correct":false},{"id":2218,"content":"hqugi","is_correct":false},{"id":2217,"content":"i77cx","is_correct":true}]},{"id":554,"content":"jylxz","result_id":null,"answers":[{"id":2216,"content":"r6bcf","is_correct":false},{"id":2215,"content":"n3sc1","is_correct":false},{"id":2214,"content":"zcou0","is_correct":true},{"id":2213,"content":"54qy6","is_correct":false}]},{"id":553,"content":"m2qy0","result_id":null,"answers":[{"id":2212,"content":"usk6j","is_correct":true},{"id":2211,"content":"0wnpz","is_correct":false},{"id":2210,"content":"keogv","is_correct":false},{"id":2209,"content":"qet5p","is_correct":false}]},{"id":552,"content":"fbqwu","result_id":null,"answers":[{"id":2208,"content":"dqtda","is_correct":false},{"id":2207,"content":"sobco","is_correct":false},{"id":2206,"content":"vgmpe","is_correct":false},{"id":2205,"content":"1t7fm","is_correct":true}]},{"id":551,"content":"lx5nj","result_id":null,"answers":[{"id":2204,"content":"3978y","is_correct":false},{"id":2203,"content":"o8r9w","is_correct":false},{"id":2202,"content":"0h7tm","is_correct":false},{"id":2201,"content":"xsi67","is_correct":true}]}]
     * total_pages : 4
     */

    private int total_pages;
    /**
     * id : 560
     * content : fwunf
     * result_id : null
     * answers : [{"id":2240,"content":"sa77w","is_correct":false},{"id":2239,"content":"p9et3","is_correct":false},{"id":2238,"content":"rrm85","is_correct":false},{"id":2237,"content":"gj3z3","is_correct":true}]
     */

    private List<WordsEntity> words;

    public int getTotal_pages() {
        return total_pages;
    }

    public List<WordsEntity> getWords() {
        return words;
    }

    public static class WordsEntity {
        private int id;
        private String content;
        private Object result_id;
        /**
         * id : 2240
         * content : sa77w
         * is_correct : false
         */

        private List<AnswersEntity> answers;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<AnswersEntity> getAnswers() {
            return answers;
        }
        public static class AnswersEntity {
            private int id;
            private String content;
            private boolean is_correct;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}