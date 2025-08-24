import Image from "next/image";
import { useState } from "react";
import closeIconPopup from "@/assets/images/svg/close-icon.svg";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import { useTranslations } from "next-intl";

interface ModalPopupProps {
  buttonOpenModal: any;
  onAccept: any;
  message: any;
  title: any;
  icon: any;
  confirmMessage?: any;
  ok: any;
  buttonDisable?: any;
}

const ModalPopup = ({ buttonOpenModal, onAccept = Function, message, title, icon, confirmMessage, ok, buttonDisable }: ModalPopupProps) => {
  const tCommon = useTranslations("common");
  const [loading, setLoading] = useState(false);
  const [open, setOpen] = useState(false);

  const showModal = () => {
    if (!buttonDisable) {
      setOpen(true);
    }
  };

  const handleOk = () => {
    setLoading(true);
    onAccept();
    setTimeout(() => {
      setLoading(false);
      setOpen(false);
    }, 500);
  };

  const handleCancel = () => {
    setOpen(false);
  };

  return (
    <div className="modal-popup-component">
      <div className="button-delete" onClick={showModal}>
        {buttonOpenModal}
      </div>
      <AppModal
        open={open}
        okText={ok}
        title={title}
        onCancel={handleCancel}
        closeIcon={<Image src={closeIconPopup} alt="" />}
        className="modal-popup-component"
        centered
        footer={[
          <AppButton
            key="back"
            onClick={handleCancel}
            classChildren="!font-normal"
            className="rounded-[6px] h-[44px] outline-none border-none min-w-[100px]"
          >
            {tCommon("close")}
          </AppButton>,
          <AppButton
            classChildren="!font-normal"
            className="rounded-[6px] h-[44px] mr-[12px] min-w-[100px]"
            key="submit"
            type="primary"
            loading={loading}
            onClick={handleOk}
          >
            {ok}
          </AppButton>,
        ]}
      >
        <div className="modal-popup-content mb-6">
          <Image className="icon" src={icon} alt="" />
          <p className="text-[20px] font-medium mt-[12px] mb-[8px]">{message}</p>
          <h4 className="text-italic text-gray-500 font-normal">{confirmMessage || tCommon("deleteConfirmMessage")}</h4>
        </div>
      </AppModal>
    </div>
  );
};

export default ModalPopup;
