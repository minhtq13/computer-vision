-- Add description column --
ALTER TABLE "elearning_support_dev"."chapter"
ADD COLUMN "description" text;

ALTER TABLE "elearning_support_dev"."chapter"
    ADD CONSTRAINT subjectChapterOrderUnqConstraint UNIQUE ("subject_id", "orders");

-- Add function get chapter json --
DROP FUNCTION IF EXISTS elearning_support_dev.get_list_chapter_subject_json(i_subject_id int8);
CREATE OR REPLACE FUNCTION elearning_support_dev.get_list_chapter_subject_json(i_subject_id int8)
    RETURNS TEXT
AS
$$

DECLARE
    result TEXT;

BEGIN
    SELECT
                '[' || string_agg(format('{"id": %s, "title": %s, "code": %s, "orders": %s}',
                                         to_json(id), to_json(title), to_json(code), to_json(orders)),  ',') || ']' AS lst_role_json
    INTO result
    FROM elearning_support_dev.chapter
    WHERE subject_id = i_subject_id
    GROUP BY subject_id;
    RETURN result;
END
$$
    LANGUAGE plpgsql;