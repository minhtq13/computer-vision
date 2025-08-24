import {Select, Space} from "antd";
import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useLocation} from 'react-router-dom';
import iconArrow from "../../assets/images/svg/arrow-under-header.svg";
import useCombo from "../../hooks/useCombo";
import {setExamClassCode} from "../../redux/slices/appSlice";
import {testTypeEnum} from "../../utils/constant";

const {Option} = Select;

const AutoScoringHeaderSelect = ({onSelectExamClass}) => {
    const location = useLocation();
    const {getAllSemesters, allSemester, allSubjects, getAllViewableSubject, getAllExamClass, examClass} =
        useCombo();
    const [semesterSelected, setSemesterSelected] = useState(null);
    const [subjectSelected, setSubjectSelected] = useState(null);
    const dispatch = useDispatch();
    const {examClassCode} = useSelector((state) => state.appReducer);
    useEffect(() => {
        getAllSemesters({});
        getAllViewableSubject({targetObject: "SCORED_EXAM_CLASS"});
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    useEffect(() => {
        if (semesterSelected && subjectSelected) {
            getAllExamClass(semesterSelected, subjectSelected, testTypeEnum.OFFLINE.value, "", {});
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [semesterSelected, subjectSelected]);

    const handleChangeSemestersSelect = (value) => {
        setSubjectSelected(null);
        setSemesterSelected(value);
        dispatch(setExamClassCode(null));
    };
    const handleChangeSubjectSelect = (value) => {
        dispatch(setExamClassCode(null));
        setSubjectSelected(value);
    };
    const handleChangeExamClassSelect = (value) => {
        dispatch(setExamClassCode(examClass.findLast(item => item?.id === value)?.code));
        onSelectExamClass(value);
    };
    useEffect(() => {
        dispatch(setExamClassCode(null));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [location]);
    const filterSubject = (input, option) => (option?.label ?? "").toLowerCase().includes(input.toLowerCase());
    return (
        <div>
            <div className="block-select">
                <div className="info">
                    {!semesterSelected || !subjectSelected || !examClassCode
                        ? `Vui lòng chọn các thông tin lớp thi để chấm điểm!`
                        : ""}
                </div>
                <div className="block-button">
                    <Space>
                        <div className="detail-button">Học kỳ:</div>
                        <Select
                            optionLabelProp="label"
                            suffixIcon={<img src={iconArrow} alt=""/>}
                            className="custom-select-antd"
                            placeholder="Chọn học kỳ"
                            onChange={handleChangeSemestersSelect}
                            style={{width: 200}}
                            value={semesterSelected}
                        >
                            {allSemester.map((item, index) => {
                                return (
                                    <Option value={item.id} label={item.name} key={index}>
                                        <div className="d-flex item_DropBar dropdown-option">
                                            <div className="dropdown-option-item text-14">{item.name}</div>
                                        </div>
                                    </Option>
                                );
                            })}
                        </Select>
                    </Space>

                    <Space>
                        <div className="detail-button">Học phần:</div>
                        <Select
                            optionLabelProp="label"
                            onChange={handleChangeSubjectSelect}
                            className="custom-select-antd"
                            suffixIcon={<img src={iconArrow} alt=""/>}
                            style={{width: 350}}
                            placeholder="Chọn học phần"
                            showSearch
                            filterOption={filterSubject}
                            value={subjectSelected}
                        >
                            {allSubjects.map((item, index) => {
                                return (
                                    <Option value={item.id} label={`${item?.name} - ${item?.code}`} key={index}>
                                        <div className="d-flex item_DropBar dropdown-option">
                                            <div
                                                className="dropdown-option-item text-14">{`${item?.name} - ${item?.code}`}</div>
                                        </div>
                                    </Option>
                                );
                            })}
                        </Select>
                    </Space>
                    <Space>
                        <div className="detail-button">Mã lớp thi:</div>
                        <Select
                            optionLabelProp="label"
                            onChange={handleChangeExamClassSelect}
                            className="custom-select-antd"
                            suffixIcon={<img src={iconArrow} alt=""/>}
                            style={{width: 250}}
                            placeholder="Chọn mã lớp thi để chấm"
                            showSearch
                            disabled={!semesterSelected || !subjectSelected}
                            // value={examClassCode}
                        >
                            {examClass.map((item, index) => {
                                return (
                                    <Option value={item?.id} label={item?.code} key={index}>
                                        <div className="d-flex item_DropBar dropdown-option">
                                            <div className="dropdown-option-item text-14">{item.code}</div>
                                        </div>
                                    </Option>
                                );
                            })}
                        </Select>
                    </Space>
                </div>
            </div>
        </div>
    );
};

export default AutoScoringHeaderSelect;
