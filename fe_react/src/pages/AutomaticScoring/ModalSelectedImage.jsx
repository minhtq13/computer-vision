import { Button, Modal, Space, Table } from "antd";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import ImageUpload from "./ImageUpload";
import "./ModalSelectedImage.scss";
import PreviewImageInFolder from "./PreviewImageInFolder";
import deleteIcon from "../../assets/images/svg/delete-icon.svg";
import useAI from "../../hooks/useAI";
import { customPaginationText } from "../../utils/tools";
import ActionButton from "../../components/ActionButton/ActionButton";
import {BASE_RESOURCE_URL} from "../../config/apiPath";


const ModalSelectedImage = ({ loading, imgInFolder }) => {
  const { deleteImgInFolder } = useAI();
  const { examClassCode } = useSelector((state) => state.appReducer);
  const [dataTable, setDataTable] = useState([]);
  const [open, setOpen] = useState(false);
  // eslint-disable-next-line no-unused-vars
  const [arrayImage, setArrayImage] = useState([]);
  const [selectedImages, setSelectedImages] = useState([]);

  const showModal = () => {
    setSelectedRowKeys([]);
    setOpen(true);
    resetInputFile()
  };
  const handleOk = () => {
    setSelectedRowKeys([])
    setOpen(false);
    resetInputFile()
  };
  const handleCancel = () => {
    setSelectedRowKeys([])
    setOpen(false);
    resetInputFile()
  };
  const resetInputFile = () => {
    const inputUpload = document.querySelector(".input-upload-scoring");
    if (inputUpload) {
      inputUpload.value = null;
    }
    setSelectedImages([])
  }
  const handleDownload = (srcImage, imageName) => {
    fetch(srcImage)
      .then((response) => response.blob())
      .then((blob) => {
        const url = URL.createObjectURL(new Blob([blob]));
        const link = document.createElement("a");
        link.href = url;
        link.download = `${imageName}`;
        document.body.appendChild(link);
        link.click();
        URL.revokeObjectURL(url);
        link.remove();
      });
  };

  useEffect(() => {
    if (imgInFolder) {
      const newDataTable = imgInFolder.map((item, index) => {
        return {
          key: index + 1,
          fileName: item.fileName,
          fileExt: item.fileExt,
          filePath: item.filePath,
        };
      });
      const newArrayImage = [];
      imgInFolder.map((item) => {
        newArrayImage.push(item.filePath);
        return null;
      });
      setArrayImage(newArrayImage);
      setDataTable(newDataTable);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [imgInFolder]);

  const [pageSize, setPageSize] = useState(10);
  const columns = [
    {
      title: "TT",
      dataIndex: "key",
      width: "10%",
      align: "center",
    },
    {
      title: "Ảnh",
      width: "20%",
      key: "action",
      align: "center",
      render: (_, record, index) => {
        return (
          <Space size="middle" style={{ cursor: "pointer" }}>
            <PreviewImageInFolder srcImage={BASE_RESOURCE_URL + record.filePath} imageName={record.fileName} />
          </Space>
        );
      },
    },
    {
      title: "Tên ảnh",
      dataIndex: "fileName",
      width: "30%",
    },
    {
      title: "Loại ảnh",
      dataIndex: "fileExt",
      width: "20%",
      align: "center",
    },
    {
      title: "Thao tác",
      key: "action",
      align: "center",
      render: (_, record) => (
        <Space size="middle" style={{ cursor: "pointer" }}>
          <ActionButton icon="download" handleClick={() => handleDownload(BASE_RESOURCE_URL + record.filePath, record.fileName)}  />
        </Space>
      ),
    },
  ];
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [lstFileName, setLstFileName] = useState([]);
  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
    selections: [Table.SELECTION_ALL, Table.SELECTION_NONE],
    onSelect: (record, selected, selectedRows, nativeEvent) => {
      if (selected) {
        setLstFileName([...lstFileName, record.fileName]);
      } else {
        setLstFileName(lstFileName.filter((fileName) => fileName !== record.fileName));
      }
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      if (selected) {
        setLstFileName(selectedRows.map((item) => item.fileName));
      } else {
        setLstFileName([]);
      }
    },
  };
  const handleDeleteImage = () => {
    const params = {
      examClassCode: examClassCode,
      lstFileName: lstFileName,
    };
    deleteImgInFolder(params)
    setSelectedRowKeys([])
  };


  return (
    <div className="modal-selected-image-component">
      <Button
        type="primary"
        onClick={showModal}
        disabled={!examClassCode}
        style={{ minWidth: 200 }}
      >
        {imgInFolder.length ? `Đã chọn ${imgInFolder.length} ảnh` : "Nhấn vào đây để chọn ảnh"}
      </Button>
      <Modal
        className="modal-selected-image"
        open={open}
        title={`Danh sách ảnh hiện có trong lớp thi ${examClassCode}`}
        onOk={handleOk}
        onCancel={handleCancel}
        footer={[
          <Button key="back" type="primary" onClick={handleCancel}>
            Xác nhận
          </Button>,
        ]}
      >
        <div>
          <div style={{ marginBottom: 16 }} className="header-table-selected-image">
            <div className="header-button">
              <ImageUpload setSelectedImages={setSelectedImages} selectedImages={selectedImages} />
              <Button
                danger
                style={{ display: "flex", alignItems: "center" }}
                disabled={!selectedRowKeys.length}
                onClick={handleDeleteImage}
              >
                <img src={deleteIcon} alt="Delete Icon" style={{marginRight: 8}} />
                Xoá ảnh
              </Button>
            </div>
          </div>
          <Table
            className="table-select-image-in-folder"
            scroll={{ y: 330 }}
            rowSelection={rowSelection}
            size="small"
            columns={columns}
            dataSource={dataTable}
            pagination={{
              pageSize: pageSize,
              total: imgInFolder.length,
              locale: customPaginationText,
              showQuickJumper: true,
              showSizeChanger: true,
              showTotal: (total, range) => (
                <span>
                  <strong>
                    {range[0]}-{range[1]}
                  </strong>{" "}
                  trong <strong>{total}</strong> ảnh hiện có
                </span>
              ),
              pageSizeOptions: ["10", "20", "50", "100"],
              onChange: (page, pageSize) => {},
              onShowSizeChange: (current, size) => {
                setPageSize(size);
              },
            }}
          />
        </div>
      </Modal>
    </div>
  );
};

export default ModalSelectedImage;
