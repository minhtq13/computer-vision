import DeleteIcon from "@/assets/images/svg/delete-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppTable from "@/components/app-table";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useDeleteImageInFolderMutation } from "@/stores/test-set/api";
import { Space, Table } from "antd";
import { useEffect, useState } from "react";
import ImageUpload from "./ImageUpload";
import PreviewImageInFolder from "./PreviewImageInFolder";
import { useTranslations } from "next-intl";
import { FilterScoring } from "@/libs/storage";
import { BASE_RESOURCE_URL_SPRING } from "@/constants/apiPath";
import JSZip from "jszip";

interface IModalSelectedImage {
  imgInFolder: any[];
  filterScoring: FilterScoring;
}

const ModalSelectedImage = ({ imgInFolder, filterScoring }: IModalSelectedImage) => {
  const tScoring = useTranslations("scoring");
  const tCommon = useTranslations("common");
  const examClassCode = filterScoring.examClassCode;
  const [dataTable, setDataTable] = useState([]);
  const [open, setOpen] = useState(false);
  const handleError = useHandleError();
  const notification = useAppNotification();
  // // eslint-disable-next-line no-unused-vars
  // const [arrayImage, setArrayImage] = useState([]);
  const [selectedImages, setSelectedImages] = useState([]);
  const [deleteImageInFolder, { isLoading: isLoadingDeleteImageInFolder }] = useDeleteImageInFolderMutation();
  const [isDownloadingAll, setIsDownloadingAll] = useState(false);

  const showModal = () => {
    setSelectedRowKeys([]);
    setOpen(true);
  };
  const handleOk = () => {
    setSelectedRowKeys([]);
    setOpen(false);
  };
  const handleCancel = () => {
    setSelectedRowKeys([]);
    setOpen(false);
  };

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

  const handleDownloadAll = async () => {
    const selectedData = dataTable.filter((item) => selectedRowKeys.includes(item.key));

    if (selectedData.length === 0) {
      notification.error({ description: "No images selected" });
      return;
    }

    setIsDownloadingAll(true);

    try {
      const zip = new JSZip();

      // Fetch all images and add to ZIP
      const promises = selectedData.map(async (item) => {
        const srcImage = BASE_RESOURCE_URL_SPRING + item.filePath;
        try {
          const response = await fetch(srcImage);
          const blob = await response.blob();
          zip.file(item.fileName, blob);
        } catch (error) {
          handleError(error);
        }
      });

      await Promise.all(promises);

      // Generate ZIP file and download
      const zipBlob = await zip.generateAsync({ type: "blob" });
      const url = URL.createObjectURL(zipBlob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `original_images_${examClassCode}.zip`;
      document.body.appendChild(link);
      link.click();
      URL.revokeObjectURL(url);
      link.remove();

      notification.success({ description: tScoring("downloadSuccess") });
    } catch (error) {
      handleError(error);
    } finally {
      setIsDownloadingAll(false);
    }
  };

  useEffect(() => {
    if (imgInFolder) {
      const newDataTable = imgInFolder?.map((item, index) => {
        return {
          key: index + 1,
          fileName: item.fileName,
          fileExt: item.fileExt,
          filePath: item.filePath,
        };
      });
      const newArrayImage = [];
      imgInFolder?.map((item) => {
        newArrayImage.push(item.filePath);
        return null;
      });
      // setArrayImage(newArrayImage);
      setDataTable(newDataTable);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [imgInFolder]);

  const columns = [
    {
      title: tCommon("index"),
      dataIndex: "key",
      width: "10%",
      align: "center",
    },
    {
      title: tScoring("img"),
      width: "20%",
      key: "action",
      align: "center",
      render: (_, record) => {
        return (
          <Space rootClassName="cursor-pointer" size="middle">
            <PreviewImageInFolder srcImage={BASE_RESOURCE_URL_SPRING + record.filePath} imageName={record.fileName} />
          </Space>
        );
      },
    },
    {
      title: tScoring("fileName"),
      dataIndex: "fileName",
      width: "30%",
    },
    {
      title: tScoring("fileExt"),
      dataIndex: "fileExt",
      width: "20%",
      align: "center",
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record) => (
        <Space rootClassName="cursor-pointer" size="middle">
          <ActionButton icon="download" handleClick={() => handleDownload(BASE_RESOURCE_URL_SPRING + record.filePath, record.fileName)} />
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
    onSelect: (record, selected) => {
      if (selected) {
        setLstFileName([...lstFileName, record.fileName]);
      } else {
        setLstFileName(lstFileName.filter((fileName) => fileName !== record.fileName));
      }
    },
    onSelectAll: (selected, selectedRows) => {
      if (selected) {
        setLstFileName(selectedRows.map((item) => item.fileName));
      } else {
        setLstFileName([]);
      }
    },
  };
  const handleDeleteImage = async () => {
    const params = {
      examClassCode: examClassCode,
      lstFileName: lstFileName,
    };
    try {
      await deleteImageInFolder(params).unwrap();
      setSelectedRowKeys([]);
      notification.success({ description: tScoring("deleteSuccess") });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <div className="modal-selected-image-component">
      <AppButton type="primary" onClick={showModal} disabled={!examClassCode} customclass="min-w-[200px]">
        {imgInFolder?.length ? tScoring("selectedImage", { count: imgInFolder?.length }) : tScoring("clickToChoose")}
      </AppButton>
      {open && (
        <AppModal
          className="modal-selected-image md:!w-[60vw]"
          open={open}
          title={tScoring("listImage", { examClassCode })}
          onOk={handleOk}
          onCancel={handleCancel}
          footer={[
            <AppButton key="back" type="primary" onClick={handleCancel}>
              {tCommon("confirm")}
            </AppButton>,
          ]}
        >
          <div>
            <div className="header-table-selected-image mb-4">
              <div className="header-button">
                <ImageUpload setSelectedImages={setSelectedImages} selectedImages={selectedImages} examClassCode={examClassCode} />
                <div className="flex items-center">
                  <AppButton loading={isLoadingDeleteImageInFolder} danger disabled={!selectedRowKeys.length} onClick={handleDeleteImage}>
                    <DeleteIcon />
                    {tScoring("deleteImg")}
                  </AppButton>
                  <AppButton
                    type="default"
                    loading={isDownloadingAll}
                    disabled={!selectedRowKeys.length || isDownloadingAll}
                    onClick={handleDownloadAll}
                    className="ml-2"
                  >
                    {tScoring("downloadSelected") || "Download All (ZIP)"}
                  </AppButton>
                </div>
              </div>
            </div>
            <AppTable
              scroll={{ y: 400, x: 500 }}
              className="table-select-image-in-folder"
              rowSelection={rowSelection}
              columns={columns as any}
              dataSource={dataTable}
              labelPagination={tScoring("img")}
            />
          </div>
        </AppModal>
      )}
    </div>
  );
};

export default ModalSelectedImage;
