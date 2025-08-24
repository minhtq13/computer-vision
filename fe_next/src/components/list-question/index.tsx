import { tagRender } from "@/helpers/tools";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { EditOutlined } from "@ant-design/icons";
import { Checkbox, Tag } from "antd";
import { useTranslations } from "next-intl";
import AppButton from "../app-button";
import HtmlRenderer from "../html-render";
import { useRouter } from "next/navigation";
import { PATH_ROUTER } from "@/constants/router";
import { HUST_COLOR } from "@/constants";

interface ListQuestionProps {
  questions: any[];
  hasTag?: boolean;
  hasCheckbox?: boolean;
  onChangeCheckbox?: (item: any) => void;
  checkedItems?: any[];
  hasEditQuestion?: boolean;
  addIndex?: number;
  hasTagLevel?: boolean;
}

const ListQuestion = ({
  questions,
  hasTag = true,
  hasTagLevel = true,
  hasCheckbox = false,
  hasEditQuestion = false,
  onChangeCheckbox,
  checkedItems,
  addIndex = 0,
}: ListQuestionProps) => {
  const { renderTag } = useLocaleOptions();
  const tCommon = useTranslations("common");
  const router = useRouter();

  const onEdit = (e: any, item: any) => {
    e.stopPropagation();
    router.push(PATH_ROUTER.DETAIL.QUESTION_EDIT(item.id));
  };
  return (
    <div>
      {questions?.map((item: any, index: number) => (
        <div
          className={`question-items mb-4 border border-disable-secondary p-4 ${hasCheckbox ? "cursor-pointer hover:bg-red-50" : ""}`}
          key={index}
          onClick={() => {
            if (hasCheckbox) {
              onChangeCheckbox(item);
            }
          }}
        >
          <div className="flex gap-2 justify-between">
            <div className="flex-1">
              <div className="topic-level flex items-start gap-5">
                <div className="question-topic !mb-2 items-start flex text-base gap-1">
                  <div className="flex items-start gap-2 flex-shrink-0">
                    {hasCheckbox && <Checkbox checked={checkedItems.some((i) => i.id === item.id)} />}
                    <div className="text-base font-medium">{`${tCommon("question")} ${addIndex + index + 1}: `}</div>
                  </div>
                  <div className="text-[14px]">
                    <HtmlRenderer htmlContent={item.content} />
                  </div>
                </div>
              </div>
              {item.lstAnswer &&
                item.lstAnswer.length > 0 &&
                item.lstAnswer.map((ans: any, ansNo: number) => (
                  <div className={ans.isCorrect ? "text-base gap-1 flex text-fill-success" : "text-base gap-1 flex"} key={`answer${ansNo}`}>
                    <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                    <div className="text-[14px]">
                      <HtmlRenderer htmlContent={ans.content} />
                    </div>
                  </div>
                ))}
            </div>
            <div className="flex gap-2 flex-col max-md:max-w-[80px]">
              <div className="flex items-center justify-end">
                {hasTagLevel && <Tag color={tagRender(item.level)}>{renderTag(item)}</Tag>}
                {hasEditQuestion && (
                  <AppButton size="small" onClick={(e) => onEdit(e, item)} customclass="!p-2">
                    <EditOutlined />
                  </AppButton>
                )}
              </div>
              <div className="flex items-center gap-2 flex-wrap max-w-[120px] max-md:max-w-[100%] justify-end">
                {hasTag &&
                  item?.tags?.map((tag: { id: number; name: string }) => (
                    <Tag color={HUST_COLOR} key={tag.id} className="!m-0">
                      {tag.name}
                    </Tag>
                  ))}
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ListQuestion;
