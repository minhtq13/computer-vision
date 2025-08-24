import { Column } from "@ant-design/plots";
import { useTranslations } from "next-intl";
import React from "react";

const ChartColumn = ({ dataColumnChart }) => {
  const tCommon = useTranslations("common");
  const data = dataColumnChart;

  const config = {
    data,
    xField: "name",
    yField: "value",
    label: {
      style: {
        fill: "#FFFFFF",
        opacity: 1,
        size: 12,
      },
    },
    style: {
      maxWidth: 25,
      fill: ({ name }) => {
        const nameReplaced = name.replace(",", ".");
        if (Number(nameReplaced) <= 3) {
          return "#d74858";
        } else if (Number(nameReplaced) > 3 && Number(nameReplaced) <= 5) return "#fc9362";
        else if (Number(nameReplaced) > 5 && Number(nameReplaced) <= 7) return "#fee291";
        else if (Number(nameReplaced) > 7 && Number(nameReplaced) <= 8) return "#e7f69d";
        else if (Number(nameReplaced) > 8 && Number(nameReplaced) <= 9) return "#9ed79a";
        return "#3c8ec1";
      },
    },
    interaction: {
      tooltip: {
        render: (e, { title, items }) => {
          return (
            <div key={title}>
              <h4>Điểm {title}</h4>
              {items.map((item, index) => {
                const { value, color } = item;
                return (
                  <div key={index}>
                    <div className="flex justify-between m-0">
                      <div>
                        <span
                          className="inline-block w-[6px] h-[6px] rounded-[50%] mr-1.5"
                          style={{
                            backgroundColor: color,
                          }}
                        ></span>
                        <span>{tCommon("quantity")}</span>
                      </div>
                      <b>{value}</b>
                    </div>
                  </div>
                );
              })}
            </div>
          );
        },
      },
    },
    xAxis: {
      label: {
        autoHide: true,
        autoRotate: false,
      },
    },
  };
  return (
    <div className="chart-column-component bg-white rounded-[12px] w-[65%] max-lg:w-full">
      <Column {...config} />
    </div>
  );
};

export default ChartColumn;
