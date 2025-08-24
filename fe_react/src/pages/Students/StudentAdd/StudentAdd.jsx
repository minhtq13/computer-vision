import "./StudentAdd.scss";
import React, { useState } from "react";
import StudentInfo from "../../../components/StudentInfo/StudentInfo";
import { addStudentsService } from "../../../services/studentsService";
import useNotify from "../../../hooks/useNotify";
import { formatDate } from "../../../utils/tools";
const StudentAdd = () => {
	const [loading, setLoading] = useState(false);
	const notify = useNotify();
	const onFinish = (value) => {
		setLoading(true);
		addStudentsService(
			{ ...value, birthday: formatDate(value.birthday) },
			(res) => {
				setLoading(false);
				notify.success("Thêm mới sinh viên thành công!");
			},
			(error) => {
				setLoading(false);
				notify.error("Lỗi thêm mới sinh viên!");
			}
		);
	};
	return (
		<div className="student-add">
			<StudentInfo
				infoHeader="Thêm sinh viên"
				onFinish={onFinish}
				btnText="Thêm"
				initialValues={{ remember: false }}
				loading={loading}
				isPasswordDisplay={true}
				isUserNameDisplay={true}
				courseDisable={false}
			/>
		</div>
	);
};
export default StudentAdd;
