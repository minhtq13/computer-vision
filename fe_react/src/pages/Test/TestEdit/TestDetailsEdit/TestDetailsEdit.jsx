import "./TestDetailsEdit.scss"
import React, {useEffect, useState} from "react";
import ReactQuill from "react-quill";
import {Button, Checkbox, Empty, Input, Select, Spin, Tag} from "antd";
import {renderTag, tagRender} from "../../../../utils/tools";
import ScrollToTop from "../../../../components/ScrollToTop/ScrollToTop";
import debounce from "lodash.debounce";
import {levelIntOptions, searchTimeDebounce} from "../../../../utils/constant";
import useTest from "../../../../hooks/useTest";
import {regexPattern} from "../../../../common/regexPattern";
import useNotify from "../../../../hooks/useNotify";


const TestDetailsEdit = ({testId, initialValues}) => {

    const initialParam = {
        testId: testId
    };
    const notify = useNotify();
    const {getListQuestionAllowedInTest, testQuestionLoading, allAllowedQuestions} = useTest();
    const {updateTest, updateTestLoading} = useTest();
    const [param, setParam] = useState(initialParam);
    const [checkedQuestions, setCheckedQuestions] = useState(new Set([]));
    const [renderQuestions, setRenderQuestions] = useState(allAllowedQuestions);

    // for filter
    const [keyword, setKeyword] = useState("");
    const [level, setLevel] = useState(-1);
    const [selectedType, setSelectedType] = useState(-1);
    const [needReRender, setNeedReRender] = useState(false);
    const [questionBank, setQuestionBank] = useState({easy: [], medium: [], hard: []});

    // form edit test
    const errorMessage = "Không đủ số lượng câu hỏi trong ngân hàng!"
    const [questionQuantity, setQuestionQuantity] = useState(initialValues?.questionQuantity);
    const [easyNumber, setEasyNumber] = useState(initialValues?.numEasyQuestion);
    const [mediumNumber, setMediumNumber] = useState(initialValues?.numMediumQuestion);
    const [hardNumber, setHardNumber] = useState(initialValues?.numHardQuestion);
    const [duration, setDuration] = useState(initialValues?.duration);
    const [totalPoint, setTotalPoint] = useState(initialValues?.totalPoint);

    const onSearch = (value, _e) => {
        const newKeyword = (value ?? "").toLowerCase();
        setKeyword(newKeyword);
        let result = [...allAllowedQuestions].filter(item => questionFilter(item, newKeyword, level, selectedType));
        setRenderQuestions(result);
    };

    // search
    const onChange = debounce((_e) => {
        const newKeyword = (_e.target.value ?? "").toLowerCase();
        setKeyword(newKeyword);
        setRenderQuestions([...allAllowedQuestions].filter(item => questionFilter(item, newKeyword, level, selectedType)));
    }, searchTimeDebounce);

    const levelOnchange = (option) => {
        setLevel(option);
        setRenderQuestions([...allAllowedQuestions].filter(item => questionFilter(item, keyword, option, selectedType)));
    }

    const selectedTypeOnChange = (option) => {
        setSelectedType(option);
        setRenderQuestions([...allAllowedQuestions].filter(item => questionFilter(item, keyword, level, option)));
    };

    // filter on change
    const questionFilter = (item, keyword, level, selectedType) => {
        // 3 điều kiện đồng thời
        const keywordCond = (keyword === "" || item.content.toLowerCase().includes(keyword));
        const levelCond = (level === -1 || item.level === level);
        const selectedTypeCond = (selectedType === -1 || (selectedType === 0 && !checkedQuestions.has(item.id)) || (selectedType === 1 && checkedQuestions.has(item.id)));
        return keywordCond && levelCond && selectedTypeCond;
    }

    const selectedTypeOption = [
        {
            value: -1,
            label: "Tất cả"
        },
        {
            value: 0,
            label: "Chưa chọn"
        },
        {
            value: 1,
            label: "Đã chọn"
        }
    ];

    // Hook reload all questions
    useEffect(() => {
        getListQuestionAllowedInTest(param);
    }, [param]);

    useEffect(() => {
        // re-render
        setCheckedQuestions(new Set([...allAllowedQuestions].filter(item => checkedQuestions.has(item.id) || item?.isInTest).map(item => item.id)));
        setQuestionBank(Object.groupBy([...allAllowedQuestions], ({level}) => {
            return (level === 0 ? "easy" : (level === 1 ? "medium" : "hard"));
        }));
        // reset filter
        setKeyword("");
        setLevel(-1);
        setSelectedType(-1);
        setNeedReRender(true);
    }, [allAllowedQuestions]);

    // question with filter
    useEffect(() => {
        setRenderQuestions([...allAllowedQuestions].filter(item => questionRender(item, keyword, level, selectedType)));
        setNeedReRender(false);
    }, [needReRender]);

    // handle checked questions
    const checkOnChange = (e, item) => {
        let result = [...checkedQuestions];
        if (e.target.checked) {
            result.push(item.id);
        } else {
            result = result.filter(val => val !== item.id);
        }
        setCheckedQuestions(new Set([...result]));
        setNeedReRender(true);
    };

    // update onSubmit
    const updateSubmit = () => {
        let invalidCondition = duration < 0 || questionQuantity < 0 || totalPoint <= 0
            || easyNumber > questionBank?.easy?.length
            || mediumNumber > questionBank?.medium?.length
            || hardNumber > questionBank?.hard?.length;
        if (invalidCondition) {
            notify.warning("Cấu hình chưa hợp lệ!")
            return;
        }
        let body = {
            questionQuantity: questionQuantity,
            totalPoint: totalPoint,
            duration: duration,
            questionIds: [...checkedQuestions],
            generateConfig: {
                numTotalQuestion: questionQuantity,
                numEasyQuestion: easyNumber,
                numMediumQuestion: mediumNumber,
                numHardQuestion: hardNumber
            }
        };
        updateTest(testId, body);
    }

    const questionRender = (item, index) => {
        return (
            <div
                className="question-items"
                key={`index-${item.id}`}
            >
                <div className="topic-level">
                    <div className="question-topic">
                        <div className="question-number">
                            <Checkbox checked={checkedQuestions.has(item.id)} onChange={(e) => checkOnChange(e, item)}/>
                            {` Câu ${index + 1}: `}
                            <Tag className="mr-0" color={item?.isNewest ? "green" : "volcano"}>
                                {item?.isNewest ? "Mới nhất" : "Cũ"}
                            </Tag>
                        </div>
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
                <p className="italic" style={{color: 'var(--hust-color)'}}>Các phương án trả lời:</p>
                {item.lstAnswer &&
                    item.lstAnswer.map((ans, ansNo) => {
                        return (
                            <div
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
        )
    }

    return (
        <div className="test-details-edit">
            <p className="header-config">CẤU HÌNH ĐỀ THI</p>
            <Spin spinning={updateTestLoading}>
                <div className="test-edit-config">
                    <div className="manual-select">
                        <div className="manual-test-left">
                            <div className="manual-item manual-duration">
                            <span className="manual-item-label">
                              Thời gian thi (phút):{" "}
                            </span>
                                <Input
                                    type="text"
                                    pattern={regexPattern.NUMBER_REGEX}
                                    placeholder="Nhập thời gian thi"
                                    onChange={(_e) => setDuration(_e.target.value)}
                                    defaultValue={duration}
                                />
                            </div>
                            <div className="manual-item manual-totalQues">
                                <span className="manual-item-label">Số câu hỏi: </span>
                                <div className="manual-config-item">
                                    <Input
                                        type="text"
                                        pattern={regexPattern.NUMBER_REGEX}
                                        placeholder="Nhập số câu hỏi"
                                        onChange={(_e) => setQuestionQuantity(_e.target.value)}
                                        required={true}
                                        defaultValue={questionQuantity}
                                    />
                                    {questionQuantity > allAllowedQuestions.length &&
                                        <div className="error-message">{errorMessage}</div>}
                                </div>
                            </div>
                            <div className="manual-item manual-totalPoint">
                                <span className="manual-item-label">Tổng điểm:</span>
                                <div className="manual-config-item">
                                    <Input
                                        type="text"
                                        pattern={regexPattern.NUMBER_REGEX}
                                        placeholder="Nhập tổng điểm:"
                                        onChange={(_e) => setTotalPoint(_e.target.value)}
                                        required={true}
                                        defaultValue={totalPoint}
                                    />
                                    {totalPoint <= 0 && <div className="error-message">{"Điểm tổng phải > 0"}</div>}
                                </div>
                            </div>
                        </div>
                        <div className="manual-test-right">
                            <div className="manual-config-item">
                                <div className="manual-item">
                                    <span className="manual-item-label">Phân loại:</span>
                                    <div className="manual-config-details">
                                        <div className="manual-config-item">
                                            <Input
                                                type="text"
                                                pattern={regexPattern.NUMBER_REGEX}
                                                placeholder="Số câu dễ"
                                                onChange={(_e) => setEasyNumber(_e.target.value)}
                                                defaultValue={easyNumber}
                                            />
                                            <span>câu</span>
                                            <Tag color={tagRender(0)}>
                                                {renderTag({level: 0})}
                                            </Tag>
                                            {easyNumber > questionBank?.easy?.length &&
                                                <div className="error-message">{errorMessage}</div>}
                                        </div>
                                        <div className="manual-config-item">
                                            <Input
                                                type="text"
                                                pattern={regexPattern.NUMBER_REGEX}
                                                placeholder="Số câu trung bình"
                                                onChange={(_e) =>
                                                    setMediumNumber(_e.target.value)
                                                }
                                                defaultValue={mediumNumber}
                                            />
                                            <span>câu</span>
                                            <Tag color={tagRender(1)}>
                                                {renderTag({level: 1})}
                                            </Tag>
                                            {mediumNumber > questionBank?.medium?.length &&
                                                <div className="error-message">{errorMessage}</div>}
                                        </div>
                                        <div className="manual-config-item">
                                            <Input
                                                type="text"
                                                pattern={regexPattern.NUMBER_REGEX}
                                                placeholder="Số câu khó"
                                                onChange={(_e) =>
                                                    setHardNumber(_e.target.value)
                                                }
                                                defaultValue={hardNumber}
                                            />
                                            <span>câu</span>
                                            <Tag color={tagRender(2)}>
                                                {renderTag({level: 2})}
                                            </Tag>
                                        </div>
                                        {hardNumber > questionBank?.hard?.length &&
                                            <div className="error-message">{errorMessage}</div>}
                                        {Number(questionQuantity) !==
                                            Number(easyNumber) +
                                            Number(mediumNumber) +
                                            Number(hardNumber) &&
                                            easyNumber &&
                                            mediumNumber &&
                                            hardNumber && (
                                                <div className="error-message">
                                                    Tổng số câu dễ, trung bình, khó phải
                                                    bằng tổng số câu hỏi.
                                                </div>
                                            )}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="btn-space">
                        <Button type="primary" loading={updateTestLoading} onClick={updateSubmit}>Cập nhật</Button>
                    </div>
                </div>
            </Spin>
            <p className="header-config">NGÂN HÀNG CÂU HỎI</p>
            <div className="question-list">
                <div className="search-level">
                    <div className="list-search">
                        <span className="list-search-filter-label">Tìm kiếm:</span>
                        <Input.Search placeholder="Nhập nội dung câu hỏi" enterButton onSearch={onSearch} allowClear
                                      onChange={onChange}/>
                    </div>
                    <div className="question-level">
                        <span className="list-search-filter-label">Mức độ:</span>
                        <Select
                            className="select-level-q"
                            defaultValue={-1}
                            optionLabelProp="label"
                            options={levelIntOptions}
                            onChange={levelOnchange}
                        />
                    </div>
                    <div className="question-selected-type">
                        <span className="list-search-filter-label">Câu hỏi:</span>
                        <Select
                            className="select-level-q"
                            defaultValue={-1}
                            optionLabelProp="label"
                            options={selectedTypeOption}
                            onChange={selectedTypeOnChange}
                        />
                    </div>
                </div>
                <div className="header-question-list">
                    <p>Đã chọn {checkedQuestions.size} / {allAllowedQuestions.length} (Dễ : {questionBank?.easy?.length},
                        Trung bình : {questionBank?.medium?.length}, Khó : {questionBank?.hard?.length}) câu hỏi hiện có
                        của học phần "{initialValues?.subjectName} - {initialValues?.subjectCode}"</p>
                </div>
                <ScrollToTop/>
                {renderQuestions.length === 0 ? <Empty
                        description="Không có dữ liệu"
                    /> :
                    <Spin className="all-question-container" spinning={testQuestionLoading} tip="Đang tải...">
                        <p className="italic" style={{color: 'var(--hust-color)'}}>Lưu ý: Các câu hỏi bao gồm phiên bản đã chọn và phiên bản mới nhất</p>
                        {renderQuestions.map((value, index) => questionRender(value, index))}
                    </Spin>
                }

            </div>
        </div>
    );

}

export default TestDetailsEdit;