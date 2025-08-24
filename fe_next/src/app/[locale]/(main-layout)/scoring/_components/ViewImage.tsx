"use client";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import { useRef, useState } from "react";
import Slider, { CustomArrowProps } from "react-slick";
import PreviewImage from "./PreviewImage";
import PreviewOriginalImage from "./PreviewOriginalImage";
import { useTranslations } from "next-intl";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { BASE_RESOURCE_URL_SPRING } from "@/constants/apiPath";

const NextArrow = ({ currentSlide, slideCount, ...props }: CustomArrowProps) => (
  <div {...props}>
    <div className="">Next</div>
  </div>
);

const PrevArrow = ({ currentSlide, slideCount, ...props }: CustomArrowProps) => (
  <div {...props}>
    <div className="">Prev</div>
  </div>
);

interface IViewImage {
  dataArray: any[];
  index: number;
}
const ViewImage = ({ dataArray, index }: IViewImage) => {
  const tScoring = useTranslations("scoring");
  const tCommon = useTranslations("common");
  const { fileStoredTypeEnum } = useLocaleOptions();
  const [currentSlide, setCurrentSlide] = useState<any>(0);
  const sliderRef = useRef<any>();
  const settings = {
    dots: false,
    infinite: true,
    speed: 300,
    slidesToShow: 1,
    slidesToScroll: 1,
    afterChange: (slideIndex) => {
      setCurrentSlide(slideIndex);
    },
    currentSlide: currentSlide,
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
  };
  const [isModalOpen, setIsModalOpen] = useState(false);
  const showModal = () => {
    setCurrentSlide(index - 1);
    setIsModalOpen(true);
  };
  const handleNext = () => {
    sliderRef.current.slickNext();
    setCurrentSlide((prevState) => prevState + 1);
  };
  const handleBack = () => {
    sliderRef.current.slickPrev();
    setCurrentSlide((prevState) => prevState - 1);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };
  const handleDownload = () => {
    const fetchUrl =
      dataArray[currentSlide]?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.value
        ? BASE_RESOURCE_URL_SPRING + dataArray[currentSlide]?.handledScoredImg
        : dataArray[currentSlide]?.handledScoredImg;
    fetch(fetchUrl)
      .then((response) => response.blob())
      .then((blob) => {
        const url = URL.createObjectURL(new Blob([blob]));
        const link = document.createElement("a");
        link.href = url;
        link.download = `handle-${dataArray[currentSlide].originalImgFileName}`;
        document.body.appendChild(link);
        link.click();
        URL.revokeObjectURL(url);
        link.remove();
      });
  };
  return (
    <div className="view-image-component">
      <div className="view-image-button flex justify-center cursor-pointer">
        <ActionButton icon="view-img-handle" handleClick={showModal} />
      </div>
      <AppModal
        className="modal-view-image max-md:w-full"
        title={tScoring("viewDetail")}
        open={isModalOpen}
        onCancel={handleCancel}
        footer={[
          <AppButton key="back" onClick={handleBack}>
            {tScoring("previousImage")}
          </AppButton>,
          <AppButton key="next" type="primary" onClick={handleNext}>
            {tScoring("nextImage")}
          </AppButton>,
          <AppButton key="download" type="primary" onClick={handleDownload}>
            Download
          </AppButton>,
        ]}
      >
        <Slider ref={sliderRef} {...settings}>
          {dataArray.map((item, index) => {
            return (
              <div className="modal-content-view-image [&:focus-visible]:outline-none" key={index}>
                <div className="header my-5 flex flex-col items-center">
                  <div className="grid grid-cols-4 w-full max-xl:grid-cols-3 max-lg:grid-cols-2">
                    <div>
                      {tCommon("index")}:{" "}
                      <strong className="value text-[16px]">
                        {currentSlide + 1}/{dataArray.length}
                      </strong>
                    </div>
                    <div>
                      <PreviewOriginalImage srcImage={BASE_RESOURCE_URL_SPRING + item.originalImg} imageName={item.originalImgFileName} />
                    </div>
                    <div>
                      {tCommon("examCode")}:<strong className="value">{item.testSetCode}</strong>
                    </div>
                    <div>
                      {tScoring("MSSV")}: <strong className="value">{item.studentCode}</strong>
                    </div>
                    <div>
                      {tCommon("examClassCode")}: <strong className="value">{item.examClassCode}</strong>
                    </div>
                    <div>
                      {tScoring("totalQuestions")}: <strong className="value">{item.numTestSetQuestions}</strong>
                    </div>
                    <div>
                      {tScoring("markedAnswers")}: <strong className="value">{item.numMarkedAnswers}</strong>
                    </div>
                    <div>
                      {tScoring("correctAnswers")}: <strong className="value text-[16px] text-green-500">{item.numCorrectAnswers}</strong>
                    </div>
                    <div>
                      {tScoring("wrongAnswers")}: <strong className="value text-[16px] text-fill-error">{item.numWrongAnswers}</strong>
                    </div>
                    <div>
                      {tScoring("score")}: <strong className="value text-[16px] text-yellow-300">{Math.round(item.totalScore * 100) / 100}</strong>
                    </div>
                  </div>
                </div>
                <div
                  className="handle-img cursor-pointer flex justify-center items-center max-h-[62vh]
                  [&>div>div>img]:max-h-[62vh] [&>div>div>img]:w-full [&>div>div>img]:object-contain"
                >
                  <PreviewImage
                    srcImage={
                      item?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.value
                        ? BASE_RESOURCE_URL_SPRING + item?.handledScoredImg
                        : item?.handledScoredImg
                    }
                    imageName={item.originalImgFileName}
                  />
                </div>
              </div>
            );
          })}
        </Slider>
      </AppModal>
    </div>
  );
};

export default ViewImage;
