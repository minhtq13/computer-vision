/* eslint-disable no-unused-vars */
"use client";
import AppButton from "@/components/app-button";
import AppSelectSmall from "@/components/app-select-small";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetComboChapterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useGetQuestionDetailQuery, useUpdateQuestionMutation } from "@/stores/questions/api";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { Checkbox, Form, Skeleton, Switch } from "antd";
import { useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";

import FilterByTag from "@/components/filter-by-tag";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";

import ButtonBack from "@/components/button-back";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useTranslations } from "next-intl";
import dynamic from "next/dynamic";
import "react-quill/dist/quill.snow.css";
import { Quill } from "react-quill";
import ImageResize from "quill-image-resize-module-react";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";
// Quill.register("modules/imageResize", ImageResize);
// const ReactQuill = dynamic(() => import("react-quill"), { ssr: false });

const QuestionEdit = ({ params }: { params: { id: string } }) => {
  Quill.register("modules/imageResize", ImageResize);
  const ReactQuill = useMemo(() => dynamic(() => import("react-quill"), { ssr: false }), []);
  const tQuestion = useTranslations("questions");
  const tCommon = useTranslations("common");
  const { levelIntWithoutAll } = useLocaleOptions();
  const modules = {
    toolbar: [
      ["bold", "italic", "underline"], // toggled buttons
      ["blockquote", "code-block"],
      [{ list: "ordered" }, { list: "bullet" }],
      [{ script: "sub" }, { script: "super" }], // superscript/subscript
      [{ align: [] }],
      ["image", "link", "formula"],
      [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
      ["clean"],
    ],

    clipboard: {
      matchVisual: false,
    },
    imageResize: {
      parchment: Quill.import("parchment"),
      modules: ["Resize", "DisplaySize", "Toolbar"],
    },
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
  const questionId = params.id;
  const router = useRouter();
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [checked, setChecked] = useState(false);
  const [numCheckedAns, setNumCheckedAns] = useState(0);
  const [isMultipleAnswers, setIsMultipleAnswers] = useState(false);
  const [isChanged, setIsChanged] = useState(false);
  const { data: questionInfo, isLoading: infoLoading } = useGetQuestionDetailQuery(
    { questionId: Number(questionId) },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const [chapterId, setChapterId] = useState(null);
  const [subjectId, setSubjectId] = useState(null);
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});
  const { data: allChapters, isLoading: chapterLoading } = useGetComboChapterQuery(
    { subjectId: subjectId },
    {
      refetchOnMountOrArgChange: true,
      skip: !subjectId,
    }
  );

  useEffect(() => {
    if (questionInfo) {
      setChapterId(questionInfo?.chapterId);
      setSubjectId(questionInfo?.subjectId);
    }
  }, [questionInfo]);

  useEffect(() => {
    if (!infoLoading) {
      setNumCheckedAns([...questionInfo?.lstAnswer].filter((ans) => ans?.isCorrect).length);
      setIsMultipleAnswers(questionInfo?.isMultipleAnswers);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [questionInfo, infoLoading]);

  const subjectOnChange = (value) => {
    setIsChanged(true);
    setSubjectId(value);
    setChapterId(null);
  };
  const chapterOnchange = (value) => {
    setIsChanged(true);
    setChapterId(value);
  };
  const sentLevel = (level: number) => {
    let levelParam = "EASY";
    if (level === 1) {
      levelParam = "MEDIUM";
    }
    if (level === 2) {
      levelParam = "HARD";
    }
    return levelParam;
  };

  const onChange = (checkValues) => {
    setIsChanged(true);
    const isChecked = checkValues?.target?.checked;
    if (isChecked) {
      setNumCheckedAns(numCheckedAns + 1);
    } else {
      setNumCheckedAns(numCheckedAns - 1);
    }
    setChecked(isChecked);
  };
  const [updateQuestion, { isLoading: updateLoading }] = useUpdateQuestionMutation();

  const onFinish = async (values: any) => {
    if (!values?.isMultipleAnswers && numCheckedAns > 1) {
      return;
    }
    try {
      await updateQuestion({
        questionsId: questionId,
        ...values,
        chapterId: chapterId,
        level: sentLevel(values.level),
        isChanged: isChanged,
        tagIds: values.tagIds?.map((tag) => Number(tag)) || [],
      }).unwrap();
      notification.success({ description: tQuestion("updateQuestionSuccess") });
      router.push(PATH_ROUTER.PROTECTED.QUESTIONS);
    } catch (error) {
      handleError(error);
    }
  };

  const handleTagChange = () => {
    setIsChanged(true);
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.QUESTION_UPDATE]}>
      <Skeleton active loading={infoLoading}>
        <div className="question-edit">
          <ButtonBack />
          <div className="question-edit-title text-[30px] font-semibold text-text-hust py-5">{tQuestion("editQuestion")}</div>
          <span className="text-hust text-[14px] italic">
            {tQuestion("warning1")} <br />
            {tQuestion("warning2")}
          </span>
          <div className="question-subject-chapter gap-5 items-center my-6 grid grid-cols-2 max-md:grid-cols-1">
            <div className="question-subject flex items-center w-full">
              <AppSelectSmall
                customClassName="w-full"
                className="w-full"
                classNameTitle="text-[14px]"
                title={tCommon("subject")}
                showSearch
                placeholder={tCommon("select")}
                defaultValue={questionInfo?.subjectId}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allSubjects)}
                onChange={subjectOnChange}
                loading={subLoading}
              />
            </div>
            <div className="question-subject question-chapter">
              <AppSelectSmall
                className="w-full"
                classNameTitle="text-[14px]"
                title={tCommon("chapter")}
                showSearch
                placeholder={tCommon("select")}
                defaultValue={questionInfo?.chapterId}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allChapters)}
                onChange={chapterOnchange}
                loading={chapterLoading || infoLoading}
              />
            </div>
          </div>
          <WrapperForm>
            <Form
              name="question-edit"
              className=""
              onFinish={onFinish}
              initialValues={{
                content: questionInfo?.content,
                lstAnswer: questionInfo?.lstAnswer ? questionInfo?.lstAnswer : [],
                level: questionInfo?.level,
                isMultipleAnswers: questionInfo?.isMultipleAnswers,
                tagIds: questionInfo?.tagIds,
              }}
            >
              <div className="flex items-center gap-4 flex-wrap mb-4 max-md:gap-2">
                <div className="max-md:w-full flex items-center gap-4">
                  <Form.Item
                    className="level !mb-0 max-md:w-1/2"
                    label={tCommon("level")}
                    name="level"
                    rules={[
                      {
                        required: true,
                        message: tCommon("requiredField"),
                      },
                    ]}
                  >
                    <AppSelectSmall
                      placeholder={tCommon("level")}
                      className="min-w-[100px]"
                      options={levelIntWithoutAll}
                      onChange={() => setIsChanged(true)}
                    />
                  </Form.Item>
                  <Form.Item
                    className="is-multiple-ans relative !mb-0 max-md:w-1/2"
                    label={tQuestion("multpleChoice")}
                    name="isMultipleAnswers"
                    rules={[
                      {
                        required: true,
                        message: tCommon("requiredField"),
                      },
                    ]}
                  >
                    <Switch
                      onChange={(e) => {
                        setIsChanged(true);
                        setIsMultipleAnswers(e);
                      }}
                    />
                    {!isMultipleAnswers && numCheckedAns > 1 && (
                      <div className="text-hust absolute w-[150px] right-0">{tCommon("moreThanOneCorrectAnswer")}</div>
                    )}
                  </Form.Item>
                </div>
                <Form.Item className="tag !mb-0 max-md:w-full" label={tCommon("tag")} name="tagIds">
                  <FilterByTag
                    onChange={handleTagChange}
                    mode="multiple"
                    className="min-w-[200px]"
                    title={null}
                    initialValues={questionInfo?.tagIds}
                  />
                </Form.Item>
              </div>
              <div className="topicText-level flex gap-2 w-full mb-10 ml-8">
                <Form.Item
                  noStyle
                  className="topic-text flex-1"
                  label={tQuestion("question")}
                  name="content"
                  rules={[
                    {
                      required: true,
                      message: tCommon("requiredField"),
                    },
                  ]}
                >
                  <ReactQuill
                    className="w-[calc(100%-80px)]"
                    theme="snow"
                    modules={modules}
                    formats={formats}
                    bounds="#root"
                    placeholder={tQuestion("question")}
                    onChange={() => {
                      setIsChanged(true);
                    }}
                  />
                </Form.Item>
              </div>
              <Form.List name={"lstAnswer"}>
                {(childFields, childListOperations) => (
                  <div className="answers">
                    {childFields.map((childField, childIndex) => (
                      <div key={`frAnswers${childIndex}`} className="answer-list w-full flex gap-4 items-center">
                        <div className="answer-list-text-checkbox flex gap-4 w-full items-center">
                          <Form.Item {...childField} name={[childField.name, `isCorrect`]} key={`isCorrect${childIndex}`} valuePropName="checked">
                            <Checkbox checked={checked} onChange={onChange} />
                          </Form.Item>
                          <Form.Item
                            {...childField}
                            name={[childField.name, `content`]}
                            key={`content${childIndex}`}
                            rules={[
                              {
                                required: true,
                                message: tCommon("requiredField"),
                              },
                            ]}
                            className="answers-item flex-1"
                          >
                            <ReactQuill
                              className="w-full"
                              theme="snow"
                              modules={modules}
                              formats={formats}
                              bounds="#root"
                              placeholder={tQuestion("inputAnswer")}
                              onChange={() => {
                                setIsChanged(true);
                              }}
                            />
                          </Form.Item>
                          <AppButton
                            type="dashed"
                            onClick={() => {
                              setIsChanged(true);
                              childListOperations.remove(childIndex);
                            }}
                          >
                            <DeleteOutlined />
                          </AppButton>
                        </div>
                      </div>
                    ))}
                    {childFields.length < 6 && (
                      <Form.Item className="add-answer-btn !mb-0">
                        <AppButton
                          onClick={() => {
                            setIsChanged(true);
                            childListOperations.add();
                          }}
                          icon={<PlusOutlined />}
                        >
                          {tQuestion("addOptions")}
                        </AppButton>
                      </Form.Item>
                    )}
                  </div>
                )}
              </Form.List>
              <Form.Item>
                <div className="flex justify-center items-center w-full mt-4">
                  <AppButton customclass="!px-10 py-3" size="large" type="primary" htmlType="submit" disabled={!isChanged} loading={updateLoading}>
                    {tCommon("save")}
                  </AppButton>
                </div>
              </Form.Item>
            </Form>
          </WrapperForm>
        </div>
      </Skeleton>
    </PermissionGuard>
  );
};
export default QuestionEdit;
