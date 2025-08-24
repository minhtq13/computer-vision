"use client";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useCreateExamClassMutation } from "@/stores/exam-class/api";
import { dateTimePattern } from "@/constants";
import dayjs from "dayjs";
import { useRouter } from "next/navigation";
import { useState } from "react";
import ExamClassForm from "../_components/form";
import { useTranslations } from "next-intl";
import { PATH_ROUTER } from "@/constants/router";
import ButtonBack from "@/components/button-back";
import { useHandleError } from "@/hooks/useHandleError";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";

const ExamClassCreate = () => {
  const tExamClass = useTranslations("examClass");
  const tCommon = useTranslations("common");
  const router = useRouter();
  const notification = useAppNotification();
  const [selectedTestId, setSelectedTestId] = useState<any>(null);
  const [lstStudentId, setLstStudentId] = useState<any>([]);
  const [lstSupervisorId, setLstSupervisorId] = useState<any>([]);
  const [lstLecturerId, setLstLecturerId] = useState<any>([]);
  const [createExamClass, { isLoading: isLoadingCreate }] = useCreateExamClassMutation();
  const handleError = useHandleError();
  const onFinish = async (value) => {
    try {
      await createExamClass({
        ...value,
        examineTime: dayjs(value.examineTime).format(dateTimePattern.FORMAT_DATE_HH_MM_YYYY_HH_MM),
        testId: selectedTestId,
        lstStudentId: lstStudentId,
        lstSupervisorId: lstSupervisorId,
        lstLecturerId: lstLecturerId.filter((id) => lstSupervisorId.includes(id)),
        fromExamClassId: value?.fromExamClassId ? value?.fromExamClassId : null,
      }).unwrap();
      notification.success({
        description: tExamClass("createExamClassSuccess"),
      });
      router.push(PATH_ROUTER.PROTECTED.EXAM_CLASS);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.EXAM_CLASS_CREATE]}>
      <div className="exam-class-add">
        <ButtonBack />
        <ExamClassForm
          infoHeader={tExamClass("createExamClass")}
          onFinish={onFinish}
          btnText={tCommon("add")}
          initialValues={{ remember: false }}
          loading={isLoadingCreate}
          onSelectTestId={(id) => setSelectedTestId(id)}
          onSelectStudents={(ids) => setLstStudentId(ids)}
          onSelectTeachers={(ids) => setLstSupervisorId(ids)}
          onSelectLecturers={(ids) => setLstLecturerId(ids)}
          action="CREATE"
        />
      </div>
    </PermissionGuard>
  );
};
export default ExamClassCreate;
