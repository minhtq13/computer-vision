import {DatePicker, Form, Input, Select, Button, Tag} from "antd";
import "./UserForm.scss";
import React, {useEffect, useState} from "react";
import {getRole} from "../../../../utils/storage";
import useCombo from "../../../../hooks/useCombo";
import {
    HUST_COLOR,
    ROLE_ADMIN,
    ROLE_STUDENT,
    ROLE_SUPER_ADMIN_CODE,
    ROLE_TEACHER,
    ROLE_TEACHER_CODE
} from "../../../../utils/constant";
import {getBaseRole} from "../../../../utils/roleUtils";

const UserForm = ({
                      onFinish,
                      initialValues,
                      infoHeader,
                      userType,
                      btnText,
                      datePickerOnchange,
                      genderOnchange,
                      loading,
                      isPasswordDisplay,
                      isUserNameDisplay,
                      formKey
                  }) => {
    const [subjectIds, setSubjectIds] = useState([]);
    const {
        subLoading,
        allSubjects,
        getAllSubjects,
    } = useCombo();

    const [searchDep, setSearchDep] = useState("");
    const {allDepartment, getAllDepartment, departmentLoading} = useCombo();
    const [selectedDepartmentIds, setSelectedDepartmentIds] = useState([]);

    useEffect(() => {
        getAllDepartment(searchDep);
    }, [searchDep]);

    const departmentOptions = allDepartment.map(item => ({
        value: item?.id,
        label: item?.name
    }));

    useEffect(() => {
        getAllSubjects({subjectCode: null, subjectTitle: null, departmentIds: selectedDepartmentIds});
    }, [selectedDepartmentIds]);

    // map subjects objects
    const subjectOptions = allSubjects.map((item) => {
        return {value: item.id, label: `${item?.name} - ${item?.code}`};
    });

    const subjectHandleOnChange = (values) => {
        setSubjectIds(values);
    };

    const genderOption = [
        {
            value: "MALE",
            label: "Nam",
        },
        {
            value: "FEMALE",
            label: "Nữ",
        },
    ];
    const roleOptionTeacher = [
        {
            value: 1,
            key: "student",
            label: "Sinh viên",
        },
    ];
    const roleOption = [
        {
            value: -1,
            key: "admin",
            label: "Quản trị viên",
        },
        {
            value: 0,
            key: "teacher",
            label: "Giảng viên",
        },
        {
            value: 1,
            key: "student",
            label: "Sinh viên",
        },
    ];
    const dateFormat = "DD/MM/YYYY";
    const messageRequired = "Thông tin là bắt buộc !";
    const [roleSelect, setRoleSelect] = useState(getRole().includes(ROLE_SUPER_ADMIN_CODE) ? ROLE_ADMIN : ROLE_STUDENT);
    const handleOnChange = (value) => {
        setRoleSelect(value)
    }
    return (
        <div className="user-form-component">
            <p className="info-header">{infoHeader}</p>
            <Form
                name="info-user-form"
                className="info-user-form"
                initialValues={initialValues}
                onFinish={onFinish}
                key={formKey}
            >
                <div className="info-user-header">Thông
                    tin {userType === "admin" ? "quản trị viên" : (userType === "teacher" ? "giảng viên" : "sinh viên")}</div>
                <Form.Item
                    name="lastName"
                    label="Họ và tên đệm"
                    colon={true}
                    rules={[
                        {
                            required: true,
                            message: messageRequired,
                        },
                    ]}
                >
                    <Input placeholder="Họ và tên đệm"/>
                </Form.Item>
                <Form.Item
                    name="firstName"
                    label="Tên"
                    colon={true}
                    rules={[
                        {
                            required: true,
                            message: messageRequired,
                        },
                    ]}
                >
                    <Input placeholder="Tên"/>
                </Form.Item>
                <Form.Item
                    name="userType"
                    colon={true}
                    label="Vai trò"
                    initialValue={userType === "admin" ? ROLE_ADMIN : (userType === "teacher" ? ROLE_TEACHER : ROLE_STUDENT)}
                    rules={[
                        {
                            required: true,
                            message: "Vai trò là thông tin bắt buộc!",
                        },
                    ]}
                >
                    <Select
                        placeholder="Chọn vai trò"
                        options={(getBaseRole() === ROLE_TEACHER_CODE ? roleOptionTeacher : roleOption).filter(item => item?.key === userType)}
                        disabled={true}
                        style={{height: 45}}
                        onChange={handleOnChange}
                    ></Select>
                </Form.Item>
                {userType === "student" && (
                    <Form.Item
                        name="metaData"
                        label="Khoá"
                        colon={true}
                        rules={[{required: true, message: messageRequired}]}
                    >
                        <Input placeholder="Nhập khóa của sinh viên"/>
                    </Form.Item>)}
                <Form.Item
                    name="code"
                    label={"Mã " + (userType === "admin" ? "Quản trị viên" : (userType === "teacher" ? "Giảng viên" : "Sinh viên"))}
                    colon={true}
                    rules={[
                        {
                            required: true,
                            message: messageRequired,
                        },
                    ]}
                >
                    <Input
                        placeholder={"Nhập mã " + (userType === "admin" ? "Quản trị viên" : (userType === "teacher" ? "Giảng viên" : "Sinh viên"))}/>
                </Form.Item>
                {isUserNameDisplay && (
                    <Form.Item
                        name="username"
                        label="Tên đăng nhập"
                        colon={true}
                        rules={[
                            {
                                required: true,
                                message: messageRequired,
                            },
                            {
                                min: 6,
                                message: "Tên người dùng phải có ít nhất 6 ký tự"
                            }
                        ]}
                    >
                        <Input placeholder="Nhập tên người dùng"/>
                    </Form.Item>
                )}
                <Form.Item
                    name="genderType"
                    colon={true}
                    label="Giới tính"
                    rules={[
                        {
                            required: true,
                            message: messageRequired,
                        },
                    ]}
                >
                    <Select
                        placeholder="Chọn giới tính"
                        options={genderOption}
                        onChange={genderOnchange}
                        style={{height: 45}}
                    ></Select>
                </Form.Item>
                {isPasswordDisplay && (
                    <Form.Item
                        name="password"
                        rules={[
                            {
                                required: true,
                                message: messageRequired,
                            },
                        ]}
                        label="Mật khẩu"
                        colon={true}
                    >
                        <Input.Password placeholder="Nhập mật khẩu"/>
                    </Form.Item>
                )}
                <Form.Item
                    name="email"
                    rules={[
                        {
                            type: "email",
                            message:
                                "Vui lòng điền đúng định dạng email. Ví dụ: abc@gmail.com",
                        },
                        {
                            required: true,
                            message: messageRequired,
                        },
                    ]}
                    label="Email"
                    colon={true}
                >
                    <Input placeholder="Nhập địa chỉ email"/>
                </Form.Item>
                <Form.Item
                    name="birthDate"
                    label="Ngày sinh"
                    colon={true}
                    rules={[]}
                >
                    <DatePicker
                        onChange={datePickerOnchange}
                        format={dateFormat}
                        placeholder="Chọn ngày sinh"
                    ></DatePicker>
                </Form.Item>
                <Form.Item
                    name="identificationNumber"
                    label="Số CCCD"
                    colon={true}
                    className="test"
                >
                    <Input placeholder="Nhập CCCD"/>
                </Form.Item>
                <Form.Item
                    name="phoneNumber"
                    label="Số điện thoại"
                    colon={true}
                    rules={[
                        {
                            pattern: /^(0|\+84)[1-9]\d{8}$/,
                            message:
                                "Vui lòng nhập đúng định dạng. Ví dụ: 0369841000",
                        }
                    ]}
                >
                    <Input placeholder="Nhập số điện thoại"/>
                </Form.Item>
                {userType === "teacher" && (
                    <Form.Item
                        name="subjectIds"
                        label="Học phần giảng dạy"
                        colon={true}>
                        <Select
                            mode="multiple"
                            disabled={roleSelect === 1 || selectedDepartmentIds.length === 0}
                            allowClear
                            options={subjectOptions}
                            filterOption={(input, option) =>
                                (option?.label ?? "").includes(input)
                            }
                            optionLabelProp="label"
                            tagRender={(props) => {
                                console.log(props);
                                return (<Tag color={HUST_COLOR}
                                             style={{fontSize: '12px', marginTop: '6px'}}>{props?.label}</Tag>);
                            }}
                            onChange={subjectHandleOnChange}
                            value={subjectIds}
                            loading={subLoading}
                        />
                    </Form.Item>
                )}
                {
                    getBaseRole() === ROLE_SUPER_ADMIN_CODE &&
                    <Form.Item
                        name="departmentIds"
                        colon={true}
                        label="Đơn vị quản lý"
                        rules={[
                            {
                                required: true,
                                message: messageRequired,
                            }
                        ]}
                    >
                        <Select
                            mode="multiple"
                            allowClear
                            filterOption={(input, option) =>
                                (option?.label ?? "").toLowerCase().includes(input.toLowerCase())
                            }
                            optionLabelProp="label"
                            options={departmentOptions}
                            tagRender={(props) => {
                                return (<Tag color={HUST_COLOR}
                                             style={{fontSize: '12px', marginTop: '6px'}}>{props?.label}</Tag>);
                            }}
                            loading={departmentLoading}
                            onChange={(value) => setSelectedDepartmentIds(value)}
                        />
                    </Form.Item>
                }
                <Form.Item className="btn-info">
                    <Button
                        type="primary"
                        htmlType="submit"
                        loading={loading}
                        style={{width: 150, height: 50}}
                    >
                        {btnText}
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};
export default UserForm;
