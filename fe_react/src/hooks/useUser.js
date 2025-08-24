import {useState} from "react";
import useNotify from "./useNotify";
import {getAllAdminService} from "../services/userService";

const useUser = () => {

    const [allAdmins, setAllAdmins] = useState([]);
    const [tableAdminLoading, setTableAdminLoading] = useState(false);
    const [paginationAdmin, setPaginationAdmin] = useState({});
    const notify = useNotify();

    const getAllAdmin = (params) => {
        setTableAdminLoading(true);
        getAllAdminService(
            params,
            (res) => {
                setAllAdmins(res?.data?.content);
                setPaginationAdmin({
                    current: res?.data?.pageable.pageNumber + 1,
                    pageSize: res?.data?.pageable.pageSize,
                    total: res?.data?.totalElements,
                });
                setTableAdminLoading(false);
            },
            (err) => {
                setTableAdminLoading(false);
                if (err?.response?.status === 404) {
                    notify.warning(
                        err.response.data.message || "Không có thông tin trong hệ thống!"
                    );
                }
            }
        ).then(() => {});
    };

    return {
        allAdmins,
        getAllAdmin,
        tableAdminLoading,
        paginationAdmin
    }

}
export default useUser;