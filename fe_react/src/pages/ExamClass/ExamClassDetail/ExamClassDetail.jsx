import {Button, Input, Modal, Select, Table, Tabs, Tooltip} from "antd";
import React, {useEffect, useRef, useState} from "react";
import exportIcon from "../../../assets/images/svg/export-icon.svg";
import useExamClasses from "../../../hooks/useExamClass";
import useImportExport from "../../../hooks/useImportExport";
import {
    fileStoredTypeEnum,
    HUST_COLOR,
    ROLE_STUDENT_CODE,
    searchTimeDebounce,
    studentTestStatusMap
} from "../../../utils/constant";
import {customPaginationText, getStaticFile} from "../../../utils/tools";
import ChartColumn from "./ChartColumn";
import ChartPie from "./ChartPie";
import "./ExamClassDetail.scss";
import {EditOutlined, FileExcelFilled, ImportOutlined} from "@ant-design/icons";
import {getBaseRole} from "../../../utils/roleUtils";
import {apiPath, BASE_RESOURCE_URL} from "../../../config/apiPath";
import {useLocation, useNavigate} from "react-router-dom";
import {MdAttachEmail} from "react-icons/md";
import SearchFilter from "../../../components/SearchFilter/SearchFilter";
import debounce from "lodash.debounce";
import {appPath} from "../../../config/appPath";
import EmailForm from "../../../components/EmailForm/EmailForm";

const ExamClassDetail = () => {
    const location = useLocation();
    const examClassId = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);
    const [fileList, setFileList] = useState(null);
    const [roleType, setRoleType] = useState("STUDENT");
    const [pageSize, setPageSize] = useState(10);
    const [classId, setClassId] = useState(examClassId);
    const [record, setRecord] = useState(undefined);
    const [classCode, setClassCode] = useState(undefined);
    const {importStudent, loadingImport} = useImportExport();
    const fileInputRef = useRef(null);
    const [keySearch, setKeySearch] = useState("");
    const [openSendEmailModal, setOpenSendEmailModal] = useState(false);
    const [selectedReceiver, setSelectedReceiver] = useState([]);
    const [additionalReceiver, setAdditionalReceiver] = useState([]);
    const {sendEmailLoading, sendEmailResultExamClass} = useExamClasses();
    const navigate = useNavigate();

    const {
        partiLoading,
        participants,
        getParticipants,
        resultLoading,
        resultData,
        getResult,
        dataPieChart,
        dataColumnChart,
        isPublishedAll,
        isHandled,
        assignLoading,
        assignStudentTestSet,
        publishLoading,
        publishStudentTestSet,
        infoLoading,
        examClassInfo,
        getExamClassDetail
    } = useExamClasses();

    // fetch exam_class
    useEffect(() => {
        if (examClassId) {
            getExamClassDetail(examClassId);
        }
    }, [examClassId, assignLoading]);

    useEffect(() => {
        setClassId(examClassInfo?.id);
        setClassCode(examClassInfo?.code);
        setRecord({...examClassInfo});
    }, [infoLoading]);

    const {exportExamClassStudent, loadingExport} = useImportExport();

    useEffect(() => {
        if (classId && roleType !== "STATISTIC") {
            getParticipants(classId, roleType);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [classId, roleType]);

    useEffect(() => {
        if (classCode && roleType !== "SUPERVISOR") {
            getResult(classCode, {});
        }
    }, [classCode, roleType]);

    // reload the API result after assigning or publishing
    useEffect(() => {
        if (classCode) {
            getResult(classCode, {});
        }
    }, [assignLoading, publishLoading]);

    const handleExportStudent = () => {
        exportExamClassStudent(classCode);
    };

    const onSearch = (value, _e) => {
        setKeySearch(value);
    };
    const handleSearchChangeFilter = debounce((_e) => {
        setKeySearch(_e?.target?.value ?? "");
    }, searchTimeDebounce);

    const handleImportStudent = () => {
        const formData = new FormData();
        formData.append("file", fileList);
        importStudent(formData, classId, getParticipants, roleType);
        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
        setFileList(null);
    }

    /**
     * call api send email
     */
    const handleSendEmail = () => {
        if (selectedReceiver.length > 0 || additionalReceiver.length > 0) {
            sendEmailResultExamClass(examClassId, {
                toAddresses: selectedReceiver.concat(additionalReceiver)
            })
        }
        setOpenSendEmailModal(false);
    };


    const handleChange = (e) => {
        setFileList(e.target.files[0]);
    };
    const tabsData = participants.map((item, index) => {
        const rowItem = resultData.find((subItem) => subItem?.studentId === item.id);
        if (resultData.length > 0) {
            return {
                key: (index + 1).toString(),
                name: item?.name,
                code: item?.code,
                email: item?.email,
                testSetCode: rowItem ? rowItem.testSetCode : null,
                totalPoints: rowItem ? Math.round(rowItem.totalPoints * 100) / 100 : "",
                handledSheetImg: rowItem?.handledSheetImg,
                status: rowItem?.status,
                statusLabel: studentTestStatusMap.get(rowItem?.status)?.label,
                statusColor: studentTestStatusMap.get(rowItem?.status)?.color,
                storedType: rowItem?.storedType
            };
        } else {
            return {
                key: (index + 1).toString(),
                name: item?.name,
                code: item?.code,
                email: item?.email
            };
        }
    });


    const tabsColumn = [
        {
            title: "STT",
            dataIndex: "key",
            key: "key",
            width: "5%",
            align: "center",
        },
        {
            title: "Họ tên",
            dataIndex: "name",
            key: "name",
            width: "20%",
            align: "center",
        },
        {
            title: roleType === "STUDENT" ? "MSSV" : "Mã cán bộ",
            dataIndex: "code",
            key: "code",
            width: "10%",
            align: "center",
        },
        {
            title: "Email",
            dataIndex: "email",
            key: "email",
            width: "20%",
            align: "center",
        }
    ];
    const addTabsColumn = [
        {
            title: "Mã đề thi",
            dataIndex: "testSetCode",
            key: "testSetCode",
            width: "10%",
            align: "center",
        },
        {
            title: "Trạng thái",
            dataIndex: "status",
            key: "status",
            width: "15%",
            align: "center",
            render: (_, record) => (<><span style={{color: `${record?.statusColor}`}}>{record?.statusLabel}</span></>)
        },
        {
            title: "Điểm thi",
            dataIndex: "totalPoints",
            key: "totalPoint",
            width: "10%",
            align: "center",
        },
        {
            title: "Bài làm", key: "handledImg", align: "center", width: "10%", render: (_, record) => {
                return ((record?.handledSheetImg && true) && (
                    <a style={{color: "#33cc33"}}
                       target="_blank"
                       href={record?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.type ? BASE_RESOURCE_URL + record?.handledSheetImg : record?.handledSheetImg}
                       title={record?.code}
                       rel="noreferrer"
                    >Bài làm {record?.code}</a>));
            }
        }
    ];

    const renderTabStatistic = () => {
        return (
            getBaseRole() !== ROLE_STUDENT_CODE && <div className="charts">
                <ChartPie dataPieChart={dataPieChart} resultData={resultData}/>
                <ChartColumn dataColumnChart={dataColumnChart} resultData={resultData}/>
            </div>
        );
    };
    const renderTab = () => {
        return (
            <div className="exam-class-tabs">
                {roleType === "STUDENT" && (
                    <div className="tab-button" style={{marginBottom: 20}}>
                        <SearchFilter placeholder="Tên hoặc MSSV" onSearch={onSearch}
                                      onChange={handleSearchChangeFilter}/>
                        {getBaseRole() !== ROLE_STUDENT_CODE &&
                            <Tooltip title="File mẫu">
                                <Button
                                    type="primary"
                                    onClick={() => getStaticFile(apiPath.studentImportTemplate)}
                                >
                                    <FileExcelFilled style={{color: '#ffffff'}}/>
                                </Button>
                            </Tooltip>
                        }
                        {getBaseRole() !== ROLE_STUDENT_CODE &&
                            <Input allowClear style={{maxWidth: 300}} type="file" name="file"
                                   onChange={(e) => handleChange(e)}></Input>
                        }
                        {getBaseRole() !== ROLE_STUDENT_CODE &&
                            <Tooltip title="Import">
                                <Button
                                    type="primary"
                                    onClick={handleImportStudent}
                                    disabled={!fileList}
                                    loading={loadingImport}
                                >
                                    <ImportOutlined/>
                                </Button>
                            </Tooltip>
                        }
                        {getBaseRole() !== ROLE_STUDENT_CODE &&
                            <Tooltip title="Export">
                                <Button className="options" onClick={handleExportStudent} loading={loadingExport}>
                                    <img src={exportIcon} alt="Tải xuống Icon"/>
                                </Button>
                            </Tooltip>
                        }
                        {(record?.testType === "Online" && getBaseRole() !== ROLE_STUDENT_CODE) &&
                            <Button
                                key="assign-test-set"
                                type="primary"
                                disabled={record?.testType !== "Online" || isHandled}
                                loading={assignLoading}
                                onClick={() => assignStudentTestSet(classId)}
                            >
                                Giao bài thi</Button>}
                        {(record?.testType === "Online" && getBaseRole() !== ROLE_STUDENT_CODE) && <Button
                            key="publish-test-set"
                            type="primary"
                            disabled={isPublishedAll}
                            loading={publishLoading}
                            onClick={() => publishStudentTestSet(classId, true)}
                        >
                            Mở bài thi
                        </Button>}
                    </div>
                )}
                {roleType === "SUPERVISOR" &&
                    (
                        <div className="tab-button" style={{marginBottom: 20}}>
                            <SearchFilter placeholder="Tên hoặc mã cán bộ" onSearch={onSearch}
                                          onChange={handleSearchChangeFilter}/>
                            {
                                getBaseRole() !== ROLE_STUDENT_CODE &&
                                <Tooltip title="Gửi email danh sách lớp thi">
                                    <Button
                                        type="primary"
                                        onClick={() => {
                                            setOpenSendEmailModal(true);
                                        }}
                                    >
                                        <MdAttachEmail/>
                                    </Button>
                                </Tooltip>
                            }
                            <Modal
                                className="modal-select-email-receiver"
                                open={openSendEmailModal}
                                onCancel={() => setOpenSendEmailModal(false)}
                                title={<p style={{
                                    fontWeight: "bold",
                                    fontSize: "20px",
                                    color: "var(--hust-color)"
                                }}>{"Chọn người nhận Email"}</p>}
                                footer={[
                                    <Button
                                        key="back"
                                        onClick={() => setOpenSendEmailModal(false)}
                                        style={{
                                            background: "#F5F8FA",
                                            borderRadius: "6px",
                                            height: "44px",
                                            outline: "none",
                                            border: "none",
                                            minWidth: "100px",
                                        }}
                                    >
                                        Đóng
                                    </Button>,
                                    <Button
                                        style={{
                                            borderRadius: "6px",
                                            height: "44px",
                                            marginRight: "12px",
                                            minWidth: "100px",
                                        }}
                                        key="submit"
                                        type="primary"
                                        loading={sendEmailLoading}
                                        disabled={selectedReceiver.length === 0 && additionalReceiver.length === 0}
                                        onClick={handleSendEmail}
                                    >
                                        Gửi
                                    </Button>,
                                ]}
                            >
                                <p style={{color: 'var(--hust-color)', fontStyle: 'italic', marginBottom: 6}}>Chọn người nhận email có sẵn:</p>

                                <Select
                                    className="email-select-box"
                                    allowClear
                                    mode='multiple'
                                    placeholder="Chọn người nhận email"
                                    style={{
                                        width: '100%',
                                        overflowY: 'scroll'
                                    }}
                                    options={participants.map(value => ({value: value?.email, label: `${value?.name} - ${value?.email}`}))}
                                    onChange={(value) => setSelectedReceiver(value)}
                                />
                                {/*TODO: Handle additional emails*/}
                                <p style={{color: 'var(--hust-color)', fontStyle: 'italic', marginBottom: 6}}>Email bổ
                                    sung:</p>
                                <EmailForm
                                    onFinish={(values) => setAdditionalReceiver(values?.emails !== undefined ? values?.emails.map(item => item?.email) : [])}/>

                                <p style={{
                                    color: 'var(--hust-color)',
                                    fontStyle: 'italic',
                                    fontWeight: 'bold',
                                    marginBottom: 6,
                                    marginTop: 6
                                }}>Danh sách email nhận: {
                                    selectedReceiver.concat(additionalReceiver).join('; ')
                                }</p>
                            </Modal>
                        </div>
                    )
                }
                <Table
                    scroll={{y: 400, x: 1300}}
                    size="small"
                    className="exam-class-detail-participants"
                    columns={roleType === "STUDENT" ? [...tabsColumn, ...addTabsColumn] : tabsColumn}
                    dataSource={tabsData.filter(item => item?.name.toLowerCase().includes(keySearch) || item?.code.toLowerCase().includes(keySearch))}
                    loading={partiLoading || resultLoading}
                    pagination={{
                        pageSize: pageSize,
                        total: tabsData.length,
                        locale: customPaginationText,
                        showQuickJumper: true,
                        showSizeChanger: true,
                        showTotal: (total, range) => (
                            <span>
                <strong>
                  {range[0]}-{range[1]}
                </strong>{" "}
                                trong <strong>{total}</strong> bản ghi
              </span>
                        ),
                        pageSizeOptions: ["10", "20", "50", "100"],
                        onChange: (page, pageSize) => {
                        },
                        onShowSizeChange: (current, size) => {
                            setPageSize(size);
                        },
                    }}
                />
            </div>
        );
    };
    const tabsOptions = [
        {
            key: "STUDENT",
            label: <h3>Sinh viên</h3>,
            children: renderTab(),
        },
        {
            key: "SUPERVISOR",
            label: <h3>Giám thị</h3>,
            children: renderTab(),
        },
        {
            key: "STATISTIC",
            label: <h3>Thống kê</h3>,
            children: renderTabStatistic(),
        },
    ];
    return (
        <div>
            <h1 style={{color: HUST_COLOR, fontSize: "30px", padding: "20px 0px", fontWeight: "600"}}>
                Chi tiết lớp thi
                {<Tooltip title="Cập nhật lớp thi">
                    <EditOutlined style={{cursor:'pointer', marginLeft:'10px'}} onClick={() => navigate(`${appPath.examClassEdit}/${examClassId}`)}/>
                </Tooltip>}
            </h1>
            <div className="exam-class-participant-details">
                <div className="exam-class-info-details">
                    <div className="exam-class-participant-left">
                        <div>{`Học phần: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.subjectTitle}</span>
                        </div>
                        <div>{`Mã lớp thi: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.code}</span>
                        </div>
                    </div>
                    <div className="exam-class-participant-right">
                        <div>{`Học kỳ: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.semester}</span>
                        </div>
                        <div>{`Phòng thi: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.roomName}</span>
                        </div>
                    </div>
                    <div className="exam-class-participant-right">
                        <div>{`Ngày thi: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.examineDate}</span>
                        </div>
                        <div>{`Giờ thi: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.examineTime}</span>
                        </div>
                    </div>
                    <div className="exam-class-participant-right">
                        <div>{`Hình thức thi: `}
                            <span style={{fontSize: 16, fontWeight: 600}}>{record?.testType}</span>
                        </div>
                        <div>
                            Trạng thái:{" "}
                            {resultData.length > 0 ? (
                                <span style={{fontSize: 16, fontWeight: 600}}>Đã có điểm thi</span>
                            ) : (
                                <span style={{fontSize: 16, fontWeight: 600, color: HUST_COLOR}}>Chưa có điểm thi</span>
                            )}
                        </div>
                    </div>
                </div>
            </div>
            <Tabs
                defaultActiveKey="STUDENT"
                items={tabsOptions}
                onChange={(key) => setRoleType(key)}
            />
        </div>
    );
};

export default ExamClassDetail;
