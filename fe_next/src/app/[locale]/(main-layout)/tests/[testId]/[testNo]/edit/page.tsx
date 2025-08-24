"use client";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppModal from "@/components/app-modal";
import ButtonDownloadPdf from "@/components/button-download-pdf";
import HtmlRenderer from "@/components/html-render";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import TestPreview from "@/components/test-preview/TestPreview";
import WrapperForm from "@/components/wrapper-form";
import ClientProvider from "@/helpers/client-provider";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetTestSetDetailMutation, useUpdateTestSetMutation } from "@/stores/test-set/api";
import { EPermission } from "@/types/enum";
import { ArrowRightOutlined } from "@ant-design/icons";
import { Form, Spin } from "antd";
import { useTranslations } from "next-intl";
import { useEffect, useState } from "react";

const TestEdit = ({ params }: { params: { testId: string; testNo: string } }) => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const testId = params.testId;
  const testNo = params.testNo;
  const notification = useAppNotification();
  // const [openModal, setOpenModal] = useState(true);
  const [initialValues, setInitialValues] = useState([]);
  const [testDetail, setTestDetail] = useState<any>({});
  const [idValues, setIdValues] = useState([]);
  const [openSuccessModal, setOpenSuccessModal] = useState(false);
  const [openPreModal, setOpenPreModal] = useState(false);
  const [checkQues, setCheckQues] = useState([]);
  const [getTestSetDetail, { isLoading }] = useGetTestSetDetailMutation();
  const handleError = useHandleError();
  const [updateTestSet, { isLoading: updateLoading }] = useUpdateTestSetMutation();

  useEffect(() => {
    (async () => {
      try {
        const res = await getTestSetDetail({ testId: Number(testId), code: testNo }).unwrap();
        setCheckQues(res.lstQuestion.length > 0 ? res.lstQuestion.map((item, index) => index + 1) : []);
        setInitialValues(
          res.lstQuestion.length > 0
            ? res.lstQuestion.map((item, index) => {
                return {
                  questionNo: String(index + 1),
                  content: item.content,
                  answers:
                    item.answers.length > 0
                      ? item.answers.map((ans, ansNo) => {
                          return {
                            answerNo: String.fromCharCode(65 + ansNo),
                            content: ans.content,
                          };
                        })
                      : [],
                };
              })
            : []
        );
        setIdValues(
          res.lstQuestion.length > 0
            ? res.lstQuestion.map((item) => {
                return {
                  questionId: item.id,
                  answers:
                    item.answers.length > 0
                      ? item.answers.map((ans) => {
                          return {
                            answerId: ans.answerId,
                          };
                        })
                      : [],
                };
              })
            : []
        );
        setTestDetail(res);
      } catch (error) {
        handleError(error);
      }
    })();
  }, [testId, testNo]);

  const convertAnsNo = (letter) => {
    const letterLowerCase = letter.toLowerCase();

    switch (letterLowerCase) {
      case "a":
        return 1;
      case "b":
        return 2;
      case "c":
        return 3;
      case "d":
        return 4;
      default:
        return null;
    }
  };

  const onFinish = async (values) => {
    try {
      await updateTestSet({
        testSetId: testDetail.testSet.testSetId,
        questions: idValues.map((itemA, index) => {
          const correspondingItemB = values.lstQuestion[index] || {
            answers: [],
          };

          return {
            ...itemA,
            questionNo: Number(correspondingItemB.questionNo) || null,
            answers: itemA.answers.map((answerA, answerIndex) => ({
              ...answerA,
              answerNo: correspondingItemB.answers[answerIndex] ? convertAnsNo(correspondingItemB.answers[answerIndex].answerNo) : null,
            })),
          };
        }),
      });
      setOpenSuccessModal(true);
      notification.success({
        description: tTests("updateTestSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleQuestionNumberChange = (index, value) => {
    const newCheckQues = [...checkQues];
    newCheckQues[index] = parseInt(value, 10) || null; // Chuyển giá trị thành số nguyên, hoặc 0 nếu không hợp lệ
    setCheckQues(newCheckQues);
  };

  const getQuesValue = (value, length) => {
    let result = "";
    if (!value || value < 0 || value > length) {
      result = "?";
    } else {
      result = value;
    }
    return result;
  };
  return (
    <PermissionGuard requiredPermissions={[EPermission.TEST_UPDATE]}>
      <div className="test-edit flex flex-col justify-center">
        <div className="test-edit-header text-[28px] font-bold text-center text-hust mb-5">{tTests("updateTest")}</div>
        <ClientProvider skeleton={<Spin tip="Loading..." spinning={isLoading} />}>
          <div className="test-edit-body flex gap-4 justify-between w-full max-md:flex-col">
            <WrapperForm className="w-[20%] !p-2 max-h-[1000px] overflow-y-auto max-md:w-full">
              <div className="test-edit-left w-full flex flex-col bg-white p-2 ">
                <div className="left-header text-[20px] text-center font-bold text-hust mb-5">{tTests("info")}</div>
                <div className="left-content flex items-center flex-col">
                  <div className="left-content-item flex mb-2 w-full gap-3">
                    {tTests("testCode")}: <span className="font-bold">{testNo}</span>
                  </div>
                  <div className="left-content-item flex mb-2 w-full gap-3">
                    {tTests("questionQuantity")}: <span className="font-bold">{initialValues.length}</span>
                  </div>
                  <div className={`flex flex-wrap w-full mb-6 ${!isLoading ? "" : "border-none"}`}>
                    {!isLoading &&
                      checkQues.map((item, index) => {
                        return (
                          <div
                            className="w-1/2 border border-disable-secondary flex items-center justify-center h-[40px] text-[16px] font-bold gap-1 max-md:w-1/3"
                            key={index}
                          >
                            <div>{index + 1}</div>
                            {item !== index + 1 && (
                              <div className="ques-change flex items-center gap-1">
                                <ArrowRightOutlined />
                                <div className="ques-change-value text-hust">{getQuesValue(item, checkQues.length)}</div>
                              </div>
                            )}
                          </div>
                        );
                      })}
                  </div>
                </div>
              </div>
            </WrapperForm>
            <div className="test-preview flex-1">
              <Spin tip="Loading..." spinning={isLoading}>
                {!isLoading && (
                  <Form
                    initialValues={{
                      lstQuestion: initialValues,
                    }}
                    onFinish={onFinish}
                    name="test-edit-form"
                  >
                    <Form.List name="lstQuestion">
                      {(parentFields) => (
                        <WrapperForm className="max-h-[1000px]">
                          <div className="overflow-y-auto max-h-[952px]">
                            {parentFields.map((parentField, parentIndex) => {
                              return (
                                <div key={parentIndex} className="question-list">
                                  <div className="question-text flex w-full justify-between gap-2 mb-1">
                                    <div className="question-text-order flex max-lg:flex-col w-full gap-2">
                                      <Form.Item
                                        {...parentField}
                                        className="topic-Text !mr-2 !mb-0 flex-shrink-0"
                                        label={<div className="text-hust font-semibold">Câu số</div>}
                                        key={`${parentIndex}-${parentField.key}-question-text`}
                                        name={[parentField.name, `questionNo`]}
                                        rules={[
                                          {
                                            required: true,
                                            message: tTests("enterQuestionOrder"),
                                          },
                                        ]}
                                      >
                                        <AppInput
                                          className="input-change-question !w-[50px] text-hust"
                                          onChange={(e) => handleQuestionNumberChange(parentIndex, e.target.value)}
                                        />
                                      </Form.Item>
                                      <Form.Item
                                        {...parentField}
                                        className="content !mb-0"
                                        key={`${parentIndex}-${parentField.key}-question-content`}
                                        name={[parentField.name, `content`]}
                                      >
                                        <HtmlRenderer htmlContent={initialValues[parentIndex].content} />
                                      </Form.Item>
                                    </div>
                                  </div>
                                  <Form.List
                                    {...parentField}
                                    key={`answers${parentField.key}-${parentField.key}`}
                                    name={[parentField.name, `answers`]}
                                  >
                                    {(childFields) => (
                                      <div className="answers">
                                        {childFields.map((childField, childIndex) => {
                                          return (
                                            <div key={`${childIndex}-${parentIndex}`} className="answer-list flex w-full items-center">
                                              <div className="answer-list flex w-full items-center">
                                                <Form.Item
                                                  {...childField}
                                                  name={[childField.name, `answerNo`]}
                                                  key={`${childIndex}-${parentIndex}-answer-no`}
                                                  rules={[
                                                    {
                                                      required: true,
                                                      message: tTests("enterAnswerOrder"),
                                                    },
                                                  ]}
                                                  className="answer-no !mr-2 !mb-4 w-[50px]"
                                                >
                                                  <AppInput className="min-w-[50px]" />
                                                </Form.Item>
                                                <Form.Item
                                                  {...childField}
                                                  name={[childField.name, `content`]}
                                                  key={`${childIndex}-${parentIndex}-answer-text`}
                                                  className="answer-item !mr-2 !mb-4"
                                                >
                                                  <HtmlRenderer htmlContent={initialValues[parentIndex].answers[childIndex].content} />
                                                </Form.Item>
                                              </div>
                                            </div>
                                          );
                                        })}
                                      </div>
                                    )}
                                  </Form.List>
                                </div>
                              );
                            })}
                          </div>
                        </WrapperForm>
                      )}
                    </Form.List>
                    <Form.Item className="add-btn flex justify-center items-center">
                      <AppButton type="primary" htmlType="submit" loading={updateLoading} customclass="mt-6 min-w-[200px]">
                        {tCommon("update")}
                      </AppButton>
                    </Form.Item>
                  </Form>
                )}
              </Spin>
            </div>
          </div>
          {/* <AppModal
          open={openModal}
          title="Hướng dẫn"
          onOk={() => setOpenModal(false)}
          onCancel={() => setOpenModal(false)}
          className="modal-instruction"
          centered={true}
          footer={[
            <AppButton key="understood" type="primary" onClick={() => setOpenModal(false)}>
              Đã hiểu
            </AppButton>,
          ]}
        >
          <p>Vui lòng điền số thứ tự câu hỏi và câu trả lời vào ô nhập liệu!</p>
        </AppModal> */}
          <AppModal
            open={openSuccessModal}
            title={tTests("updateTestSuccess")}
            okText={tTests("viewTest")}
            onOk={() => setOpenPreModal(true)}
            onCancel={() => setOpenSuccessModal(false)}
            cancelText={tCommon("close")}
            centered={true}
          >
            <p>{tTests("updateTestSuccess")}</p>
          </AppModal>
          <AppModal
            className="test-edit-preview"
            open={openPreModal}
            okText={tTests("downloadOrPrint")}
            onOk={() => {}}
            onCancel={() => setOpenPreModal(false)}
            cancelText={tCommon("close")}
            centered={true}
            footer={[
              <ButtonDownloadPdf
                key="download-pdf"
                questions={testDetail.lstQuestion}
                testDetail={testDetail.testSet}
                testNo={testNo}
                classNameButton="!w-full"
              />,
            ]}
          >
            <Spin tip="Loading..." spinning={isLoading}>
              <TestPreview questions={testDetail.lstQuestion} testDetail={testDetail.testSet} testNo={testNo} />
            </Spin>
          </AppModal>
        </ClientProvider>
      </div>
    </PermissionGuard>
  );
};
export default TestEdit;
