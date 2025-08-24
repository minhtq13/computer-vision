"use client";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppModal from "@/components/app-modal";
import AppSelectSmall from "@/components/app-select-small";
import SubmitButton from "@/components/submit-button";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetComboSemesterQuery } from "@/stores/combo/api";
import { useCreateAutoTestMutation } from "@/stores/tests/api";
import Storage from "@/libs/storage";
import { DatePicker, Form } from "antd";
import dayjs from "dayjs";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useTranslations } from "next-intl";
import useLocaleOptions from "@/hooks/useLocaleOptions";

const AutoTest = ({ chapterIds, subjectId, levelCal, sumQues, subjectOptions, isAllowedUsingDocuments }) => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const { testTypeWithoutAll } = useLocaleOptions();
  const [openModal, setOpenModal] = useState(false);
  const [testId, setTestId] = useState(null);
  const [totalQuestion, setTotalQuestion] = useState(0);
  const [testType, setTestType] = useState(null);
  const [disable, setDisable] = useState(true);
  const [formValue, setFormValue] = useState(null);
  const form = Form.useForm()[0];
  const router = useRouter();
  const handleError = useHandleError();

  const [createAutoTest, { isLoading: isLoadingCreateAutoTest }] = useCreateAutoTestMutation();

  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({ search: "" });

  // rule input number

  const validatorNumber = (_, value) => {
    if (!value) {
      return Promise.reject(tCommon("requiredField"));
    }
    if (!Number.isInteger(Number(value)) || Number(value) < 1) {
      return Promise.reject(tTests("mustBeNatural"));
    }
    return Promise.resolve();
  };

  const disabled = !(chapterIds.length > 0);
  const onFinish = async (value) => {
    setFormValue(value);
    if (subjectId !== null && chapterIds.length > 0) {
      try {
        const res = await createAutoTest({
          name: value.name,
          subjectId: subjectId,
          testType: testType,
          chapterIds: chapterIds,
          startTime: dayjs(value.startTime).format("DD/MM/YYYY HH:mm"),
          endTime: dayjs(value.endTime).format("DD/MM/YYYY HH:mm"),
          duration: +value.duration,
          questionQuantity: +value.questionQuantity,
          totalPoint: 10,
          semesterId: +value.semesterId,
          generateConfig: {
            numEasyQuestion: +value.easy,
            numMediumQuestion: +value.medium,
            numHardQuestion: +value.hard,
            numTotalQuestion: +totalQuestion,
          },
          isAllowedUsingDocuments: isAllowedUsingDocuments,
          questionIds: [],
          description: "",
        }).unwrap();
        setOpenModal(true);
        setTestId(res);
      } catch (error) {
        handleError(error);
      }
    }
  };

  const checkSum = (_, value) => {
    if (!Number.isInteger(Number(value)) || Number(value) < 1) {
      return Promise.reject(tTests("mustBeNatural"));
    }
    if (totalQuestion && totalQuestion > sumQues) {
      return Promise.reject(tTests("notEnoughQuestions"));
    }
    return Promise.resolve();
  };

  const checkConfigLevel = (inputLevel: number, quesLevel: number) => {
    if (!Number.isInteger(Number(inputLevel))) {
      return Promise.reject(tTests("mustBeNatural"));
    }
    if (inputLevel > form.getFieldValue("questionQuantity")) {
      return Promise.reject(tTests("questionsGreaterThanQuestionQuantity"));
    }
    if (inputLevel > quesLevel) {
      return Promise.reject(tTests("notEnoughQuestionsInQuestionBank"));
    }
    return Promise.resolve();
  };

  const questionNumOnchange = (e) => {
    setTotalQuestion(e.target.value);
    if (e.target.value.trim().length > 0) {
      setDisable(false);
    } else {
      setDisable(true);
    }
  };

  return (
    <div className="test-create-view">
      <div className="flex items-center gap-2 mt-2 mb-4">
        <div className="text-base font-medium text-text-secondary ">
          {tTests("totalQuestionsInQuestionBank")}: <span className="text-lg font-medium text-text-hust">{sumQues}</span>
        </div>
        <div className="text-base font-medium text-text-secondary">
          {tCommon("easy")}: <span className="text-lg font-medium text-text-hust">{levelCal[0]}</span>
        </div>
        <div className="text-base font-medium text-text-secondary">
          {tCommon("medium")}: <span className="text-lg font-medium text-text-hust">{levelCal[1]}</span>
        </div>
        <div className="text-base font-medium text-text-secondary">
          {tCommon("hard")}: <span className="text-lg font-medium text-text-hust">{levelCal[2]}</span>
        </div>
      </div>
      <WrapperForm>
        <Form onFinish={onFinish} name="test-create" form={form} className="flex flex-wrap justify-between relative">
          <Form.Item
            name="name"
            className="w-[40%] !mb-2"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tTests("testName")} disabled={disabled} title={tTests("testName")} required size="small" />
          </Form.Item>
          <Form.Item
            className="w-[40%] !mb-2"
            name="duration"
            rules={[
              {
                validator: validatorNumber,
              },
            ]}
          >
            <AppInput type="text" placeholder={tTests("duration")} disabled={disabled} title={tTests("duration")} required size="small" />
          </Form.Item>

          <div className="w-[40%] !mb-2">
            <div className="w-full flex items-center justify-between mb-1.5">
              <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                <span className="font-medium">{tTests("startTime")}</span>
                <RequiredFieldIcon className="ml-1" />
              </div>
            </div>

            <Form.Item name="startTime" className="w-full flex flex-col !mb-0" rules={[{ required: true, message: tCommon("requiredField") }]}>
              <DatePicker
                className="w-full h-[32px]"
                format={"HH:mm DD-MM-YYYY"}
                showTime={{ format: "HH:mm" }}
                disabled={disabled}
                placeholder={tTests("startTime")}
              />
            </Form.Item>
          </div>

          <div className="w-[40%] !mb-2">
            <div className="w-full flex items-center justify-between mb-1.5">
              <div className="text-13px font-medium text-text-secondary flex flex-row items-center">
                <span className="font-medium">{tTests("endTime")}</span>
                <RequiredFieldIcon className="ml-1" />
              </div>
            </div>

            <Form.Item name="endTime" className="w-full flex flex-col !mb-0" rules={[{ required: true, message: tCommon("requiredField") }]}>
              <DatePicker
                className="w-full h-[32px]"
                format={"HH:mm DD-MM-YYYY"}
                showTime={{ format: "HH:mm" }}
                disabled={disabled}
                placeholder={tTests("endTime")}
              />
            </Form.Item>
          </div>

          <Form.Item
            className="w-[40%] !mb-2"
            name="semesterId"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppSelectSmall
              required
              loading={semesterLoading}
              placeholder={tCommon("semester")}
              options={getOptionsFromCombo(allSemester)}
              disabled={disabled}
              title={tCommon("semester")}
              size="middle"
            />
          </Form.Item>
          <Form.Item
            className="w-[40%] !mb-4"
            name="testType"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppSelectSmall
              size="middle"
              required
              placeholder={tCommon("testType")}
              options={testTypeWithoutAll}
              disabled={disabled}
              onChange={(value) => setTestType(value)}
              title={tCommon("testType")}
            />
          </Form.Item>
          <Form.Item
            className="w-[40%] !mb-2"
            name="questionQuantity"
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
              {
                validator: checkSum,
              },
            ]}
          >
            <AppInput
              type="text"
              placeholder={tTests("questionQuantity")}
              onChange={questionNumOnchange}
              disabled={disabled}
              title={tTests("questionQuantity")}
              required
              size="small"
            />
          </Form.Item>
          <div className="w-full">
            <div className="grid grid-cols-3 gap-4">
              <div>
                <Form.Item name={"easy"} rules={[{ validator: () => checkConfigLevel(form.getFieldValue("easy"), levelCal[0]) }]}>
                  <AppInput
                    placeholder={tCommon("easy")}
                    type="text"
                    disabled={disable || !(chapterIds.length > 0)}
                    size="small"
                    required
                    title={tCommon("easy")}
                  />
                </Form.Item>
              </div>
              <div>
                <Form.Item
                  name={"medium"}
                  rules={[
                    {
                      validator: () => checkConfigLevel(form.getFieldValue("medium"), levelCal[1]),
                    },
                  ]}
                >
                  <AppInput
                    placeholder={tCommon("medium")}
                    type="text"
                    disabled={disable || !(chapterIds.length > 0)}
                    size="small"
                    title={tCommon("medium")}
                    required
                  />
                </Form.Item>
              </div>
              <div>
                <Form.Item
                  name={"hard"}
                  rules={[
                    {
                      validator: () => checkConfigLevel(form.getFieldValue("hard"), levelCal[2]),
                    },
                  ]}
                >
                  <AppInput
                    placeholder={tCommon("hard")}
                    type="text"
                    disabled={disable || !(chapterIds.length > 0)}
                    size="small"
                    title={tCommon("hard")}
                    required
                  />
                </Form.Item>
              </div>
            </div>
          </div>

          <div className="flex justify-center items-center w-full">
            <Form.Item className="btn-create !my-4 !w-[200px]">
              <SubmitButton form={form} customclass="!w-[200px]" loading={isLoadingCreateAutoTest}>
                {tTests("createExam")}
              </SubmitButton>
            </Form.Item>
          </div>
        </Form>
      </WrapperForm>

      <AppModal
        className="test-set-create-modal"
        open={openModal}
        title={tTests("createExamSuccess")}
        onOk={() => {
          router.push(`${PATH_ROUTER.DETAIL.TEST_SET_DETAIL(testId)}`);
          Storage.setDetailTest({
            duration: formValue.duration,
            questionQuantity: formValue.questionQuantity,
            subjectName: subjectOptions && subjectOptions.length > 0 ? (subjectOptions.find((item) => item.value === subjectId) || {}).label : null,
            semester: getOptionsFromCombo(allSemester).find((item) => item.value === formValue.semesterId).label,
            generateConfig: formValue.generateConfig,
            isAllowedUsingDocuments: isAllowedUsingDocuments,
          });
        }}
        onCancel={() => setOpenModal(false)}
        footer={[
          <AppButton key="back" onClick={() => setOpenModal(false)}>
            {tCommon("back")}
          </AppButton>,
          <AppButton type="primary" key="create-test-list" onClick={() => router.push(`${PATH_ROUTER.DETAIL.TEST_SET_DETAIL(testId)}`)}>
            {tTests("createExam")}
          </AppButton>,
        ]}
      >
        <p>{tTests("createExamConfirm")}</p>
      </AppModal>
    </div>
  );
};
export default AutoTest;
