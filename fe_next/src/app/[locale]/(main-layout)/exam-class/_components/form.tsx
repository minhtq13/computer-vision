"use client";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppSelectSmall from "@/components/app-select-small";
import { useGetComboExamClassQuery, useGetComboSemesterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { DatePicker, Form, Popover } from "antd";
import { useState } from "react";
import ModalSelectStudents from "./ModalSelectStudents";
import ModalSelectTeachers from "./ModalSelectTeachers";
import ModalSelectTests from "./ModalSelectTests";
import React from "react";
import WrapperForm from "@/components/wrapper-form";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import { getOptionsFromCombo } from "@/helpers";
import { useTranslations } from "next-intl";
import useLocaleOptions from "@/hooks/useLocaleOptions";

const ExamClassForm = ({
  onFinish,
  initialValues,
  infoHeader,
  btnText,
  loading,
  onSelectTestId,
  onSelectStudents,
  onSelectTeachers,
  onSelectLecturers,
  testDisplay,
  lstStudentId,
  lstSupervisorId,
  lstLecturerId,
  testId,
  action,
}: any) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const { testTypeWithoutAll } = useLocaleOptions();
  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({});
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});
  const { data: examClass } = useGetComboExamClassQuery({});
  const [studentSelected, setStudentSelected] = useState(lstStudentId ?? []);
  const [teacherSelected, setTeacherSelected] = useState(lstSupervisorId ?? []);

  const [openModal, setOpenModal] = useState(false);
  const [testValue, setTestValue] = useState(testDisplay ?? "");

  const [openStudentModal, setOpenStudentModal] = useState(false);
  const [openTeacherModal, setOpenTeacherModal] = useState(false);

  const [selectStudentFromExistedExClass, setSelectStudentFromExistedExClass] = useState(false);
  const [testSelectedId, setTestSelectedId] = useState([testId]);

  const initialParam = {
    subjectId: initialValues.subjectId ?? null,
    semesterId: initialValues.semesterId ?? null,
    page: 0,
    size: 10,
  };

  const [param, setParam] = useState<any>(initialParam);
  const [form] = Form.useForm();

  return (
    <div className="exam-class-info">
      <div className="lg:py-4">
        <p className="text-2xl font-bold text-hust my-3 md:text-[22px]">{infoHeader}</p>
      </div>
      <WrapperForm>
        <Form
          name="info-exam-class-form"
          className="info-exam-class-form flex flex-wrap justify-between"
          initialValues={initialValues}
          onFinish={onFinish}
          form={form}
        >
          <Form.Item name="semesterId" className="w-[45%] !mb-2" rules={[{ required: true, message: tCommon("requiredField") }]}>
            <AppSelectSmall
              title={tCommon("semester")}
              showSearch
              loading={semesterLoading}
              placeholder={tCommon("select")}
              options={getOptionsFromCombo(allSemester)}
              optionFilterProp="children"
              filterOption={(input, option: any) => (option?.label.toLowerCase() ?? "").includes(input.toLowerCase())}
              optionLabelProp="label"
              onChange={(value) => {
                setParam({ ...param, semesterId: value });
                setTestSelectedId(null);
                setTestValue(null);
                onSelectTestId(null);
                form.setFieldValue("testId", null);
              }}
            />
          </Form.Item>
          <Form.Item
            className="w-[45%] !mb-2"
            name="subjectId"
            colon={true}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppSelectSmall
              title={tCommon("subject")}
              placeholder={tCommon("select")}
              loading={subLoading}
              showSearch
              options={getOptionsFromCombo(allSubjects, true)}
              onChange={(value) => {
                setParam({ ...param, subjectId: value });
                setTestSelectedId(null);
                setTestValue(null);
                onSelectTestId(null);
                form.setFieldValue("testId", null);
              }}
              optionFilterProp="children"
              filterOption={(input, option: any) => (option?.label.toLowerCase() ?? "").includes(input.toLowerCase())}
              optionLabelProp="label"
            />
          </Form.Item>
          <Form.Item
            className="w-[45%] !mb-2"
            name="code"
            colon={true}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tCommon("examClassCode")} title={tCommon("examClassCode")} />
          </Form.Item>
          <Form.Item
            className="w-[45%] !mb-2"
            name="roomName"
            colon={true}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <AppInput placeholder={tExamClass("roomName")} title={tExamClass("roomName")} />
          </Form.Item>

          <div className="w-[45%] !mb-2">
            <div className="w-full flex items-center justify-between mb-1.5">
              <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                <span className="font-medium">{tExamClass("examineTime")}</span>
                <RequiredFieldIcon className="ml-1" />
              </div>
            </div>

            <Form.Item
              className="w-full flex flex-col !mb-0"
              name="examineTime"
              colon={true}
              rules={[
                {
                  required: true,
                  message: tCommon("requiredField"),
                },
              ]}
            >
              <DatePicker
                placeholder={tExamClass("examineTime")}
                format={"HH:mm - DD/MM/YYYY"}
                showTime={{ format: "HH:mm" }}
                className="w-full h-[32px] !border-[--color-text-disable]"
              />
            </Form.Item>
          </div>

          {/* cập nhật hình thức thi */}
          {action === "EDIT" && (
            <Form.Item
              className="w-[45%] !mb-2"
              name="testType"
              colon={true}
              rules={[
                {
                  required: true,
                  message: tCommon("requiredField"),
                },
              ]}
            >
              <AppSelectSmall
                title={tCommon("testType")}
                placeholder={tCommon("testType")}
                options={testTypeWithoutAll}
                disabled={action !== "EDIT" || initialValues?.existedResult}
              />
            </Form.Item>
          )}

          <Form.Item name="lstStudentId" className="w-[45%] !mb-2">
            <div className="w-full flex items-end justify-between gap-2 2xs:max-md:flex-col 2xs:max-md:items-start">
              <AppInput
                rootClassName="flex-1"
                title={tCommon("student")}
                placeholder={tCommon("student")}
                disabled={selectStudentFromExistedExClass}
                value={studentSelected.length > 0 ? `${tCommon("selected")} ${studentSelected.length} ${tCommon("student")}` : ""}
              />
              <AppButton disabled={selectStudentFromExistedExClass} onClick={() => setOpenStudentModal(true)}>
                {tCommon("select")}
              </AppButton>
            </div>
          </Form.Item>
          <Form.Item
            className="w-[45%] !mb-2"
            name="testId"
            colon={true}
            rules={[
              {
                required: true,
                message: tCommon("requiredField"),
              },
            ]}
          >
            <div className="w-full flex items-end justify-between gap-2 2xs:max-md:flex-col 2xs:max-md:items-start">
              <Popover content={testValue} placement="bottom" trigger="hover">
                <AppInput
                  rootClassName="flex-1"
                  disabled={param?.subjectId === null || param?.subjectId === undefined}
                  title={tCommon("test")}
                  placeholder={tCommon("select")}
                  value={testValue}
                />
              </Popover>
              <AppButton disabled={param?.subjectId === null || param?.subjectId === undefined} onClick={() => setOpenModal(true)}>
                {tCommon("select")}
              </AppButton>
            </div>
          </Form.Item>
          {action === "CREATE" && (
            <Form.Item name="fromExamClassId" className="w-[45%] !mb-2" colon={true}>
              <AppSelectSmall
                title={tExamClass("selectExistedExClass")}
                allowClear
                showSearch
                placeholder={tExamClass("selectExistedExClass")}
                options={getOptionsFromCombo(examClass, true)}
                onChange={(value) => setSelectStudentFromExistedExClass(!!value)}
                optionLabelProp="label"
                filterOption={(input, option: any) => (option?.label.toLowerCase() ?? "").includes(input.toLowerCase())}
              />
            </Form.Item>
          )}
          <Form.Item name="lstSupervisorId" className="w-[45%] !mb-2">
            <div className="w-full flex items-end justify-between gap-2 2xs:max-md:flex-col 2xs:max-md:items-start">
              <AppInput
                title={tCommon("supervisors")}
                rootClassName="flex-1"
                placeholder={tCommon("select")}
                value={teacherSelected.length > 0 ? `${tCommon("selected")} ${teacherSelected.length} ${tCommon("supervisors")}` : ""}
              />
              <AppButton onClick={() => setOpenTeacherModal(true)}>Chọn</AppButton>
            </div>
          </Form.Item>
          <Form.Item className="w-full flex items-center justify-center !my-2">
            <AppButton size="large" type="primary" htmlType="submit" block loading={loading} customclass="w-[150px] h-[50px]">
              {btnText}
            </AppButton>
          </Form.Item>
        </Form>
      </WrapperForm>
      <ModalSelectStudents
        studentSelected={studentSelected}
        setStudentSelected={setStudentSelected}
        openModal={openStudentModal}
        setOpenModal={setOpenStudentModal}
        onSelectStudents={onSelectStudents}
      />
      <ModalSelectTeachers
        openModal={openTeacherModal}
        setOpenModal={setOpenTeacherModal}
        lstLecturerId={lstLecturerId}
        onSelectLecturers={onSelectLecturers}
        onSelectTeachers={onSelectTeachers}
        teacherSelected={teacherSelected}
        setTeacherSelected={setTeacherSelected}
      />
      <ModalSelectTests
        openModal={openModal}
        setOpenModal={setOpenModal}
        onSelectTestId={onSelectTestId}
        testSelectedId={testSelectedId}
        setTestSelectedId={setTestSelectedId}
        testValue={testValue}
        setTestValue={setTestValue}
        param={param}
        setParam={setParam}
        form={form}
      />
    </div>
  );
};
export default ExamClassForm;
