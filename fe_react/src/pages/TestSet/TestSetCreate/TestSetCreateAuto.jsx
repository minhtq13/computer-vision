import {Button, Input, List, Spin, Tooltip} from "antd";
import "./TestSetCreate.scss";
import {useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {AiOutlineDownload, AiFillEye, AiFillEdit, AiFillDelete} from "react-icons/ai";
import {testSetCreateService} from "../../../services/testServices";
import useNotify from "../../../hooks/useNotify";
import TestPreview from "../../../components/TestPreview/TestPreview";
import {downloadTestPdf} from "../../../utils/tools";
import useTest from "../../../hooks/useTest";
import {HUST_COLOR} from "../../../utils/constant";
import {appPath} from "../../../config/appPath";
import {regexPattern} from "../../../common/regexPattern";
import deletePopUpIcon from "../../../assets/images/svg/delete-popup-icon.svg";
import ModalPopup from "../../../components/ModalPopup/ModalPopup";

const TestSetCreateAuto = ({testId}) => {
    const {getTestSetDetail, testSetDetail, detailLoading} = useTest();
    const notify = useNotify();
    const [testSetNum, setTestSetNum] = useState(null);
    const [isError, setIsError] = useState(false);
    const [testNos, setTestNos] = useState([]);
    const [btnLoading, setBtnLoading] = useState(false);
    const [testNo, setTestNo] = useState(null);
    const {getListTestSet, listTestSet, listTestSetLoading, deleteTestSet, deleteTestSetLoading} = useTest();
    const onView = (test) => {
        getTestSetDetail({testId: testId, code: test.testSetCode});
    };

    // effect when generate / delete a test set
    useEffect(() => {
        getListTestSet({testId: testId});
    }, [testNos, deleteTestSetLoading]);

    const navigate = useNavigate();
    const onCreate = () => {
        if (!testSetNum) {
            setIsError(true);
        } else {
            setBtnLoading(true);
            testSetCreateService(
                {testId: testId, numOfTestSet: testSetNum},
                (res) => {
                    notify.success("Tạo bộ đề thi thành công!");
                    setTestNos(res.data);
                    setBtnLoading(false);
                },
                () => {
                    notify.error("Lỗi tạo bộ đề thi!");
                    setBtnLoading(false);
                }
            ).then(() => {
            });
        }
    };
    const handleUpdate = (testNo) => {
        navigate(`${appPath.testEdit}/${testId}/${testNo}`);
    }

    const handleDelete = (testSetId) => {
        deleteTestSet({testSetId: testSetId});
    }

    return (
        <div className="test-set-create-auto">
            <div className="test-set-left">
                <div className="test-set-quantity">
                    <div className="test-set-input">
                        <div className="test-set-input-label">Số lượng:</div>
                        <Input
                            type="text"
                            pattern={regexPattern.NUMBER_REGEX}
                            placeholder="Nhập số lượng đề thi"
                            value={testSetNum}
                            onChange={(e) => {
                                if (!e.target.value) {
                                    setIsError(false);
                                } else {
                                    setIsError(false);
                                }
                                setTestSetNum(e.target.value);
                            }}
                            style={{width: 175}}
                        />
                    </div>
                    {isError && (
                        <span className="is-error">
              Chưa điền số lượng bộ đề thi !
            </span>
                    )}
                </div>
                <Button
                    type="primary"
                    htmlType="submit"
                    onClick={onCreate}
                    style={{width: 80, height: 40}}
                    loading={btnLoading}
                >
                    Tạo
                </Button>
                <List
                    header={"Danh sách mã đề thi đã tạo:"}
                    itemLayout="horizontal"
                    className="test-set-list"
                    dataSource={listTestSet}
                    loading={listTestSetLoading}
                    renderItem={(item) => (
                        <List.Item
                            actions={[
                                <div key="list-view" className="edit-preview">
                                    <div
                                        className="preview"
                                        style={{cursor: "pointer"}}
                                    >
                                        <div className="preview-text"
                                             onClick={() => {
                                                 onView(item);
                                                 setTestNo(item.testSetCode);
                                             }}>Xem
                                        </div>
                                        <AiFillEye color={HUST_COLOR}/>
                                    </div>
                                    <Tooltip title={item?.isHandled ? "Đề thi đã được sử dụng" : "Sửa đề thi"}>
                                        <div
                                            className="edit"
                                            style={{cursor: "pointer"}}
                                        >
                                            <div className={`edit-text ${(item?.isHandled ? "is-handled" : "")}`} onClick={() => {
                                                if (!item?.isHandled) {
                                                    handleUpdate(item.testSetCode);
                                                }}
                                            }>Sửa</div>
                                            <AiFillEdit className={item?.isHandled ? "is-handled" : ""} color={HUST_COLOR}/>
                                        </div>
                                    </Tooltip>
                                    <Tooltip title={item?.isUsed ? "Đề thi đã được sử dụng" : "Xóa đề thi"}>
                                        <div
                                            className="delete"
                                            style={{cursor: "pointer"}}
                                        >
                                            <ModalPopup
                                                buttonOpenModal={
                                                    <div className={`delete-text ${item?.isUsed ? "is-used" : ""}`}>Xóa</div>
                                                }
                                                title="Xóa bộ đề thi"
                                                message="Bạn có chắc chắn muốn xóa bộ đề thi này không?"
                                                confirmMessage={
                                                    "Thao tác này không thể hoàn tác!"
                                                }
                                                ok={"Đồng ý"}
                                                icon={deletePopUpIcon}
                                                onAccept={() => {
                                                    if (!item?.isUsed) {
                                                        handleDelete(item?.testSetId);
                                                    }
                                                }}
                                                loading={deleteTestSetLoading}
                                            />
                                            <AiFillDelete className={item?.isUsed ? "is-used" : ""} color={HUST_COLOR}/>
                                        </div>
                                    </Tooltip>
                                </div>
                            ]}
                        >
                            <List.Item.Meta
                                title={`Mã đề: ${item.testSetCode}`}
                            ></List.Item.Meta>
                        </List.Item>
                    )}
                />
            </div>
            <div className="test-set-right">
                <Spin tip="Đang tải..." spinning={detailLoading}>
                    {testSetDetail.lstQuestion &&
                    testSetDetail.lstQuestion.length > 0 ? (
                        <TestPreview
                            questions={
                                testSetDetail.lstQuestion
                                    ? testSetDetail.lstQuestion
                                    : []
                            }
                            testDetail={
                                testSetDetail.testSet
                                    ? testSetDetail.testSet
                                    : {}
                            }
                            testNo={testNo}
                        />
                    ) : (
                        <div className="test-preview-test-set">
                            <div>
                                Nhập số lượng đề thi muốn tạo và xem trước đề
                                thi ở đây!
                            </div>
                        </div>
                    )}
                </Spin>
                <div className="btn-test-set-create">
                    <Button
                        type="primary"
                        htmlType="submit"
                        icon={<AiOutlineDownload size={18}/>}
                        disabled={
                            testSetDetail.lstQuestion &&
                            testSetDetail.lstQuestion.length < 1
                        }
                        onClick={() =>
                            downloadTestPdf(
                                testSetDetail.lstQuestion
                                    ? testSetDetail.lstQuestion
                                    : [],
                                testSetDetail.testSet ? testSetDetail.testSet : {},
                                testNo
                            )
                        }
                    >
                        Tải xuống / In
                    </Button>
                </div>
            </div>
        </div>
    );
};
export default TestSetCreateAuto;
