import { Input } from "antd";
import AppSelectSmall from "@/components/app-select-small";
import { useTranslations } from "next-intl";

interface SearchFilterProps {
  placeholder: string;
  displayFilter?: boolean;
  onSearch: any;
  options?: any;
  onChange?: any;
  onSelect?: any;
  disabled?: boolean;
}

const SearchFilter = ({ placeholder, displayFilter, onSearch, options, onChange, onSelect, disabled }: SearchFilterProps) => {
  const tCommon = useTranslations("common");
  return (
    <div className="list-search-filter flex gap-4 max-md:flex-col max-md:w-full">
      <div className="flex items-center gap-2 max-md:w-full">
        <span className="text-[14px] font-bold flex-shrink-0">{tCommon("search")}:</span>
        <Input.Search
          className="md:!w-[350px]"
          placeholder={placeholder}
          enterButton
          onSearch={onSearch}
          allowClear
          onChange={onChange}
          disabled={disabled}
        />
      </div>
      {displayFilter && (
        <div className="flex items-center gap-2">
          <span className="text-[14px] font-bold flex-shrink-0">{tCommon("course")}:</span>
          <AppSelectSmall
            className="filter-select-course-num min-w-[100px]"
            mode="multiple"
            placeholder={tCommon("course")}
            showSearch
            allowClear
            options={options}
            onChange={onSelect}
          />
        </div>
      )}
    </div>
  );
};
export default SearchFilter;
