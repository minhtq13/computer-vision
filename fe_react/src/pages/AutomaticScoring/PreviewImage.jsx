import { DownloadOutlined, RotateLeftOutlined, RotateRightOutlined, SwapOutlined, ZoomInOutlined, ZoomOutOutlined } from "@ant-design/icons";
import { Image, Space } from "antd";
import React from "react";
import "./PreviewImage.scss";
const PreviewImage = ({ srcImage, imageName, className = "" }) => {
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
        alt="Xem image"
        preview={{
          scaleStep: 0.2,
          toolbarRender: (_, { transform: { scale }, actions: { onFlipY, onFlipX, onRotateLeft, onRotateRight, onZoomOut, onZoomIn } }) => (
            <Space size={12} className="toolbar-wrapper">
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
