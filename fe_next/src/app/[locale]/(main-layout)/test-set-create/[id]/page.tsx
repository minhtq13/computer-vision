"use client";
import ButtonBack from "@/components/button-back";
import WrapperForm from "@/components/wrapper-form";
import { useGetTestDetailQuery } from "@/stores/tests/api";
import { Tabs } from "antd";
import { useTranslations } from "next-intl";
import { useState } from "react";
import TestDetailsEdit from "./_components/TestDetailsEdit";
import TestSetCreateAuto from "./_components/TestSetCreateAuto";
import TestSetCreateManual from "./_components/TestSetCreateManual";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";

const TestSetCreate = ({ params }: { params: any }) => {
  const tTestSetCreate = useTranslations("testSetCreate");
  const tCommon = useTranslations("common");
  const testId = params?.id;
  const { data: testDetail } = useGetTestDetailQuery({ testId: testId });
  const [tabs, setTabs] = useState("auto");

  const items = [
    {
      key: "auto",
      label: <h3 className="text-[14px] font-medium">{tTestSetCreate("createAuto")}</h3>,
      children: <TestSetCreateAuto testId={testId} />,
    },
    {
      key: "manual",
      label: <h3 className="text-[14px] font-medium">{tTestSetCreate("createManual")}</h3>,
      children: <TestSetCreateManual testDetail={testDetail} />,
    },
    {
      key: "edit",
      label: <h3 className="text-[14px] font-medium">{tTestSetCreate("edit")}</h3>,
      children: (
        <TestDetailsEdit
          testId={testId}
          initialValues={{
            questionQuantity: testDetail?.questionQuantity,
            numEasyQuestion: testDetail?.generateConfig?.numEasyQuestion,
            numMediumQuestion: testDetail?.generateConfig?.numMediumQuestion,
            numHardQuestion: testDetail?.generateConfig?.numHardQuestion,
            duration: testDetail?.duration,
            totalPoint: testDetail?.totalPoint,
            subjectCode: testDetail?.subjectCode,
            subjectName: testDetail?.subjectName,
          }}
        />
      ),
    },
  ];
  const renderLevel = () => {
    if (tabs === "auto") {
      if (testDetail?.generateConfig) {
        return `(${testDetail?.generateConfig?.numEasyQuestion} dễ, 
          ${testDetail?.generateConfig?.numMediumQuestion} trung bình, ${testDetail?.generateConfig?.numHardQuestion} khó)`;
      }
    } else return "";
  };

  const TEST_SET_INFO = [
    {
      label: tCommon("subject"),
      value: `${testDetail?.subjectName} - ${testDetail?.subjectCode}` || "",
    },
    {
      label: tCommon("test"),
      value: `${testDetail?.name} - ${testDetail?.testType}`,
    },
    {
      label: tCommon("semester"),
      value: testDetail?.semester,
    },
    {
      label: tCommon("question"),
      value: `${testDetail?.questionQuantity} ${renderLevel()}`,
    },
    {
      label: tTestSetCreate("time"),
      value: `${testDetail?.duration} ${tCommon("minutes")}`,
    },
    {
      label: tTestSetCreate("testSet"),
      value: testDetail?.lstTestSetCode ? testDetail?.lstTestSetCode.split(",").join(", ") : "",
    },
  ];

  const handleChange = (e) => {
    setTabs(e);
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.TEST_CREATE]}>
      <div className="test-set-create">
        <ButtonBack />
        <p className="info-header text-[24px] font-bold text-text-hust py-5 max-lg:py-2">{tTestSetCreate("testSetDetail")}</p>
        <WrapperForm>
          <div className="grid grid-cols-3 gap-3 justify-between w-full text-[14px]">
            {TEST_SET_INFO.map((item, index) => (
              <div className="test-create-info-row" key={index}>
                <span>{item.label}: </span>
                <span className="font-bold">{item.value}</span>
              </div>
            ))}
          </div>
        </WrapperForm>
        <Tabs defaultActiveKey={tabs} items={items} className="test-content" onChange={handleChange}></Tabs>
      </div>
    </PermissionGuard>
  );
};
export default TestSetCreate;
