"use client";
import AppButton from "@/components/app-button";
import AppSelectSmall from "@/components/app-select-small";
import ButtonDownloadPdf from "@/components/button-download-pdf";
import TestPreview from "@/components/test-preview/TestPreview";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { useGetTestSetDetailMutation } from "@/stores/test-set/api";
import { useLocale, useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import IconArrow from "@/assets/images/svg/arrow-under-header.svg";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";

const PreviewTestPage = ({ params }: { params: { testId: string; testNo: string } }) => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const testId = params.testId as string;
  const testNo = params.testNo as string;
  const [testDetail, setTestDetail] = useState<any>([]);
  const [getTestSetDetail, { isLoading, isError }] = useGetTestSetDetailMutation();
  const router = useRouter();
  const TestPreviewRef = useRef<any>(null);
  const locale = useLocale();
  const [testLanguage, setTestLanguage] = useState(locale);
  const handleChangeTestLanguage = (value) => {
    setTestLanguage(value);
  };
  const handleDownloadHTML = () => {
    if (TestPreviewRef.current) {
      TestPreviewRef.current.downloadHTML();
    }
  };

  useEffect(() => {
    (async () => {
      if (!testId || !testNo) return;
      try {
        const res = await getTestSetDetail({ testId: Number(testId), code: testNo }).unwrap();
        setTestDetail(res);
      } catch (error) {
        console.log(error);
      }
    })();
  }, [testId, testNo]);
  const handleEdit = () => {
    router.push(`${PATH_ROUTER.DETAIL.TESTS_EDIT(testId, testNo)}`);
  };

  if (isError) return <div>{tTests("notFoundTest")}</div>;

  return (
    <PermissionGuard requiredPermissions={[EPermission.TEST_DETAIL]}>
      <WrapperForm>
        <div className="flex gap-4 justify-between relative max-xl:flex-col ">
          <div className="sticky top-10 h-max max-xl:mb-4 max-xl:static">
            <p className="text-2xl font-bold text-hust my-3">{tTests("viewTest")}</p>
            <AppSelectSmall
              rootClassName="w-full [&>.ant-select-selector]:w-full"
              title={tCommon("testLanguage")}
              optionLabelProp="label"
              suffixIcon={<IconArrow />}
              className="custom-select-antd"
              placeholder={"Test Language"}
              onChange={handleChangeTestLanguage}
              value={testLanguage}
              options={[
                { value: "vi", label: "Vietnamese" },
                { value: "en", label: "English" },
              ]}
            />
            <div className="flex flex-col gap-4 mt-10 max-xl:grid max-xl:grid-cols-3">
              <AppButton key="update" type="primary" onClick={handleEdit} customclass="!w-full">
                {tCommon("update")}
              </AppButton>
              <AppButton key="submit" type="primary" onClick={handleDownloadHTML} customclass="!w-full">
                {tTests("downloadHtml")}
              </AppButton>
              <ButtonDownloadPdf testLanguage={testLanguage} questions={testDetail?.lstQuestion} testDetail={testDetail?.testSet} testNo={testNo} />
            </div>
          </div>
          {isLoading ? (
            <div>Loading...</div>
          ) : (
            <div className="flex-1">
              <TestPreview
                testLanguage={testLanguage}
                ref={TestPreviewRef}
                questions={testDetail?.lstQuestion}
                testDetail={testDetail?.testSet}
                testNo={testNo}
              />
            </div>
          )}
        </div>
      </WrapperForm>
    </PermissionGuard>
  );
};

export default PreviewTestPage;
