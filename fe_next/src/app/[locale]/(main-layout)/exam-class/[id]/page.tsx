"use client";
import AppTooltip from "@/components/app-tooltip";
import { PATH_ROUTER } from "@/constants/router";
import useRole from "@/hooks/useRole";
import { useGetExamClassDetailQuery } from "@/stores/exam-class/api";
import { useGetResultsQuery } from "@/stores/student-test-set/api";
import { EditOutlined } from "@ant-design/icons";
import { Tabs } from "antd";
import { useRouter } from "next/navigation";
import Statistics from "./_components/Statistics";
import Student from "./_components/Student";
import SuperVisor from "./_components/SuperVisor";
import { useTranslations } from "next-intl";
import ButtonBack from "@/components/button-back";
import AppButton from "@/components/app-button";
import { EPermission, RoleBase } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";

const ExamClassDetail = ({ params }: { params: { id: string } }) => {
  const tExamClass = useTranslations("examClass");
  const tCommon = useTranslations("common");
  const examClassId = params?.id;
  const router = useRouter();
  const { role } = useRole();

  const { data: examClassDetail, refetch: refetchExamClassDetail } = useGetExamClassDetailQuery(
    { examClassId },
    {
      refetchOnMountOrArgChange: true,
      skip: !examClassId,
    }
  );
  const { data: results } = useGetResultsQuery(
    { examClassCode: examClassDetail?.code },
    {
      refetchOnMountOrArgChange: true,
      skip: !examClassDetail?.code,
    }
  );

  const tabsOptions = [
    {
      key: "STUDENT",
      label: <h3>{tCommon("student")}</h3>,
      children: (
        <Student examClassId={examClassId} examClassDetail={examClassDetail} refetchExamClassDetail={refetchExamClassDetail} results={results} />
      ),
    },
    {
      key: "SUPERVISOR",
      label: <h3>{tCommon("supervisors")}</h3>,
      children: <SuperVisor examClassId={examClassId} examClassDetail={examClassDetail} />,
    },
    role !== RoleBase.STUDENT && {
      key: "STATISTIC",
      label: <h3>{tExamClass("statistics")}</h3>,
      children: <Statistics results={results} />,
    },
  ];

  return (
    <PermissionGuard requiredPermissions={[EPermission.EXAM_CLASS_VIEW]}>
      <div>
        <ButtonBack />
        <div className="flex items-center gap-2">
          <p className="info-header text-[24px] font-bold text-text-hust py-5 max-lg:py-2">{tExamClass("examClassDetail")}</p>
          <AppButton onClick={() => router.push(`${PATH_ROUTER.DETAIL.EXAM_CLASS_EDIT(examClassId)}`)}>
            <AppTooltip title={tExamClass("updateExamClass")}>
              <EditOutlined />
            </AppTooltip>
          </AppButton>
        </div>
        <div className="exam-class-participant-details">
          <div className="exam-class-info-details text-[14px] flex justify-between flex-wrap gap-4">
            <div className="exam-class-participant-left">
              <div>
                {`${tCommon("subject")}: `}
                <span className="text-sm font-medium">{examClassDetail?.subjectTitle}</span>
              </div>
              <div>
                {`${tCommon("examClassCode")}: `}
                <span className="text-sm font-medium">{examClassDetail?.code}</span>
              </div>
            </div>
            <div className="exam-class-participant-right">
              <div>
                {`${tCommon("semester")}: `}
                <span className="text-sm font-medium">{examClassDetail?.semester}</span>
              </div>
              <div>
                {`${tExamClass("roomName")}: `}
                <span className="text-sm font-medium">{examClassDetail?.roomName}</span>
              </div>
            </div>
            <div className="exam-class-participant-right">
              <div>
                {`${tExamClass("date")}: `}
                <span className="text-sm font-medium">{examClassDetail?.examineDate}</span>
              </div>
              <div>
                {`${tExamClass("time")}: `}
                <span className="text-sm font-medium">{examClassDetail?.examineTime}</span>
              </div>
            </div>
            <div className="exam-class-participant-right">
              <div>
                {`${tCommon("testType")}: `}
                <span className="text-sm font-medium">{examClassDetail?.testType}</span>
              </div>
              <div>
                {`${tCommon("status")}: `}
                {results?.results?.length > 0 ? (
                  <span className="text-sm font-medium">{tCommon("hasResult")}</span>
                ) : (
                  <span className="text-sm font-medium text-text-hust">{tExamClass("noResult")}</span>
                )}
              </div>
            </div>
          </div>
        </div>
        <Tabs defaultActiveKey="STUDENT" items={tabsOptions} />
      </div>
    </PermissionGuard>
  );
};

export default ExamClassDetail;
