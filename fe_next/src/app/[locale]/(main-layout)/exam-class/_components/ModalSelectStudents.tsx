import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppTable from "@/components/app-table";
import SearchFilter from "@/components/search-filter";
import { courseNumOptions, searchTimeDebounce } from "@/constants";
import { useGetStudentsQuery } from "@/stores/students/api";
import debounce from "lodash/debounce";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useState } from "react";

interface ModalSelectStudentsProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  onSelectStudents: (lstStudentId: string[]) => void;
  studentSelected: string[];
  setStudentSelected: (studentSelected: string[]) => void;
}

const ModalSelectStudents = ({ openModal, setOpenModal, onSelectStudents, studentSelected, setStudentSelected }: ModalSelectStudentsProps) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const studentParamInit = {
    name: null,
    code: null,
    page: 0,
    size: 10,
    courseNums: null,
    sort: "lastModifiedAt",
  };
  const [studentParam, setStudentParam] = useState<any>(studentParamInit);

  const { data: students, isFetching: tableStudentFetching } = useGetStudentsQuery(
    {
      search: studentParam.search || undefined,
      courseNums: studentParam.courseNums || undefined,
      page: studentParam.page,
      size: studentParam.size,
      sort: "lastModifiedAt",
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const totalElements = get(students, "totalElements", 10);

  const studentList = students?.content?.map((obj: any) => ({
    key: obj.id,
    name: obj.lastName + " " + obj.firstName,
    email: obj.email,
    code: obj.code,
    id: obj.id,
    courseNum: obj.courseNum,
  }));

  const handleStudentSelect = (record, selected) => {
    const updatedSelectedRows = selected ? [...studentSelected, record.id] : studentSelected.filter((existingItem) => existingItem !== record.id);

    setStudentSelected(updatedSelectedRows);
    onSelectStudents(updatedSelectedRows);
  };
  const rowStudentSelection = {
    selectedRowKeys: studentSelected,
    onSelect: handleStudentSelect,
    hideSelectAll: true,
  };

  const onStudentChange = debounce((_e) => {
    setStudentParam({ ...studentParam, search: _e.target.value });
  }, searchTimeDebounce);
  const onStudentSearch = (value) => {
    setStudentParam({ ...studentParam, search: value });
  };
  const onStudentSelect = (options) => {
    setStudentParam({ ...studentParam, courseNums: options });
  };

  const studentColumns = [
    {
      title: tCommon("code"),
      dataIndex: "code",
      key: "code",
      width: "12%",
      align: "center",
    },
    {
      title: tCommon("fullName"),
      dataIndex: "name",
      key: "name",
      width: "33%",
      align: "center",
      // eslint-disable-next-line jsx-a11y/anchor-is-valid
      render: (text) => <a>{text}</a>,
    },
    {
      title: tCommon("courseNum"),
      dataIndex: "courseNum",
      key: "courseNum",
      width: "15%",
      align: "center",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "32%",
      align: "center",
    },
  ];

  const handleSelectStudents = () => {
    onSelectStudents(studentSelected);
    setOpenModal(false);
  };

  return (
    <div>
      <AppModal
        className="exam-class-modal md:!w-[70vw] max-md:!w-full"
        open={openModal}
        cancelText={tCommon("back")}
        title={tExamClass("studentList")}
        onOk={() => setOpenModal(false)}
        onCancel={() => setOpenModal(false)}
        maskClosable={true}
        centered={true}
        footer={
          <div className="flex justify-end gap-2">
            <AppButton type="default" onClick={() => setOpenModal(false)}>
              {tCommon("back")}
            </AppButton>
            <AppButton type="primary" onClick={handleSelectStudents}>
              {tCommon("select")}
            </AppButton>
          </div>
        }
      >
        <div className="selected-number-text">
          {tCommon("selected")}: <span className="text-text-hust font-bold text-base">{studentSelected.length}</span> {tCommon("student")}
        </div>
        <div className="my-3">
          <SearchFilter
            displayFilter
            placeholder={tExamClass("search")}
            options={courseNumOptions}
            onSearch={onStudentSearch}
            onChange={onStudentChange}
            onSelect={onStudentSelect}
          />
        </div>
        <AppTable
          scroll={{ y: 308, x: 500 }}
          className="student-exam-class-table"
          columns={studentColumns as any}
          dataSource={studentList}
          total={totalElements}
          rowSelection={rowStudentSelection as any}
          loading={tableStudentFetching}
          params={studentParam}
          setParams={setStudentParam}
          labelPagination={tCommon("student")}
        />
      </AppModal>
    </div>
  );
};

export default ModalSelectStudents;
