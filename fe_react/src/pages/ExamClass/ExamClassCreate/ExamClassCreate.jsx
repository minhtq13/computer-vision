import dayjs from "dayjs";
import React, {useState} from "react";
import useNotify from "../../../hooks/useNotify";
import {addExamClassService} from "../../../services/examClassServices";
import UpdateExamClassInfoForm from "../components/UpdateExamClassInfoForm/UpdateExamClassInfoForm"
import "./ExamClassCreate.scss";
import {useNavigate} from "react-router-dom";
import {dateTimePattern} from "../../../utils/constant";
import {processApiError} from "../../../utils/apiUtils";

const ExamClassAdd = () => {
    const navigate = useNavigate()
    const [loading, setLoading] = useState(false);
    const [selectedTestId, setSelectedTestId] = useState(null);
    const [lstStudentId, setLstStudentId] = useState([]);
    const [lstSupervisorId, setLstSupervisorId] = useState([]);
    const [lstLecturerId, setLstLecturerId] = useState([]);
    const notify = useNotify();
    const onFinish = (value) => {
        setLoading(true);
        addExamClassService(
            {
                ...value,
                examineTime: dayjs(value.examineTime).format(
                    dateTimePattern.FORMAT_DATE_HH_MM_YYYY_HH_MM
                ),
                testId: selectedTestId,
                lstStudentId: lstStudentId,
                lstSupervisorId: lstSupervisorId,
                lstLecturerId: lstLecturerId.filter(id => lstSupervisorId.includes(id)),
                fromExamClassId: value?.fromExamClassId ? value?.fromExamClassId : null
            },
            () => {
                setLoading(false);
                notify.success("Tạo mới lớp thi thành công!");
                navigate("/exam-class-list")
            },
            (error) => {
                setLoading(false);
                notify.error(processApiError(error, "Tạo mới lớp thi không thành công!"));
            }
        ).then(() => {
        });
    };
    return (
        <div className="exam-class-add">
            <UpdateExamClassInfoForm
                infoHeader="Thêm lớp thi"
                onFinish={onFinish}
                btnText="Thêm"
                initialValues={{remember: false}}
                loading={loading}
                onSelectTestId={(id) => setSelectedTestId(id)}
                onSelectStudents={(ids) => setLstStudentId(ids)}
                onSelectTeachers={(ids) => setLstSupervisorId(ids)}
                onSelectLecturers={(ids) => setLstLecturerId(ids)}
                action="CREATE"
            />
        </div>
    );
};
export default ExamClassAdd;
