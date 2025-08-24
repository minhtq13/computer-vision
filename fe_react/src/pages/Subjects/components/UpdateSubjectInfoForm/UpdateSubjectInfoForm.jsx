import {Form, Input, Button, Skeleton, Select} from "antd";
import {DeleteOutlined} from "@ant-design/icons";
import "./UpdateSubjectInfoForm.scss";
import React, {useEffect} from "react";
import useNotify from "../../../../hooks/useNotify";
import deletePopUpIcon from "../../../../assets/images/svg/delete-icon.svg";
import ModalPopup from "../../../../components/ModalPopup/ModalPopup";
import {deleteChaptersService} from "../../../../services/subjectsService";
import useSubjects from "../../../../hooks/useSubjects";
import useCombo from "../../../../hooks/useCombo";
import {processApiError} from "../../../../utils/apiUtils";
import {ROLE_SUPER_ADMIN_CODE} from "../../../../utils/constant";
import {getBaseRole} from "../../../../utils/roleUtils";

const UpdateSubjectInfoForm = ({
                                   onFinish,
                                   initialValues,
                                   infoHeader,
                                   btnText,
                                   loading,
                                   skeletonLoading,
                                   chaptersVisible,
                                   editItems,
                                   id,
                               }) => {
    const {querySubjectDetail} = useSubjects();
    const {allDepartment, getAllDepartment, departmentLoading} = useCombo();
    const notify = useNotify();
    const errorMessage = "Thông tin này là bắt buộc !";

    useEffect(() => {
        getAllDepartment("");
    }, []);

    const departmentOptions = allDepartment.filter(item => item?.code !== "ADMIN_CENTER").map(item => ({
        value: item?.id,
        label: item?.name
    }));

    return (
        <div className="subject-info">
            <p className="info-header">{infoHeader}</p>
            <Skeleton active loading={skeletonLoading}>
                <Form
                    name="info-subject-form"
                    className="info-subject-form"
                    initialValues={initialValues}
                    onFinish={onFinish}
                >
                    <div className="info-subject-header">
                        Thông tin học phần
                    </div>
                    <Form.Item
                        name="code"
                        label="Mã học phần"
                        colon={true}
                        rules={[
                            {
                                required: true,
                                message: errorMessage,
                            },
                        ]}
                    >
                        <Input placeholder="Nhập mã học phần"/>
                    </Form.Item>
                    <Form.Item
                        name="title"
                        label="Tên học phần"
                        colon={true}
                        rules={[
                            {
                                required: true,
                                message: errorMessage,
                            },
                        ]}
                    >
                        <Input placeholder="Nhập tên học phần"/>
                    </Form.Item>
                    <Form.Item
                        name="credit"
                        label="Số tín chỉ"
                        colon={true}
                        rules={[
                            {
                                required: true,
                                message: errorMessage,
                            },
                            {
                                pattern: /^[1-9]\d*$/,
                                message: "Vui lòng nhập một số",
                            },
                        ]}
                    >
                        <Input placeholder="Nhập số tín chỉ"/>
                    </Form.Item>
                    <Form.Item
                        name="departmentId"
                        colon={true}
                        label="Đơn vị quản lý"
                        rules={[
                            {
                                required: true,
                                message: errorMessage,
                            }
                        ]}
                    >
                        <Select
                            disabled={getBaseRole() !== ROLE_SUPER_ADMIN_CODE}
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                width: '100%',
                                height: '100%',
                            }}
                            allowClear
                            filterOption={(input, option) =>
                                (option?.label ?? "").toLowerCase().includes(input.toLowerCase())
                            }
                            optionLabelProp="label"
                            options={departmentOptions}
                            loading={departmentLoading}
                            placeholder={"Chọn đơn vị quản lý"}
                        />
                    </Form.Item>
                    <Form.Item
                        name="description"
                        label="Mô tả"
                        colon={true}
                        rules={[
                            {
                                required: true,
                                message: errorMessage,
                            },
                        ]}
                    >
                        <Input.TextArea placeholder="Nhập mô tả"/>
                    </Form.Item>
                    {chaptersVisible && (
                        <div className="subject-chapters">
                            <div className="subject-chapter-header">
                                Nội dung
                            </div>
                            <Form.List
                                name="lstChapter"
                                initialValue={editItems}
                            >
                                {(fields, {add, remove}) => {
                                    return (
                                        <>
                                            {fields.map((field, index) => (
                                                <div
                                                    className="form-space"
                                                    key={`editChapter${index}`}
                                                >
                                                    <div className="subject-order-title">
                                                        <span>Order:</span>
                                                        <Form.Item
                                                            {...field}
                                                            name={[
                                                                field.name,
                                                                `order`,
                                                            ]}
                                                            key={`order${index}`}
                                                            style={{
                                                                width: "20%",
                                                            }}
                                                            noStyle
                                                            label="Order"
                                                            //initialValue={field.order}
                                                        >
                                                            <Input
                                                                aria-label="Order"
                                                                placeholder="Enter the order"
                                                                style={{
                                                                    width: "100%",
                                                                }}
                                                            />
                                                        </Form.Item>
                                                    </div>
                                                    <div className="subject-order-title subject-title">
                                                        <span>Title:</span>
                                                        <Form.Item
                                                            {...field}
                                                            name={[
                                                                field.name,
                                                                `title`,
                                                            ]}
                                                            key={`title${index}`}
                                                            noStyle
                                                            style={{
                                                                width: "50%",
                                                            }}
                                                            //initialValue={field.title}
                                                        >
                                                            <Input
                                                                placeholder="Enter the title"
                                                                style={{
                                                                    width: "100%",
                                                                }}
                                                            />
                                                        </Form.Item>
                                                    </div>
                                                    <ModalPopup
                                                        buttonOpenModal={
                                                            <Button
                                                                icon={
                                                                    <DeleteOutlined/>
                                                                }
                                                            ></Button>
                                                        }
                                                        title="Delete Chapter"
                                                        message="Bạn có chắc chắn muốn xóa học phần này không?"
                                                        ok="Đồng ý"
                                                        onAccept={() => {
                                                            deleteChaptersService(
                                                                editItems[index]
                                                                    .id,
                                                                null,
                                                                (res) => {
                                                                    remove(
                                                                        index
                                                                    );
                                                                    notify.success(
                                                                        "Xoá thành công!"
                                                                    );
                                                                    querySubjectDetail(
                                                                        {},
                                                                        id
                                                                    );
                                                                },
                                                                (error) => {
                                                                    notify.error(processApiError(error));
                                                                }
                                                            ).then(() => {
                                                            });
                                                        }}
                                                        confirmMessage="Thao tác này không thể hoàn tác"
                                                        icon={deletePopUpIcon}
                                                    />
                                                </div>
                                            ))}
                                        </>
                                    );
                                }}
                            </Form.List>
                        </div>
                    )}
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
            </Skeleton>
        </div>
    );
};
export default UpdateSubjectInfoForm;
