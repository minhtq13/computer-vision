import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import { useTranslations } from "next-intl";
import { useState } from "react";
import PreviewImage from "./PreviewImage";
import { BASE_RESOURCE_URL_SPRING } from "@/constants/apiPath";

interface MayBeWrongProps {
  mayBeWrong: string[];
  examClassCode: string;
}

const MayBeWrong = ({ mayBeWrong, examClassCode }: MayBeWrongProps) => {
  const tScoring = useTranslations("scoring");
  const tCommon = useTranslations("common");
  const [open, setOpen] = useState(false);
  const downloadTxtFile = () => {
    const fileName = `MayBeWrong_${examClassCode}.txt`;
    const fileContent = mayBeWrong.join("\n");
    const element = document.createElement("a");
    const file = new Blob([fileContent], { type: "text/plain" });
    element.href = URL.createObjectURL(file);
    element.download = fileName;
    document.body.appendChild(element);
    element.click();
  };
  function extractImageName(input) {
    const regex = /ảnh\s+([^\s,"]+\.\w+)/i;
    const match = input.match(regex);
    const isCaseOne = input.trim().startsWith("Ảnh");
    return match ? { imageName: match[1], isCaseOne } : { imageName: null, isCaseOne: false };
  }

  const warningFileCount = mayBeWrong?.filter((item) => item.split(",").length > 1).length;
  const errorFileCount = mayBeWrong?.length - warningFileCount;

  return (
    <>
      <AppButton type="primary" onClick={() => setOpen(true)} disabled={mayBeWrong?.length === 0}>
        {tScoring("viewResultMayBeWrong")} ({mayBeWrong?.length})
      </AppButton>
      <AppModal
        className="may-be-wrong-modal !min-w-[52vw]"
        open={open}
        title={tScoring("resultScoring")}
        onOk={() => setOpen(false)}
        onCancel={() => setOpen(false)}
        footer={[
          <AppButton key="download" onClick={downloadTxtFile}>
            {tScoring("downloadTxt")}
          </AppButton>,
          <AppButton key="submit" type="primary" onClick={() => setOpen(false)}>
            {tCommon("ok")}
          </AppButton>,
        ]}
      >
        <div className="may-be-wrong-content">
          <div className="header-mbw-content my-5 text-[16px]">{tScoring("warning1")}</div>
          <div className="result-mbw text-[16px] mb-3">
            {tScoring("have")}: <strong className="text-[16px] text-fill-error">{errorFileCount}</strong> {tScoring("imgError")},{" "}
            <strong className="text-[16px] text-fill-warning">{warningFileCount}</strong> {tScoring("imgWarning")}{" "}
            <strong className="text-[16px] text-fill-error">{mayBeWrong?.length}</strong> {tScoring("img")}
          </div>
          <h3>{tScoring("detailScoring")}</h3>
          <div className="block-mbw-value max-h-[300px] overflow-y-auto">
            {mayBeWrong?.map((item, index) => {
              const splitItem = item.split(",");
              const { imageName, isCaseOne } = extractImageName(item);
              const imgWarning = isCaseOne
                ? `${BASE_RESOURCE_URL_SPRING}/public/shared/data/AnsweredSheets/${examClassCode}/${imageName}`
                : `${BASE_RESOURCE_URL_SPRING}/public/shared/data/AnsweredSheets/${examClassCode}/HandledSheets/handled_${imageName}`;

              return (
                <div key={index}>
                  <div className="mbw-value my-3 flex gap-2">
                    <div className={`${splitItem.length > 1 ? "text-fill-warning" : "text-fill-error"}`}>{index + 1}. </div>
                    <div className="flex-1">{item}</div>
                    <div className="flex items-center gap-2 min-w-[150px]">
                      <div className="ml-4 font-bold">
                        {tScoring("viewImage")} {`=>`}
                      </div>
                      <PreviewImage srcImage={imgWarning} imageName={imageName} className="!w-8" />
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
          <p className="hint-mbw text-text-secondary italic">
            {tScoring("hint1")}
            <br /> {tScoring("hint2")}
          </p>
        </div>
      </AppModal>
    </>
  );
};
export default MayBeWrong;
