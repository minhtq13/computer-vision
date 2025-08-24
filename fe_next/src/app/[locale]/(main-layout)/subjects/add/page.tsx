"use client";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useAddSubjectMutation } from "@/stores/subjects/api";
import { useRouter } from "next/navigation";
import SubjectForm from "../components/SubjectForm";
import { useHandleError } from "@/hooks/useHandleError";
import { RAddSubject } from "@/stores/subjects/type";
import { useTranslations } from "next-intl";
import ButtonBack from "@/components/button-back";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";

const SubjectAdd = () => {
  const tSubjects = useTranslations("subjects");
  const notification = useAppNotification();
  const router = useRouter();
  const [addSubject, { isLoading }] = useAddSubjectMutation();
  const handleError = useHandleError();

  const onFinish = async (values: RAddSubject) => {
    try {
      await addSubject(values).unwrap();
      notification.success({
        description: tSubjects("addSubjectSuccess"),
      });
      router.push(PATH_ROUTER.PROTECTED.SUBJECTS);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <PermissionGuard requiredPermissions={[EPermission.SUBJECT_CREATE]}>
      <div>
        <div className="mb-3">
          <ButtonBack />
        </div>
        <SubjectForm onFinish={onFinish} loading={isLoading} infoHeader={tSubjects("addSubject")} textBtn={tSubjects("addSubject")} />
      </div>
    </PermissionGuard>
  );
};
export default SubjectAdd;
