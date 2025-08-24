import { StudentTestSetResults } from "@/stores/student-test-set/type";
import ChartColumn from "./ChartColumn";
import ChartPie from "./ChartPie";

const Statistics = ({ results }: { results: StudentTestSetResults }) => {
  return (
    <div className="charts flex items-center gap-2 max-lg:flex-col">
      <ChartPie dataPieChart={results?.pieChart} resultData={results?.results} />
      <ChartColumn dataColumnChart={results?.columnChart} />
    </div>
  );
};

export default Statistics;
