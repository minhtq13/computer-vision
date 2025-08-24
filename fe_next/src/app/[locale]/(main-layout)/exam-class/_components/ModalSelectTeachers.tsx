import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppTable from "@/components/app-table";
import SearchFilter from "@/components/search-filter";
import { searchTimeDebounce } from "@/constants";
import { useGetTeachersQuery } from "@/stores/teachers/api";
import { Checkbox } from "antd";
import debounce from "lodash/debounce";
import { useTranslations } from "next-intl";
import { useState } from "react";

interface ModalSelectTeachersProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  onSelectTeachers: (lstSupervisorId: string[]) => void;
  lstLecturerId: string[];
  onSelectLecturers: (lstLecturerId: string[]) => void;
  teacherSelected: string[];
  setTeacherSelected: (teacherSelected: string[]) => void;
}

const ModalSelectTeachers = ({
  openModal,
  setOpenModal,
  onSelectTeachers,
  lstLecturerId,
  onSelectLecturers,
  teacherSelected,
  setTeacherSelected,
}: ModalSelectTeachersProps) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const [lecturerSelected, setLecturerSelected] = useState(lstLecturerId ?? []);

  const teacherParamInit = {
    name: null,
    code: null,
    page: 0,
    size: 10,
    sort: "lastModifiedAt",
  };
  const [teacherParam, setTeacherParam] = useState<any>(teacherParamInit);
  const { data: allTeachers, isFetching: tableTeacherLoading } = useGetTeachersQuery(
    { search: teacherParam.search || undefined, page: teacherParam.page, size: teacherParam.size, sort: "lastModifiedAt" },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const teacherColumns = [
    {
      title: tExamClass("supervisorsCode"),
      dataIndex: "code",
      key: "code",
      width: "20%",
      align: "center",
    },
    {
      title: tCommon("fullName"),
      dataIndex: "name",
      key: "name",
      width: "35%",
      align: "center",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "35%",
      align: "center",
    },
    {
      title: tCommon("phoneNumber"),
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      width: "18%",
      align: "center",
    },
    {
      title: tCommon("supervisor"),
      dataIndex: "isExamClassOwner",
      key: "isExamClassOwner",
      width: "12%",
      align: "center",
      render: (_, record) => (
        <Checkbox
          className="flex justify-center"
          defaultChecked={lecturerSelected.includes(record?.id)}
          disabled={!teacherSelected.includes(record?.id)}
          onChange={(e) => {
            const newLecturedChecked =
              e?.target?.checked && teacherSelected.includes(record?.id)
                ? [...lecturerSelected, record?.id]
                : lecturerSelected.filter((id) => id !== record?.id);
            setLecturerSelected(newLecturedChecked);
            onSelectLecturers(newLecturedChecked);
          }}
        />
      ),
    },
  ];
  const handleTeacherSelect = (record, selected) => {
    const updatedSelectedRows = selected ? [...teacherSelected, record.id] : teacherSelected.filter((existingItem) => existingItem !== record.id);

    setTeacherSelected(updatedSelectedRows);
    onSelectTeachers(updatedSelectedRows);
  };
  const onTeacherChange = debounce((_e) => {
    setTeacherParam({ ...teacherParam, search: _e.target.value });
  }, searchTimeDebounce);
  const onTeacherSearch = (value) => {
    setTeacherParam({ ...teacherParam, search: value });
  };

  const rowTeacherSelection = {
    selectedRowKeys: teacherSelected,
    onSelect: handleTeacherSelect,
    hideSelectAll: true,
  };

  const handleSelectTeachers = () => {
    onSelectTeachers(teacherSelected);
    setOpenModal(false);
  };

  const teacherList = allTeachers?.content?.map((obj: any) => ({
    key: obj.id,
    name: obj.lastName + " " + obj.firstName,
    email: obj.email,
    phoneNumber: obj.phoneNumber,

    code: obj.code,
    id: obj.id,
  }));
  return (
    <div>
      {" "}
      <AppModal
        className="exam-class-modal md:!w-[70vw] max-md:!w-full"
        cancelText={tCommon("back")}
        open={openModal}
        title={tExamClass("supervisorList")}
        onOk={() => setOpenModal(false)}
        onCancel={() => setOpenModal(false)}
        maskClosable={true}
        centered={true}
        footer={
          <div className="flex justify-end gap-2">
            <AppButton type="default" onClick={() => setOpenModal(false)}>
              {tCommon("back")}
            </AppButton>
            <AppButton type="primary" onClick={handleSelectTeachers}>
              {tCommon("select")}
            </AppButton>
          </div>
        }
      >
        <div className="selected-number-text">
          {tCommon("selected")}: <span className="text-text-hust font-bold text-base">{teacherSelected.length}</span> {tCommon("supervisor")}
        </div>
        <div className="my-3">
          <SearchFilter displayFilter={false} placeholder={tExamClass("searchTeacher")} onChange={onTeacherChange} onSearch={onTeacherSearch} />
        </div>
        <AppTable
          scroll={{ y: 308, x: 730 }}
          className="teacher-exam-class-table"
          columns={teacherColumns as any}
          dataSource={teacherList}
          rowSelection={rowTeacherSelection as any}
          loading={tableTeacherLoading}
          params={teacherParam}
          setParams={setTeacherParam}
          labelPagination={tCommon("teacher")}
        />
      </AppModal>
    </div>
  );
};

export default ModalSelectTeachers;
