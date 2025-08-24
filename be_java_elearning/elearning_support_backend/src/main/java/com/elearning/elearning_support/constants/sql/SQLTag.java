package com.elearning.elearning_support.constants.sql;

public class SQLTag {

    public static final String GET_LIST_TAG_BY_OBJECT_APPLIED_TYPE =
        "select id, name \n" +
            "from {h-schema}tags \n" +
            "where \n" +
            "   is_enabled is true and \n" +
            "   deleted_flag = 1 and \n" +
            "   object_applied_types ilike ('%' || :objectAppliedType || '%') and \n" +
            "   ('' = :filter or name ilike ('%' || :filter || '%')) ";

}
