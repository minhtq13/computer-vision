import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useUpdatePasswordMutation } from "@/stores/user/api";
import { Form, Input } from "antd";
import { useState } from "react";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import SubmitButton from "@/components/submit-button";
import { useTranslations } from "next-intl";
import { ChangePasswordTypeEnum } from "@/constants";
const ModalUpdatePassword = ({ userInfo, changeType, openButton }: any) => {
  const tCreateUser = useTranslations("createUser");
  const tCommon = useTranslations("common");
  const [form] = Form.useForm();
  const [openModal, setOpenModal] = useState(false);

  const notification = useAppNotification();
  const [updatePassword, { isLoading }] = useUpdatePasswordMutation();
  const handleError = useHandleError();

  // handle update password request
  const handleUpdatePassword = async (values: any) => {
    const invalid = values?.newPassword === "" || values?.confirmedNewPassword === "" || values?.newPassword !== values?.confirmedNewPassword;
    if (!invalid) {
      try {
        await updatePassword({ userId: userInfo?.id, newPassword: values?.confirmedNewPassword, changeType: changeType }).unwrap();
        notification.success({ description: tCreateUser("updatePasswordSuccess") });
      } catch (error) {
        handleError(error);
      } finally {
        setOpenModal(false);
      }
    }
  };

  const validateConfirmedNewPassword = async (_: any, value: any) => {
    if (!value) {
      return Promise.reject(new Error(tCreateUser("newPasswordRequired")));
    }
    const newPassword = form.getFieldValue("newPassword");
    if (value !== newPassword) {
      return Promise.reject(new Error(tCreateUser("confirmedNewPasswordNotMatch")));
    }
    return Promise.resolve();
  };

  const validateNewPassword = async (_: any, value: any) => {
    if (!value) {
      return Promise.reject(new Error(tCreateUser("newPasswordRequired")));
    }
    if (value.length < 6) {
      return Promise.reject(new Error(tCreateUser("newPasswordMinLength")));
    }

    return Promise.resolve();
  };

  return (
    <>
      <div
        className="update-password-container flex"
        onClick={() => {
          setOpenModal(true);
        }}
      >
        {openButton}
      </div>
      <AppModal
        className="update-password-modal"
        open={openModal}
        onCancel={() => setOpenModal(false)}
        title={
          <p className="font-semibold text-[20px] text-hust">
            {changeType === ChangePasswordTypeEnum.UPDATE ? tCreateUser("updatePassword") : tCreateUser("resetPassword")}
          </p>
        }
        footer={null}
      >
        <p className="flex justify-center">{`${userInfo?.name} - ${userInfo?.code}`}</p>
        <Form
          form={form}
          name="update-password-form"
          className="update-password-form flex gap-4 flex-col mt-5 rounded-[16px] p-8 flex-wrap justify-between"
          colon={true}
          onFinish={handleUpdatePassword}
        >
          {/* {changeType === ChangePasswordTypeEnum.UPDATE && (
            <Form.Item name="currentPassword" colon={true} initialValue={currentPassword} noStyle>
              <div className="flex flex-col gap-2">
                <p className="text-text-secondary font-italic">Mật khẩu hiện tại:</p>
                <Input.Password placeholder="Nhập lại hiện tại"/>
              </div> */}
          {/* <span className="text-hust-color text-sm font-italic">{newPassword === "" ? "Mật khẩu mới không được bỏ trống" : ""}</span> */}
          {/* </Form.Item>
          )} */}
          <Form.Item name="newPassword" colon={true} className="!mb-0" rules={[{ validator: validateNewPassword }]}>
            <div className="flex flex-col gap-2">
              <p className="text-text-secondary font-italic">{tCreateUser("newPassword")}:</p>
              <Input.Password placeholder={tCreateUser("newPassword")} />
            </div>
          </Form.Item>
          <Form.Item name="confirmedNewPassword" colon={true} className="!mb-0" rules={[{ validator: validateConfirmedNewPassword }]}>
            <div className="flex flex-col gap-2">
              <p className="text-text-secondary font-italic">{tCreateUser("confirmedNewPassword")}:</p>
              <Input.Password placeholder={tCreateUser("confirmedNewPassword")} />
            </div>
          </Form.Item>
          <Form.Item className="!mb-0">
            <div className="flex items-center justify-end gap-4">
              <AppButton key="back" onClick={() => setOpenModal(false)} type="default">
                {tCommon("close")}
              </AppButton>
              <SubmitButton form={form} loading={isLoading}>
                {tCommon("update")}
              </SubmitButton>
            </div>
          </Form.Item>
        </Form>
      </AppModal>
    </>
  );
};
export default ModalUpdatePassword;
