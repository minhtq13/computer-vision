import { PAGE_SIZE_OPTIONS } from "@/constants";
import { Pagination, PaginationProps } from "antd";
import { useTranslations } from "next-intl";
import React from "react";
import "./style.scss";

interface AppPaginationProps extends PaginationProps {
  total: number;
  params: any;
  setParams: (params: any) => void;
}

const AppPagination = ({ total, params, setParams, ...props }: AppPaginationProps) => {
  const tCommon = useTranslations("common");
  const customPaginationText = {
    items_per_page: `/ ${tCommon("page")}`,
    jump_to: `${tCommon("toPage")}`,
    page: "",
    prev_page: `${tCommon("prevPage")}`,
    next_page: `${tCommon("nextPage")}`,
  };
  return (
    <Pagination
      locale={customPaginationText}
      total={total}
      pageSizeOptions={PAGE_SIZE_OPTIONS.map((item) => item.value)}
      onChange={(page, pageSize) => {
        setParams({ ...params, page, size: pageSize });
      }}
      showSizeChanger
      showQuickJumper
      showTotal={(total) => `${tCommon("total")} ${total} ${tCommon("items")}`}
      responsive={true}
      {...props}
    />
  );
};

export default AppPagination;
