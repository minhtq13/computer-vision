-- FLYWAY DATABASE MIGRATION --

-- === CORE SYSTEM === ---
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TABLE IF EXISTS elearning_support_dev."configuration";
CREATE TABLE IF NOT EXISTS elearning_support_dev."configuration"
(
    "id"         bigserial   not null unique,
    "code"       varchar(50) not null,
    "text_value" text,
    "int_value"  int4,
    "type"       int2        not null,
    "user_id"    int8,
    primary key ("id"),
    constraint configUniqueUserCode unique ("user_id", "code")
    );
COMMENT ON TABLE "elearning_support_dev"."configuration" IS 'Bảng lưu cấu hình hệ thống';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."id" IS 'Id cấu hình';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."code" IS 'Mã cấu hình';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."text_value" IS 'Giá trị cấu hình kiểu text';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."int_value" IS 'Giá trị cấu hình kiểu số nguyên';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."type" IS 'Loại cấu hình (-1: Cấu hình hệ thống, 0: Cấu hình doanh nghiệp)';
COMMENT ON COLUMN "elearning_support_dev"."configuration"."user_id" IS 'Id người dùng với loại cấu hình là 0';

-- === USER == --
DROP TABLE IF EXISTS elearning_support_dev."users";
CREATE TABLE IF NOT EXISTS elearning_support_dev."users"
(
    "id"                    bigserial unique   not null,
    "identification_number" varchar(20),
    "identity_type"         int2,
    "gender"                int2               not null,
    "avatar_id"             int8,
    "code"                  varchar(20)        not null,
    "first_name"            varchar(50),
    "last_name"             varchar(50),
    "birth_date"            date,
    "address"               text,
    "phone_number"          varchar(11),
    "email"                 varchar(50) unique,
    "username"              varchar(50) unique not null,
    "password"              varchar(255)       not null,
    "status"                int4               not null default 1,
    "user_type"             int2               not null,
    "department_id"         int8               not null,
    "created_at"            timestamp          not null,
    "created_by"            int8               not null,
    "modified_at"           timestamp,
    "modified_by"           int8,
    "fcm_token"             text,
    "activation_key"        text,
    "created_source"        int2               not null,
    "user_uuid"             uuid               not null default uuid_generate_v4(),
    primary key ("id"),
    constraint uniqueUserTypeCode unique ("user_type", "code")
    );

COMMENT ON TABLE "elearning_support_dev"."users" IS 'Bảng chứa thông tin của người dùng hệ thống';
COMMENT ON COLUMN "elearning_support_dev"."users"."id" IS 'Id của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."identification_number" IS 'Số chứng thực cá nhân của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."identity_type" IS 'Loại giấy tờ chứng thực (0: CMTND, 1: CCCD, 2: Hộ chiếu)';
COMMENT ON COLUMN "elearning_support_dev"."users"."avatar_id" IS 'Id file avatar của người dùng lưu trong bảng file_attach';
COMMENT ON COLUMN "elearning_support_dev"."users"."code" IS 'Mã người dùng (Nếu là GV: Mã giáo viên, HSSV: Mã số HSSV)';
COMMENT ON COLUMN "elearning_support_dev"."users"."gender" IS 'Giới tính người dùng (0: Nữ, 1:Name, : Khác)';
COMMENT ON COLUMN "elearning_support_dev"."users"."first_name" IS 'Tên của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."last_name" IS 'Họ và tên đệm của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."birth_date" IS 'Ngày sinh của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."address" IS 'Địa chỉ người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."phone_number" IS 'Số điện thoại liên hệ của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."email" IS 'Email của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."users"."username" IS 'Username đăng nhập';
COMMENT ON COLUMN "elearning_support_dev"."users"."password" IS 'Password đăng nhập';
COMMENT ON COLUMN "elearning_support_dev"."users"."status" IS 'Trạng thái hoạt động của người dùng (0: Tắt hoạt động, 1:Đang hoạt động)';
COMMENT ON COLUMN "elearning_support_dev"."users"."user_type" IS 'Loại người dùng (-1: Admin, 0: Giảng viên/Giáo viên, 1: Học sinh/Sinh viên)';
COMMENT ON COLUMN "elearning_support_dev"."users"."department_id" IS 'Id đơn vị trực thuộc (HSSV: Lớp/Khoá, GV: Khoa/Viện)';
COMMENT ON COLUMN "elearning_support_dev"."users"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."users"."created_by" IS 'Id Người thực hiện tạo';
COMMENT ON COLUMN "elearning_support_dev"."users"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."users"."modified_by" IS 'Id Người thực hiện cập nhật';
COMMENT ON COLUMN "elearning_support_dev"."users"."created_source" IS 'Nguồn tạo tài khoản (0: Tao trực tiếp, 1: OAuth Google, 2: OAuth Facebook, 3: OAuth Microsoft)';
COMMENT ON COLUMN "elearning_support_dev"."users"."fcm_token" IS 'Token Firebase để cho việc gửi thông báo';
COMMENT ON COLUMN "elearning_support_dev"."users"."activation_key" IS 'Mã kích hoạt tài khoản';
COMMENT ON COLUMN "elearning_support_dev"."users"."user_uuid" IS 'Mã uuid tài khoản để phục vụ việc đồng bộ';
-- Init admin user --
-- Init dữ liệu --
INSERT INTO "elearning_support_dev"."users" ("identification_number", "identity_type", "code", "first_name", "last_name", "username", "password",
                                             "created_at", "created_by", "status", "gender", "user_type", "department_id", "created_source")
VALUES ('00293849828', 1, 'ADMIN_SUPER1', 'Admin', 'Super', 'admin', '$2a$12$4FTmJ2x/BfKIN9as9ivNKuo8CJZd4jtk0UDEijm7OYqrusJN251du', now(), 1, 1, 1, -1, -1, 0);

-- Bảng department --
DROP TABLE IF EXISTS "elearning_support_dev"."department";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."department"
(
    "id"           bigserial    not null unique,
    "parent_id"    int8         not null unique,
    "parent_code"  varchar(50)  not null unique,
    "code"         varchar(50)  not null unique,
    "logo"         int8,
    "name"         varchar(100) not null,
    "address"      text,
    "phone_number" varchar(11),
    "email"        varchar(100),
    "description"  text,
    "created_at"   timestamp    not null,
    "created_by"   int8         not null,
    "modified_at"  timestamp,
    "modified_by"  int8,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."department" IS 'Bảng chứa thông tin khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."id" IS 'Id của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."code" IS 'Mã của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."logo" IS 'Đường dẫn file logo khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."name" IS 'Tên của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."address" IS 'Địa chỉ của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."phone_number" IS 'Số điện thoại liên hệ của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."email" IS 'Email liên hệ của khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."description" IS 'Thông tin mô tả khoa/bộ phận/đơn vị';
COMMENT ON COLUMN "elearning_support_dev"."department"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."department"."created_by" IS 'Id Người thực hiện tạo';
COMMENT ON COLUMN "elearning_support_dev"."department"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."department"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng role --
DROP TABLE IF EXISTS elearning_support_dev."role";
CREATE TABLE IF NOT EXISTS elearning_support_dev."role"
(
    "id"             bigserial   not null unique,
    "code"           varchar(30) not null unique,
    "name"           text        not null,
    "displayed_name" text        not null,
    "is_default"     boolean     not null,
    primary key ("id")
    );
COMMENT ON COLUMN elearning_support_dev."role"."id" IS 'Id vai trò';
COMMENT ON COLUMN elearning_support_dev."role"."code" IS 'Mã code vai trò';
COMMENT ON COLUMN elearning_support_dev."role"."name" IS 'Tên của vai trò';
COMMENT ON COLUMN elearning_support_dev."role"."displayed_name" IS 'Tên hiển thị của vai trò';
COMMENT ON COLUMN elearning_support_dev."role"."is_default" IS 'Cờ kiểm tra vai trò mặc định của hệ thống';

-- Bảng users_roles --
DROP TABLE IF EXISTS elearning_support_dev."users_roles";
CREATE TABLE IF NOT EXISTS elearning_support_dev."users_roles"
(
    "row_id"  bigserial not null unique,
    "user_id" int8      not null,
    "role_id" int8      not null,
    primary key ("user_id", "role_id")
    );
COMMENT ON TABLE elearning_support_dev."users_roles" IS 'Bảng liên kết người dùng và vai trò';
COMMENT ON COLUMN elearning_support_dev."users_roles"."user_id" IS 'Id của người dùng';
COMMENT ON COLUMN elearning_support_dev."users_roles"."role_id" IS 'Id của vai trò';

-- Bảng quyền permission --
DROP TABLE IF EXISTS "elearning_support_dev"."permission";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."permission"
(
    "id"          bigserial    not null unique,
    "code"        varchar(30)  not null unique,
    "parent_id"   int8         not null,
    "parent_code" varchar(30)  not null,
    "name"        varchar(255) not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."permission" IS 'Bảng lưu các quyền sử dụng tính năng hệ thống';
COMMENT ON COLUMN "elearning_support_dev"."permission"."id" IS 'Id quyền';
COMMENT ON COLUMN "elearning_support_dev"."permission"."code" IS 'Mã code quyền';
COMMENT ON COLUMN "elearning_support_dev"."permission"."name" IS 'Tên quyền';
COMMENT ON COLUMN "elearning_support_dev"."permission"."parent_id" IS 'Id quyền cha';
COMMENT ON COLUMN "elearning_support_dev"."permission"."parent_code" IS 'Mã code quyền cha';

-- Bảng roles_permissions --
DROP TABLE IF EXISTS "elearning_support_dev"."roles_permissions";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."roles_permissions"
(
    "id"            bigserial not null unique,
    "role_id"       int8      not null,
    "permission_id" int8      not null,
    primary key ("role_id", "permission_id")
    );
COMMENT ON TABLE "elearning_support_dev"."roles_permissions" IS 'Bảng liên kết giữa vai trò và quyền';
COMMENT ON COLUMN "elearning_support_dev"."roles_permissions"."id" IS 'Id bản ghi';
COMMENT ON COLUMN "elearning_support_dev"."roles_permissions"."role_id" IS 'Id vai trò';
COMMENT ON COLUMN "elearning_support_dev"."roles_permissions"."permission_id" IS 'Id quyền';

-- Bảng file_attach --
DROP TABLE IF EXISTS "elearning_support_dev"."file_attach";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."file_attach"
(
    "id"          bigserial   not null unique,
    "name"        text        not null,
    "type"        int2        not null,
    "file_ext"    varchar(10) not null,
    "size"        int8,
    "stored_type" int2        not null,
    "file_path"    text,
    "external_link"    text,
    "created_by"  int8        not null,
    "created_at"  timestamp   not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."file_attach" IS 'Bảng lưu thông tin file tải lên';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."id" IS 'Id file đính kèm';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."name" IS 'Tên file đính kèm';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."type" IS 'Loại file đính kèm (0: File avatar/ảnh chân dung, 1: File ảnh, 2: File tài liệu 3: Video)';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."file_ext" IS 'Định dạng file';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."size" IS 'Kích thước file (byte)';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."stored_type" IS 'Loại kho lưu trữ file (0: Lưu tại server, 1: Lưu tại cloud server bên thứ 3)';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."file_path" IS 'Đường dẫn đến địa chỉ lưu trữ file (internal server)';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."external_link" IS 'Đường dẫn đến địa chỉ lưu trữ file (external server)';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."created_by" IS 'Người tải lên file';
COMMENT ON COLUMN "elearning_support_dev"."file_attach"."created_at" IS 'Thời gian tải lên file';

-- Bảng Auth_info --
DROP TABLE IF EXISTS "elearning_support_dev"."auth_info";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."auth_info"
(
    "id"            bigserial not null unique,
    "user_id"       int8      not null unique,
    "token"         text      not null unique,
    "status"        int2      not null,
    "ip_address"    varchar(32),
    "created_at"    timestamp not null,
    "last_login_at" timestamp not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."auth_info" IS 'Bảng lưu thông tin xác thực và đăng nhập của người dùng';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."id" IS 'Id bản ghi lưu token';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."user_id" IS 'Id user';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."status" IS 'Trạng thái token (0 : INVALID, 1: VALID)';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."token" IS 'Token đăng nhập hiện tại';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."last_login_at" IS 'Thời gian đăng nhập sử dụng token hiện tại lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."auth_info"."ip_address" IS 'Địa chỉ ip đăng nhập';

-- Bảng notification --
DROP TABLE IF EXISTS "elearning_support_dev"."notification";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."notification"
(
    "id"         bigserial not null unique,
    "content"    text,
    "created_by" int8      not null,
    "created_at" timestamp not null,
    "type"       int2      not null,
    "user_id"    int8      not null,
    "is_seen"    boolean,
    "sent_at"    timestamp,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."notification" IS 'Bảng lưu thông báo người dùng';
COMMENT ON COLUMN elearning_support_dev."notification"."id" IS 'Id bản ghi thông báo';
COMMENT ON COLUMN elearning_support_dev."notification"."user_id" IS 'Id người dùng';
COMMENT ON COLUMN elearning_support_dev."notification"."content" IS 'Nội dùng thông báo';
COMMENT ON COLUMN elearning_support_dev."notification"."type" IS 'Loại thông báo ()';
COMMENT ON COLUMN elearning_support_dev."notification"."user_id" IS 'Id người dùng';
COMMENT ON COLUMN "elearning_support_dev"."notification"."created_by" IS 'Id người tạo';
COMMENT ON COLUMN "elearning_support_dev"."notification"."created_at" IS 'Thời gian tạo thông báo';
COMMENT ON COLUMN "elearning_support_dev"."notification"."sent_at" IS 'Thời gian gửi thông báo';

-- Bảng mail --
DROP TABLE IF EXISTS "elearning_support_dev"."mail";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."mail"
(
    "id"                 bigserial   not null unique,
    "code"               varchar(30) not null unique,
    "lst_to_address"     text,
    "from_address"       text        not null,
    "subject"            text        not null,
    "content"            text        not null,
    "status"             int4        not null,
    "is_has_attachments" boolean     not null,
    "sent_time"          timestamp   not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."mail" IS 'Bảng lưu lịch sử gửi mail hệ thống';
COMMENT ON COLUMN "elearning_support_dev"."mail"."id" IS 'Id bản ghi lịch sử gửi mail';
COMMENT ON COLUMN "elearning_support_dev"."mail"."code" IS 'Mã code của email lưu trong hệ thống (phục vụ cho việc gửi lại nếu cần)';
COMMENT ON COLUMN "elearning_support_dev"."mail"."lst_to_address" IS 'Danh sách địa chỉ email được gửi';
COMMENT ON COLUMN "elearning_support_dev"."mail"."subject" IS 'Tiêu đề gửi mail';
COMMENT ON COLUMN "elearning_support_dev"."mail"."from_address" IS 'Địa chỉ gửi';
COMMENT ON COLUMN "elearning_support_dev"."mail"."content" IS 'Nội dung email';
COMMENT ON COLUMN "elearning_support_dev"."mail"."is_has_attachments" IS 'Có/Không file đính kèm';
COMMENT ON COLUMN "elearning_support_dev"."mail"."status" IS 'Trạng thái email (0: Chờ gửi, 1: Đã gửi, 2: Gửi thành công, 3 : Gửi thất bại)';
COMMENT ON COLUMN "elearning_support_dev"."mail"."sent_time" IS 'Thời gian gửi';

-- Bảng mail_templates --
DROP TABLE IF EXISTS "elearning_support_dev"."mail_templates";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."mail_templates"
(
    "id"              bigserial    not null unique,
    "name"            varchar(100) not null,
    "code"            varchar(30)  not null unique,
    "raw_content"     text         not null,
    "lst_mail_params" text         not null default '[]',
    "subject"         text         not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."mail_templates" IS 'Bảng lưu các mẫu email trong hệ thống';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."id" IS 'Id mẫu email';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."name" IS 'Tên mẫu email';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."code" IS 'Mã code mẫu email';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."raw_content" IS 'Form html mẫu email';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."subject" IS 'Tiêu đề mẫu email';
COMMENT ON COLUMN "elearning_support_dev"."mail_templates"."lst_mail_params" IS 'Danh sách các param của email';

-- ====== CORE E-LEARNING ====== --

-- Bảng lưu kỳ học semester --
DROP TABLE IF EXISTS elearning_support_dev."semester";
CREATE TABLE IF NOT EXISTS elearning_support_dev."semester"
(
    "id"          bigserial   not null unique,
    "name"        varchar(50),
    "code"        varchar(10) not null unique,
    "school_year" varchar(15),
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."semester" IS 'Bảng lưu danh sách các kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."semester"."id" IS 'Id kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."semester"."name" IS 'Tên kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."semester"."code" IS 'Mã kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."semester"."school_year" IS 'Năm học';

-- Bảng subject --
DROP TABLE IF EXISTS "elearning_support_dev"."subject";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."subject"
(
    "id"          bigserial          NOT NULL unique,
    "created_at"  timestamp          not null,
    "created_by"  int8               not null,
    "is_enabled"  boolean,
    "modified_at" timestamp,
    "modified_by" varchar(255),
    "code"        varchar(10) unique not null,
    "credit"      int4,
    "description" text,
    "title"       varchar(255)       not null,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."subject" IS 'Bảng lưu các chương của các môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."code" IS 'Mã môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."is_enabled" IS 'Trạng thái hiển thị môn học (Ẩn/Hiện)';
COMMENT ON COLUMN "elearning_support_dev"."subject"."title" IS 'Tiêu đề môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."credit" IS 'Số tín chỉ của môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."description" IS 'Mô tả môn học';
COMMENT ON COLUMN "elearning_support_dev"."subject"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."subject"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."subject"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."subject"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng chapter --
DROP TABLE IF EXISTS "elearning_support_dev"."chapter";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."chapter"
(
    "id"          bigserial   NOT NULL unique,
    "code"        varchar(20) not null unique,
    "created_at"  timestamp,
    "created_by"  int8,
    "is_enabled"  boolean default true,
    "modified_at" timestamp,
    "modified_by" int8,
    "orders"      int4 not null ,
    "title"       varchar(255),
    "subject_id"  int8        not null,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."chapter" IS 'Bảng lưu các chương của các môn học';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."id" IS 'Id chương';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."code" IS 'Mã chương';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."is_enabled" IS 'Trạng thái chương (Ẩn/Hiện)';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."title" IS 'Tiêu đề chương';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."orders" IS 'Thứ tự chương';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."subject_id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."chapter"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng lớp (course) ---
DROP TABLE IF EXISTS elearning_support_dev."course";
CREATE TABLE IF NOT EXISTS elearning_support_dev."course"
(
    "id"          bigserial   not null unique,
    "code"        varchar(10) not null unique,
    "name"        varchar(255),
    "teacher_id"  int8,
    "subject_id"  int8        not null,
    "semester_id" int8        not null,
    "created_by"  int8        not null,
    "created_at"  timestamp   not null,
    "modified_at" int8,
    "modified_by" int8,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."course" IS 'Bảng lưu các lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."id" IS 'Id lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."code" IS 'Mã lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course"."teacher_id" IS 'Id GV giảng dạy';
COMMENT ON COLUMN "elearning_support_dev"."course"."subject_id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."course"."semester_id" IS 'Mã học kỳ';
COMMENT ON COLUMN "elearning_support_dev"."course"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."course"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."course"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."course"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng liên kết student_course ---
DROP TABLE IF EXISTS elearning_support_dev."student_course";
CREATE TABLE IF NOT EXISTS elearning_support_dev."student_course"
(
    "id"         bigserial not null unique,
    "student_id" int8      not null,
    "course_id"   int8      not null,
    primary key ("student_id", "course_id"),
    constraint studentClassUniqueConstraint unique ("student_id", "course_id")
    );
COMMENT ON TABLE "elearning_support_dev"."student_course" IS 'Bảng lưu liên kết HSSV và lớp học';
COMMENT ON COLUMN "elearning_support_dev"."student_course"."id" IS 'Id lớp học';
COMMENT ON COLUMN "elearning_support_dev"."student_course"."student_id" IS 'Id học sinh';
COMMENT ON COLUMN "elearning_support_dev"."student_course"."course_id" IS 'Id lớp học';

-- Bảng course_file_attach --
DROP TABLE IF EXISTS elearning_support_dev."course_file_attach";
CREATE TABLE IF NOT EXISTS elearning_support_dev."course_file_attach"
(
    "id"             bigserial not null unique,
    "course_id"       int8      not null,
    "file_attach_id" int8      not null,
    primary key ("course_id", "file_attach_id")
    );
COMMENT ON TABLE "elearning_support_dev"."course_file_attach" IS 'Bảng lưu liên kết lớp học và file tài liệu lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course_file_attach"."id" IS 'Id bản ghi';
COMMENT ON COLUMN "elearning_support_dev"."course_file_attach"."course_id" IS 'Id lớp học';
COMMENT ON COLUMN "elearning_support_dev"."course_file_attach"."file_attach_id" IS 'Id file';

-- Bảng lưu các bài học (lesson) một chương --
DROP TABLE IF EXISTS elearning_support_dev."lesson";
CREATE TABLE IF NOT EXISTS elearning_support_dev."lesson"
(
    "id"          bigserial    not null unique,
    "name"        varchar(255) not null,
    "code"        varchar(50)  not null,
    "description" text,
    "chapter_id"  int8         not null,
    "lesson_no"   int4,
    "video"       int8,
    "created_by"  int8         not null,
    "created_at"  timestamp    not null,
    "modified_at" int8,
    "modified_by" int8,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."lesson" IS 'Bảng lưu các bài học của môn học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."id" IS 'Id bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."name" IS 'Tên bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."code" IS 'Mã bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."description" IS 'Mô tả bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."chapter_id" IS 'Id chương';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."lesson_no" IS 'Số thứ tự bài học trong chương';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."video" IS 'File video tài liệu bài học bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."created_by" IS 'Id Người thực hiện tạo';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."lesson"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng lesson_file_attach --
DROP TABLE IF EXISTS elearning_support_dev."lesson_file_attach";
CREATE TABLE IF NOT EXISTS elearning_support_dev."lesson_file_attach"
(
    "id"             bigserial not null unique,
    "lesson_id"      int8      not null,
    "file_attach_id" int8      not null,
    primary key ("lesson_id", "file_attach_id")
    );
COMMENT ON TABLE "elearning_support_dev"."lesson_file_attach" IS 'Bảng lưu liên kết bài học và file tài liệu bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson_file_attach"."id" IS 'Id';
COMMENT ON COLUMN "elearning_support_dev"."lesson_file_attach"."lesson_id" IS 'Id bài học';
COMMENT ON COLUMN "elearning_support_dev"."lesson_file_attach"."file_attach_id" IS 'Id file';

-- Bảng comment --
DROP TABLE IF EXISTS elearning_support_dev."comment";
CREATE TABLE IF NOT EXISTS elearning_support_dev."comment"
(
    "id"          bigserial not null unique,
    "parent_id"   int8,
    "title"       varchar(255),
    "content"     text,
    "is_edited"   boolean default false,
    "type"        int2      not null,
    "object_id"   int8      not null,
    "created_by"  int8      not null,
    "created_at"  timestamp not null,
    "modified_at" int8,
    "modified_by" int8,
    "course_id"    int8      not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."comment" IS 'Bảng lưu các bình luận';
COMMENT ON COLUMN "elearning_support_dev"."comment"."id" IS 'Id bình luận';
COMMENT ON COLUMN "elearning_support_dev"."comment"."parent_id" IS 'Id của bình luận cha (-1: Bình luận cấp 1, <> -1: Bình luận trả lời)';
COMMENT ON COLUMN "elearning_support_dev"."comment"."title" IS 'Tiêu đề bình luận';
COMMENT ON COLUMN "elearning_support_dev"."comment"."content" IS 'Nội dung bình luận (plaintext/html)';
COMMENT ON COLUMN "elearning_support_dev"."comment"."type" IS 'Loại bình luận (0: Bình luận bài học, 1: Bình luận chương, 2: Khác)';
COMMENT ON COLUMN "elearning_support_dev"."comment"."object_id" IS 'Id của đối tượng gắn với loại bình luận';
COMMENT ON COLUMN "elearning_support_dev"."comment"."course_id" IS 'Id lớp học để phân quyền dữ liệu hiển thị';
COMMENT ON COLUMN "elearning_support_dev"."comment"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."comment"."created_by" IS 'Id Người thực hiện tạo';
COMMENT ON COLUMN "elearning_support_dev"."comment"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."comment"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng comment_file_attach --
DROP TABLE IF EXISTS elearning_support_dev."comment_file_attach";
CREATE TABLE IF NOT EXISTS elearning_support_dev."comment_file_attach"
(
    "id"             bigserial not null unique,
    "comment_id"     int8      not null,
    "file_attach_id" int8      not null,
    primary key ("comment_id", "file_attach_id")
    );
COMMENT ON TABLE "elearning_support_dev"."comment_file_attach" IS 'Bảng lưu liên kết bình luận và file đính kèm';
COMMENT ON COLUMN "elearning_support_dev"."comment_file_attach"."id" IS 'Id';
COMMENT ON COLUMN "elearning_support_dev"."comment_file_attach"."comment_id" IS 'Id bình luận';
COMMENT ON COLUMN "elearning_support_dev"."comment_file_attach"."file_attach_id" IS 'Id file';


-- ==== CORE QUESTION MARK ==== --
-- Bảng question --
DROP TABLE IF EXISTS "elearning_support_dev"."question";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."question"
(
    "id"          bigserial   NOT NULL unique,
    "code"        varchar(20) not null unique,
    "created_at"  timestamp   not null,
    "created_by"  int8        not null,
    "is_enabled"  boolean default true,
    "modified_at" timestamp,
    "modified_by" int8,
    "level"       int2,
    "image_id"    int8,
    "content"     text,
    "chapter_id"  int8,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."question" IS 'Bảng lưu các câu hỏi trong ngân hàng câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."question"."id" IS 'Id câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."question"."code" IS 'Mã câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."question"."is_enabled" IS 'Trạng thái câu hỏi (Ẩn/Hiện)';
COMMENT ON COLUMN "elearning_support_dev"."question"."level" IS 'Mức độ câu hỏi (0: Dễ, 1: Trung bình, 2: Khó)';
COMMENT ON COLUMN "elearning_support_dev"."question"."image_id" IS 'Id file ảnh của câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."question"."content" IS 'Nội dung câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."question"."chapter_id" IS 'Id chương';
COMMENT ON COLUMN "elearning_support_dev"."question"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."question"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."question"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."question"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng answer --
DROP TABLE IF EXISTS "elearning_support_dev"."answer";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."answer"
(
    "id"          bigserial NOT NULL unique,
    "created_at"  timestamp,
    "created_by"  varchar(255),
    "is_enabled"  boolean default true,
    "modified_at" timestamp,
    "modified_by" varchar(255),
    "content"     text,
    "image_id"  int8,
    "is_correct"  boolean,
    "question_id" int8,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."answer" IS 'Bảng lưu các đáp án các câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."answer"."id" IS 'Id đáp án';
COMMENT ON COLUMN "elearning_support_dev"."answer"."content" IS 'Nội dung đáp án';
COMMENT ON COLUMN "elearning_support_dev"."answer"."is_enabled" IS 'Trạng thái hiện thị đáp án';
COMMENT ON COLUMN "elearning_support_dev"."answer"."is_correct" IS 'Cờ kiểm tra xem đáp án đúng hay không';
COMMENT ON COLUMN "elearning_support_dev"."answer"."image_id" IS 'Id file ảnh câu trả lời';
COMMENT ON COLUMN "elearning_support_dev"."answer"."question_id" IS 'Id câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."answer"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."answer"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."answer"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."answer"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng exam_class --
DROP TABLE IF EXISTS "elearning_support_dev"."exam_class";
CREATE TABLE IF NOT EXISTS "elearning_support_dev"."exam_class"
(
    "id"           bigserial   not null unique,
    "code"         varchar(10) not null unique,
    "created_at"   timestamp   not null,
    "created_by"   int8        not null,
    "is_enabled"   boolean default true,
    "modified_at"  timestamp,
    "modified_by"  int8,
    "room_name"    varchar(255),
    "examine_time" timestamp,
    "semester_id"  int8,
    "subject_id"   int8,
    "teacher_id"   int8,
    "test_id"      int8,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."exam_class" IS 'Bảng lưu các lớp thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."id" IS 'Id lớp thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."code" IS 'Mã mã lớp thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."is_enabled" IS 'Trạng thái hiển thị lớp thi (Ẩn/Hiện)';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."examine_time" IS 'Thời gian thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."room_name" IS 'Tên phòng thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."subject_id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."teacher_id" IS 'Id giáo viên (? coi thi/dạy)';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."test_id" IS 'Id kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."semester_id" IS 'Id kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."exam_class"."modified_by" IS 'Id Người thực hiện cập nhật';

-- add table user_course --
DROP TABLE IF EXISTS "elearning_support_dev"."user_exam_class";
CREATE TABLE "elearning_support_dev"."user_exam_class"
(
    "id"         bigserial not null unique,
    "user_id"    int8      not null,
    "exam_class_id" int8      not null,
    "role_type" int2,
    primary key ("id")
);
COMMENT ON TABLE "elearning_support_dev"."user_exam_class" IS 'Bảng lưu thông tin liên kết giữa Người dung (GV/SV) và Lớp thi';
COMMENT ON COLUMN "elearning_support_dev"."user_exam_class"."user_id" IS 'Mã người dùng (users)';
COMMENT ON COLUMN "elearning_support_dev"."user_exam_class"."exam_class_id" IS 'Id lớp thi';
COMMENT ON COLUMN "elearning_support_dev"."user_exam_class"."role_type" IS 'Vai trò của người dùng trong lớp thi (0: Thí sinh/SV, 1: Giám thị)';

-- Bảng student_test (Bảng mapping giữa student và test_set (đề thi đã được mix))  --
DROP TABLE IF EXISTS elearning_support_dev."student_test_set";
CREATE TABLE IF NOT EXISTS elearning_support_dev."student_test_set"
(
    "id"               bigserial NOT NULL unique,
    "created_at"       timestamp not null,
    "created_by"       int8      not null,
    "is_enabled"       boolean,
    "modified_at"      timestamp,
    "modified_by"      int8,
    "marker_rate"      double precision,
    "marked"           int4,
    "state"            varchar(255),
    "test_date"        date,
    "student_id"       int8,
    "test_set_id"      int8,
    "test_type"        int2 default 0,
    "handed_test_file" int8,
    PRIMARY KEY ("student_id", "test_set_id")
    );
COMMENT ON TABLE "elearning_support_dev"."student_test_set" IS 'Bảng lưu thông tin liên kết giữa HSSV và đề thi đã được trộn';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."id" IS 'Id bản ghi';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."student_id" IS 'Mã học sinh (users)';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."marker_rate" IS 'Tỷ lệ số câu hỏi đã trả lời: (marked/all questions) * 100 [%]';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."marked" IS 'Số câu hỏi đã trả lời';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."state" IS 'Trạng thái (-1: Không tham gia, 0: Đang thực hiện, 1: Hoàn thành)';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."test_date" IS 'Ngày làm bài';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."student_id" IS 'Id HSSV';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."test_set_id" IS 'Id đề thi';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."test_type" IS 'Hình thức làm bài (0: Offline, 1: Online)';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."handed_test_file" IS 'Id file bài làm nếu hình thức thi là Offline';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng student_test_detail lưu các câu trả lời của student trong 1 đề thi --
DROP TABLE IF EXISTS elearning_support_dev."student_test_set_detail";
CREATE TABLE IF NOT EXISTS elearning_support_dev."student_test_set_detail"
(
    "id"                   bigserial NOT NULL unique,
    "created_at"           timestamp not null,
    "created_by"           int8      not null,
    "is_enabled"           boolean default true,
    "modified_at"          timestamp,
    "modified_by"          int8,
    "is_corrected"         boolean,
    "selected_answer"      int2[] default '{}',
    "student_test_set_id"  int8,
    "test_set_question_id" int8      not null,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."student_test_set_detail" IS 'Bảng lưu chi tiết các câu trả lời của HSSV';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."id" IS 'Id';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."is_corrected" IS 'Cờ kiếm tra câu trả lời đúng hay không';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."selected_answer" IS 'Các câu trả lời đã chọn (Có thể chọn nhiều đáp án cho 1 câu hỏi)';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."student_test_set_id" IS 'Id bảng student_test_set';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."test_set_question_id" IS 'Id câu hỏi trong đề thi';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."student_test_set_detail"."modified_by" IS 'Id Người thực hiện cập nhật';

--- === TEST === ---
-- Bảng test: Lưu thông tin kỳ thi --
DROP TABLE IF EXISTS elearning_support_dev."test";
CREATE TABLE IF NOT EXISTS elearning_support_dev."test"
(
    "id"                bigserial NOT NULL unique,
    "name"              varchar(255),
    "created_at"        timestamp,
    "created_by"        int8,
    "is_enabled"        boolean,
    "modified_at"       timestamp,
    "modified_by"       int8,
    "duration"          int,
    "question_quantity" int,
    "start_time"        timestamp,
    "end_time"          timestamp,
    "total_point"       int4,
    "subject_id"        int8,
    "semester_id"       int8,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."test" IS 'Bảng lưu các kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."id" IS 'Id kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."name" IS 'Tên kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."question_quantity" IS 'Số lượng câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."test"."duration" IS 'Thời gian làm bài';
COMMENT ON COLUMN "elearning_support_dev"."test"."start_time" IS 'Thời gian bắt đầu kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."end_time" IS 'Thời gian kết thúc kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."total_point" IS 'Tổng điểm bài thi';
COMMENT ON COLUMN "elearning_support_dev"."test"."subject_id" IS 'Id môn học';
COMMENT ON COLUMN "elearning_support_dev"."test"."semester_id" IS 'Id kỳ học';
COMMENT ON COLUMN "elearning_support_dev"."test"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."test"."created_by" IS 'Id Người thực hiện tạo';
COMMENT ON COLUMN "elearning_support_dev"."test"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."test"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng test_question -- Các câu hỏi trong bộ câu hỏi của kỳ thi --
DROP TABLE IF EXISTS elearning_support_dev."test_question";
CREATE TABLE IF NOT EXISTS elearning_support_dev."test_question"
(
    "id"            bigserial        not null unique,
    "test_id"       int8             not null,
    "question_id"   int8             not null,
    "question_mark" double precision not null,
    primary key ("test_id", "question_id")
    );
COMMENT ON TABLE "elearning_support_dev"."test_question" IS 'Bảng lưu các câu trong ngân hàng đề thi của kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test_question"."id" IS 'Id bàn ghi';
COMMENT ON COLUMN "elearning_support_dev"."test_question"."test_id" IS 'Id kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test_question"."question_id" IS 'Id câu hỏi trong ngân hàng câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."test_question"."question_mark" IS 'Điểm câu hỏi';

-- Bảng test_set: các đề thi được tạo ra từ bộ câu hỏi --
DROP TABLE IF EXISTS elearning_support_dev."test_set";
CREATE TABLE IF NOT EXISTS elearning_support_dev."test_set"
(
    "id"          bigserial   NOT NULL unique,
    "code"        varchar(10) not null,
    "created_at"  timestamp,
    "created_by"  int8,
    "is_enabled"  boolean,
    "modified_at" timestamp,
    "modified_by" int8,
    "test_no"     varchar(255),
    "test_id"     int8        not null,
    PRIMARY KEY ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."test_set" IS 'Bảng lưu các đề thi được tạo ra từ bộ test';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."id" IS 'Id đề';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."code" IS 'Mã đề';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."test_no" IS 'Thứ tự đề trong đề thi';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."test_id" IS 'Id của kỳ thi';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."created_at" IS 'Thời gian tạo';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."created_by" IS 'Id Người thực hiện tạo ';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."modified_at" IS 'Thời gian cập nhật lần cuối';
COMMENT ON COLUMN "elearning_support_dev"."test_set"."modified_by" IS 'Id Người thực hiện cập nhật';

-- Bảng test_set_question --
DROP TABLE IF EXISTS elearning_support_dev."test_set_question";
CREATE TABLE IF NOT EXISTS elearning_support_dev."test_set_question"
(
    "id"          bigserial not null unique,
    "test_set_id" int8      not null,
    "question_id" int8      not null,
    "question_no" int4      not null,
    primary key ("test_set_id", "question_id")
    );
COMMENT ON TABLE "elearning_support_dev"."test_set_question" IS 'Bảng lưu các câu hỏi và thứ tự câu hỏi trong 1 đề thi';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question"."id" IS 'Id';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question"."test_set_id" IS 'Id đề thi';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question"."question_id" IS 'Id câu hỏi trong ngân hàng câu hỏi';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question"."question_no" IS 'Thứ tự câu hỏi trong đề thi';

-- Bảng test_set_question_answer --
DROP TABLE IF EXISTS elearning_support_dev."test_set_question_answer";
CREATE TABLE IF NOT EXISTS elearning_support_dev."test_set_question_answer"
(
    "id"                   bigserial not null unique,
    "answer_id"            int8      not null,
    "test_set_question_id" int8      not null,
    "answer_no"            int4      not null,
    primary key ("id")
    );
COMMENT ON TABLE "elearning_support_dev"."test_set_question_answer" IS 'Bảng lưu các đáp án và thứ tự các đáp án trong 1 câu hỏi trong 1 đề thi';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question_answer"."id" IS 'Id';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question_answer"."answer_id" IS 'Id đáp án';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question_answer"."answer_no" IS 'Thứ tự đáp án';
COMMENT ON COLUMN "elearning_support_dev"."test_set_question_answer"."test_set_question_id" IS 'Id câu hỏi trong 1 đề thi';









