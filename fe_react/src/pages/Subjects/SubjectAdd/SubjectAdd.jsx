import React, { useState } from "react";
import useNotify from "../../../hooks/useNotify";
import { addSubjectsService } from "../../../services/subjectsService";
import UpdateSubjectInfoForm from "../components/UpdateSubjectInfoForm/UpdateSubjectInfoForm";
import { useNavigate } from "react-router-dom";
const SubjectAdd = () => {
  const [loading, setLoading] = useState(false);
  const notify = useNotify();
  const navigate = useNavigate()
  const onFinish = (values) => {
    setLoading(true);
    addSubjectsService(
        values,
        (res) => {
          setLoading(false);
          notify.success("Thêm học phần thành công!");
          navigate("/subject-list")
        },
        (error) => {
          setLoading(false);
          notify.error("Lỗi thêm học phần!");
         }
    ).then(r => {});
  };
  return (
    <UpdateSubjectInfoForm
      chaptersVisible={false}
      infoHeader="Thêm học phần"
      btnText="Thêm"
      initialValues={{ remember: false }}
      onFinish={onFinish}
      loading={loading}
    />
  );
};
export default SubjectAdd;
