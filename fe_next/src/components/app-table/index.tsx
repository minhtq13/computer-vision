"use client";
import { Table, TableProps } from "antd";
import { useEffect } from "react";
import "./style.scss";
import { PAGINATION_OPTIONS, SCROLL_TABLE_HEIGHT, SCROLL_TABLE_WIDTH } from "@/constants";
import { useTranslations } from "next-intl";

interface AppTableProps extends TableProps {
  total?: number;
  labelPagination?: string;
  setParams?: (value: any) => void;
  params?: any;
  customClassName?: string;
}

const AppTable = (props: AppTableProps) => {
  const tCommon = useTranslations("common");
  // const handleLocaleTable = () => {
  //   if (props.loading) {
  //     return null;
  //   }
  //   if (!props.loading && props.dataSource?.length === 0) {
  //     return <Empty description="No Data"></Empty>;
  //   }
  // };

  useEffect(() => {
    document.querySelectorAll('[aria-label="Page"]').forEach((element) => {
      element.addEventListener("keydown", function (event: any) {
        const key = event.key;

        // Allow only numbers and control keys like 'Backspace', 'Enter', 'Arrow keys'
        if (
          !/^\d$/.test(key) && // Check if it's not a digit
          key !== "Backspace" && // Allow backspace
          key !== "Enter" && // Allow enter
          key !== "ArrowLeft" && // Allow left arrow
          key !== "ArrowRight" && // Allow right arrow
          key !== "Tab" && // Allow tab
          key !== "Delete" // Allow delete
        ) {
          event.preventDefault(); // Prevent any invalid key press
        }
      });
    });
  });

  const customPaginationText = {
    items_per_page: `/ ${tCommon("page")}`,
    jump_to: `${tCommon("toPage")}`,
    page: "",
    prev_page: `${tCommon("prevPage")}`,
    next_page: `${tCommon("nextPage")}`,
  };

  return (
    // <div className={`app-table ${props.customClassName || ""}`}>
    <Table
      // locale={{
      //   emptyText: handleLocaleTable(),
      // }}
      // rowKey={(record) => record.id}
      size="small"
      scroll={{ y: SCROLL_TABLE_HEIGHT, x: SCROLL_TABLE_WIDTH }}
      pagination={{
        total: props.total,
        showSizeChanger: true,
        pageSizeOptions: PAGINATION_OPTIONS,
        locale: customPaginationText,
        showQuickJumper: true,
        showTotal: (total, range) => (
          <span>
            <strong>
              {range[0]}-{range[1]}
            </strong>{" "}
            {tCommon("in")} <strong>{total}</strong> {props.labelPagination || tCommon("student")}
          </span>
        ),
        onChange: (page, pageSize) => {
          props.setParams &&
            props.setParams({
              ...props.params,
              page: page - 1,
              size: pageSize,
            });
        },
        onShowSizeChange: (current, size) => {
          props.setParams &&
            props.setParams({
              ...props.params,
              size: size,
            });
        },
      }}
      {...props}
      className={`app-table custom-scroll ${props.customClassName || ""}`}
    />
    // </div>
  );
};

export default AppTable;
