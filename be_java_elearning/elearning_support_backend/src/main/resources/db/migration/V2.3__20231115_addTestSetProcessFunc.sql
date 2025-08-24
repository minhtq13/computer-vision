-- funct get lst correct answer idx of a question in test set --
DROP FUNCTION IF EXISTS elearning_support_dev.get_correct_in_lst_answer_json(i_answer_json jsonb);
CREATE OR REPLACE FUNCTION elearning_support_dev.get_correct_in_lst_answer_json(i_answer_json jsonb)
    RETURNS TEXT
AS
$$

DECLARE
    result TEXT;

BEGIN
    SELECT '{' || string_agg(TEXT(testQuestAnswer."answerNo"), ',') || '}'
    INTO result
    FROM jsonb_to_recordset(i_answer_json) AS testQuestAnswer("answerId" int8, "answerNo" int4)
             JOIN elearning_support_dev.answer ON testQuestAnswer."answerId" = answer.id
    WHERE answer.is_correct;
    RETURN result;
END
$$
    LANGUAGE plpgsql;