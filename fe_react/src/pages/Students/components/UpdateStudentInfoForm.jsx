import {Button, DatePicker, Form, Input, Select, Tag} from "antd";
import React, {useEffect, useState} from "react";
import "./UpdateStudentInfoForm.scss";
import useCombo from "../../../hooks/useCombo";
import {HUST_COLOR, ROLE_SUPER_ADMIN_CODE} from "../../../utils/constant";
import {getBaseRole} from "../../../utils/roleUtils";

const UpdateStudentInfoForm = ({
     onFinish,
     initialValues,
     infoHeader,
     btnText,
     loading
 }) => {
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
    const dateFormat = "DD/MM/YYYY";
    const errorMessange = "Chưa điền đầy đủ thông tin";
    const requiredMessage = "Thông tin là bắt buộc !";

    const [departmentIds, setDepartmentIds] = useState(initialValues?.departmentIds);
    const [searchDep, setSearchDep] = useState("");
    const {allDepartment, getAllDepartment, departmentLoading} = useCombo();

    useEffect(() => {
        if (getBaseRole() === ROLE_SUPER_ADMIN_CODE) {
            getAllDepartment(searchDep);
        }
    }, []);

    const departmentOptions = allDepartment.map(item => ({
        value: item?.id,
        label: item?.name
    }));

    return (
        <div className="student-info">
            <p className="info-header">{infoHeader}</p>
            <Form
                name="info-student-form"
                className="info-student-form"
                initialValues={initialValues}
                onFinish={onFinish}
            >
                <div className="info-student-header">Thông tin sinh viên</div>
                <Form.Item
                    name="lastName"
                    label="Họ và tên đệm"
                    colon={true}
                    rules={[{required: true, message: errorMessange}]}
                >
                    <Input placeholder="Nhập họ và tên đệm sinh viên"/>
                </Form.Item>
                <Form.Item
                    name="firstName"
                    label="Tên"
                    colon={true}
                    rules={[
                        {
                            required: true,
                            message: errorMessange,
                        },
                    ]}
                >
                    <Input placeholder="Nhập tên sinh viên"/>
                </Form.Item>
                <Form.Item
                    name="username"
                    label="Tên đăng nhập"
                    colon={true}
                    rules={[{ required: true, message: errorMessange }]}
                >
                    <Input disabled={true}/>
                </Form.Item>
                <Form.Item
                    name="identificationNumber"
                    label="Số CCCD"
                    colon={true}
                >
                    <Input placeholder="Nhập MSSV"/>
                </Form.Item>
                <Form.Item
                    name="code"
                    label="MSSV"
                    colon={true}
                    rules={[
                        {
                            required: true,
                            message: errorMessange,
                        },
                    ]}
                >
                    <Input placeholder="Nhập MSSV"/>
                </Form.Item>
                <Form.Item
                    name="courseNum"
                    label="Khóa"
                    rules={[{required: true, message: errorMessange}]}
                >
                    <Input placeholder="Nhập khóa"/>
                </Form.Item>
                <Form.Item
                    name="genderType"
                    colon={true}
                    label="Giới tính"
                    rules={[{required: true, message: errorMessange}]}
                >
                    <Select
                        placeholder="Chọn giới tính"
                        options={genderOption}
                        style={{height: 45}}
                    ></Select>
                </Form.Item>
                <Form.Item
                    name="email"
                    rules={[
                        {
                            type: "email",
                            message:
                                "Vui lòng nhập đúng định dạng email. Ví dụ: abc@gmail.com",
                        },
                        {
                            required: true,
                            message: errorMessange,
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
                >
                    <DatePicker
                        format={dateFormat}
                        placeholder="Chọn ngày sinh"
                    ></DatePicker>
                </Form.Item>
                <Form.Item
                    name="phoneNumber"
                    label="Số điện thoại"
                    colon={true}
                    rules={[
                        {
                            pattern: /^(0|\+84)[1-9]\d{8}$/,
                            message: "Vui lòng nhập đúng định dạng. Ví dụ: 0369841000",
                        },
                    ]}
                >
                    <Input placeholder="Nhập số điện thoại"/>
                </Form.Item>
                {
                    getBaseRole() === ROLE_SUPER_ADMIN_CODE &&
                    <Form.Item
                        name="departmentIds"
                        colon={true}
                        label="Đơn vị quản lý"
                        rules={[
                            {
                                required: true,
                                message: requiredMessage,
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
                            value={departmentIds}
                        />
                    </Form.Item>
                }
                <Form.Item className="btn-info">
                    <Button
                        type="primary"
                        htmlType="submit"
                        block
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
export default UpdateStudentInfoForm;
