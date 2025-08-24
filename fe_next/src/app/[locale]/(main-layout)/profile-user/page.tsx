"use client";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppSelectSmall from "@/components/app-select-small";
import SubmitButton from "@/components/submit-button";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import { useAppDispatch, useAppSelector } from "@/libs/redux/store";
import { setRefreshUserInfo } from "@/stores/refresh";
import { useGetUserInfoQuery, usePostImageMutation, useUpdateProfileMutation } from "@/stores/user/api";
import { getUserId } from "@/stores/user/selectors";
import { dateTimePattern } from "@/constants";
import { formatDateParam } from "@/helpers/tools";
import { EditOutlined, PlusOutlined } from "@ant-design/icons";
import { DatePicker, Form, message, Upload } from "antd";
import { useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import dayjs from "dayjs";
import AppTooltip from "@/components/app-tooltip";
import { useTranslations } from "next-intl";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { BASE_RESOURCE_URL_SPRING } from "@/constants/apiPath";
import AppAvatar from "@/components/app-avatar";
import { RoleBase, RoleBaseCode } from "@/types/enum";

const ProfileUser = () => {
  const tCommon = useTranslations("common");
  const tProfileUser = useTranslations("profileUser");
  const { genderOption, roleOption } = useLocaleOptions();
  const [avatarPath, setAvatarPath] = useState("");
  const [avatarId, setAvatarId] = useState(null);
  const [avatarChanged, setAvatarChanged] = useState(false);
  const form = Form.useForm()[0];
  const userId = useAppSelector(getUserId);
  const router = useRouter();
  const notification = useAppNotification();
  const dispatch = useAppDispatch();
  const handleError = useHandleError();
  const [postImage] = usePostImageMutation();
  const { data: userInfo, refetch } = useGetUserInfoQuery({ userId: userId }, { refetchOnMountOrArgChange: true, skip: !userId });
  const [updateProfile, { isLoading: updateProfileLoading }] = useUpdateProfileMutation();
  const { role } = useRole();
  const onFinish = async (value: any) => {
    try {
      await updateProfile({
        ...value,
        birthDate: formatDateParam(value.birthDate),
        userType: role === RoleBase.ADMIN ? RoleBaseCode.ADMIN : role === RoleBase.TEACHER ? RoleBaseCode.TEACHER : RoleBaseCode.STUDENT,
        identityType: "CITIZEN_ID_CARD",
        avatarId: avatarId,
      }).unwrap();
      refetch();
      dispatch(setRefreshUserInfo(Date.now()));
      router.push(PATH_ROUTER.PROTECTED.STUDENTS);
      notification.success({ description: tProfileUser("updateProfileSuccess") });
    } catch (error) {
      handleError(error);
    }
  };

  const initialValues = useMemo(() => {
    if (userInfo) {
      return {
        firstName: userInfo?.firstName,
        lastName: userInfo?.lastName,
        email: userInfo?.email,
        birthDate: dayjs(userInfo.birthDate, "DD/MM/YYYY"),
        username: userInfo?.username,
        phoneNumber: userInfo?.phoneNumber,
        code: userInfo?.code,
        userType: userInfo?.userType,
        genderType: userInfo?.gender,
        avatar: userInfo?.avatarStoredType === 1 ? userInfo?.avatarPath : BASE_RESOURCE_URL_SPRING + userInfo?.avatarPath,
        avatarId: userInfo?.avatarId,
        identificationNumber: userInfo?.identificationNum,
      };
    }
    return {};
  }, [userInfo]);

  useEffect(() => {
    if (initialValues !== undefined) {
      form.setFieldsValue(initialValues);
      setAvatarPath(initialValues?.avatar);
      setAvatarId(initialValues?.avatarId);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [initialValues]);

  // validate before uploading
  const beforeUpload = (file) => {
    const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
    if (!isJpgOrPng) {
      message.error(tProfileUser("imageFormatError")).then(() => {});
    }
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
      message.error(tProfileUser("imageSizeError")).then(() => {});
    }
    return isJpgOrPng && isLt10M;
  };
  const handleUploadAvatar = async (e: any) => {
    try {
      setAvatarChanged(true);
      const { name, type, originFileObj } = e?.file;
      const formData = new FormData();
      formData.append("file", new File([originFileObj], name, { type }));
      const uploadedData = await postImage(formData).unwrap();
      setAvatarPath(uploadedData?.fileAttachDB?.storedType === 1 ? uploadedData?.filePath : BASE_RESOURCE_URL_SPRING + uploadedData?.filePath);
      setAvatarId(uploadedData?.id);
      notification.success({ description: tProfileUser("updateAvatarSuccess") });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <div className="profile-user">
      <div className="update-user-info-form-component">
        <p className="info-header text-2xl font-[600] text-text-hust pb-3">{tProfileUser("updateProfileTitle")}</p>
        <div className="user-avatar flex w-full !mb-2 justify-center">
          <div className="avatar-preview-upload inline-flex flex-col items-center">
            <AppTooltip className="cursor-pointer" title={avatarChanged ? tProfileUser("notSavedAvatar") : tProfileUser("savedAvatar")}>
              <AppAvatar imageUrl={avatarPath} name={initialValues?.lastName?.charAt(0)} size={128} />
            </AppTooltip>
            <Upload
              accept="image/png, image/jpeg"
              fileList={[]}
              customRequest={() => {}} // prevent a default form submission by a NOP callback
              beforeUpload={beforeUpload}
              onChange={handleUploadAvatar}
            >
              <AppButton typeButton="outline" customclass="my-3">
                {avatarPath ? <EditOutlined /> : <PlusOutlined />} {tProfileUser("updateAvatar")}
              </AppButton>
            </Upload>
          </div>
        </div>
        <WrapperForm>
          {" "}
          <Form
            form={form}
            initialValues={initialValues}
            name="info-user-form"
            className="info-user-form flex flex-wrap justify-between relative"
            onFinish={onFinish}
          >
            <Form.Item name="lastName" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
              <AppInput required title={tCommon("firstName")} placeholder={tCommon("firstName")} />
            </Form.Item>
            <Form.Item name="firstName" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
              <AppInput required title={tCommon("firstName")} placeholder={tCommon("firstName")} />
            </Form.Item>
            <Form.Item name="userType" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
              <AppSelectSmall disabled={true} placeholder={tCommon("role")} options={roleOption} title={tCommon("role")} />
            </Form.Item>
            <Form.Item name="code" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
              <AppInput disabled={!(role === RoleBase.ADMIN)} placeholder={tCommon("code")} title={tCommon("code")} />
            </Form.Item>

            <Form.Item
              className="w-[45%] !mb-2"
              name="username"
              colon={true}
              rules={[
                { required: true, message: tCommon("requiredField") },
                {
                  min: 5,
                  message: tProfileUser("usernameMinLength"),
                },
              ]}
            >
              <AppInput disabled={true} placeholder={tCommon("username")} title={tCommon("username")} />
            </Form.Item>

            <Form.Item name="genderType" className="w-[45%] !mb-2" colon={true} rules={[{ required: true, message: tCommon("requiredField") }]}>
              <AppSelectSmall required placeholder={tCommon("gender")} options={genderOption} title={tCommon("gender")} />
            </Form.Item>
            <Form.Item
              name="email"
              className="w-[45%] !mb-2"
              rules={[
                {
                  type: "email",
                  message: tCommon("emailFormatError"),
                },
                {
                  required: true,
                  message: tCommon("requiredField"),
                },
              ]}
            >
              <AppInput disabled={!(role === RoleBase.ADMIN)} placeholder="Nhập địa chỉ email" title="Email" required />
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
            {/* 
          {false && (
            <Form.Item
              name="password"
              className="w-[45%] !mb-2"
              rules={[{ required: true, message: tCommon("requiredField") }]}
              label="Mật khẩu"
              colon={true}
            >
              <AppInput placeholder="Nhập mật khẩu" autoComplete="on" title="Mật khẩu" required />
            </Form.Item>
          )} */}
            <Form.Item
              className="w-[45%] !mb-2"
              name="phoneNumber"
              colon={true}
              rules={[
                {
                  pattern: /^(0|\+84)[1-9]\d{8}$/,
                  message: tCommon("wrongFormat"),
                },
              ]}
            >
              <AppInput placeholder={tCommon("phoneNumber")} title={tCommon("phoneNumber")} required />
            </Form.Item>
            <Form.Item
              name="identificationNumber"
              className="w-[45%] !mb-2"
              colon={true}
              rules={[{ required: true, message: tCommon("requiredField") }]}
            >
              <AppInput placeholder="Nhập CCCD" title="Số CCCD" required />
            </Form.Item>
            <Form.Item className="btn-info w-full h-[50px] flex justify-center !mb-0 !mt-4 bg-white">
              <SubmitButton form={form} customclass="!w-[250px]" loading={updateProfileLoading}>
                {tCommon("update")}
              </SubmitButton>
            </Form.Item>
          </Form>
        </WrapperForm>
      </div>
    </div>
  );
};

export default ProfileUser;
