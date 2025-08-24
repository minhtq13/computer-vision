import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import AppInput from "@/components/app-input";
import AppSelectSmall from "@/components/app-select-small";
import SubmitButton from "@/components/submit-button";
import WrapperForm from "@/components/wrapper-form";
import { getOptionsFromCombo } from "@/helpers";
import { useGetComboDepartmentQuery } from "@/stores/combo/api";
import { RAddSubject } from "@/stores/subjects/type";
import { HUST_COLOR } from "@/constants";
import { Form, Tag } from "antd";
import { useTranslations } from "next-intl";
import { useEffect } from "react";

interface SubjectFormProps {
  onFinish: (values: RAddSubject) => void;
  loading?: boolean;
  infoHeader: string;
  editItems?: any;
  textBtn?: string;
  initialValues?: any;
}

const SubjectForm = ({ onFinish, loading, infoHeader, textBtn, initialValues }: SubjectFormProps) => {
  const tSubjects = useTranslations("subjects");
  const tCommon = useTranslations("common");
  // const { role } = useRole();
  const [form] = Form.useForm();
  // const handleError = useHandleError();
  // const notification = useAppNotification();

  const { data: departments, isLoading: departmentLoading } = useGetComboDepartmentQuery({ search: "" });

  // const [deleteChapter, { isLoading: deleteChapterLoading }] = useDeleteChapterMutation();

  // const handleDelete = async (index: number, remove: any) => {
  //   try {
  //     await deleteChapter({ id: editItems[index].id }).unwrap();
  //     remove(index);
  //     notification.success({
  //       description: "Xoá thành công!",
  //     });
  //   } catch (error) {
  //     handleError(error);
  //   }
  // };

  useEffect(() => {
    if (initialValues) {
      form.setFieldsValue(initialValues);
    }
  }, [initialValues]);
  return (
    <div className="subject-info">
      <p className="info-header text-[20px] font-bold text-text-hust mb-4 w-full">{infoHeader}</p>
      <WrapperForm>
        <Form
          form={form}
          name="info-subject-form"
          className="info-subject-form flex flex-wrap justify-between"
          initialValues={initialValues}
          onFinish={onFinish}
        >
          <Form.Item
            name="code"
            className="w-[45%] !mb-2"
            colon={true}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tSubjects("subjectCode")} title={tSubjects("subjectCode")} required />
          </Form.Item>
          <Form.Item
            name="title"
            colon={true}
            className="w-[45%] !mb-2"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tSubjects("subjectName")} title={tSubjects("subjectName")} required />
          </Form.Item>
          <Form.Item
            name="credit"
            colon={true}
            className="w-[45%] !mb-2"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
              {
                pattern: /^[1-9]\d*$/,
                message: tCommon("wrongFormat"),
              },
            ]}
          >
            <AppInput placeholder={tSubjects("subjectCredit")} title={tSubjects("subjectCredit")} required />
          </Form.Item>
          <div className="w-[45%] !mb-2">
            <div className="w-full flex items-center justify-between mb-1.5">
              <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                <span className="font-medium">{tSubjects("departmentName")}</span>
                <RequiredFieldIcon className="ml-1" />
              </div>
            </div>
            <Form.Item
              className="w-full !mb-0"
              name="departmentId"
              rules={[
                {
                  required: true,
                  message: tCommon("requiredField"),
                },
              ]}
              getValueFromEvent={(departmentId: number) => departmentId}
            >
              <AppSelectSmall
                placeholder={tSubjects("departmentName")}
                className="w-full"
                allowClear
                loading={departmentLoading}
                options={getOptionsFromCombo(departments)}
                tagRender={(props) => {
                  return (
                    <Tag color={HUST_COLOR} className="!text-xs !m-1.5">
                      {props?.label}
                    </Tag>
                  );
                }}
              />
            </Form.Item>
          </div>
          <Form.Item
            name="description"
            colon={true}
            className="w-[45%] !mb-2"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tSubjects("description")} title={tSubjects("description")} required />
          </Form.Item>
          {/* {chaptersVisible && (
          <div className="subject-chapters">
            <div className="subject-chapter-header">Nội dung</div>
            <Form.List name="lstChapter" initialValue={editItems}>
              {(fields, { add, remove }) => {
                return (
                  <>
                    {fields.map((field, index) => (
                      <div className="form-space" key={`editChapter${index}`}>
                        <div className="subject-order-title">
                          <span>Order:</span>
                          <Form.Item
                            {...field}
                            name={[field.name, `order`]}
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
                            name={[field.name, `title`]}
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
                          buttonDisable={true}
                          buttonOpenModal={<Button icon={<DeleteOutlined />}></Button>}
                          title="Delete Chapter"
                          message="Bạn có chắc chắn muốn xóa học phần này không?"
                          ok="Đồng ý"
                          onAccept={() => handleDelete(index, remove)}
                          icon={<DeletePopUpIcon />}
                        />
                      </div>
                    ))}
                  </>
                );
              }}
            </Form.List>
          </div>
        )} */}
          <Form.Item className="btn-info w-full flex justify-center !mt-6 !mb-0">
            <SubmitButton form={form} customclass="!w-[150px] !h-[50px]" loading={loading}>
              {textBtn}
            </SubmitButton>
          </Form.Item>
        </Form>
      </WrapperForm>
    </div>
  );
};

export default SubjectForm;
