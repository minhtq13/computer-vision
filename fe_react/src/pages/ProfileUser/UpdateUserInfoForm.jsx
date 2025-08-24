import {Avatar, Button, DatePicker, Form, Input, Select, Upload, message, Tooltip} from "antd";
import React, {useEffect, useState} from "react";
import {genderOption, roleOption} from "../../utils/constant";
import "./UpdateUserInfoForm.scss";
import {EditOutlined, PlusOutlined} from "@ant-design/icons";
import useAccount from "../../hooks/useAccount";
import {BASE_RESOURCE_URL} from "../../config/apiPath";

const UpdateUserInfoForm = ({
        onFinish,
        initialValues,
        infoHeader,
        btnText,
        datePickerOnchange,
        genderOnchange,
        isPasswordDisplay,
        isUserNameDisplay,
        isMailDisplay,
        isCodeDisplay
    }) => {
    const dateFormat = "DD/MM/YYYY";
    const messageRequired = "Trường này là bắt buộc!"
    const [avatarPath, setAvatarPath] = useState("");
    const [avatarId, setAvatarId] = useState(null);
    const [avatarChanged, setAvatarChanged] = useState(false);
    const {uploadAvatar, uploadLoading, uploadedData} = useAccount();
    // Patch value to form
    const form = Form.useForm()[0];

    useEffect(() => {
        if (initialValues !== undefined) {
            form.setFieldsValue(initialValues);
            setAvatarPath(initialValues?.avatar);
            setAvatarId(initialValues?.avatarId);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [initialValues]);

    // re-update avatar
    useEffect(() => {
        if (!uploadLoading && uploadedData) {
            setAvatarPath(uploadedData?.fileAttachDB?.storedType === 1 ? uploadedData?.filePath : BASE_RESOURCE_URL + uploadedData?.filePath);
            setAvatarId(uploadedData?.id);
        }

    }, [uploadLoading, uploadedData]);

    // validate before uploading
    const beforeUpload = (file) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('Vui lòng chọn định dạng ảnh JPG/PNG!').then(() => {
            });
        }
        const isLt10M = file.size / 1024 / 1024 < 10;
        if (!isLt10M) {
            message.error('Ảnh của bạn chỉ có thể nhỏ hơn 10MB!').then(() => {
            });
        }
        return isJpgOrPng && isLt10M;
    };

    return (
        <div className="update-user-info-form-component">
            <p className="info-header">{infoHeader}</p>
            <div className="user-avatar">
                <div className="avatar-preview-upload">
                    <Tooltip className="cursor-pointer" title={avatarChanged ? "Chưa lưu ảnh đại diện" : "Đã lưu ảnh đại diện"}>
                        <Avatar style={{
                            border: avatarChanged ? '2px solid red' : '2px solid greenyellow',
                            cursor: 'pointer',
                            backgroundColor: '#f56a00',
                            color: '#ffffff',
                            display: 'flex',
                            alignItems: 'center'
                        }} size={128} src={avatarPath} shape={"circle"}>
                            <p style={{fontSize: '36pt'}}>{initialValues?.lastName?.charAt(0)}</p>
                        </Avatar>
                    </Tooltip>
                    <Upload
                        accept="image/png, image/jpeg"
                        fileList={[]}
                        customRequest={() => {}} // prevent a default form submission by a NOP callback
                        beforeUpload={beforeUpload}
                        onChange={(e) => {
                            setAvatarChanged(true);
                            const {name, type, originFileObj} = e?.file;
                            const formData = new FormData();
                            formData.append("file", new File([originFileObj], name, {type}));
                            uploadAvatar(formData);
                        }}
                    >
                        <Button
                            className="avatar-upload-btn"
                            style={{
                                border: 0,
                                background: '#ffffff',
                            }}
                        >
                            {avatarPath ? <EditOutlined/> : <PlusOutlined/>} Cập nhật ảnh đại diện
                        </Button>
                    </Upload>
                </div>
            </div>
            <Form
                form={form}
                name="info-user-form"
                className="info-user-form"
                onFinish={(values) => {
                    onFinish({...values, avatarId: avatarId});
                    setAvatarChanged(false);
                }}
            >
                <Form.Item
                    name="lastName"
                    label="Họ và tên đệm"
                    colon={true}
                    rules={[{required: true, message: messageRequired}]}
                >
                    <Input placeholder="Họ và tên đệm"/>
                </Form.Item>
                <Form.Item
                    name="firstName"
                    label="Tên"
                    colon={true}
                    rules={[{required: true, message: messageRequired}]}
                >
                    <Input placeholder="Tên"/>
                </Form.Item>
                <Form.Item
                    name="userType"
                    colon={true}
                    label="Vai trò"
                    rules={[{required: true, message: messageRequired}]}
                >
                    <Select
                        disabled={true}
                        placeholder="Chọn vai trò"
                        options={roleOption}
                        style={{height: 45}}
                    ></Select>
                </Form.Item>
                <Form.Item
                    name="code"
                    label="Mã số SV/GV"
                    colon={true}
                    rules={[{required: true, message: messageRequired}]}
                >
                    <Input disabled={!isCodeDisplay} placeholder="Nhập mã số SV/GV"/>
                </Form.Item>
                {isUserNameDisplay && (
                    <Form.Item
                        name="username"
                        label="Tên đăng nhập"
                        colon={true}
                        rules={[{required: true, message: messageRequired},
                            {
                                min: 5,
                                message: "Tên người dùng phải có ít nhất 6 ký tự"
                            }
                        ]}
                    >
                        <Input disabled={true} placeholder="Nhập tên người dùng"/>
                    </Form.Item>
                )}
                <Form.Item
                    name="genderType"
                    colon={true}
                    label="Giới tính"
                    rules={[{required: true, message: messageRequired}]}
                >
                    <Select
                        placeholder="Chọn giới tính"
                        options={genderOption}
                        onChange={genderOnchange}
                        style={{height: 45}}
                    ></Select>
                </Form.Item>
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
                    <Input disabled={!isMailDisplay} placeholder="Nhập địa chỉ email"/>
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
                {isPasswordDisplay && (
                    <Form.Item
                        name="password"
                        rules={[{required: true, message: messageRequired}]}
                        label="Mật khẩu"
                        colon={true}
                    >
                        <Input.Password placeholder="Nhập mật khẩu" autoComplete="on"/>
                    </Form.Item>
                )}
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
                <Form.Item
                    name="identificationNumber"
                    label="Số CCCD"
                    colon={true}
                    className="test"
                >
                    <Input placeholder="Nhập CCCD"/>
                </Form.Item>
                <Form.Item className="btn-info">
                    <Button
                        type="primary"
                        htmlType="submit"
                        style={{width: 150, height: 50}}
                    >
                        {btnText}
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};
export default UpdateUserInfoForm;
