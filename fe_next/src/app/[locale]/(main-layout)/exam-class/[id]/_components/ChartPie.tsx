import { Pie } from "@ant-design/plots";
import { useTranslations } from "next-intl";
import { useMemo } from "react";

const ChartPie = ({ dataPieChart, resultData }: { dataPieChart: any; resultData: any }) => {
  const tCommon = useTranslations("common");
  const data = dataPieChart;

  const updateValues = (items) => {
    items.forEach((elementA) => {
      const matchingElementB = data.find((elementB) => elementB.name === elementA.name);
      if (matchingElementB) {
        elementA.value = matchingElementB.value;
      }
    });

    return items;
  };
  const config = {
    data,
    angleField: "value",
    colorField: "name",
    innerRadius: 0.6,
    labels: [
      // { text: "name", style: { fontSize: 12, fontWeight: "bold" } },
      {
        text: (d, i, data) => {
          if (resultData.length === 0) return "";
          if (d.value === 0) return "";
          return `${((d.value / data.reduce((a, b) => a + b.value, 0)) * 100).toFixed(2)}%`;
        },
        style: {
          fontSize: 12,
          dy: 12,
        },
      },
    ],
    style: {
      stroke: "#fff",
      inset: 1,
      radius: 10,
    },
    scale: {
      color: {
        palette: "spectral",
        offset: (t) => t * 0.8 + 0.1,
      },
    },
    legend: {
      color: {
        title: tCommon("score"),
        position: "bottom",
        layout: {
          justifyContent: "center",
          alignItems: "center",
          flexDirection: "column",
        },
      },
    },
    interaction: {
      tooltip: {
        render: (e, { title, items }) => {
          items = updateValues(items);
          return (
            <div key={title} className="min-w-[150px]">
              {items.map((item, index) => {
                const { value, color } = item;
                return (
                  <div key={index}>
                    <div className="m-0 flex justify-between">
                      <div className="flex items-center">
                        <span
                          className="inline-block w-[6px] h-[6px] rounded-[50%] mr-1.5"
                          style={{
                            backgroundColor: color,
                          }}
                        />
                        <span>{tCommon("quantity")}: </span>
                      </div>
                      <b>{value}</b>
                    </div>
                    <div className="m-0 flex justify-between">
                      <div className="flex items-center">
                        <span
                          className="inline-block w-[6px] h-[6px] rounded-[50%] mr-1.5"
                          style={{
                            backgroundColor: color,
                          }}
                        />
                        <span>{tCommon("percentage")}: </span>
                      </div>
                      <b>{value === 0 ? "0" : ((value / data.reduce((a, b) => a + b.value, 0)) * 100).toFixed(2)}%</b>
                    </div>
                  </div>
                );
              })}
            </div>
          );
        },
      },
    },
  };
  const renderPieChart = useMemo(() => {
    return <Pie {...config} />;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data]);
  return <div className="chart-pie-component bg-white rounded-[12px] w-[35%] max-lg:w-full">{renderPieChart}</div>;
};

export default ChartPie;
