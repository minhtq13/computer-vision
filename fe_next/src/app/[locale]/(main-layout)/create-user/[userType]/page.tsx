"use client";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import AppInput from "@/components/app-input";
import AppSelectSmall from "@/components/app-select-small";
import ButtonBack from "@/components/button-back";
import SubmitButton from "@/components/submit-button";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useGetComboDepartmentQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useCreateUserMutation } from "@/stores/user/api";
import { HUST_COLOR, dateTimePattern } from "@/constants";
import { formatDateParam } from "@/helpers/tools";
import { DatePicker, Form, Tag } from "antd";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { ERole, RoleBaseCode, RoleBaseId } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";

enum UserType {
  STUDENT = "student",
  TEACHER = "teacher",
  ADMIN = "admin",
}

const CreateUser = ({ params }: { params: { userType: UserType } }) => {
  const tCreateUser = useTranslations("createUser");
  const tCommon = useTranslations("common");
  const { genderOption, roleOption } = useLocaleOptions();
  const notification = useAppNotification();
  const router = useRouter();
  const userType: UserType = params.userType;
  const [form] = Form.useForm();
  const handleError = useHandleError();

  const { data: departments, isLoading: departmentLoading } = useGetComboDepartmentQuery({ search: "" });

  const [createUser, { isLoading: createUserLoading }] = useCreateUserMutation();

  const onFinish = async (value: any) => {
    try {
      await createUser({
        ...value,
        birthDate: formatDateParam(value.birthDate),
        lstRoleId: [
          value.userType === RoleBaseCode.ADMIN
            ? RoleBaseId.ADMIN
            : value.userType === RoleBaseCode.TEACHER
            ? RoleBaseId.TEACHER
            : RoleBaseId.STUDENT,
        ],
        departmentId: -1,
        metaData:
          value?.userType === RoleBaseCode.TEACHER
            ? { subjectIds: value?.subjectIds }
            : value.userType === RoleBaseCode.STUDENT
            ? { courseNum: +value.metaData }
            : {},
        departmentIds: value?.departmentIds,
      }).unwrap();
      notification.success({
        description: tCreateUser("createUserSuccess"),
      });
      if (value?.userType === RoleBaseCode.ADMIN) {
        router.push(PATH_ROUTER.PROTECTED.STUDENTS);
      } else if (value?.userType === RoleBaseCode.TEACHER) {
        router.push(PATH_ROUTER.PROTECTED.TEACHERS);
      } else {
        router.push(PATH_ROUTER.PROTECTED.STUDENTS);
      }
    } catch (error) {
      handleError(error);
    }
  };
  const { data: subjects } = useGetComboSubjectQuery({});

  const textTitle = userType === UserType.STUDENT ? tCommon("student") : userType === UserType.TEACHER ? tCommon("teacher") : tCommon("admin");
  return (
    <PermissionGuard requiredRoles={userType === UserType.ADMIN || userType === UserType.TEACHER ? [ERole.ADMIN] : [ERole.ADMIN, ERole.TEACHER]}>
      <div className="student-add">
        <ButtonBack />
        <div className="student-info">
          <p className="info-header text-[24px] font-bold text-text-hust py-5 max-lg:py-2">{tCreateUser("createUserTitle", { textTitle })}</p>
          <WrapperForm>
            <Form name="info-student-form" className="info-student-form flex flex-wrap justify-between" form={form} onFinish={onFinish}>
              <Form.Item name="lastName" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppInput title={tCommon("firstName")} required placeholder={`${tCommon("firstName")} ${textTitle}`} />
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
                <AppInput title={tCommon("lastName")} required placeholder={`${tCommon("lastName")} ${textTitle}`} />
              </Form.Item>
              <Form.Item
                name="userType"
                className="w-[45%] !mb-2"
                colon={true}
                initialValue={
                  userType === UserType.STUDENT ? RoleBaseCode.STUDENT : userType === UserType.TEACHER ? RoleBaseCode.TEACHER : RoleBaseCode.ADMIN
                }
                rules={[{ required: true, message: tCommon("requiredField") }]}
              >
                <AppSelectSmall required placeholder={tCommon("role")} options={roleOption} title={tCommon("role")} disabled={true} />
              </Form.Item>
              {userType === UserType.STUDENT && (
                <Form.Item name="courseNum" className="w-[45%] !mb-2" rules={[{ required: true, message: tCommon("requiredField") }]}>
                  <AppInput title={tCommon("course")} required placeholder={tCommon("course")} />
                </Form.Item>
              )}
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
                <AppInput
                  title={userType === UserType.STUDENT ? "MSSV" : userType === UserType.TEACHER ? "Mã giảng viên" : "Mã quản trị viên"}
                  placeholder={userType === UserType.STUDENT ? "MSSV" : userType === UserType.TEACHER ? "Mã giảng viên" : "Mã quản trị viên"}
                  required
                />
              </Form.Item>
              <Form.Item name="username" className="w-[45%] !mb-2" rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppInput required title={tCommon("username")} placeholder={tCommon("username")} />
              </Form.Item>

              <Form.Item name="genderType" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
                <AppSelectSmall required placeholder={tCommon("gender")} options={genderOption} title={tCommon("gender")} />
              </Form.Item>

              <Form.Item
                className="w-[45%] !mb-2"
                name="password"
                rules={[
                  {
                    required: true,
                    message: tCommon("requiredField"),
                  },
                ]}
              >
                <AppInput isPassword required placeholder={tCommon("password")} title={tCommon("password")} />
              </Form.Item>

              <Form.Item
                name="email"
                className="w-[45%] !mb-2"
                rules={[
                  {
                    type: "email",
                    message: tCommon("wrongFormat"),
                  },
                  {
                    required: true,
                    message: tCommon("requiredField"),
                  },
                ]}
              >
                <AppInput placeholder={tCommon("email")} title={tCommon("email")} required />
              </Form.Item>
              <div className="w-[45%] !mb-2">
                <div className="w-full flex items-center justify-between mb-1.5">
                  <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                    <span className="font-medium">{tCommon("birthDate")}</span>
                    <RequiredFieldIcon className="ml-1" />
                  </div>
                </div>

                <Form.Item name="birthDate" className="w-full flex flex-col" noStyle rules={[{ required: true, message: tCommon("requiredField") }]}>
                  <DatePicker
                    format={dateTimePattern.FORMAT_DD_MM_YYYY_SLASH}
                    placeholder={tCommon("birthDate")}
                    className="w-full h-[32px] !border-[--color-text-disable]"
                  />
                </Form.Item>
              </div>
              <Form.Item name="identificationNumber" className="w-[45%] !mb-2">
                <AppInput title={tCommon("identificationNumber")} placeholder={tCommon("identificationNumber")} />
              </Form.Item>

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
                <AppInput title={tCommon("phoneNumber")} placeholder={tCommon("phoneNumber")} />
              </Form.Item>

              {userType === UserType.TEACHER && (
                <div className="w-[45%] !mb-2">
                  <div className="w-full flex items-center justify-between mb-1.5">
                    <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                      <span className="font-medium">Môn giảng dạy</span>
                      <RequiredFieldIcon className="ml-1" />
                    </div>
                  </div>
                  <Form.Item name="subjectIds" noStyle>
                    <AppSelectSmall
                      className="w-full"
                      mode="multiple"
                      allowClear
                      options={getOptionsFromCombo(subjects, true)}
                      filterOption={(input, option: any) => (option?.label ?? "")?.toLowerCase()?.includes(input?.toLowerCase())}
                      optionLabelProp="label"
                      tagRender={(props) => {
                        return (
                          <Tag color={HUST_COLOR} rootClassName="!text-xs !mt-1.5">
                            {props?.label}
                          </Tag>
                        );
                      }}
                    />
                  </Form.Item>
                </div>
              )}

              <div className="w-[45%] !mb-2">
                <div className="w-full flex items-center justify-between mb-1.5">
                  <div className="text-14px-normal text-text-secondary flex flex-row items-center">
                    <span className="font-medium">{tCommon("department")}</span>
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
                    placeholder={tCommon("department")}
                    className="w-full"
                    mode="multiple"
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
              <Form.Item className="btn-info w-full flex justify-center !mt-3">
                <SubmitButton form={form} customclass="!w-[250px]" loading={createUserLoading}>
                  {tCommon("add")}
                </SubmitButton>
              </Form.Item>
            </Form>
          </WrapperForm>
        </div>
      </div>
    </PermissionGuard>
  );
};

export default CreateUser;
