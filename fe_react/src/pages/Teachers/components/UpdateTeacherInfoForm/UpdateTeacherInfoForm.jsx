import {DatePicker, Form, Input, Select, Button, Tag} from "antd";
import "./UpdateTeacherInfoForm.scss";
import React, {useEffect, useState} from "react";
import useCombo from "../../../../hooks/useCombo";
import {
  dateTimePattern,
  HUST_COLOR,
  ROLE_ADMIN,
  ROLE_ADMIN_SYSTEM_CODE,
  ROLE_SUPER_ADMIN_CODE,
  ROLE_TEACHER
} from "../../../../utils/constant";
import {getBaseRole} from "../../../../utils/roleUtils";
import {getRole} from "../../../../utils/storage";
const UpdateTeacherInfoForm = ({
  onFinish,
  initialValues,
  infoHeader,
  btnText,
  loading,
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

  const [searchDep, setSearchDep] = useState("");
  const [departmentIds, setDepartmentIds] = useState(initialValues?.departmentIds);
  const [subjectIds, setSubjectIds] = useState(initialValues?.subjectIds);
  const {allDepartment, getAllDepartment, departmentLoading} = useCombo();

  useEffect(() => {
    getAllDepartment(searchDep);
  }, [searchDep]);

  const departmentOptions = allDepartment.map(item => ({
    value: item?.id,
    label: item?.name
  }));

  const {
    subLoading,
    allSubjects,
    getAllSubjects,
  } = useCombo();

  useEffect(() => {
      getAllSubjects({subjectCode: null, subjectTitle: null});
  }, []);

  // map subjects objects
  const subjectOptions = allSubjects.map((item) => {
    return {value: item.id, label: `${item?.name} - ${item?.code}`};
  });

  const subjectHandleOnChange = (values) => {
    setSubjectIds(values);
  };

  const dateFormat = dateTimePattern.FORMAT_DD_MM_YYYY_SLASH;
  const errorMessange = "Chưa điền đầy đủ thông tin";
  const requiredMessage = "Thông tin là bắt buộc !";
  return (
    <div className="teacher-info">
      <p className="info-header">{infoHeader}</p>
      <Form
        name="info-teacher-form"
        className="info-teacher-form"
        initialValues={initialValues}
        onFinish={onFinish}
      >
        <div className="info-teacher-header">{initialValues?.userType === ROLE_ADMIN ? "Thông tin quản trị viên" : "Thông tin giảng viên"}</div>
        <Form.Item
          name="lastName"
          label="Họ và tên đệm"
          colon={true}
          rules={[{ required: true, message: errorMessange }]}
        >
          <Input placeholder="Nhập họ và tên đệm giảng viên" />
        </Form.Item>
        <Form.Item
          name="firstName"
          label="Tên"
          colon={true}
          rules={[{ required: true, message: errorMessange }]}
        >
          <Input placeholder="Nhập tên giảng viên" />
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
          className="test"
        >
          <Input placeholder="Nhập CCCD" />
        </Form.Item>
        <Form.Item
          name="code"
          label="Mã cán bộ"
          colon={true}
          rules={[{ required: true, message: errorMessange }]}
        >
          <Input placeholder="Nhập mã cán bộ" />
        </Form.Item>
        <Form.Item
          name="genderType"
          colon={true}
          label="Giới tính"
          rules={[{ required: true, message: errorMessange }]}
        >
          <Select
            placeholder="Chọn giới tính"
            options={genderOption}
            style={{ height: 45 }}
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
              message: errorMessange,
            },
          ]}
          label="Email"
          colon={true}
        >
          <Input placeholder="Nhập địa chỉ email" />
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
            }
          ]}
        >
          <Input placeholder="Nhập số điện thoại" />
        </Form.Item>
        {
          initialValues?.userType === ROLE_TEACHER &&
          <Form.Item
              name="subjectIds"
              label="Học phần giảng dạy"
              colon={true}>
            <Select
                mode="multiple"
                allowClear
                options={subjectOptions}
                filterOption={(input, option) =>
                    (option?.label ?? "").toLowerCase().includes(input.toLowerCase())
                }
                optionLabelProp="label"
                tagRender={(props) => {
                  return (<Tag color={HUST_COLOR} style={{fontSize: '12px', marginTop: '6px'}}>{props?.label}</Tag>);
                }}
                onChange={subjectHandleOnChange}
                value={subjectIds}
                loading={subLoading}
            />
          </Form.Item>
        }
        {
          getBaseRole() === ROLE_SUPER_ADMIN_CODE && getRole().includes(ROLE_ADMIN_SYSTEM_CODE) &&
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
                  return (<Tag color={HUST_COLOR} style={{fontSize: '12px', marginTop: '6px'}}>{props?.label}</Tag>);
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
            loading={loading}
            style={{ width: 150, height: 50 }}
          >
            {btnText}
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};
export default UpdateTeacherInfoForm;
