"use client";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import AppInput from "@/components/app-input";
import AppSelectSmall from "@/components/app-select-small";
import ButtonBack from "@/components/button-back";
import SubmitButton from "@/components/submit-button";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import ClientProvider from "@/helpers/client-provider";
import { getOptionsFromCombo } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import useRole from "@/hooks/useRole";
import { useGetComboDepartmentQuery } from "@/stores/combo/api";
import { useGetUserInfoQuery, useUpdateUserMutation } from "@/stores/user/api";
import { dateTimePattern, HUST_COLOR, USER_TYPE } from "@/constants";
import { formatDateParam } from "@/helpers/tools";
import { DatePicker, Form, Tag } from "antd";
import dayjs from "dayjs";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import { EPermission, RoleBase, RoleBaseId } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";

const StudentDetail = ({
  params,
}: {
  params: {
    id: string;
  };
}) => {
  const tStudents = useTranslations("students");
  const tCommon = useTranslations("common");
  const { genderOption } = useLocaleOptions();
  const [loading, setLoading] = useState(false);
  const notification = useAppNotification();
  const router = useRouter();
  const form = Form.useForm()[0];
  const { role } = useRole();
  const handleError = useHandleError();
  const { data: userInfo, refetch } = useGetUserInfoQuery({ userId: params.id }, { refetchOnMountOrArgChange: true });
  const [updateUser] = useUpdateUserMutation();
  const onFinish = async (value: any) => {
    try {
      setLoading(true);
      await updateUser({
        userId: params.id,
        payload: {
          phoneNumber: value.phoneNumber,
          genderType: value.genderType,
          avatarId: userInfo?.avatarId,
          email: value.email,
          birthDate: formatDateParam(value.birthDate),
          firstName: value.firstName,
          lastName: value.lastName,
          departmentId: -1,
          lstRoleId: [RoleBaseId.STUDENT],
          userType: USER_TYPE.STUDENT,
          code: value.code,
          metaData: { courseNum: Number(value.courseNum) },
          identityType: "CITIZEN_ID_CARD",
          identificationNumber: value.identificationNumber,
          departmentIds: value?.departmentIds,
        },
      }).unwrap();
      notification.success({ description: tStudents("updateStudentSuccess") });
      refetch();
      router.push(PATH_ROUTER.PROTECTED.STUDENTS);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  const getFormatDate = (dateString) => {
    let formattedDate = "";
    if (dateString) {
      const parts = dateString.split("/");
      formattedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
    }
    return formattedDate;
  };

  const initialValues = useMemo(() => {
    if (userInfo) {
      return {
        remember: false,
        identificationNumber: userInfo.identificationNum,
        firstName: userInfo.firstName,
        lastName: userInfo.lastName,
        username: userInfo?.username ?? "",
        email: userInfo.email,
        code: userInfo.code,
        phoneNumber: userInfo.phoneNumber,
        birthDate: userInfo?.birthDate ? dayjs(getFormatDate(userInfo.birthDate), "YYYY-MM-DD") : "",
        genderType: userInfo.gender,
        courseNum: userInfo.courseNum,
        departmentIds: userInfo?.departmentIds,
      };
    }
    return {};
  }, [userInfo]);

  useEffect(() => {
    if (initialValues.firstName) {
      form.setFieldsValue(initialValues);
    }
  }, [initialValues]);

  const { data: departments, isLoading: departmentLoading } = useGetComboDepartmentQuery({});

  return (
    <PermissionGuard requiredPermissions={[EPermission.USER_UPDATE]}>
      <div className="student-add">
        <div className="mb-3">
          <ButtonBack />
        </div>
        <div className="student-info">
          <p className="info-header text-[24px] font-bold text-text-hust py-5 max-lg:py-2">{tStudents("studentDetail")}</p>
          <WrapperForm>
            <Form
              name="info-student-form"
              className="info-student-form flex flex-wrap justify-between"
              initialValues={initialValues}
              form={form}
              onFinish={onFinish}
            >
              <Form.Item name="lastName" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppInput title={tStudents("enterFirstName")} required placeholder={tStudents("enterFirstName")} />
              </Form.Item>
              <Form.Item
                name="firstName"
                className="w-[45%] !mb-2"
                rules={[
                  {
                    required: true,
                    message: tCommon("requiredField"),
                  },
                ]}
              >
                <AppInput title={tStudents("enterFirstName")} required placeholder={tStudents("enterFirstName")} />
              </Form.Item>
              <Form.Item name="username" className="w-[45%] !mb-2" rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppInput title={tStudents("enterUsername")} disabled={true} />
              </Form.Item>
              <Form.Item name="identificationNumber" className="w-[45%] !mb-2">
                <AppInput title={tStudents("enterIdentificationNumber")} placeholder={tStudents("enterIdentificationNumber")} />
              </Form.Item>
              <Form.Item
                name="code"
                className="w-[45%] !mb-2"
                rules={[
                  {
                    required: true,
                    message: tCommon("requiredField"),
                  },
                ]}
              >
                <AppInput title={tStudents("enterStudentCode")} placeholder={tStudents("enterStudentCode")} required />
              </Form.Item>
              <Form.Item name="courseNum" className="w-[45%] !mb-2" rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppInput title={tStudents("enterCourseNum")} placeholder={tStudents("enterCourseNum")} />
              </Form.Item>
              <Form.Item name="genderType" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppSelectSmall required placeholder={tStudents("enterGender")} options={genderOption} title={tStudents("enterGender")} />
              </Form.Item>

              <Form.Item
                name="email"
                className="w-[45%] !mb-2"
                rules={[
                  {
                    type: "email",
                    message: tStudents("enterEmail"),
                  },
                  {
                    required: true,
                    message: tCommon("requiredField"),
                  },
                ]}
              >
                <AppInput placeholder={tStudents("enterEmail")} title={tStudents("enterEmail")} />
              </Form.Item>

              <div className="w-[45%] !mb-2">
                <div className="w-full flex items-center justify-between mb-1.5">
                  <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                    <span className="font-medium">{tStudents("enterBirthDate")}</span>
                    <RequiredFieldIcon className="ml-1" />
                  </div>
                </div>

                <Form.Item name="birthDate" className="w-full flex flex-col" noStyle rules={[{ required: true, message: tCommon("requiredField") }]}>
                  <DatePicker
                    format={dateTimePattern.FORMAT_DD_MM_YYYY_SLASH}
                    placeholder={tStudents("enterBirthDate")}
                    className="w-full h-[32px] !border-[--color-text-disable]"
                  />
                </Form.Item>
              </div>
              <Form.Item
                name="phoneNumber"
                className="w-[45%] !mb-2"
                rules={[
                  {
                    pattern: /^(0|\+84)[1-9]\d{8}$/,
                    message: tCommon("wrongFormat"),
                  },
                ]}
              >
                <AppInput title={tStudents("enterPhoneNumber")} placeholder={tStudents("enterPhoneNumber")} />
              </Form.Item>

              <ClientProvider skeleton={<div className="w-[45%] !mb-2"></div>}>
                {role === RoleBase.ADMIN && (
                  <div className="w-[45%] !mb-2">
                    <div className="w-full flex items-center justify-between mb-1.5">
                      <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                        <span className="font-medium">{tStudents("enterDepartment")}</span>
                        <RequiredFieldIcon className="ml-1" />
                      </div>
                    </div>
                    <Form.Item
                      className="w-full"
                      name="departmentIds"
                      rules={[
                        {
                          required: true,
                          message: tCommon("requiredField"),
                        },
                      ]}
                    >
                      <AppSelectSmall
                        mode="multiple"
                        allowClear
                        options={getOptionsFromCombo(departments)}
                        tagRender={(props) => {
                          return <Tag color={HUST_COLOR}>{props?.label}</Tag>;
                        }}
                        loading={departmentLoading}
                      />
                    </Form.Item>
                  </div>
                )}
              </ClientProvider>

              <Form.Item className="btn-info w-full flex justify-center">
                <SubmitButton form={form} customclass="!w-[250px]" loading={loading}>
                  {tCommon("update")}
                </SubmitButton>
              </Form.Item>
            </Form>
          </WrapperForm>
        </div>
      </div>
    </PermissionGuard>
  );
};

export default StudentDetail;
