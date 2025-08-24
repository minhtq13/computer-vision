import React, { useRef } from "react";
import AppButton from "@/components/app-button";
import TestPreview2 from "@/components/test-preview/TestPreview2";
import { useTranslations } from "next-intl";
// import dynamic from "next/dynamic";

// const TestPreview2 = dynamic(() => import("../test-preview/TestPreview2"), { ssr: false });

interface ButtonDownloadPdfProps {
  questions: any[];
  testDetail: any;
  testNo: string;
  classNameButton?: string;
  testLanguage?: string;
}

const ButtonDownloadPdf = ({ questions, testDetail, testNo, classNameButton, testLanguage }: ButtonDownloadPdfProps) => {
  const tCommon = useTranslations("common");
  const TestPreview2Ref = useRef<any>(null);
  const handleDownloadPDF = () => {
    if (TestPreview2Ref.current) {
      TestPreview2Ref.current.downloadPDF();
    }
  };

  return (
    <div>
      <AppButton key="submit" type="primary" onClick={handleDownloadPDF} customclass={`!w-full ${classNameButton || ""}`}>
        {tCommon("download")} {tCommon("pdf")}
      </AppButton>
      <TestPreview2 ref={TestPreview2Ref} questions={questions} testDetail={testDetail} testNo={testNo} testLanguage={testLanguage} />
    </div>
  );
};

export default ButtonDownloadPdf;
