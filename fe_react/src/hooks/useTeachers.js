import { useState } from "react";
import { getPagingTeachersService } from "../services/teachersServices";
import useNotify from "./useNotify";

const useTeachers = () => {
	const notify = useNotify();
	const [allTeachers, setAllTeachers] = useState([]);
	const [tableTeacherLoading, setTableTeacherLoading] = useState(true);
	const [paginationTeacher, setPaginationTeacher] = useState({});

	const getAllTeachers = (params) => {
		setTableTeacherLoading(true);
		getPagingTeachersService(
			params.search,
			params.page,
			params.size,
			params.sort,
			(res) => {
				setAllTeachers(res?.data?.content);
				setPaginationTeacher({
					current: res?.data?.pageable.pageNumber + 1,
					pageSize: res?.data?.pageable.pageSize,
					total: res?.data?.totalElements,
				});
				setTableTeacherLoading(false);
			},
			(err) => {
				setTableTeacherLoading(false);
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
		allTeachers,
		getAllTeachers,
		tableTeacherLoading,
		paginationTeacher,
	};
};

export default useTeachers;
