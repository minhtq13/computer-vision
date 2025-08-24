import { useState } from "react";
import { getPagingStudentsService } from "../services/studentsService";
import useNotify from "./useNotify";

const useStudents = () => {
	const notify = useNotify();
	const [allStudents, setAllStudents] = useState([]);
	const [tableStudentLoading, setTableStudentLoading] = useState(true);
	const [paginationStudent, setPaginationStudent] = useState({});

	const getAllStudents = (params) => {
		setTableStudentLoading(true);
		getPagingStudentsService(
			params.search,
			params.page,
			params.courseNums && params.courseNums.length > 0
				? params.courseNums.join(",")
				: null,
			params.size,
			params.sort,
			(res) => {
				setAllStudents(res?.data?.content);
				setPaginationStudent({
					current: res?.data?.pageable.pageNumber + 1,
					pageSize: res?.data?.pageable.pageSize,
					total: res?.data?.totalElements,
				});
				setTableStudentLoading(false);
			},
			(err) => {
				setTableStudentLoading(false);
				if (err?.response?.status === 404) {
					notify.warning(
						err?.response?.data?.message ||
						"Không có thông tin trong cơ sở dữ liệu!"
					);
				}
			}
		).then(() => {});
	};

	return {
		allStudents,
		getAllStudents,
		tableStudentLoading,
		paginationStudent,
	};
};

export default useStudents;
