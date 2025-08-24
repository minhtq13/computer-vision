package com.elearning.elearning_support.constants.sql;

public class SQLDepartment {

    public static final String GET_LIST_DEPARTMENT =
        "select * from {h-schema}department where deleted_flag = 1";

}
