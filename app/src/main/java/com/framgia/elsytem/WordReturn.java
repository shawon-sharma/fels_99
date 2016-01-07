package com.framgia.elsytem;

import com.framgia.elsytem.jsonResponse.LessonResponse;

import java.util.ArrayList;

/**
 * Created by ahsan on 12/23/15.
 */
public class WordReturn {
    String contain;
    ArrayList<LessonResponse.LessonEntity.WordsEntity.AnswersEntity> answersEntities;

    public WordReturn(String contain, ArrayList<LessonResponse.LessonEntity.WordsEntity.AnswersEntity> answersEntities) {
        this.contain = contain;
        this.answersEntities = answersEntities;
    }
}
