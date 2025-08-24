"use client";
import AddIcon from "@/assets/images/svg/add-icon.svg";
import deletePopUpIcon from "@/assets/images/svg/delete-popup-icon.svg";
import AppButton from "@/components/app-button";
import AppPagination from "@/components/app-pagination";
import AppSelectSmall from "@/components/app-select-small";
import AppTooltip from "@/components/app-tooltip";
import FilterByTag from "@/components/filter-by-tag";
import HtmlRenderer from "@/components/html-render";
import ModalPopup from "@/components/modal-popup";
import ScrollToTop from "@/components/scroll-to-top";
import WrapperForm from "@/components/wrapper-form";
import { API_PATH } from "@/constants/apiPath";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { DEFAULT_FILTER_QUESTIONS, useFilterQuestions } from "@/hooks/useFilterQuestions";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import Storage from "@/libs/storage";
import { useGetComboChapterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useImportQuestionMutation } from "@/stores/import-export/api";
import { useDeleteQuestionMutation, useGetPaginationQuestionsQuery } from "@/stores/questions/api";
import { searchTimeDebounce } from "@/constants";
import { getStaticFile, tagRender } from "@/helpers/tools";
import { DeleteOutlined, EditOutlined, FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import { Input, Spin, Tag } from "antd";
import debounce from "lodash.debounce";
import get from "lodash/get";
import isEqual from "lodash/isEqual";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useRef, useState } from "react";
import NoData from "@/components/no-data";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission, ERole } from "@/types/enum";
import useRole from "@/hooks/useRole";

const Questions = () => {
  const tQuestion = useTranslations("questions");
  const tCommon = useTranslations("common");
  const { levelOptions, renderTag } = useLocaleOptions();
  const fileInputRef = useRef(null);
  const [fileList, setFileList] = useState(null);
  const router = useRouter();
  const handleError = useHandleError();
  const notification = useAppNotification();
  const [importQuestion, { isLoading: loadingImport }] = useImportQuestionMutation();
  const filterQuestions = useFilterQuestions();
  const [searchValue, setSearchValue] = useState("");
  const { checkPermission } = useRole();

  const { data: allChapters, isLoading: chapterLoading } = useGetComboChapterQuery(
    { subjectId: Number(filterQuestions.subjectId) },
    {
      skip: !filterQuestions.subjectId,
    }
  );
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});

  const { data: allQuestions, isFetching: quesLoading } = useGetPaginationQuestionsQuery(
    {
      ...filterQuestions,
      page: filterQuestions.page - 1,
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const totalQuestions = get(allQuestions, "totalElements", 0);
  const [deleteQuestion, { isLoading: deleteLoading }] = useDeleteQuestionMutation();

  const chapterOptions = getOptionsFromCombo(allChapters);

  const handleImportQuestion = async () => {
    try {
      const formData = new FormData();
      formData.append("file", fileList);
      await importQuestion({ file: formData });
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }
      setFileList(null);
      notification.success({
        description: tQuestion("importSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleFileInputChange = (e: any) => {
    setFileList(e.target.files[0]);
  };
  const subjectOnChange = (value: number) => {
    Storage.updateFilterQuestions({ subjectId: value, chapterIds: [] });
  };
  const chapterOnchange = (values: number[]) => {
    if (values.includes(0)) {
      Storage.updateFilterQuestions({ chapterIds: chapterOptions.filter((item) => item.value !== 0).map((item) => item.value) });
    } else {
      Storage.updateFilterQuestions({ chapterIds: values });
    }
  };
  const onRemove = async (id: any) => {
    try {
      await deleteQuestion({ questionsId: id }).unwrap();
      notification.success({
        description: tQuestion("deleteQuestionSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };
  const onEdit = (item: any) => {
    router.push(PATH_ROUTER.DETAIL.QUESTION_EDIT(item.id));
  };
  const tagOnchange = (value: any) => {
    Storage.updateFilterQuestions({ tagId: value ? Number(value) : undefined });
  };

  const levelOnchange = (value: string) => {
    Storage.updateFilterQuestions({ level: value });
  };

  const debouncedSearch = useCallback(
    debounce((value) => {
      Storage.updateFilterQuestions({ search: value });
    }, searchTimeDebounce),
    []
  );

  const handleSearch = (e: any) => {
    setSearchValue(e);
    if (e) debouncedSearch(e);
  };

  const handleClearFilter = () => {
    setSearchValue("");
    Storage.resetFilterQuestions();
  };
  const handleClearSearch = () => {
    setSearchValue("");
    Storage.updateFilterQuestions({ search: undefined });
  };
  useEffect(() => {
    setSearchValue(filterQuestions.search);
  }, [filterQuestions.search]);

  const hasFilter = !isEqual(filterQuestions, DEFAULT_FILTER_QUESTIONS);

  return (
    <PermissionGuard requiredRoles={[ERole.ADMIN, ERole.TEACHER]}>
      <div className="question-list">
        <div className="header-test-list lg:py-3">
          <p className="text-2xl font-bold text-hust my-3 mb-3">{tQuestion("questionBank")}</p>
        </div>
        <WrapperForm>
          <div className="subject-chapters-top flex gap-4 flex-wrap flex-col">
            <div className="flex gap-4 flex-wrap justify-between">
              <div className="subject-chapter flex gap-4 max-lg:flex-col w-full">
                <AppSelectSmall
                  value={filterQuestions.subjectId}
                  customClassName="lg:max-xl:w-1/2 md:min-w-[350px]"
                  rootClassName="[&>.ant-select-selector]:w-full w-full"
                  title={tCommon("subject")}
                  allowClear
                  showSearch
                  placeholder={tCommon("subject")}
                  optionFilterProp="children"
                  optionLabelProp="label"
                  options={getOptionsFromCombo(allSubjects)}
                  onChange={subjectOnChange}
                  loading={subLoading}
                />
                <AppSelectSmall
                  customClassName="lg:max-xl:w-1/2 md:min-w-[350px]"
                  rootClassName="[&>.ant-select-selector]:w-full w-full"
                  title={tCommon("chapter")}
                  disabled={!filterQuestions.subjectId}
                  mode="multiple"
                  showSearch
                  allowClear
                  placeholder={tCommon("chapter")}
                  optionFilterProp="children"
                  optionLabelProp="label"
                  options={[{ value: 0, label: tCommon("selectAll") }, ...chapterOptions]}
                  onChange={chapterOnchange}
                  loading={chapterLoading}
                  value={filterQuestions.chapterIds}
                />
              </div>
              <div className="flex items-center gap-2 flex-wrap">
                <Input
                  allowClear
                  id="input-import"
                  className="!w-[300px] !text-[12px] !cursor-pointer"
                  type="file"
                  name="file"
                  onChange={(e) => handleFileInputChange(e)}
                />
                {checkPermission(EPermission.QUESTION_CREATE) && (
                  <AppTooltip className="options" title="Import câu hỏi">
                    <AppButton type="primary" onClick={handleImportQuestion} disabled={!fileList} loading={loadingImport}>
                      <ImportOutlined style={{ color: "#ffffff" }} />
                    </AppButton>
                  </AppTooltip>
                )}
                <AppTooltip className="options" title="File mẫu">
                  <AppButton type="primary" onClick={() => getStaticFile(API_PATH.QUESTION_IMPORT_TEMPLATE)}>
                    <FileExcelFilled style={{ color: "#ffffff" }} />
                  </AppButton>
                </AppTooltip>
                {checkPermission(EPermission.QUESTION_CREATE) && (
                  <AppButton className="options" onClick={() => router.push(PATH_ROUTER.PROTECTED.ADD_QUESTIONS)}>
                    <AddIcon />
                    {tQuestion("addQuestion")}
                  </AppButton>
                )}
              </div>
            </div>
            <div className="search-level flex items-end gap-4 flex-wrap">
              <div className="list-search max-md:w-full">
                <span className="text-[13px] text-text-secondary flex flex-row items-center mb-1.5 font-medium">{tCommon("search")}:</span>
                <Input.Search
                  className="md:!min-w-[320px]"
                  value={searchValue}
                  placeholder={tQuestion("inputQuestion")}
                  enterButton
                  allowClear
                  onClear={handleClearSearch}
                  onChange={(e) => handleSearch(e.target.value)}
                />
              </div>
              <div className="test-level">
                <AppSelectSmall
                  className="[&>.ant-select-selector]:min-w-[100px]"
                  title={tCommon("level")}
                  value={filterQuestions.level}
                  optionLabelProp="label"
                  options={levelOptions}
                  onChange={levelOnchange}
                />
              </div>
              <div className="test-level">
                <FilterByTag value={filterQuestions.tagId} onChange={tagOnchange} mode={null} canAddTag={false} />
              </div>
              <div className="flex items-center gap-2">
                {hasFilter && (
                  <div className="flex items-end h-full">
                    <AppButton className="!text-[12px] !px-4" onClick={() => handleClearFilter()}>
                      Clear Filter
                    </AppButton>
                  </div>
                )}
              </div>
            </div>{" "}
          </div>
        </WrapperForm>
        <ScrollToTop />
        {totalQuestions > 0 && (
          <WrapperForm className="mt-4">
            <div className="flex items-center gap-4 justify-center">
              <AppPagination
                total={totalQuestions}
                params={filterQuestions}
                setParams={Storage.updateFilterQuestions}
                current={filterQuestions.page}
                pageSize={filterQuestions.size}
              />
            </div>
          </WrapperForm>
        )}
        <Spin className="all-question-container min-h-[200px]" spinning={quesLoading} tip="Loading...">
          <h2 className="italic text-text-hust my-3">{tQuestion("warning3")}</h2>
          {allQuestions?.content?.map((item, index) => {
            return (
              <div className="question-items mb-4 border border-disable-secondary p-4" key={`index-${item.id}`}>
                <div className="flex justify-between gap-4">
                  <div className="topic-remove flex gap-1 flex-col flex-1">
                    <div className="question-topic !mb-2 items-start flex text-base gap-1">
                      <div className="text-base font-medium flex-shrink-0">{`${tCommon("question")} ${
                        (filterQuestions.page - 1) * filterQuestions.size + index + 1
                      }: `}</div>
                      <div className="text-[14px]">
                        <HtmlRenderer htmlContent={item.content} />
                      </div>
                    </div>

                    {item.lstAnswer &&
                      item.lstAnswer.map((ans, ansNo) => {
                        return (
                          <div className={ans.isCorrect ? "text-base gap-1 flex text-fill-success" : "text-base gap-1 flex"} key={`answer${ansNo}`}>
                            <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                            <div className="text-[14px]">
                              <HtmlRenderer htmlContent={ans.content} />
                            </div>
                          </div>
                        );
                      })}
                  </div>

                  <div className="md:w-[200px]">
                    <div className="btn-space flex gap-2 justify-end max-md:flex-col">
                      <Tag className="font-bold !m-0 !px-2 !py-1 !flex items-center justify-center w-max" color={tagRender(item.level)}>
                        {renderTag(item)}
                      </Tag>
                      {checkPermission(EPermission.QUESTION_UPDATE) && (
                        <AppButton onClick={() => onEdit(item)}>
                          <EditOutlined />
                        </AppButton>
                      )}
                      {checkPermission(EPermission.QUESTION_DELETE) && (
                        <ModalPopup
                          buttonDisable={deleteLoading}
                          buttonOpenModal={
                            <AppButton>
                              <DeleteOutlined />
                            </AppButton>
                          }
                          title={tQuestion("deleteQuestion")}
                          message={tQuestion("deleteQuestionConfirm")}
                          ok={tCommon("ok")}
                          icon={deletePopUpIcon}
                          onAccept={() => onRemove(item.id)}
                        />
                      )}
                    </div>
                    <div className="flex gap-2 justify-end flex-wrap mt-3 max-md:flex-col">
                      {item?.tags?.map((tag: { id: number; name: string }) => (
                        <Tag color="var(--color-text-primary-1)" key={tag.id}>
                          {tag.name}
                        </Tag>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </Spin>
        {totalQuestions === 0 && !quesLoading && <NoData />}
      </div>
    </PermissionGuard>
  );
};
export default Questions;
