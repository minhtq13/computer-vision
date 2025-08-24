import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { useEffect, useState } from "react";
import "./TestEdit.scss";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";
import { useLocation } from "react-router-dom";
import { testSetDetailService } from "../../../services/testServices";
import { Button, Spin, Modal } from "antd";
import useNotify from "../../../hooks/useNotify";
import useTest from "../../../hooks/useTest";
const TestEdit = () => {
  const { updateTestSet } = useTest();
  const [loadingData, setLoadingData] = useState(true);
  const [openModal, setOpenModal] = useState(true);
  const [stores, setStores] = useState([]);
  const [testDetail, setTestDetail] = useState({});
  const notify = useNotify();
  const location = useLocation();
  const code = location.pathname.split("/")[3];
  const testId = location.pathname.split("/")[4];
  useEffect(() => {
    setLoadingData(true);
    testSetDetailService(
      { testId: testId, code: code },
      (res) => {
        setLoadingData(false);
        setStores(
          res.data.lstQuestion.length > 0
            ? res.data.lstQuestion.map((item) => {
              return {
                ...item,
                id: String(item.id),
                answers:
                  item.answers.length > 0
                    ? item.answers.map((ans) => {
                      return {
                        id: String(
                          ans.answerId
                        ),
                        content: ans.content,
                        isCorrect:
                          ans.isCorrect,
                      };
                    })
                    : [],
              };
            })
            : []
        );

        setTestDetail(res.data.testSet);
      },
      () => {
        notify.error("Lỗi!");
        setLoadingData(true);
      }
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleDragAndDrop = (results) => {
    const { source, destination, type } = results;

    if (!destination) return;

    if (
      source.droppableId === destination.droppableId &&
      source.index === destination.index
    )
      return;

    if (type === "group") {
      const reorderedStores = [...stores];

      const storeSourceIndex = source.index;
      const storeDestinatonIndex = destination.index;

      const [removedStore] = reorderedStores.splice(storeSourceIndex, 1);
      reorderedStores.splice(storeDestinatonIndex, 0, removedStore);

      return setStores(reorderedStores);
    }
    const itemSourceIndex = source.index;
    const itemDestinationIndex = destination.index;

    const storeSourceIndex = stores.findIndex(
      (store) => store.id === source.droppableId
    );
    const storeDestinationIndex = stores.findIndex(
      (store) => store.id === destination.droppableId
    );

    const newSourceItems = [...stores[storeSourceIndex].answers];
    const newDestinationItems =
      source.droppableId !== destination.droppableId
        ? [...stores[storeDestinationIndex].answers]
        : newSourceItems;

    const [deletedItem] = newSourceItems.splice(itemSourceIndex, 1);
    newDestinationItems.splice(itemDestinationIndex, 0, deletedItem);

    const newStores = [...stores];

    newStores[storeSourceIndex] = {
      ...stores[storeSourceIndex],
      answers: newSourceItems,
    };
    newStores[storeDestinationIndex] = {
      ...stores[storeDestinationIndex],
      answers: newDestinationItems,
    };

    setStores(newStores);
  };
  const handleUpdate = () => {
    updateTestSet({
      testSetId: testDetail.testSetId,
      questions: stores.map((item, index) => {
        return {
          questionId: Number(item.id),
          questionNo: index + 1,
          answers: item.answers.map((ans, ansNo) => {
            return {
              answerId: Number(ans.id),
              answerNo: ansNo + 1,
            };
          }),
        };
      }),
    });
  };

  return (
    <div className="test-edit">
      <div className="test-edit-header">Cập nhật đề thi</div>
      <Spin tip="Đang tải..." spinning={loadingData}>
        <div className="test-preview">
          <div className="test-top">
            <div className="test-bgd">
              <p>BỘ GIÁO DỤC VÀ ĐÀO TẠO</p>
              <p className="text-bold">
                ĐẠI HỌC BÁCH KHOA HÀ NỘI
              </p>
            </div>
            <div className="test-semester">
              <p className="text-bold">ĐỀ THI CUỐI KỲ</p>
              <p className="text-bold">{`HỌC KỲ:`} </p>
            </div>
          </div>
          <div className="test-header-content">
            <div className="test-header-content-left">
              <p>Hình thức tổ chức thi: Trắc nghiệm</p>
              <p>{`Mã học phần: ${testDetail.subjectCode}`}</p>
              <p>{`Tên học phần: ${testDetail.subjectTitle}`}</p>
            </div>
            <div className="test-header-content-right">
              <p className="text-bold">{`Thời gian làm bài:  ${testDetail.duration} phút`}</p>
              <p className="non-ref">(Không sử dụng tài liệu)</p>
              <p className="text-bold">{`Mã đề: ${code}`}</p>
            </div>
          </div>
          <DragDropContext onDragEnd={handleDragAndDrop}>
            <Droppable droppableId="ROOT" type="group">
              {(provided) => (
                <div
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                >
                  {stores.map((store, index) => (
                    <Draggable
                      draggableId={store.id}
                      index={index}
                      key={store.id}
                    >
                      {(provided) => (
                        <div
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
                          ref={provided.innerRef}
                        >
                          <StoreList
                            {...store}
                            index={index}
                          />
                        </div>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </div>
              )}
            </Droppable>
          </DragDropContext>
          <div className="test-footer">
            <div className="test-end">
              <p>(Cán bộ coi thi không giải thích gì thêm)</p>
              <p className="text-bold">HẾT</p>
            </div>
            <div className="test-sig">
              <div className="sig-left">
                <p className="text-bold">
                  DUYỆT CỦA KHOA/BỘ MÔN
                </p>
                <p>(Ký tên, ghi rõ họ tên)</p>
              </div>
              <div className="sig-right">
                <p>Hà Nội, ngày ..... tháng ..... năm ......</p>
                <p className="text-bold">GIẢNG VIÊN RA ĐỀ</p>
                <p>(Ký tên, ghi rõ họ tên)</p>
              </div>
            </div>
            <div className="test-note">
              <p className="text-bold text-note">Lưu ý:</p>
              <p>{`-	Sử dụng khổ giấy A4;`}</p>
              <p>{`-	Phiếu trả lời trắc nghiệm theo mẫu của TTKT;`}</p>
              <p>{`-	Phải thể hiện số thứ tự trang nếu số trang lớn hơn 1;`}</p>
              <p>{`-	Thí sinh không được sử dụng tài liệu, mọi thắc mắc về đề thi vui lòng hỏi giám thị coi thi.`}</p>
            </div>
          </div>
        </div>
      </Spin>
      <div className="test-edit-button">
        <div className="test-edit-button-save">
          <Button type="primary" onClick={handleUpdate}>
            Lưu
          </Button>
        </div>
        <div className="test-edit-button-download">
          <Button type="primary" onClick={handleUpdate}>
            Tải xuống / In
          </Button>
        </div>
      </div>
      <Modal
        open={openModal}
        title="Hướng dẫn"
        onOk={() => setOpenModal(false)}
        onCancel={() => setOpenModal(false)}
        okText="Đã hiểu"
        centered={true}
      >
        <p>
          Vui lòng kéo thả câu hỏi và câu trả lời vào vị trí mong
          muốn!
        </p>
      </Modal>
    </div>
  );
};
function StoreList({ content, answers, id, index }) {
  return (
    <Droppable droppableId={id}>
      {(provided) => (
        <div
          {...provided.droppableProps}
          ref={provided.innerRef}
          className="test-edit-view"
        >
          <div className="store-container">
            <span className="text-title">{`Câu ${index + 1
              }:`}</span>
            <ReactQuill
              key={index}
              value={content}
              readOnly={true}
              theme="snow"
              modules={{ toolbar: false }}
            />
          </div>
          <div className="answers-container">
            {answers.map((ans, ansIndex) => (
              <Draggable
                draggableId={ans.id}
                index={ansIndex}
                key={ans.id}
              >
                {(provided) => (
                  <div
                    className={
                      ans.isCorrect
                        ? "item-container corrected"
                        : "item-container"
                    }
                    {...provided.dragHandleProps}
                    {...provided.draggableProps}
                    ref={provided.innerRef}
                  >
                    <span className="text-title">{`${String.fromCharCode(
                      65 + ansIndex
                    )}.`}</span>
                    <ReactQuill
                      key={ansIndex}
                      value={ans.content}
                      readOnly={true} // Đặt readOnly để ngăn người dùng chỉnh sửa nội dung
                      theme="snow"
                      modules={{ toolbar: false }}
                    />
                  </div>
                )}
              </Draggable>
            ))}
            {provided.placeholder}
          </div>
        </div>
      )}
    </Droppable>
  );
}
export default TestEdit;
