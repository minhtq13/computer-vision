import { Pie } from "@ant-design/plots";
import React, { useMemo } from "react";
import "./ChartPie.scss";

const ChartPie = ({ dataPieChart, resultData }) => {
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
        title: "Điểm",
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
            <div key={title} style={{ minWidth: 150 }}>
              {items.map((item, index) => {
                const { value, color } = item;
                return (
                  <div key={index}>
                    <div style={{ margin: 0, display: "flex", justifyContent: "space-between" }}>
                      <div style={{display: "flex", alignItems: "center"}}>
                        <span
                          style={{
                            display: "inline-block",
                            width: 6,
                            height: 6,
                            borderRadius: "50%",
                            backgroundColor: color,
                            marginRight: 6,
                          }}
                        ></span>
                        <span>Số lượng: </span>
                      </div>
                      <b>{value}</b>
                    </div>
                    <div style={{ margin: 0, display: "flex", justifyContent: "space-between" }}>
                      <div style={{display: "flex", alignItems: "center"}}>
                        <span
                          style={{
                            display: "inline-block",
                            width: 6,
                            height: 6,
                            borderRadius: "50%",
                            backgroundColor: color,
                            marginRight: 6,
                          }}
                        ></span>
                        <span>Phần trăm: </span>
                      </div>
                      <b>
                        {value === 0
                          ? "0"
                          : ((value / data.reduce((a, b) => a + b.value, 0)) * 100).toFixed(2)}
                        %
                      </b>
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
  return <div className="chart-pie-component">{renderPieChart}</div>;
};

export default ChartPie;
