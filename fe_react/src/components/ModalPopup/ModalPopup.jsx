import { Button, Modal } from "antd";
import React, { useState } from "react";
import "./ModalPopup.scss";
import closeIconPopup from "../../assets/images/svg/close-icon.svg";

const ModalPopup = ({
	buttonOpenModal,
	onAccept = Function,
	message,
	title,
	icon,
	confirmMessage,
	ok,
	buttonDisable,
}) => {
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
		<>
			<div className="button-delete" onClick={showModal}>
				{buttonOpenModal}
			</div>
			<Modal
				style={{ height: "30vh" }}
				open={open}
				ok={ok}
				title={title}
				onCancel={handleCancel}
				closeIcon={<img src={closeIconPopup} alt="" />}
				className="modal-popup-component"
				centered
				footer={[
					<Button
						key="back"
						onClick={handleCancel}
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
						loading={loading}
						onClick={handleOk}
					>
						{ok}
					</Button>,
				]}
			>
				<div className="modal-popup-content">
					<img className="icon" src={icon} alt="" />
					<p
						className="text-bold"
						style={{
							marginTop: "24px",
							marginBottom: "8px",
							fontSize: "24px",
						}}
					>
						{message}
					</p>
					<h4
						style={{
							marginTop: "10px",
							fontStyle: "italic",
							color: "#bbb",
							fontWeight: "400"
						}}
					>
						{confirmMessage}
					</h4>
				</div>
			</Modal>
		</>
	);
};

export default ModalPopup;
