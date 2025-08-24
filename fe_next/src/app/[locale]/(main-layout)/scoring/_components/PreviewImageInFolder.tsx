import {
  DownloadOutlined,
  EyeOutlined,
  RotateLeftOutlined,
  RotateRightOutlined,
  SwapOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
} from "@ant-design/icons";
import { Image, Space } from "antd";
import React from "react";

interface IPreviewImageInFolder {
  srcImage: string;
  imageName: string;
}

const PreviewImageInFolder = ({ srcImage, imageName }: IPreviewImageInFolder) => {
  const onDownload = () => {
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
  return (
    <div>
      <Image
        style={{ maxHeight: 50, maxWidth: 50 }}
        className="preview-original-image-in-folder"
        src={srcImage}
        alt="View image in folder"
        preview={{
          scaleStep: 0.2,
          toolbarRender: (_, { transform: { scale }, actions: { onFlipY, onFlipX, onRotateLeft, onRotateRight, onZoomOut, onZoomIn } }) => (
            <Space
              size={12}
              className="toolbar-wrapper fixed bottom-[32px] left-[50%] px-6 text-[13px] rounded-full translate-x-[-50%] w-max bg-[rgba(0,0,0,0.1)] text-white [&>.ant-space-item>.anticon]:p-3"
            >
              <span>{imageName}</span>
              <DownloadOutlined onClick={onDownload} />
              <SwapOutlined rotate={90} onClick={onFlipY} />
              <SwapOutlined onClick={onFlipX} />
              <RotateLeftOutlined onClick={onRotateLeft} />
              <RotateRightOutlined onClick={onRotateRight} />
              <ZoomOutOutlined disabled={scale === 1} onClick={onZoomOut} />
              <ZoomInOutlined disabled={scale === 50} onClick={onZoomIn} />
            </Space>
          ),
          mask: <EyeOutlined />,
        }}
      />
    </div>
  );
};
export default PreviewImageInFolder;
