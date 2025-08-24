"use client";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetExamClassDetailQuery, useUpdateExamClassMutation } from "@/stores/exam-class/api";
import { Skeleton } from "antd";
import dayjs from "dayjs";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useTranslations } from "next-intl";
import ButtonBack from "@/components/button-back";
import ExamClassForm from "../../_components/form";
import { EPermission } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";

const ExamClassEdit = ({ params }: { params: { id: string } }) => {
  const tExamClass = useTranslations("examClass");
  const tCommon = useTranslations("common");
  const router = useRouter();
  const [selectedTestId, setSelectedTestId] = useState(null);
  const [lstStudentId, setLstStudentId] = useState(null);
  const [lstSupervisorId, setLstSupervisorId] = useState([]);
  const [lstLecturerId, setLstLecturerId] = useState([]);
  const notification = useAppNotification();
  const handleError = useHandleError();

  const [updateExamClass, { isLoading: isLoadingUpdate }] = useUpdateExamClassMutation();

  const id = params.id;

  const { data: examClassInfo, isLoading: infoLoading } = useGetExamClassDetailQuery(
    { examClassId: id },
    {
      refetchOnMountOrArgChange: true,
      skip: !id,
    }
  );

  const onFinish = async (value) => {
    const studentIds = lstStudentId ? lstStudentId : examClassInfo?.lstStudentId ? examClassInfo?.lstStudentId.split(",").map(Number) : [];
    const supervisorIds =
      lstSupervisorId && lstSupervisorId.length > 0
        ? lstSupervisorId
        : examClassInfo?.lstSupervisorId
        ? examClassInfo?.lstSupervisorId.split(",").map(Number)
        : [];
    const lecturerIds = lstLecturerId && lstLecturerId.length > 0 ? lstLecturerId : examClassInfo?.lstLecturerId;

    try {
      await updateExamClass({
        id,
        ...value,
        testId: selectedTestId ?? examClassInfo?.testId,
        examineTime: dayjs(value.examineTime).format("HH:mm DD/MM/YYYY"),
        lstStudentId: studentIds,
        lstSupervisorId: supervisorIds,
        lstLecturerId: lecturerIds.filter((id) => supervisorIds.includes(id)),
      }).unwrap();
      notification.success({ description: tExamClass("updateExamClassSuccess") });
      router.push(PATH_ROUTER.PROTECTED.EXAM_CLASS);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.EXAM_CLASS_EDIT]}>
      <div className="exam-class-edit">
        <ButtonBack />
        <Skeleton active loading={infoLoading}>
          <ExamClassForm
            infoHeader={tExamClass("updateExamClass")}
            semesterDisabled={true}
            onFinish={onFinish}
            btnText={tCommon("update")}
            initialValues={{
              remember: false,
              subjectId: examClassInfo ? examClassInfo.subjectId : null,
              semesterId: examClassInfo ? examClassInfo.semesterId : null,
              roomName: examClassInfo ? examClassInfo.roomName : null,
              examineTime: examClassInfo?.examineTime ? dayjs(`${examClassInfo?.examineTime} ${examClassInfo?.examineDate}`, "HH:mm DD-MM-YYYY") : "",
              code: examClassInfo ? examClassInfo?.code : null,
              testId: examClassInfo ? examClassInfo?.testId : null,
              testType: (examClassInfo?.testType ?? "").toUpperCase(),
              existedResult: examClassInfo?.existedResult,
            }}
            testDisplay={`${examClassInfo?.testName} - ${examClassInfo?.duration ?? 0} ${tCommon("minutes")} - ${
              examClassInfo?.lstTestSetCode ? examClassInfo?.lstTestSetCode.split(",").length : 0
            } ${tCommon("examCode")}`}
            lstStudentId={examClassInfo && examClassInfo?.lstStudentId ? examClassInfo?.lstStudentId.split(",").map(Number) : []}
            lstSupervisorId={examClassInfo && examClassInfo?.lstSupervisorId ? examClassInfo?.lstSupervisorId.split(",").map(Number) : []}
            lstLecturerId={examClassInfo?.lstLecturerId ?? []}
            loading={isLoadingUpdate}
            testId={examClassInfo?.testId}
            onSelectTestId={(id) => setSelectedTestId(id)}
            onSelectStudents={(ids) => setLstStudentId(ids)}
            onSelectTeachers={(ids) => setLstSupervisorId(ids)}
            onSelectLecturers={(ids) => setLstLecturerId(ids)}
            testType={(examClassInfo?.testType ?? "").toUpperCase()}
            action="EDIT"
          />
        </Skeleton>
      </div>
    </PermissionGuard>
  );
};
export default ExamClassEdit;
