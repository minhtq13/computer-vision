import AppTooltip from "@/components/app-tooltip";
import { HUST_COLOR } from "@/constants";
import { wordLimitImg } from "@/helpers/tools";
import { DownloadOutlined, RotateLeftOutlined, RotateRightOutlined, SwapOutlined, ZoomInOutlined, ZoomOutOutlined } from "@ant-design/icons";
import { Image, Space } from "antd";
import { useTranslations } from "next-intl";
import { useState } from "react";

interface IPreviewOriginalImage {
  srcImage: string;
  imageName: string;
}
const PreviewOriginalImage = ({ srcImage, imageName }: IPreviewOriginalImage) => {
  const tScoring = useTranslations("scoring");
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
      <div className="preview-original-image-title flex gap-1 items-centers" onClick={showImageViewer}>
        <div>{tScoring("originalImage")}: </div>
        <AppTooltip title={imageName} color={HUST_COLOR} key={HUST_COLOR}>
          <span className="underline text-blue-500 cursor-pointer font-medium">{wordLimitImg(imageName, 10)}</span>
        </AppTooltip>
      </div>
      <div className="wrapper-preview-original-image [&>.ant-image]:hidden">
        <Image
          className="preview-original-image"
          alt="View original image"
          src={srcImage}
          preview={{
            scaleStep: 0.2,
            visible,
            onVisibleChange: (visible) => {
              if (!visible) {
                hideImageViewer();
              }
            },
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
          }}
        />
      </div>
    </div>
  );
};
export default PreviewOriginalImage;
