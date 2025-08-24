import { DownloadOutlined, RotateLeftOutlined, RotateRightOutlined, SwapOutlined, ZoomInOutlined, ZoomOutOutlined } from "@ant-design/icons";
import { Image, Space } from "antd";
import React from "react";

interface IPreviewImage {
  srcImage: string;
  imageName: string;
  className?: string;
}
const PreviewImage = ({ srcImage, imageName, className = "" }: IPreviewImage) => {
  const imgHandleName = `handle-${imageName}`;
  const onDownload = () => {
    fetch(srcImage)
      .then((response) => response.blob())
      .then((blob) => {
        const url = URL.createObjectURL(new Blob([blob]));
        const link = document.createElement("a");
        link.href = url;
        link.download = imgHandleName;
        document.body.appendChild(link);
        link.click();
        URL.revokeObjectURL(url);
        link.remove();
      });
  };
  return (
    <div>
      <Image
        className={className}
        src={srcImage}
        alt="View image"
        preview={{
          scaleStep: 0.2,
          toolbarRender: (_, { transform: { scale }, actions: { onFlipY, onFlipX, onRotateLeft, onRotateRight, onZoomOut, onZoomIn } }) => (
            <Space
              size={12}
              className="toolbar-wrapper fixed bottom-[32px] left-[50%] px-6 text-[13px] rounded-full translate-x-[-50%] w-max bg-[rgba(0,0,0,0.1)] text-white [&>.ant-space-item>.anticon]:p-3"
            >
              <span>{imgHandleName}</span>
              <DownloadOutlined onClick={onDownload} />
              <SwapOutlined rotate={90} onClick={onFlipY} />
              <SwapOutlined onClick={onFlipX} />
              <RotateLeftOutlined onClick={onRotateLeft} />
              <RotateRightOutlined onClick={onRotateRight} />
              <ZoomOutOutlined disabled={scale === 1} onClick={onZoomOut} />
              <ZoomInOutlined disabled={scale === 50} onClick={onZoomIn} />
            </Space>
          ),
        }}
      />
    </div>
  );
};
export default PreviewImage;
