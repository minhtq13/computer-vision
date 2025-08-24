import "./SubjectView.scss";
import { Skeleton, Form, Input, Button } from "antd";
import useSubjects from "../../../hooks/useSubjects";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { addChapterService } from "../../../services/subjectsService";
import useNotify from "../../../hooks/useNotify";

const SubjectView = () => {
	const { querySubjectDetail, subjectInfo, infoLoading } = useSubjects();
	const [btnLoading, setBtnLoading] = useState(false);
	const [formKey, setFormKey] = useState();
	const notify = useNotify();
	const onFinish = (values) => {
		if (values?.chaptersAdd.length > 0) {
			setBtnLoading(true);
			addChapterService(
				{
					subjectId: code,
					lstChapter: values.chaptersAdd.map((item) => {
						return {orders: item.orderAdd, title: item.titleAdd};
					}),
				},
				() => {
					querySubjectDetail({}, code);
					setBtnLoading(false);
					notify.success("Thêm chap mới thành công!");
					setFormKey((pre) => pre + 1);
				},
				(error) => {
					setBtnLoading(false);
					notify.error(error.response.data.message);
				}
			).then(() => {});
		}
	};
	const location = useLocation();
	const code = location.pathname.split("/")[2];
	useEffect(() => {
		querySubjectDetail({}, code);
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);
	const contents = subjectInfo?.chapters
		? subjectInfo?.chapters.sort((a, b) => a.orders - b.orders)
		: [];

	return (
		<div className="subject-view">
			<div className="subject-view-header"> Thông tin học phần</div>
			<div className="subject-view-info">
				<div className="subject-main-info">
					<div className="subject-info-title subject-column">
						<span className="subject-info-item-title">
							Mã học phần:
						</span>
						<span className="subject-info-item-title">
							Tên học phần:
						</span>
						<span className="subject-info-item-title">
							Đơn vị quản lý:
						</span>
						<span className="subject-info-item-title">Mô tả:</span>
						<span className="subject-info-item-title">
							Số tín chỉ:
						</span>
					</div>
					<div className="subject-column">
						<Skeleton loading={infoLoading}>
							<span>{subjectInfo ? subjectInfo?.code : ""}</span>
							<span>{subjectInfo ? subjectInfo?.title : ""}</span>
							<span>{subjectInfo ? subjectInfo?.departmentName : ""}</span>
							<span>
								{subjectInfo ? subjectInfo.description : ""}
							</span>
							<span>{subjectInfo ? subjectInfo.credit : ""}</span>
						</Skeleton>
					</div>
				</div>
				<div className="subject-content-tab">
					<div className="chapter-title">Nội dung</div>
					<div className="subject-content">
						<Skeleton loading={infoLoading}>
							{contents.map((item, index) => {
								return (
									<div
										className="subject-content-nonedit"
										key={index}
									>
										<span>{`Chương ${item.orders}:`}</span>
										<span>{`${item.title}`}</span>
									</div>
								);
							})}
						</Skeleton>
					</div>
					<Form
						name="subject-content-form"
						onFinish={onFinish}
						className="subject-content-form"
						key={formKey}
					>
						<Form.List name="chaptersAdd">
							{(fields, fieldOperations) => {
								return (
									<>
										{fields.map((field, index) => (
											<div
												className="item-space"
												key={`addchapter${index}`}
											>
												<div className="form-space">
													<Form.Item
														{...field}
														className="add-chapter-order"
														name={[
															field.name,
															`orderAdd`,
														]}
														key={`orders-add${index}`}
														label="Chương"
														rules={[
															{
																required: true,
																message:
																	"Chưa điền số chương",
															},
															{
																validator: ( _, value ) => { if ( value && value < 1 ) {
																		return Promise.reject("Số chương bắt đầu từ 1");
																	}
																	return Promise.resolve();
																},
															},
														]}
													>
														<Input
															type="number"
															placeholder="Nhập thứ tự chương"
														/>
													</Form.Item>
													<Form.Item
														{...fields}
														className="add-chapter-content"
														name={[
															field.name,
															`titleAdd`,
														]}
														key={`title-add${index}`}
														label="Nội dung"
														rules={[
															{
																required: true,
																message:
																	"Chưa điền nội dung",
															},
														]}
													>
														<Input placeholder="Nhập tên chương" />
													</Form.Item>
												</div>
												<div className="btn-space">
													<Button
														onClick={() =>
															fieldOperations.remove(
																index
															)
														}
														icon={
															<DeleteOutlined />
														}
													></Button>
												</div>
											</div>
										))}
										<Form.Item
											className="btn"
											key={`btnAdd`}
										>
											<Button
												type="dashed"
												onClick={() => {
													fieldOperations.add();
												}}
												block
												icon={<PlusOutlined />}
												style={{
													width: 300,
													marginTop: 20,
												}}
											>
												Thêm chương mới
											</Button>
										</Form.Item>
									</>
								);
							}}
						</Form.List>
						<Form.Item className="btn btn-submit">
							<Button
								type="primary"
								htmlType="submit"
								style={{ width: 150, height: 50 }}
								loading={btnLoading}
							>
								Lưu
							</Button>
						</Form.Item>
					</Form>
				</div>
			</div>
		</div>
	);
};
export default SubjectView;
