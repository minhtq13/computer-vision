package com.elearning.elearning_support.constants.sql;

public class SQLNotification {

    public static final String GET_LIST_NOTIFICATION =
        "SELECT \n" +
            "   id, \n" +
            "   content_type AS contentType, \n" +
            "   content AS content, \n" +
            "   title, \n" +
            "   created_at AS createdAt, \n" +
            "   object_identifier AS objectIdentifier, \n" +
            "   object_type AS objectType, \n" +
            "   is_new AS isNew \n" +
            "FROM {h-schema}notification WHERE user_id = :userId \n" +
            "ORDER BY created_at DESC";

    public static final String GET_PAGE_NOTIFICATION =
            "SELECT \n" +
                    "   id, \n" +
                    "   content_type AS contentType, \n" +
                    "   content AS content, \n" +
                    "   title, \n" +
                    "   created_at AS createdAt, \n" +
                    "   object_identifier AS objectIdentifier, \n" +
                    "   object_type AS objectType, \n" +
                    "   is_new AS isNew \n" +
                    "FROM {h-schema}notification WHERE user_id = :userId \n" +
                    "ORDER BY created_at DESC";

    public static final String UPDATE_IS_NEW_NOTIFICATION_BY_USER_ID =
        "UPDATE {h-schema}notification SET is_new = false WHERE user_id = :userId \n";

    public static final String COUNT_NEW_NOTIFICATIONS_BY_USER_ID =
        "SELECT COUNT(1) AS numOfNewNotifications FROM {h-schema}notification WHERE user_id = :userId AND is_new IS true";

}
