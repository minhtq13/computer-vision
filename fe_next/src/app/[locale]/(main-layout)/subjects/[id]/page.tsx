"use client";
import AppButton from "@/components/app-button";
import AppTable from "@/components/app-table";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetSubjectDetailQuery, useUpdateChapterMutation, useUpdateSubjectMutation } from "@/stores/subjects/api";
import { TChapter } from "@/stores/subjects/type";
import { Form, Input, InputNumber, Popconfirm } from "antd";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useState } from "react";
import SubjectForm from "../components/SubjectForm";
import AddChapter from "../components/AddChapter";
import WrapperForm from "@/components/wrapper-form";
import ButtonBack from "@/components/button-back";
import { EPermission } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";

const SubjectDetail = ({ params }: { params: { id: string } }) => {
  const tSubjects = useTranslations("subjects");
  const tCommon = useTranslations("common");
  const notification = useAppNotification();
  const isEditing = (record: TChapter) => record?.id === editingKey;
  const [form] = Form.useForm();
  const [editingKey, setEditingKey] = useState<string | number>("");
  const handleError = useHandleError();

  const { data: subjectDetail, isLoading: subjectDetailLoading, refetch } = useGetSubjectDetailQuery({ id: params.id });

  const chapters = get(subjectDetail, "chapters", []);
  const chaptersSort = chapters.slice().sort((a, b) => a?.orders - b?.orders);

  const [updateSubject] = useUpdateSubjectMutation();
  const [updateChapter] = useUpdateChapterMutation();

  const EditableCell = ({ editing, dataIndex, inputType, children, ...restProps }) => {
    const inputNode = inputType === "number" ? <InputNumber /> : <Input />;
    return (
      <td {...restProps}>
        {editing ? (
          <Form.Item
            name={dataIndex}
            style={{
              margin: 0,
            }}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            {inputNode}
          </Form.Item>
        ) : (
          children
        )}
      </td>
    );
  };
  const chapterColumn = [
    {
      title: tSubjects("chapter"),
      dataIndex: "orders",
      width: "12%",
      editable: true,
      align: "center",
    },
    {
      title: tSubjects("content"),
      dataIndex: "title",
      width: "70%",
      editable: true,
    },
    {
      title: tCommon("action"),
      dataIndex: "action",
      align: "center",
      fixed: "right",
      width: "18%",
      render: (_, record: TChapter) => {
        const editable = isEditing(record);
        return editable ? (
          <span>
            <AppButton customclass="mr-2" onClick={() => save(record)}>
              {tCommon("save")}
            </AppButton>
            <Popconfirm cancelText={tCommon("close")} okText={tCommon("confirm")} title={tSubjects("confirmExit")} onConfirm={cancel}>
              <AppButton>{tCommon("close")}</AppButton>
            </Popconfirm>
          </span>
        ) : (
          <AppButton disabled={editingKey !== ""} onClick={() => edit(record)}>
            {tCommon("update")}
          </AppButton>
        );
      },
    },
  ];

  const edit = (record: TChapter) => {
    form.setFieldsValue({
      orders: "",
      title: "",
      ...record,
    });
    setEditingKey(record.id);
  };

  const cancel = () => {
    setEditingKey("");
  };

  const save = async (record: TChapter) => {
    const row = await form.validateFields();
    try {
      await updateChapter({
        chapterId: record.id.toString(),
        orders: row.orders,
        title: row.title,
        description: row.description,
      }).unwrap();
      refetch();
      setEditingKey("");
      notification.success({
        description: tSubjects("updateChapterSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const mergedColumns = chapterColumn.map((col) => {
    if (!col.editable) {
      return col;
    }
    return {
      ...col,
      onCell: (record) => ({
        record,
        inputType: col.dataIndex === "orders" ? "number" : "text",
        dataIndex: col.dataIndex,
        title: col.title,
        editing: isEditing(record),
      }),
    };
  });

  const onFinish = async (value: any) => {
    try {
      await updateSubject({
        subjectId: params.id,
        code: value.code,
        credit: value.credit,
        description: value.description,
        title: value.title,
        departmentId: value.departmentId,
      }).unwrap();
      refetch();
      notification.success({
        description: tSubjects("updateSubjectSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.SUBJECT_UPDATE]}>
      <div>
        <ButtonBack />
        <div className="lg:py-4">
          <p className="text-2xl font-bold text-hust my-3">{`${subjectDetail?.title || ""} - ${subjectDetail?.code || ""}`}</p>
          <p className="text-sm text-text-secondary-1 mt-2">{`${subjectDetail?.description || ""}`}</p>
        </div>

        <SubjectForm
          initialValues={subjectDetail}
          onFinish={onFinish}
          infoHeader={tSubjects("subjectInfo")}
          editItems={chapters}
          textBtn={tCommon("update")}
        />
        <p className="info-header text-[20px] font-bold text-text-hust mb-4 w-full pt-6 pb-3">{tSubjects("subjectProgram")}</p>
        <WrapperForm>
          <Form form={form} component={false}>
            <AppTable
              scroll={{ x: 800 }}
              labelPagination={tCommon("subject")}
              loading={subjectDetailLoading}
              components={{
                body: {
                  cell: EditableCell,
                },
              }}
              bordered
              dataSource={chaptersSort}
              columns={mergedColumns as any}
              pagination={false}
            />
          </Form>
          <div className="py-3">
            <AddChapter subjectDetail={subjectDetail} refetchSubjectDetail={refetch} />
          </div>
        </WrapperForm>
      </div>
    </PermissionGuard>
  );
};

export default SubjectDetail;
