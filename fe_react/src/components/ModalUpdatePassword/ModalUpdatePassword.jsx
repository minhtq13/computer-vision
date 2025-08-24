import React, { useState } from "react";
import { Button, Form, Input, Modal } from "antd";
import { ChangePasswordTypeEnum } from "../../utils/constant";
import { updatePasswordService } from "../../services/userService";
import useNotify from "../../hooks/useNotify";
import { processApiError } from "../../utils/apiUtils";

const ModalUpdatePassword = ({ userInfo, changeType, openButton }) => {
  const [openModal, setOpenModal] = useState(false);

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmedNewPassword, setConfirmedNewPassword] = useState("");
  const notify = useNotify();

  // handle update password request
  const handleUpdatePassword = () => {
    let invalid = newPassword === "" || confirmedNewPassword === "" || newPassword !== confirmedNewPassword;
    if (!invalid) {
      updatePasswordService(
        {
          userId: userInfo?.id,
          newPassword: confirmedNewPassword,
          changeType: changeType,
        },
        () => {
          setOpenModal(false);
          notify.success("Cập nhật mật khẩu người dùng thành công!");
        },
        (error) => {
          setOpenModal(false);
          notify.error(processApiError(error));
        }
      ).then(() => {});
    }
  };

  return (
    <>
      <div
        className="update-password-container"
        onClick={() => {
          console.log(userInfo);
          setOpenModal(true);
        }}
      >
        {openButton}
      </div>
      <Modal
        className="update-password-modal"
        open={openModal}
        onCancel={() => setOpenModal(false)}
        title={
          <p
            style={{
              fontWeight: "bold",
              fontSize: "20px",
              color: "var(--hust-color)",
            }}
          >
            {changeType === ChangePasswordTypeEnum.UPDATE ? "Đổi mật khẩu" : "Đặt lại mật khẩu"}
          </p>
        }
        footer={[
          <Button
            key="back"
            onClick={() => setOpenModal(false)}
            style={{
              background: "#F5F8FA",
              borderRadius: "6px",
              height: "44px",
              outline: "none",
              border: "none",
              minWidth: "100px",
            }}
          >
            Đóng
          </Button>,
          <Button
            style={{
              borderRadius: "6px",
              height: "44px",
              marginRight: "12px",
              minWidth: "100px",
            }}
            key="submit"
            type="primary"
            onClick={handleUpdatePassword}
          >
            Cập nhật
          </Button>,
        ]}
      >
        <p
          style={{
            display: "flex",
            width: "inherit",
            justifyContent: "center",
          }}
        >{`${userInfo?.name} - ${userInfo?.code}`}</p>
        <Form name="update-password-form" className="update-password-form" colon={true}>
          {changeType === ChangePasswordTypeEnum.UPDATE && (
            <Form.Item colon={true} initialValue={currentPassword}>
              <p style={{ color: "var(--hust-color)", fontStyle: "italic", marginBottom: 6 }}>Mật khẩu hiện tại:</p>
              <Input.Password placeholder="Nhập lại hiện tại" onChange={(e) => setCurrentPassword(e?.target?.value)} />
              <span style={{ color: "var(--hust-color)", fontSize: 13, fontStyle: "italic" }}>
                {newPassword === "" ? "Mật khẩu mới không được bỏ chống" : ""}
              </span>
            </Form.Item>
          )}
          <Form.Item colon={true} initialValue={newPassword}>
            <p style={{ color: "var(--hust-color)", fontStyle: "italic", marginBottom: 6 }}>Mật khẩu mới:</p>
            <Input.Password placeholder="Nhập mật khẩu mới" onChange={(e) => setNewPassword(e?.target?.value)} />
            <span style={{ color: "var(--hust-color)", fontSize: 13, fontStyle: "italic" }}>
              {newPassword === "" ? "Mật khẩu mới không được bỏ chống" : ""}
            </span>
          </Form.Item>
          <Form.Item colon={true}>
            <p style={{ color: "var(--hust-color)", fontStyle: "italic", marginBottom: 6 }}>Xác nhận mật khẩu mới:</p>
            <Input.Password placeholder="Nhập lại mật khẩu mới" onChange={(e) => setConfirmedNewPassword(e?.target?.value)} />
            <span style={{ color: "var(--hust-color)", fontSize: 13, fontStyle: "italic" }}>
              {confirmedNewPassword === "" ? "Mật khẩu mới không được bỏ chống" : ""}
            </span>
            <span style={{ color: "var(--hust-color)", fontSize: 13, fontStyle: "italic" }}>
              {confirmedNewPassword !== "" && confirmedNewPassword !== newPassword ? "Mật khẩu xác nhận không khớp" : ""}
            </span>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
export default ModalUpdatePassword;
