import {
  DownloadOutlined,
  RotateLeftOutlined,
  RotateRightOutlined,
  SwapOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
} from "@ant-design/icons";
import { Image, Space, Tooltip } from "antd";
import React, { useState } from "react";
import { wordLimitImg } from "../../utils/tools";
import "./PreviewOriginalImage.scss";
import { HUST_COLOR } from "../../utils/constant";
const PreviewOriginalImage = ({ srcImage, imageName }) => {
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
  const [visible, setVisible] = useState(false);

  const showImageViewer = () => {
    setVisible(true);
  };

  const hideImageViewer = () => {
    setVisible(false);
  };
  return (
    <div>
      <div className="preview-original-image-title" onClick={showImageViewer}>
        Ảnh gốc: {" "}
        <Tooltip title={imageName} color={HUST_COLOR} key={HUST_COLOR}>
          <span>{wordLimitImg(imageName, 10)}</span>
        </Tooltip>
      </div>
      <div className="wrapper-preview-original-image">
        <Image
          className="preview-original-image"
          alt="Xem original image"
          src={srcImage}
          preview={{
            scaleStep: 0.2,
            visible,
            onVisibleChange: (visible) => {
              if (!visible) {
                hideImageViewer();
              }
            },
            toolbarRender: (
              _,
              {
                transform: { scale },
                actions: { onFlipY, onFlipX, onRotateLeft, onRotateRight, onZoomOut, onZoomIn },
              }
            ) => (
              <Space size={12} className="toolbar-wrapper">
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
          }}
        />
      </div>
    </div>
  );
};
export default PreviewOriginalImage;
