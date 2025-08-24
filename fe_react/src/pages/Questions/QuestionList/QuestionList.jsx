import {Button, Select, Spin, Tag, Input, Tooltip} from "antd";
import React, {useEffect, useRef, useState} from "react";
import useQuestions from "../../../hooks/useQuestion";
import deletePopUpIcon from "../../../assets/images/svg/delete-popup-icon.svg";
import "./QuestionList.scss";
import {DeleteOutlined, EditOutlined, FileExcelFilled, ImportOutlined} from "@ant-design/icons";
import ModalPopup from "../../../components/ModalPopup/ModalPopup";
import {useNavigate} from "react-router-dom";
import {appPath} from "../../../config/appPath";
import {setQuestionItem} from "../../../redux/slices/appSlice";
import {useDispatch} from "react-redux";
import debounce from "lodash.debounce";
import useCombo from "../../../hooks/useCombo";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import {fetchSizeOptions, levelOptions, searchTimeDebounce} from "../../../utils/constant";
import {getStaticFile, renderTag, tagRender} from "../../../utils/tools";
import ScrollToTop from "../../../components/ScrollToTop/ScrollToTop";
import addIcon from "../../../assets/images/svg/add-icon.svg";
import {apiPath} from "../../../config/apiPath";
import useImportExport from "../../../hooks/useImportExport";

const QuestionList = () => {
    const initialParam = {
        subjectId: null,
        subjectCode: null,
        chapterCode: null,
        chapterIds: [],
        search: null,
        level: "ALL",
        fetchSize: 10
    };
    const {
        allQuestions,
        totalSize,
        getAllQuestions,
        quesLoading,
        deleteQuestion,
        deleteLoading,
    } = useQuestions();
    const {
        chapterLoading,
        subLoading,
        allChapters,
        allSubjects,
        getAllChapters,
        getAllSubjects,
    } = useCombo();
    const [param, setParam] = useState(initialParam);
    const [subjectId, setSubjectId] = useState(null);
    const [chapterIds, setChapterIds] = useState([]);
    const [fetchSize, setFetchSize] = useState(10);
    const fileInputRef = useRef(null);
    const [fileList, setFileList] = useState(null);
    const {importQuestion, loadingImport} = useImportExport();
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        if (!deleteLoading) {
            getAllQuestions(param);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [param, deleteLoading, loadingImport]);

    useEffect(() => {
        getAllSubjects({subjectCode: null, subjectTitle: null});
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (subjectId) {
            getAllChapters({
                subjectId: subjectId,
                chapterCode: null,
                chapterId: null,
            });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [subjectId]);

    const subjectOptions = allSubjects.map((item) => {
        return {value: item.id, label: `${item?.name} - ${item?.code}`};
    });

    const chapterOptions = allChapters.map((item) => {
        return {value: item.id, label: item.name};
    });

    const handleImportQuestion = () => {
        const formData = new FormData();
        formData.append("file", fileList);
        importQuestion(formData);
        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
        setFileList(null);
    }

    // onChange file input handled
    const handleFileInputChange = (e) => {
        // for debugging
        setFileList(e.target.files[0]);
    };
    const subjectOnChange = (value) => {
        setSubjectId(value);
        setParam({...param, subjectId: value, chapterIds: []});
        setChapterIds([]);
    };
    const chapterOnchange = (values) => {
        if (values.includes(0)) {
            setParam({...param, chapterIds: chapterOptions.filter(item => item !== 0).map(item => item.value)})
            setChapterIds(chapterOptions.filter(item => item !== 0).map(item => item.value))
        } else {
            setParam({...param, chapterIds: values});
            setChapterIds(values);
        }
    };
    const onRemove = (id) => {
        deleteQuestion(id, null);
    };
    const onEdit = (item) => {
        navigate(`${appPath.questionEdit}/${item.id}`);
        dispatch(setQuestionItem(item));
    };
    const levelOnchange = (value) => {
        setParam({...param, level: value});
    };
    const onSearch = (value, _e) => {
        setParam({...param, search: value})
    };
    const onChange = debounce((_e) => {
        setParam({...param, search: _e.target.value})
    }, searchTimeDebounce)
    return (
        <div className="question-list">
            <div className="header-question-list">
                <p className="font-weight-bold">Ngân hàng câu hỏi</p>
            </div>
            <div className="subject-chapters-top">
                <div className="subject-chapter">
                    <div className="test-subject">
                        <span className="select-label">Học phần:</span>
                        <Select
                            allowClear
                            showSearch
                            placeholder="Chọn học phần"
                            optionFilterProp="children"
                            filterOption={(input, option) =>
                                (option?.label.toLowerCase() ?? "").includes(
                                    input.toLowerCase()
                                )
                            }
                            optionLabelProp="label"
                            options={subjectOptions}
                            onChange={subjectOnChange}
                            loading={subLoading}
                        />
                    </div>
                    <div className="test-chapters">
                        <span className="select-label">Chương:</span>
                        <Select
                            disabled={subjectId === null}
                            mode="multiple"
                            showSearch
                            allowClear
                            placeholder="Chọn chương"
                            optionFilterProp="children"
                            filterOption={(input, option) =>
                                (option?.label.toLowerCase() ?? "").includes(
                                    input.toLowerCase()
                                )
                            }
                            optionLabelProp="label"
                            options={[{value: 0, label: "Chọn tất cả"}, ...chapterOptions]}
                            onChange={chapterOnchange}
                            loading={chapterLoading}
                            value={chapterIds}
                        />
                    </div>
                </div>
                <div className="search-level">
                    <div className="list-search">
                        <span className="list-search-filter-label">Tìm kiếm:</span>
                        <Input.Search placeholder="Nhập nội dung câu hỏi" enterButton onSearch={onSearch} allowClear
                                      onChange={onChange}/>
                    </div>
                    <div className="test-level">
                        <span className="select-label">Mức độ:</span>
                        <Select
                            defaultValue={"ALL"}
                            optionLabelProp="label"
                            options={levelOptions}
                            onChange={levelOnchange}
                        />
                    </div>
                    <div className="fetch-size">
                        <span className="select-label">Số câu hỏi hiển thị:</span>
                        <Select
                            defaultValue={fetchSize}
                            optionLabelProp="label"
                            options={fetchSizeOptions.filter((item) => item?.value <= totalSize)}
                            onChange={(value) => {
                                setFetchSize(value);
                                setParam({...param, fetchSize: value});
                            }}
                        />
                        <span className="font-bold"> / {totalSize} câu</span>
                    </div>
                </div>
            </div>
            <ScrollToTop/>
            <div className="question-add-btn-container">
                <Button className="options" onClick={() => navigate(appPath.addQuestions)}>
                    <img src={addIcon} alt="Add Icon"/>
                    Thêm câu hỏi
                </Button>
                <Tooltip className="options" title="File mẫu">
                    <Button
                        type="primary"
                        onClick={() => getStaticFile(apiPath.questionImportTemplate)}
                    >
                        <FileExcelFilled style={{color: '#ffffff'}}/>
                    </Button>
                </Tooltip>
                <Input allowClear style={{maxWidth: 300}} type="file" name="file"
                       onChange={(e) => handleFileInputChange(e)}/>
                <Tooltip className="options" title="Import câu hỏi">
                    <Button
                        type="primary"
                        onClick={handleImportQuestion}
                        disabled={!fileList}
                        loading={loadingImport}
                    >
                        <ImportOutlined style={{color: '#ffffff'}}/>
                    </Button>
                </Tooltip>
            </div>
            <Spin className="all-question-container" spinning={quesLoading} tip="Đang tải...">
                <p className="italic" style={{color: 'var(--hust-color)'}}>Lưu ý: Các câu hỏi đều là phiên bản mới nhất</p>
                {allQuestions.map((item, index) => {
                    return (
                        <div
                            className="question-items"
                            key={`index-${item.id}`}
                        >
                            <div className="topic-remove">
                                <div className="topic-level">
                                    <div className="question-topic">
                                        <div className="question-number">{`Câu ${index + 1
                                        }: `}</div>
                                        <ReactQuill
                                            key={index}
                                            value={item.content}
                                            readOnly={true}
                                            theme="snow"
                                            modules={{toolbar: false}}
                                        />
                                    </div>
                                    <Tag className="font-bold" color={tagRender(item.level)}>
                                        {renderTag(item)}
                                    </Tag>
                                </div>
                                <div className="btn-space">
                                    <Button
                                        icon={<EditOutlined/>}
                                        onClick={() => onEdit(item)}
                                    />
                                    <ModalPopup
                                        buttonOpenModal={
                                            <Button icon={<DeleteOutlined/>}/>
                                        }
                                        title="Xóa câu hỏi"
                                        message="Bạn có chắc chắn muốn xóa câu hỏi này không?"
                                        confirmMessage={
                                            "Thao tác này không thể hoàn tác"
                                        }
                                        ok={"Đồng ý"}
                                        icon={deletePopUpIcon}
                                        onAccept={() => onRemove(item.id)}
                                        loading={deleteLoading}
                                    />
                                </div>
                            </div>
                            <p className="italic" style={{color: 'var(--hust-color)'}}>Các phương án trả lời:</p>
                            {item.lstAnswer &&
                                item.lstAnswer.map((ans, ansNo) => {
                                    return (<div
                                        className={ans.isCorrect ? "answer-items corrected" : "answer-items"}
                                        key={`answer${ansNo}`}>
                                        <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                                        <ReactQuill
                                            key={ansNo}
                                            value={ans.content}
                                            readOnly={true}
                                            theme="snow"
                                            modules={{toolbar: false}}
                                        />
                                    </div>);
                                })}
                        </div>
                    );
                })}
            </Spin>
        </div>
    );
};
export default QuestionList;
