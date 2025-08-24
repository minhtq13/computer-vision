/* eslint-disable no-unused-vars */
import {Button, Checkbox, Form, Select, Skeleton, Switch} from "antd";
import "./QuestionEdit.scss";
import { useEffect, useState } from "react";
import { DeleteOutlined, PlusOutlined } from "@ant-design/icons";
import { useLocation, useNavigate } from "react-router-dom";
import { updateQuesionsService } from "../../../services/questionServices";
import ReactQuill, { Quill} from "react-quill";
import "react-quill/dist/quill.snow.css";
import ImageResize from "quill-image-resize-module-react";
import useNotify from "../../../hooks/useNotify";
import useQuestions from "../../../hooks/useQuestion";
import useCombo from "../../../hooks/useCombo";
import katex from "katex";
import "katex/dist/katex.min.css";

window.katex = katex;
Quill.register("modules/imageResize", ImageResize);
const QuestionEdit = () => {
	const navigate = useNavigate();
	const { getQuestionDetail, questionInfo, infoLoading } = useQuestions();
	const [checked, setChecked] = useState(false);
	const [numCheckedAns, setNumCheckedAns] = useState(0);
	const [isMultipleAnswers, setIsMultipleAnswers] = useState(false);
	const [chapterId, setChapterId] = useState(null);
	const [subjectId, setSubjectId] = useState(null);
	const [isFirstMount, setIsFirstMount] = useState(true);
	const [isChanged, setIsChanged] = useState(false);
	const {
		chapterLoading,
		subLoading,
		allChapters,
		allSubjects,
		getAllChapters,
		getAllSubjects,
	} = useCombo();
	const location = useLocation();
	const notify = useNotify();
	const questionId = location.pathname.split("/")[2];
	const modules = {
		toolbar: [
			["bold", "italic", "underline"], // toggled buttons
			["blockquote", "code-block"],
			[{ list: "ordered" }, { list: "bullet" }],
			[{ script: "sub" }, { script: "super" }], // superscript/subscript
			[{ align: [] }],
			["image", "link", "formula"],
			[{ indent: "-1" }, { indent: "+1" }], // outdent/indent
			["clean"]
		],

		clipboard: {
			matchVisual: false,
		},
		imageResize: {
			parchment: Quill.import("parchment"),
			modules: ["Resize", "DisplaySize", "Toolbar"],
		},
	};
	const formats = [
		"list",
		"size",
		"bold",
		"italic",
		"underline",
		"blockquote",
		"indent",
		"link",
		"image",
		"code-block",
		"align",
		"script",
		"formula"
	];
	useEffect(() => {
		getQuestionDetail({}, questionId);
		// eslint-disable-next-line
	}, []);
	useEffect(() => {
		getAllSubjects({ subjectCode: null, subjectTitle: null });
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);
	useEffect(() => {
		if (!infoLoading) {
			getAllChapters({
				subjectId: subjectId ?? questionInfo.subjectId,
				chapterCode: null,
				chapterId: null,
			});
			if (isFirstMount) {
				setChapterId(questionInfo.chapterId);
			}
			setIsFirstMount(false);
			setNumCheckedAns([...(questionInfo?.lstAnswer)].filter(ans => ans?.isCorrect).length);
			setIsMultipleAnswers(questionInfo?.isMultipleAnswers);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [subjectId, infoLoading]);
	const subjectOptions = allSubjects.map((item) => {
		return { value: item.id, label: item.name };
	});
	const chapterOptions = allChapters.map((item) => {
		return { value: item.id, label: item.name };
	});
	const subjectOnChange = (value) => {
		setIsChanged(true);
		setSubjectId(value);
		setChapterId(null);
	};
	const chapterOnchange = (value) => {
		setIsChanged(true);
		setChapterId(value);
	};
	const sentLevel = (level) => {
		let levelParam = "EASY";
		if (level === 1) {
			levelParam = "MEDIUM";
		}
		if (level === 2) {
			levelParam = "HARD";
		}
		return levelParam;
	};
	const levelOption = [
		{
			value: 0,
			label: "Dễ",
		},
		{
			value: 1,
			label: "Trung bình",
		},
		{
			value: 2,
			label: "Khó",
		},
	];
	const onChange = (checkValues) => {
		setIsChanged(true);
		let isChecked = checkValues?.target?.checked;
		if (isChecked) {
			setNumCheckedAns(numCheckedAns + 1);
		} else {
			setNumCheckedAns(numCheckedAns - 1);
		}
		setChecked(isChecked);
	};
	const onFinish = (values) => {
		if (!values?.isMultipleAnswers && numCheckedAns > 1) {
			return;
		}
		updateQuesionsService(
			questionId,
			{...values, chapterId: chapterId, level: sentLevel(values.level), isChanged: isChanged},
			() => {
				notify.success("Cập nhật câu hỏi thành công!");
				navigate("/question-list")
			},
			() => {
				notify.error("Lỗi cập nhật câu hỏi!");
			}
		).then(() => {});
	};
	return (
		<Skeleton active loading={infoLoading}>
			<div className="question-edit">
				<div className="question-edit-title">Chỉnh sửa câu hỏi</div>
				<span style={{color: 'var(--hust-color)', fontSize: '12pt', fontStyle: 'italic'}}>Lưu ý (***): Sau khi chỉnh sửa, một phiên bản câu hỏi mới được tạo ra để không làm ảnh hưởng đến các đề thi đã được tạo có sử dụng phiên bản câu hỏi hiện tại. <br/>
					Vì vậy, để sử dụng phiên bản mới của câu hỏi trong các đề thi tạo sau thời điểm chỉnh sửa, vui lòng thêm phiên bản mới và bỏ phiên bản cũ trong ngân hàng câu hỏi của kỳ thi.</span>
				<div className="question-subject-chapter">
					<div className="question-subject">
						<span className="question-select-title">
							Học phần:{" "}
						</span>
						<Select
							showSearch
							placeholder="Select a subject"
							optionFilterProp="children"
							filterOption={(input, option) =>
								(option?.label ?? "").includes(input)
							}
							defaultValue={questionInfo.subjectId}
							optionLabelProp="label"
							options={subjectOptions}
							onChange={subjectOnChange}
							loading={subLoading}
						/>
					</div>
					<div className="question-subject question-chapter">
						<span className="question-select-title">Chương: </span>
						<Select
							showSearch
							placeholder="Select a chapter"
							value={chapterId}
							optionFilterProp="children"
							filterOption={(input, option) =>
								(option?.label ?? "").includes(input)
							}
							optionLabelProp="label"
							options={chapterOptions}
							onChange={chapterOnchange}
							loading={chapterLoading || infoLoading}
						/>
					</div>
				</div>
				<Form
					name="question-edit"
					className="question-form"
					onFinish={onFinish}
					initialValues={{
						content: questionInfo.content,
						lstAnswer: questionInfo.lstAnswer
							? questionInfo.lstAnswer
							: [],
						level: questionInfo.level,
						isMultipleAnswers: questionInfo?.isMultipleAnswers
					}}
				>
					<div className="topicText-level">
						<Form.Item
							className="topic-text"
							label="Câu hỏi: "
							name="content"
							rules={[
								{
									required: true,
									message: "Chưa nhập câu hỏi!",
								},
							]}
						>
							<ReactQuill
								theme="snow"
								modules={modules}
								formats={formats}
								bounds="#root"
								placeholder="Nhập câu hỏi..."
								onChange={() => {
									setIsChanged(true);
								}}
							/>
						</Form.Item>
						<div className= "question-level-multiple-ans">
							<Form.Item
								className="level"
								label="Mức độ"
								name="level"
								rules={[
									{
										required: true,
										message: "Chưa chọn mức độ!",
									},
								]}
							>
								<Select
									placeholder="Chọn mức độ"
									options={levelOption}
									onChange={() => setIsChanged(true)}
									style={{ height: 45 }}
								></Select>
							</Form.Item>
							<Form.Item
								className="is-multiple-ans"
								label= "Nhiều đáp án:"
								name="isMultipleAnswers"
								rules={[
									{
										required: true,
										message: "Chưa chọn cấu hình nhiều đáp án"
									}
								]}
							>
								<Switch
									onChange={(e) => {
										setIsChanged(true);
										setIsMultipleAnswers(e);
									}}
								/>
								{!isMultipleAnswers && numCheckedAns > 1 && <div style={{color: "#FF4D4F"}}>Có nhiều hơn một đáp án đúng</div>}
							</Form.Item>
						</div>
					</div>
					<Form.List name={"lstAnswer"}>
						{(childFields, childListOperations) => (
							<div className="answers">
								{childFields.map((childField, childIndex) => (
									<div
										key={`frAnswers${childIndex}`}
										name={[
											childField.name,
											`frAnswers${childIndex}`,
										]}
										className="answer-list"
									>
										<div className="answer-list-text-checkbox">
											<Form.Item
												{...childField}
												name={[
													childField.name,
													`isCorrect`,
												]}
												key={`isCorrect${childIndex}`}
												valuePropName="checked"
											>
												<Checkbox
													checked={checked}
													onChange={onChange}
												/>
											</Form.Item>
											<Form.Item
												{...childField}
												name={[
													childField.name,
													`content`,
												]}
												key={`content${childIndex}`}
												rules={[
													{
														required: true,
														message:
															"Chưa nhập câu trả lời!",
													},
												]}
												className="answers-item"
											>
												<ReactQuill
													theme="snow"
													modules={modules}
													formats={formats}
													bounds="#root"
													placeholder="Nhập câu trả lời..."
													onChange={() => {
														setIsChanged(true);
													}}
												/>
											</Form.Item>
											<Button
												type="dashed"
												onClick={() =>{
													setIsChanged(true);
													childListOperations.remove(childIndex);
												}}
												icon={<DeleteOutlined />}
											/>
										</div>
									</div>
								))}
								{childFields.length < 6 && (
									<Form.Item className="add-answer-btn">
										<Button
											onClick={() => {
												setIsChanged(true);
												childListOperations.add();
											}}
											icon={<PlusOutlined />}
										>
											Thêm tùy chọn
										</Button>
									</Form.Item>
								)}
							</div>
						)}
					</Form.List>
					<Form.Item>
						<Button
							type="primary"
							htmlType="submit"
							style={{ width: 100, height: 40 }}
							disabled={!isChanged}
						>
							Lưu
						</Button>
					</Form.Item>
				</Form>
			</div>
		</Skeleton>
	);
};
export default QuestionEdit;
