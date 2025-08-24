DO $$
DECLARE
p_test_management_id BIGINT;
    p_subject_management_id BIGINT;
    p_teacher_management_id BIGINT;
    p_student_management_id BIGINT;
    p_exam_class_management_id BIGINT;
    p_scoring_management_id BIGINT;
    p_course_management_id BIGINT;
    p_question_management_id BIGINT;
    p_user_management_id BIGINT;
BEGIN
INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_TEST_MANAGEMENT', -1, 'Test Management', 'Full access to manage tests, including creation, editing, deletion, and result management.')
    RETURNING id INTO p_test_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_SUBJECT_MANAGEMENT', -1, 'Subject Management', 'Full access to manage academic subjects, including creation, updating, and categorization.')
    RETURNING id INTO p_subject_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_TEACHER_MANAGEMENT', -1, 'Teacher Management', 'Teacher Management')
    RETURNING id INTO p_teacher_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_STUDENT_MANAGEMENT', -1, 'Student Management', 'Full access to manage all student accounts, including creation, editing, and deletion, as well as maintaining academic records.')
    RETURNING id INTO p_student_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_EXAM_CLASS_MANAGEMENT', -1, 'Exam Class Management', 'Full control over exam classes, including scheduling, participant management, and exam settings.')
    RETURNING id INTO p_exam_class_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_SCORING_MANAGEMENT', -1, 'Scoring Management', 'Full access to manage scoring activities, including automated grading and manual score adjustments.')
    RETURNING id INTO p_scoring_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_COURSE_MANAGEMENT', -1, 'Course Management', 'Full access to create, update, and organize courses, including linking them with subjects and chapters.')
    RETURNING id INTO p_course_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_QUESTION_MANAGEMENT', -1, 'Question Management', 'Full access to manage all questions in the question bank, including creation, editing, viewing, and deletion.')
    RETURNING id INTO p_question_management_id;

INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
    ('P_USER_MANAGEMENT', -1, 'User Management', 'Full access to manage system users and their permissions.')
    RETURNING id INTO p_user_management_id;

-- Sau đó, INSERT các mục con, sử dụng ID của các mục cha đã lấy
INSERT INTO "elearning_support_dev"."permission" (code, parent_id, name, description) VALUES
      ('P_TEST_CREATE', p_test_management_id, 'Test Create', 'Create new tests, configure questions, and set scoring rules.'),
      ('P_TEST_UPDATE', p_test_management_id, 'Test Update', 'Modify test details such as questions, time limits, and scoring parameters.'),
      ('P_TEST_DELETE', p_test_management_id, 'Test Delete', 'Permanently remove tests from the system.'),
      ('P_TEST_DETAIL', p_test_management_id, 'Test Detail', 'View complete test information, including questions, settings, and assigned classes'),

      ('P_SUBJECT_UPDATE', p_subject_management_id, 'Subject Update', 'Subject Update'),
      ('P_SUBJECT_CREATE', p_subject_management_id, 'Subject Create', 'Subject Create'),

      ('P_TEACHER_ADD', p_teacher_management_id, 'Teacher Add', 'Add Teacher'),
      ('P_TEACHER_DELETE', p_teacher_management_id, 'Teacher Delete', 'Delete Teacher'),
      ('P_TEACHER_UPDATE', p_teacher_management_id, 'Teacher Update', 'Update Teacher'),

      ('P_STUDENT_ADD', p_student_management_id, 'Student Add', 'Create and register new student accounts in the system.'),
      ('P_STUDENT_DELETE', p_student_management_id, 'Student Delete', 'Permanently remove student accounts and all related data from the system.'),
      ('P_STUDENT_EDIT', p_student_management_id, 'Student Edit', 'Update student information such as personal details, academic status, and contact information.'),

      ('P_EXAM_CLASS_VIEW', p_exam_class_management_id, 'Exam Class View', 'View details of exam classes, including schedules, assigned tests, and participants.'),
      ('P_EXAM_CLASS_CREATE', p_exam_class_management_id, 'Exam Class Create', 'Create new exam classes, assign tests, and schedule exam dates.'),
      ('P_EXAM_CLASS_EDIT', p_exam_class_management_id, 'Exam Class Edit', 'Update exam class details, schedules, and participant lists.'),
      ('P_EXAM_CLASS_DELETE', p_exam_class_management_id, 'Exam Class Delete', 'Remove exam classes and all associated data.'),

      ('P_SCORING', p_scoring_management_id, 'Scoring', 'Perform scoring of completed tests based on the answer key or marking scheme.'),
      ('P_SCORING_UPLOAD', p_scoring_management_id, 'Scoring Upload', 'Upload answer sheets or scoring files for automated processing.'),
      ('P_SCORING_SAVE_RESULT', p_scoring_management_id, 'Scoring Save Result', 'Scoring Save Result'),

      ('P_COURSE_CREATE', p_course_management_id, 'Course Create', 'Create new courses and set their details, including linked chapters and subjects.'),
      ('P_COURSE_UPDATE', p_course_management_id, 'Course Update', 'Edit course details such as syllabus, description, and linked content.'),

      ('P_QUESTION_CREATE', p_question_management_id, 'Question Create', 'Add new questions to the question bank, including test, images, and answer keys.'),
      ('P_QUESTION_UPDATE', p_question_management_id, 'Question Update', 'Edit question content, answer options, and correct answers.'),
      ('P_QUESTION_DELETE', p_question_management_id, 'Question Delete', 'Remove questions from the question bank.'),
      ('P_QUESTION_DETAIL', p_question_management_id, 'Question View Detail', 'View complete question details, including content, answers, and metadata.'),

      ('P_USER_CREATE', p_user_management_id, 'User Create', 'Create new user accounts and assign roles/permissions.'),
      ('P_USER_UPDATE', p_user_management_id, 'User Update', 'Update user account details, including permissions and personal information.'),
      ('P_USER_DELETE', p_user_management_id, 'User Delete', 'Permanently remove user accounts from the system.');
END $$;