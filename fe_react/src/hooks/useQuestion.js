import { useState } from "react";
import {
	deleteQuesionsService,
	getQuestionDetailsService,
	getQuestionService,
} from "../services/questionServices";
import useNotify from "./useNotify";
import {processApiError} from "../utils/apiUtils";

const useQuestions = () => {
	const [allQuestions, setAllQuestions] = useState([]);
	const [totalSize, setTotalSize] = useState(0);
	const [quesLoading, setQuesLoading] = useState(true);
	const [questionInfo, setQuestionInfo] = useState({});
	const [infoLoading, setInfoLoading] = useState(true);
	const [deleteLoading, setDeleteLoading] = useState(false);
	const notify = useNotify();
	const getAllQuestions = (payload) => {
		setQuesLoading(true);
		getQuestionService(
			payload.subjectId,
			payload.subjectCode,
			payload.chapterCode,
			payload.chapterIds && payload.chapterIds.length > 0
				? payload.chapterIds.join(",")
				: null,
			payload.level,
			payload.search,
			payload.testId,
			payload?.fetchSize,
			(res) => {
				setAllQuestions(res.data?.questions);
				setTotalSize(res?.data?.totalSize);
				setQuesLoading(false);
			},
			(error) => {
				notify.error("Lỗi lấy danh sách câu hỏi!");
				setQuesLoading(true);
			}
		).then(() => {});
	};

	const getQuestionDetail = (payload, questionId) => {
		setInfoLoading(true);
		getQuestionDetailsService(
			questionId,
			payload,
			(res) => {
				setQuestionInfo(res.data);
				setInfoLoading(false);
			},
			() => {
				setInfoLoading(false);
				notify.error("Không thể lấy thông tin câu hỏi!");
			}
		).then(() => {});
	};

	const deleteQuestion = (questionId, payload) => {
		setDeleteLoading(true);
		deleteQuesionsService(
			questionId,
			payload,
			() => {
				setDeleteLoading(false);
				notify.success("Xóa câu hỏi thành công!");
			},
			(error) => {
				setDeleteLoading(false);
				notify.error(processApiError(error));
			}
		).then(() => {});
	};
	return {
		allQuestions,
		totalSize,
		getAllQuestions,
		quesLoading,
		getQuestionDetail,
		questionInfo,
		infoLoading,
		deleteQuestion,
		deleteLoading,
	};
};
export default useQuestions;
