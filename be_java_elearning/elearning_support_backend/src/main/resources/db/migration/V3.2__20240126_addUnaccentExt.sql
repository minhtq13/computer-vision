CREATE EXTENSION IF NOT EXISTS unaccent;

-- function covert vietnamese characters to unaccent characters --
DROP FUNCTION IF EXISTS "elearning_support_dev"."custom_unaccent"(text);
CREATE OR REPLACE FUNCTION "elearning_support_dev"."custom_unaccent"(input text)
    RETURNS text AS
$$
DECLARE
    old_chars text; new_chars text; res text;
BEGIN
    old_chars = 'áàảãạâấầẩẫậăắằẳẵặđéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶĐÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴ';
    new_chars = 'aaaaaaaaaaaaaaaaadeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyAAAAAAAAAAAAAAAAADEEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYY';
    res = input;
    for i in 0..length(old_chars)
    loop
            res = replace(res, substr(old_chars,i,1), substr(new_chars,i,1));
    end loop;
    return res;
end;

$$ LANGUAGE plpgsql;