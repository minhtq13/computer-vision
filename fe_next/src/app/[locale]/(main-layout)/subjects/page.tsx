"use client";
import { useState } from "react";

import AddIcon from "@/assets/images/svg/add-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppTable from "@/components/app-table";
import SearchFilter from "@/components/search-filter";
import { searchTimeDebounce } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import useRole from "@/hooks/useRole";
import { useAppDispatch } from "@/libs/redux/store";
import { setSelectedItem } from "@/stores/app";
import { useGetSubjectsQuery } from "@/stores/subjects/api";
import { EPermission } from "@/types/enum";
import debounce from "lodash/debounce";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";

const SubjectList = () => {
  const tSubjects = useTranslations("subjects");
  const tCommon = useTranslations("common");
  const router = useRouter();
  const dispatch = useAppDispatch();
  const { checkPermission } = useRole();
  const initialParam = {
    search: "",
    page: 0,
    size: 10,
    // sort: "DESC",
  };

  const [param, setParam] = useState(initialParam);

  const { data: subjects, isLoading: subjectsLoading } = useGetSubjectsQuery(param, {
    refetchOnMountOrArgChange: true,
  });

  // const handleEdit = (record: any) => {
  //   router.push(PATH_ROUTER.DETAIL.SUBJECT_DETAIL(record.id));
  // };
  const handleView = (record: any) => {
    // querySubjectDetail({}, record.id);

    router.push(PATH_ROUTER.DETAIL.SUBJECT_DETAIL(record.id));
  };

  const onRow = (record: any) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const columns = [
    {
      title: tSubjects("subjectCode"),
      dataIndex: "code",
      key: "code",
      width: "15%",
      align: "center",
    },
    {
      title: tSubjects("subjectName"),
      dataIndex: "title",
      key: "title",
      // eslint-disable-next-line jsx-a11y/anchor-is-valid
      render: (text) => <a>{text}</a>,
      width: "40%",
    },
    {
      title: tSubjects("subjectCredit"),
      dataIndex: "credit",
      key: "credit",
      width: "10%",
      align: "center",
    },
    {
      title: tSubjects("departmentName"),
      dataIndex: "departmentName",
      key: "departmentName",
      width: "25%",
      align: "center",
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      width: "10%",
      render: (_, record) => (
        <div className="cursor-pointer flex items-center justify-center gap-2">
          <ActionButton icon="content" handleClick={() => handleView(record)} />
        </div>
      ),
    },
  ];

  const dataFetch = subjects?.content?.map((obj: any, index: number) => ({
    key: (index + 1).toString(),
    title: obj?.title,
    credit: obj?.credit,
    description: obj?.description,
    code: obj?.code,
    id: obj?.id,
    departmentName: obj?.departmentName,
  }));
  const handleClickAddSubject = () => {
    router.push(PATH_ROUTER.PROTECTED.ADD_SUBJECT);
  };

  const onSearch = (value) => {
    setParam({ ...param, search: value });
  };

  const onChange = debounce((_e) => {
    setParam({ ...param, search: _e.target.value });
  }, searchTimeDebounce);
  return (
    <div className="h-full w-full">
      <div className="lg:py-4">
        <p className="text-2xl font-bold text-hust my-3">{tSubjects("title")}</p>
      </div>
      <div className="flex justify-between my-4 max-2xl:gap-4 max-md:flex-col">
        <SearchFilter placeholder={tSubjects("enterSubjectName")} onChange={onChange} onSearch={onSearch} displayFilter={false} options={[]} />
        <div className="block-button">
          {checkPermission(EPermission.SUBJECT_CREATE) && (
            <AppButton className="options" typeButton="outline" onClick={handleClickAddSubject} classChildren="flex items-center gap-2">
              <AddIcon />
              {tSubjects("addSubject")}
            </AppButton>
          )}
        </div>
      </div>
      <div className="subject-list-wrapper">
        <AppTable
          labelPagination={tCommon("subject")}
          className="subject-list-table"
          columns={columns as any}
          dataSource={dataFetch}
          // rowSelection={rowSelection}
          onRow={onRow}
          loading={subjectsLoading}
          total={subjects?.totalElements}
          setParams={setParam}
          params={param}
        />
      </div>
    </div>
  );
};
export default SubjectList;
