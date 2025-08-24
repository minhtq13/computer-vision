package com.elearning.elearning_support.constants.sql;

public class SQLChapter {


    public static final String GET_LIST_EXISTED_CHAPTER_ORDERS_IN_SUBJECT =
        "SELECT orders FROM {h-schema}chapter WHERE subject_id = :subjectId ";

    public static final String EXISTS_BY_SUBJECT_ID_AND_ORDERS_AND_ID_NOT =
        "SELECT EXISTS (SELECT * FROM {h-schema}chapter WHERE subject_id = :subjectId AND orders = :orders AND id <> :chapterId) ";

    public static final String GET_ALL_SUBJECT_CHAPTER_MAPPINGS =
        "SELECT id AS chapterId, orders AS chapterNo, subject_id AS subjectId FROM {h-schema}chapter WHERE deleted_flag = 1";
}
