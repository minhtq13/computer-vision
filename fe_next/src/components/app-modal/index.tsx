"use client";
import { Modal, ModalProps } from "antd";
import "./style.scss";

interface IModalProps extends ModalProps {
  customClassName?: string;
  maskClosable?: boolean;
}

const AppModal = ({ children, customClassName, maskClosable = true, ...props }: IModalProps) => {
  return (
    <Modal
      footer={false}
      maskClosable={maskClosable}
      className={`app-modal !max-2md:w-full ${customClassName}`}
      rootClassName="app-modal"
      centered
      {...props}
    >
      {children}
    </Modal>
  );
};

export default AppModal;
