import {useEffect, useState} from "react";
import {Form, Button, Checkbox, Select, Switch} from "antd";
import {PlusOutlined, DeleteOutlined} from "@ant-design/icons";
import ReactQuill, {Quill} from "react-quill";
import "react-quill/dist/quill.snow.css";
import ImageResize from "quill-image-resize-module-react";
import "./AddQuestions.scss";
import {addQuestionService} from "../../../services/questionServices";
import useNotify from "../../../hooks/useNotify";
import useCombo from "../../../hooks/useCombo";
import katex from "katex";
import "katex/dist/katex.min.css";

window.katex = katex;
Quill.register("modules/imageResize", ImageResize);
const AddQuestions = () => {
    const [checked, setChecked] = useState(false);
    const [loading, setLoading] = useState(false);
    const [formKey, setFormKey] = useState(0);
    const [preChapter, setPreChapter] = useState(null);
    const [value, setValue] = useState("");
    const [errorStates, setErrorStates] = useState([]);
    const [mapNumCheckedAnswer, setMapNumCheckedAnswer] = useState(new Map([[0, 0]]));
    const [mapIsMultipleAnswer, setMapIsMultipleAnswer] = useState(new Map([[0, true]]));
    const [counterRerender, setCounterRerender] = useState(0);
    const {
        chapterLoading,
        subLoading,
        allChapters,
        allSubjects,
        getAllChapters,
        getAllSubjects,
    } = useCombo();
    const [subjectId, setSubjectId] = useState(null);
    const [chapterId, setChapterId] = useState(null);
    const modules = {
        toolbar: [
            ["bold", "italic", "underline"], // toggled buttons
            ["blockquote", "code-block"],
            [{list: "ordered"}, {list: "bullet"}],
            [{script: "sub"}, {script: "super"}], // superscript/subscript
            [{align: []}],
            ["link", "image", "formula"],
            [{indent: "-1"}, {indent: "+1"}], // outdent/indent
            ["clean"]
        ],

        clipboard: {
            matchVisual: false,
        },
        imageResize: {
            parchment: Quill.import("parchment"),
            modules: ["Resize", "DisplaySize", "Toolbar"]
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
        return {value: item.id, label: item.name};
    });
    const chapterOptions = allChapters.map((item) => {
        return {value: item.id, label: item.name};
    });
    const subjectOnChange = (value) => {
        setSubjectId(value);
        setChapterId(null);
        setFormKey((prevKey) => prevKey + 1);
    };
    const chapterOnchange = (values) => {
        if (preChapter !== values) {
            setPreChapter(values);
            setFormKey((prevKey) => prevKey + 1);
        }
        setChapterId(values);
    };
    const handleClickButtonAddQuestion = (childListOperations) => {
        childListOperations.add()
    }
    const notify = useNotify();
    const onFinish = (values) => {
        const isValid =
            values.lstQuestion &&
            values.lstQuestion.every((question, questionIndex) => {
                const isQuestionValid = question.lstAnswer.some(
                    (answer) => answer.isCorrect
                );
                const updatedErrorStates = [...errorStates];
                updatedErrorStates[questionIndex] = !isQuestionValid;
                setErrorStates(updatedErrorStates);
                return isQuestionValid;
            });

        // check config multiple answers
        let isMultipleAnsValid = [...mapNumCheckedAnswer.entries()].every(([key, value]) => {
            let isMultiAns = mapIsMultipleAnswer.get(key) !== undefined ? mapIsMultipleAnswer.get(key) : true;
            return isMultiAns || (!isMultiAns && value <= 1);
        });

        if (isValid && isMultipleAnsValid) {
            setLoading(true);
            values.lstQuestion &&
            values.lstQuestion.length > 0 &&
            addQuestionService(
                {
                    chapterId: chapterId,
                    lstQuestion: values.lstQuestion.map((item) => {
                        return {
                            ...item,
                            imageId: null,
                            lstAnswer: item.lstAnswer.map((answer) => {
                                let isCorrectBol = answer.isCorrect;
                                return {
                                    content: answer.content,
                                    isCorrect: isCorrectBol ?? false,
                                    imageId: null,
                                };
                            }),
                        };
                    }),
                },
                () => {
                    setLoading(false);
                    notify.success("Thêm câu hỏi thành công!");
                    setFormKey((prevKey) => prevKey + 1);
                },
                () => {
                    setLoading(false);
                    notify.error("Lỗi thêm mới câu hỏi!");
                }
            );
        }
    };
    // level question create / update question
    const levelOptions = [
        {
            value: "EASY",
            label: "Dễ",
        },
        {
            value: "MEDIUM",
            label: "Trung bình",
        },
        {
            value: "HARD",
            label: "Khó",
        },
    ];

    return (
        <div className="question-add">
            <div className="question-add-header">Thêm câu hỏi</div>
            <div className="question-subject-chapter">
                <div className="question-subject">
                    <span style={{fontWeight: 600}}>Học phần: </span>
                    <Select
                        allowClear
                        showSearch
                        placeholder="Chọn học phần"
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            (option?.label ?? "").includes(input)
                        }
                        optionLabelProp="label"
                        options={subjectOptions}
                        onChange={subjectOnChange}
                        loading={subLoading}
                    />
                </div>
                <div className="question-subject question-chapter">
                    <span style={{fontWeight: 600}}>Chương: </span>
                    <Select
                        showSearch
                        placeholder="Chọn chương"
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            (option?.label ?? "").includes(input)
                        }
                        optionLabelProp="label"
                        options={chapterOptions}
                        onChange={chapterOnchange}
                        loading={chapterLoading}
                        value={chapterId}
                    />
                </div>
            </div>
            <Form onFinish={onFinish} name="question-form" key={formKey}>
                <Form.List name="lstQuestion">
                    {(parentFields, parentListOperations) => (
                        <>
                            {parentFields.map((parentField, parentIndex) => (
                                <div
                                    key={parentIndex}
                                    className="question-list"
                                    name={[parentField.name, `fragQuetion${parentField.key}`,]}
                                >
                                    <div className="question-text">
                                        <Form.Item
                                            className="topic-Text"
                                            {...parentField}
                                            label={`Câu ${parentIndex + 1}:`}
                                            name={[parentField.name, `content`]}
                                            rules={[{required: true, message: "Chưa nhập câu hỏi!"}]}
                                            key={`${parentIndex}-${parentField.key}-question-text`}
                                        >
                                            <ReactQuill
                                                className="question-content-text"
                                                theme="snow"
                                                modules={modules}
                                                formats={formats}
                                                bounds="#root"
                                                placeholder="Nhập câu hỏi..."
                                            />
                                        </Form.Item>
                                        <div className="question-level-multiple-ans">
                                            <Form.Item
                                                {...parentField}
                                                label={"Mức độ"}
                                                name={[parentField.name, `level`]}
                                                rules={[{required: true, message: "Chưa chọn mức độ câu hỏi!"}]}
                                                initialValue={"EASY"}
                                                key={`${parentIndex}-${parentField.key}-question-level`}
                                            >
                                                <Select
                                                    options={levelOptions}
                                                    style={{width: 120}}
                                                ></Select>
                                            </Form.Item>
                                            <Form.Item
                                                {...parentField}
                                                label="Nhiều đáp án"
                                                name={[parentField.name, `isMultipleAnswers`]}
                                                rules={[{required: true, message: "Chưa chọn cấu hình nhiều đáp án!"}]}
                                                initialValue={true}
                                                key={`${parentIndex}-${parentField.key}-question-multiple-ans`}
                                            >
                                                <Switch
                                                    onChange={(value) => {
                                                        setMapIsMultipleAnswer((prev) => {
                                                            prev.set(parentIndex, value);
                                                            return prev;
                                                        });
                                                        setCounterRerender(counterRerender + 1);
                                                    }}/>
                                                {counterRerender > 0 && !mapIsMultipleAnswer.get(parentIndex) && mapNumCheckedAnswer.get(parentIndex) > 1 &&
                                                    <div style={{color: "#FF4D4F"}}>Có nhiều hơn một đáp án đúng</div>}
                                            </Form.Item>
                                        </div>
                                        <div className="btn-remove">
                                            <Button
                                                type="dashed"
                                                onClick={() => parentListOperations.remove(parentIndex)}
                                                icon={<DeleteOutlined/>}
                                            ></Button>
                                        </div>
                                    </div>
                                    <Form.List
                                        {...parentField}
                                        name={[parentField.name, `lstAnswer`]}
                                        initialValue={[{content: "", isCorrect: undefined}, {
                                            content: "",
                                            isCorrect: undefined
                                        }]}
                                    >
                                        {(childFields, childListOperations) => {
                                            return (
                                                <div className="answers">
                                                    {childFields.map((childField, childIndex) => {
                                                            return (
                                                                <div
                                                                    key={`${childIndex}-${parentIndex}`}
                                                                    name={[childField.name, `frAnswers${childField.key}`,]}
                                                                    className="answer-list"
                                                                >
                                                                    <div className="answer-list-text-checkbox">
                                                                        <div className="answer-checkbox">
                                                                            <Form.Item
                                                                                {...childField}
                                                                                name={[childField.name, `isCorrect`]}
                                                                                valuePropName="checked"
                                                                                key={`${childIndex}-${parentIndex}-answer-checkbox`}
                                                                            >
                                                                                <Checkbox
                                                                                    checked={checked}
                                                                                    onChange={(e) => {
                                                                                        setChecked(e.target.checked);
                                                                                        let currentNumCheckedAns = mapNumCheckedAnswer.get(parentIndex) ? mapNumCheckedAnswer.get(parentIndex) : 0;
                                                                                        if (e?.target?.checked) {
                                                                                            currentNumCheckedAns = currentNumCheckedAns + 1;
                                                                                        } else {
                                                                                            currentNumCheckedAns = currentNumCheckedAns - 1;
                                                                                        }
                                                                                        // change count checked value to map
                                                                                        setMapNumCheckedAnswer((prev) => {
                                                                                            prev.set(parentIndex, currentNumCheckedAns);
                                                                                            return prev;
                                                                                        });
                                                                                        setCounterRerender(counterRerender + 1);
                                                                                    }}
                                                                                />
                                                                            </Form.Item>
                                                                            <Form.Item
                                                                                {...childField}
                                                                                name={[childField.name, `content`,]}
                                                                                rules={[{
                                                                                    required: true,
                                                                                    message: "Chưa điền câu trả lời"
                                                                                }]}
                                                                                className="answers-item"
                                                                                key={`${childIndex}-${parentIndex}-answer-text`}
                                                                            >
                                                                                <ReactQuill
                                                                                    theme="snow"
                                                                                    modules={modules}
                                                                                    formats={formats}
                                                                                    value={value}
                                                                                    onChange={(value) => setValue(value)}
                                                                                    bounds="#root"
                                                                                    placeholder="Nhập câu trả lời..."
                                                                                />
                                                                            </Form.Item>
                                                                        </div>
                                                                        <div className="remove-answer">
                                                                            <Button
                                                                                type="dashed"
                                                                                onClick={() => childListOperations.remove(childIndex)}
                                                                                icon={<DeleteOutlined/>}
                                                                            />
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            );
                                                        }
                                                    )}
                                                    {childFields.length < 6 && (
                                                        <Form.Item className="add-answer-btn">
                                                            <Button
                                                                onClick={() => handleClickButtonAddQuestion(childListOperations)}
                                                                icon={<PlusOutlined/>}
                                                            >
                                                                Thêm tùy chọn
                                                            </Button>
                                                        </Form.Item>
                                                    )}
                                                </div>
                                            )
                                        }}
                                    </Form.List>
                                    <span
                                        style={{color: "red", display: errorStates[parentIndex] ? "block" : "none"}}
                                    >
                    Chưa chọn đáp án đúng cho câu hỏi!
                  </span>
                                </div>
                            ))}
                            <Form.Item className="add-question-btn">
                                <Button
                                    onClick={() => {
                                        parentListOperations.add();
                                    }}
                                    icon={<PlusOutlined/>}
                                    disabled={chapterId === null}
                                >
                                    Thêm mới
                                </Button>
                            </Form.Item>
                        </>
                    )}
                </Form.List>
                <Form.Item className="add-btn">
                    <Button
                        type="primary"
                        htmlType="submit"
                        style={{width: 150, height: 50}}
                        loading={loading}
                    >
                        Lưu
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
};

export default AddQuestions;
