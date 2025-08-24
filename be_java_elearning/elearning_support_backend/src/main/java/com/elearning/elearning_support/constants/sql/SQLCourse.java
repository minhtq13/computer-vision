package com.elearning.elearning_support.constants.sql;

public class SQLCourse {

    public static final String GET_COUNTER_COURSE_PARTICIPANT =
        "SELECT \n" +
            "    course_id AS courseId, \n" +
            "    string_agg(CAST(user_id AS TEXT), ',') FILTER (WHERE role_type = 0) AS lstStudentId, \n" +
            "    string_agg(CAST(user_id AS TEXT), ',') FILTER (WHERE role_type = 1) AS lstTeacherId, \n" +
            "    COUNT(user_id) FILTER (WHERE role_type = 1) AS numTeachers, \n" +
            "    COUNT(user_id) FILTER (WHERE role_type = 0) AS numStudents \n" +
            "FROM {h-schema}user_course \n" +
            "GROUP BY course_id \n";

    public static final String GET_LIST_COURSE =
        "WITH courseParticipantCTE AS ( " + GET_COUNTER_COURSE_PARTICIPANT + " ) \n" +
            "SELECT \n" +
            "    course.id AS id, \n" +
            "    course.code AS code, \n" +
            "    course.room_name AS roomName, \n" +
            "    'Thứ ' || TO_CHAR(course.course_time, 'D - HH24:MI') AS courseTime, \n" +
            "    TEXT(course.course_weeks) as courseWeeks, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.code AS subjectCode, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    courseParticipantCTE.numStudents AS numberOfStudents, \n" +
            "    courseParticipantCTE.numTeachers AS numberOfTeachers \n" +
            "FROM {h-schema}course \n" +
            "    LEFT JOIN {h-schema}subject ON course.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON course.semester_id = semester.id \n" +
            "    LEFT JOIN courseParticipantCTE ON course.id = courseParticipantCTE.courseId \n" +
            "WHERE \n" +
            "    course.is_enabled = true AND \n" +
            "    course.deleted_flag = 1 AND \n" +
            "    ('' = :code OR course.code ILIKE ('%' || :code || '%')) AND \n" +
            "    (-1 = :semesterId OR semester.id = :semesterId) AND \n" +
            "    (-1 = :subjectId OR subject.id = :subjectId) AND \n" +
            "    (:currentUserId = -1 OR :currentUserId = ANY(CAST(('{' || courseParticipantCTE.lstTeacherId || '}') AS int8[])))";

    public static final String GET_LIST_COURSE_PARTICIPANT =
        "SELECT \n" +
            "   participant.id AS id, \n" +
            "   CONCAT_WS(' ', participant.last_name, participant.first_name) AS name, \n" +
            "   participant.code AS code, \n" +
            "   userCourse.role_type AS roleType, \n" +
            "   CASE \n" +
            "       WHEN userCourse.role_type = 0 THEN 'Học Sinh/Sinh Viên' \n" +
            "       WHEN userCourse.role_type = 1 THEN 'Giáo viên/Giảng Viên' \n" +
            "       ELSE '' \n" +
            "   END AS roleName, \n" +
            "   course.id AS courseId, \n" +
            "   course.code AS courseCode \n" +
            "FROM {h-schema}user_course AS userCourse \n" +
            "    JOIN {h-schema}users AS participant ON userCourse.user_id = participant.id \n" +
            "    JOIN {h-schema}course ON course.id = userCourse.course_id \n" +
            "WHERE userCourse.course_id = :courseId AND userCourse.role_type = :roleType ";

    public static final String DELETE_COURSE_PARTICIPANT_BY_ID =
        "DELETE FROM {h-schema}user_course WHERE course_id = :courseId";

    public static final String GET_LIST_COURSE_PARTICIPANT_ID =
        "SELECT \n" +
            "   userCourse.user_id AS id \n" +
            "FROM {h-schema}user_course AS userCourse \n" +
            "WHERE userCourse.course_id = :courseId AND userCourse.role_type = :roleType ";

    public static final String GET_COURSE_DETAILS =
        "WITH courseParticipantCTE AS ( " + GET_COUNTER_COURSE_PARTICIPANT + " )\n" +
            "SELECT \n" +
            "    course.id AS id, \n" +
            "    course.code AS code, \n" +
            "    course.room_name AS roomName, \n" +
            "    'Thứ ' || TO_CHAR(course.course_time, 'D - HH24:MI') AS courseTime, \n" +
            "    TEXT(course.course_weeks) as courseWeeks, \n" +
            "    semester.id AS semesterId, \n" +
            "    semester.code AS semester, \n" +
            "    subject.id AS subjectId, \n" +
            "    subject.title AS subjectTitle, \n" +
            "    courseParticipantCTE.lstStudentId AS lstStudentId, \n" +
            "    courseParticipantCTE.lstTeacherId AS lstTeacherId, \n" +
            "    courseParticipantCTE.numStudents AS numberOfStudents, \n" +
            "    courseParticipantCTE.numTeachers AS numberOfTeachers, \n" +
            "    COALESCE(course.modified_at, course.created_at) AS lastModifiedAt \n" +
            "FROM {h-schema}course \n" +
            "    LEFT JOIN {h-schema}subject ON course.subject_id = subject.id \n" +
            "    LEFT JOIN {h-schema}semester ON course.semester_id = semester.id \n" +
            "    LEFT JOIN courseParticipantCTE ON course.id = courseParticipantCTE.courseId \n" +
            "WHERE \n" +
            "    course.is_enabled = true AND \n" +
            "    course.deleted_flag = 1 AND \n" +
            "    course.id = :id ";

}
