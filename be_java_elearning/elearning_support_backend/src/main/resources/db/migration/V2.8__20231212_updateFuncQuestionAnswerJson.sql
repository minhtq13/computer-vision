-- Func get json: test_set_question_answer
DROP FUNCTION IF EXISTS elearning_support_dev.get_list_test_question_answer_json(i_test_question_answer jsonb);
CREATE OR REPLACE FUNCTION elearning_support_dev.get_list_test_question_answer_json(i_test_question_answer jsonb)
    RETURNS TEXT
AS
$$

DECLARE
    result TEXT;

BEGIN
    with testAnswerJson as (
        select
            testAnswer."answerNo" AS "answerNo",
            testAnswer."answerId" AS "answerId",
            answer.content AS "content",
            answer.is_correct AS "isCorrect",
            case
                when testAnswer."answerNo" = 1 then 'A'
                when testAnswer."answerNo" = 2 then 'B'
                when testAnswer."answerNo" = 3 then 'C'
                when testAnswer."answerNo" = 4 then 'D'
                when testAnswer."answerNo" = 5 then 'E'
                when testAnswer."answerNo" = 6 then 'F'
                else ''
                end as "answerNoMask"
        from jsonb_to_recordset(i_test_question_answer) as testAnswer("answerId" int8, "answerNo" int4)
                 join elearning_support_dev.answer ON testAnswer."answerId" = answer.id
        order by testAnswer."answerNo"
    ) select json_agg(row_to_json(testAnswerJson)) from testAnswerJson
    INTO result;
    RETURN result;
END
$$
    LANGUAGE plpgsql;