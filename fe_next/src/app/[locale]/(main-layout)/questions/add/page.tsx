"use client";
import AppButton from "@/components/app-button";
import AppSelectSmall from "@/components/app-select-small";
import ButtonBack from "@/components/button-back";
import FilterByTag from "@/components/filter-by-tag";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import WrapperForm from "@/components/wrapper-form";
import { getOptionsFromCombo } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useGetComboChapterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useAddQuestionMutation } from "@/stores/questions/api";
import { EPermission } from "@/types/enum";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { Checkbox, Form, Switch } from "antd";
import { useTranslations } from "next-intl";
import dynamic from "next/dynamic";
import { useMemo, useState } from "react";
import "react-quill/dist/quill.snow.css";
// import { Quill } from "react-quill";
// const ImageResize = dynamic(() => import("quill-image-resize-module-react"), { ssr: false });

const AddQuestions = () => {
  const ReactQuill = useMemo(() => dynamic(() => import("react-quill"), { ssr: false }), []);
  // Quill.register("modules/imageResize", ImageResize);
  const tQuestion = useTranslations("questions");
  const tCommon = useTranslations("common");
  const { levelOptionsWithoutAll } = useLocaleOptions();
  const [checked, setChecked] = useState(false);
  const [formKey, setFormKey] = useState(0);
  const [preChapter, setPreChapter] = useState(null);
  const [value, setValue] = useState("");
  const [subjectId, setSubjectId] = useState(null);
  const [chapterId, setChapterId] = useState(null);
  const notification = useAppNotification();
  const handleError = useHandleError();

  const { data: allChapters, isLoading: chapterLoading } = useGetComboChapterQuery(
    { subjectId: subjectId },
    {
      skip: !subjectId,
    }
  );
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});
  const modules = {
    toolbar: [
      ["bold", "italic", "underline"], // toggled buttons
      ["blockquote", "code-block"],
      [{ list: "ordered" }, { list: "bullet" }],
      [{ script: "sub" }, { script: "super" }], // superscript/subscript
      [{ align: [] }],
      ["link", "image", "formula"],
      [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
      ["clean"],
    ],

    clipboard: {
      matchVisual: false,
    },
    // imageResize: {
    //   parchment: (Quill as any).import("parchment"),
    //   modules: ["Resize", "DisplaySize", "Toolbar"],
    // },
  };
  const formats = [
    "list",
    "size",
    "bold",
    "italic",
    "underline",
    "blockquote",
    "indent",
    "link",
    "image",
    "code-block",
    "align",
    "script",
    "formula",
  ];

  const subjectOnChange = (value) => {
    setSubjectId(value);
    setChapterId(null);
    setFormKey((prevKey) => prevKey + 1);
  };
  const chapterOnchange = (values) => {
    if (preChapter !== values) {
      setPreChapter(values);
      setFormKey((prevKey) => prevKey + 1);
    }
    setChapterId(values);
  };
  const handleClickButtonAddQuestion = (childListOperations) => {
    childListOperations.add();
  };

  const [addQuestion, { isLoading: addQuestionLoading }] = useAddQuestionMutation();
  const onFinish = async (values: any) => {
    if (!values.lstQuestion || values.lstQuestion.length === 0) {
      notification.error({
        description: tQuestion("pleaseAddAtLeastOneQuestion"),
      });
      return;
    }

    // Validate each question
    for (let i = 0; i < values.lstQuestion.length; i++) {
      const question = values.lstQuestion[i];
      const questionNumber = i + 1;

      // Case 1: Check if question has at least one correct answer
      const correctAnswers = question.lstAnswer.filter((answer) => answer.isCorrect === true);

      if (correctAnswers.length === 0) {
        notification.error({
          description: tQuestion("questionNeedsCorrectAnswer", { questionNumber }),
        });
        return;
      }

      // Case 2: Check single answer questions have only one correct answer
      if (question.isMultipleAnswers === false && correctAnswers.length > 1) {
        notification.error({
          description: tQuestion("singleAnswerQuestionError", { questionNumber, correctAnswersLength: correctAnswers.length }),
        });
        return;
      }
    }

    // If all validations pass, proceed with submission
    try {
      await addQuestion({
        chapterId: chapterId,
        lstQuestion: values.lstQuestion.map((item) => {
          return {
            ...item,
            imageId: null,
            lstAnswer: item.lstAnswer.map((answer) => {
              const isCorrectBol = answer.isCorrect;
              return {
                content: answer.content,
                isCorrect: isCorrectBol ?? false,
                imageId: null,
              };
            }),
            tagIds: item.tagIds || [],
          };
        }),
      }).unwrap();
      notification.success({
        description: tQuestion("addQuestionSuccess"),
      });
      setFormKey((prevKey) => prevKey + 1);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.QUESTION_CREATE]}>
      <div className="question-add">
        <div className="mb-3">
          <ButtonBack />
        </div>
        <p className="text-2xl font-bold text-hust my-3 mb-3">{tQuestion("addQuestion")}</p>
        <WrapperForm className="mb-4">
          <div className="subject-chapter flex gap-4 max-lg:flex-col max-lg:w-full">
            <AppSelectSmall
              customClassName="lg:max-xl:w-1/2 min-w-[350px]"
              rootClassName="[&>.ant-select-selector]:w-full w-full"
              title={tCommon("subject")}
              allowClear
              showSearch
              placeholder={tCommon("subject")}
              optionFilterProp="children"
              optionLabelProp="label"
              options={getOptionsFromCombo(allSubjects)}
              onChange={subjectOnChange}
              loading={subLoading}
            />
            <AppSelectSmall
              customClassName="lg:max-xl:w-1/2 min-w-[350px]"
              rootClassName="[&>.ant-select-selector]:w-full w-full"
              title={tCommon("chapter")}
              showSearch
              allowClear
              placeholder={tCommon("chapter")}
              optionFilterProp="children"
              optionLabelProp="label"
              options={getOptionsFromCombo(allChapters)}
              onChange={chapterOnchange}
              loading={chapterLoading}
              value={chapterId}
            />
          </div>
        </WrapperForm>
        <WrapperForm>
          <Form onFinish={onFinish} name="question-form" key={formKey}>
            <Form.List name="lstQuestion">
              {(parentFields, parentListOperations) => (
                <>
                  {parentFields.map((parentField, parentIndex) => (
                    <div key={parentIndex} className="question-list p-6 bg-white rounded-lg max-md:p-0">
                      <div className="question-level-multiple-ans flex gap-4 max-md:flex-col mb-4">
                        <Form.Item
                          {...parentField}
                          className="!mb-0"
                          label={tCommon("level")}
                          name={[parentField.name, `level`]}
                          rules={[{ required: true, message: tCommon("requiredField") }]}
                          initialValue={"EASY"}
                          key={`${parentIndex}-${parentField.key}-question-level`}
                        >
                          <AppSelectSmall options={levelOptionsWithoutAll} customClassName="!w-[80px]" />
                        </Form.Item>
                        <Form.Item
                          {...parentField}
                          className="!mb-0"
                          label={tQuestion("multpleChoice")}
                          name={[parentField.name, `isMultipleAnswers`]}
                          rules={[{ required: true, message: tQuestion("notSelectMultipleAnswer") }]}
                          initialValue={true}
                          key={`${parentIndex}-${parentField.key}-question-multiple-ans`}
                        >
                          <Switch />
                        </Form.Item>
                        <Form.Item className="tag !mb-0" label={tCommon("tag")} name={[parentField.name, "tagIds"]}>
                          <FilterByTag mode="multiple" className="min-w-[200px]" onChange={() => {}} title={null} />
                        </Form.Item>
                      </div>
                      <div className="question-text flex gap-3 flex-col">
                        <div className="font-semibold text-[16px]">{`${tQuestion("question")} ${parentIndex + 1}:`}</div>
                        <div className="flex gap-4 items-center pl-[64px] justify-between max-md:pl-0 max-md:gap-2">
                          <Form.Item
                            className="topic-Text w-full flex-1 max-w-[1100px]"
                            {...parentField}
                            name={[parentField.name, `content`]}
                            rules={[{ required: true, message: tCommon("requiredField") }]}
                            key={`${parentIndex}-${parentField.key}-question-text`}
                          >
                            <ReactQuill
                              className="question-content-text"
                              theme="snow"
                              modules={modules}
                              formats={formats}
                              bounds="#root"
                              placeholder={tQuestion("inputQuestion")}
                            />
                          </Form.Item>

                          <AppButton type="dashed" onClick={() => parentListOperations.remove(parentIndex)}>
                            <DeleteOutlined />
                          </AppButton>
                        </div>
                      </div>
                      <Form.List
                        {...parentField}
                        name={[parentField.name, `lstAnswer`]}
                        initialValue={[
                          { content: "", isCorrect: undefined },
                          {
                            content: "",
                            isCorrect: undefined,
                          },
                        ]}
                      >
                        {(childFields, childListOperations) => {
                          return (
                            <div className="answers">
                              {childFields.map((childField, childIndex) => {
                                return (
                                  <div key={`${childIndex}-${parentIndex}`} className="answer-list pl-6 flex justify-between w-full max-md:pl-0">
                                    <div className="answer-list-text-checkbox flex gap-4 w-full items-center relative justify-between max-md:gap-2">
                                      <div className="answer-checkbox flex gap-4 w-full items-center max-md:gap-2">
                                        <Form.Item
                                          {...childField}
                                          name={[childField.name, `isCorrect`]}
                                          valuePropName="checked"
                                          key={`${childIndex}-${parentIndex}-answer-checkbox`}
                                        >
                                          <Checkbox
                                            className="h-6 w-6"
                                            checked={checked}
                                            onChange={(e) => {
                                              setChecked(e.target.checked);
                                            }}
                                          />
                                        </Form.Item>
                                        <Form.Item
                                          {...childField}
                                          name={[childField.name, `content`]}
                                          rules={[
                                            {
                                              required: true,
                                              message: tCommon("requiredField"),
                                            },
                                          ]}
                                          className="answers-item w-full max-w-[1100px]"
                                          key={`${childIndex}-${parentIndex}-answer-text`}
                                        >
                                          <ReactQuill
                                            theme="snow"
                                            modules={modules}
                                            formats={formats}
                                            value={value}
                                            onChange={(value) => setValue(value)}
                                            bounds="#root"
                                            placeholder={tQuestion("inputAnswer")}
                                          />
                                        </Form.Item>
                                      </div>
                                      <AppButton type="dashed" onClick={() => childListOperations.remove(childIndex)}>
                                        <DeleteOutlined />
                                      </AppButton>
                                    </div>
                                  </div>
                                );
                              })}
                              {childFields.length < 6 && (
                                <Form.Item className="!mb-0">
                                  <AppButton onClick={() => handleClickButtonAddQuestion(childListOperations)} icon={<PlusOutlined />}>
                                    {tQuestion("addOptions")}
                                  </AppButton>
                                </Form.Item>
                              )}
                            </div>
                          );
                        }}
                      </Form.List>
                      {/* <span className={`text-text-hust ${errorStates[parentIndex] ? "block" : "none"}`}>{tQuestion("notSelectCorrectAnswer")}</span> */}
                    </div>
                  ))}
                  <Form.Item className="!ml-6 max-md:!mt-4 max-md:!ml-0">
                    <AppButton
                      size="large"
                      type="dashed"
                      onClick={() => {
                        parentListOperations.add();
                      }}
                      icon={<PlusOutlined />}
                      disabled={chapterId === null}
                    >
                      {tCommon("add")}
                    </AppButton>
                  </Form.Item>
                </>
              )}
            </Form.List>
            <Form.Item className="add-btn w-full h-1/2 flex justify-center items-center">
              <AppButton customclass="!w-[200px]" size="large" type="primary" htmlType="submit" loading={addQuestionLoading}>
                {tCommon("save")}
              </AppButton>
            </Form.Item>
          </Form>
        </WrapperForm>
      </div>
    </PermissionGuard>
  );
};

export default AddQuestions;
