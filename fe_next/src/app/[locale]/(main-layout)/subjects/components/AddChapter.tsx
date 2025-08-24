import { Form } from "antd";

import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import SubmitButton from "@/components/submit-button";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useAddChapterMutation } from "@/stores/subjects/api";
import { TSubjectDetail } from "@/stores/subjects/type";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { useTranslations } from "next-intl";
import { useState } from "react";

interface IAddChapter {
  subjectDetail: TSubjectDetail;
  refetchSubjectDetail: any;
}

const AddChapter = ({ subjectDetail, refetchSubjectDetail }: IAddChapter) => {
  const tSubjects = useTranslations("subjects");
  const tCommon = useTranslations("common");
  const [formKey, setFormKey] = useState();
  const notification = useAppNotification();
  const [addChapter, { isLoading }] = useAddChapterMutation();
  const handleError = useHandleError();
  const form = Form.useForm()[0];
  const onFinish = async (values: any) => {
    if (!values.chaptersAdd || values.chaptersAdd.length === 0) {
      notification.error({
        description: tSubjects("noChapterToAdd"),
      });
      return;
    }
    try {
      if (values?.chaptersAdd.length > 0) {
        await addChapter({
          subjectId: subjectDetail?.id.toString(),
          lstChapter: values.chaptersAdd.map((item) => {
            return { orders: item.orderAdd, title: item.titleAdd, description: item.descriptionAdd || "" };
          }),
        }).unwrap();
        notification.success({
          description: tSubjects("addChapterSuccess"),
        });
        setFormKey((pre: any) => pre + 1);
        refetchSubjectDetail();
      }
    } catch (error) {
      handleError(error);
    }
  };

  const numberOfChapter = subjectDetail?.chapters?.length || 0;

  return (
    <div className="subject-view">
      <div className="subject-view-info">
        <div className="subject-content-tab">
          <Form form={form} name="subject-content-form" onFinish={onFinish} className="subject-content-form" key={formKey}>
            <Form.List name="chaptersAdd">
              {(fields, fieldOperations) => {
                return (
                  <>
                    {fields.map((field, index) => {
                      return (
                        <div key={index} className="item-space my-3 border-b border-disable-secondary pb-3">
                          <div className="flex gap-4 items-center">
                            <div className="form-space flex gap-2 flex-col py-2 flex-1 ">
                              <Form.Item
                                {...field}
                                initialValue={numberOfChapter + index + 1}
                                className="add-chapter-order !mb-0"
                                name={[field.name, `orderAdd`]}
                                key={`orders-add${index}`}
                              >
                                <AppInput disabled={true} title={tSubjects("chapter")} />
                              </Form.Item>
                              <Form.Item
                                {...fields}
                                className="add-chapter-content !mb-0"
                                name={[field.name, `titleAdd`]}
                                key={`title-add${index}`}
                                rules={[
                                  {
                                    required: true,
                                    message: tSubjects("enterChapterContent"),
                                  },
                                ]}
                              >
                                <AppInput placeholder={tSubjects("enterChapterContent")} title={tSubjects("content")} required />
                              </Form.Item>
                            </div>
                            <div className="btn-space">
                              <AppButton onClick={() => fieldOperations.remove(index)}>
                                <DeleteOutlined />
                              </AppButton>
                            </div>
                          </div>
                        </div>
                      );
                    })}
                    <Form.Item className="btn !mt-4" key={`btnAdd`}>
                      <AppButton
                        className="w-full mt-4"
                        type="dashed"
                        onClick={() => {
                          fieldOperations.add();
                        }}
                        block
                        icon={<PlusOutlined />}
                      >
                        {tSubjects("addChapter")}
                      </AppButton>
                    </Form.Item>
                  </>
                );
              }}
            </Form.List>
            <div className="flex justify-center w-full">
              <Form.Item className="btn btn-submit">
                <SubmitButton form={form} customclass="!w-[150px] !h-[50px]" loading={isLoading}>
                  {tCommon("update")}
                </SubmitButton>
              </Form.Item>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
};
export default AddChapter;
