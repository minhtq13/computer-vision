import { Space } from "antd";
import React, { useState } from "react";
import AppSelectSmall from "@/components/app-select-small";
import { getOptionsFromCombo } from "@/helpers";
import { useHandleError } from "@/hooks/useHandleError";
import { Divider } from "antd";
import AppInput from "@/components/app-input";
import { useGetComboTagQuery } from "@/stores/combo/api";
import { useAddTagMutation } from "@/stores/questions/api";
import { PlusOutlined } from "@ant-design/icons";
import { useTranslations } from "next-intl";
import AppButton from "@/components/app-button";

interface FilterByTagProps {
  onChange: (value: any) => void;
  className?: string;
  mode?: any;
  canAddTag?: boolean;
  initialValues?: any;
  value?: any;
  title?: string;
  customClassName?: string;
}

const FilterByTag = ({
  onChange,
  className,
  mode = null,
  canAddTag = true,
  initialValues,
  value,
  title = "Tag",
  customClassName,
}: FilterByTagProps) => {
  const tCommon = useTranslations("common");
  const [nameTag, setNameTag] = useState("");
  const handleError = useHandleError();
  const [addTag] = useAddTagMutation();

  const { data: allTags, refetch: refetchTags } = useGetComboTagQuery({ objectAppliedType: "ALL" });
  const onNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNameTag(event.target.value);
  };
  const addItem = async (e: React.MouseEvent<HTMLButtonElement | HTMLAnchorElement>) => {
    e.preventDefault();
    try {
      await addTag({ name: nameTag, objectAppliedTypes: ["ALL"] }).unwrap();
      setNameTag("");
      refetchTags();
    } catch (error) {
      handleError(error);
    }
  };
  const handleTagChange = (value: any) => {
    onChange(value);
  };

  return (
    <AppSelectSmall
      customClassName={customClassName}
      title={title}
      allowClear
      mode={mode}
      placeholder={tCommon("selectLabel")}
      className={`min-w-[100px] ${className}`}
      options={getOptionsFromCombo(allTags)}
      defaultValue={initialValues}
      value={value}
      dropdownRender={(menu) => (
        <>
          {menu}
          {canAddTag && (
            <>
              <Divider style={{ margin: "4px 0px 8px 0px" }} />
              <Space style={{ padding: "0 8px 4px" }}>
                <AppInput placeholder={tCommon("enterLabel")} value={nameTag} onChange={onNameChange} onKeyDown={(e) => e.stopPropagation()} />
                <AppButton type="text" onClick={addItem}>
                  <PlusOutlined />
                </AppButton>
              </Space>{" "}
            </>
          )}
        </>
      )}
      onChange={handleTagChange}
      showSearch
    />
  );
};

export default FilterByTag;
