import AppButton from "@/components/app-button";
import { Award, ArrowUpRight, FileText } from "lucide-react";

const Research = () => {
  const handleClick = () => {
    window.open("https://thesai.org/Publications/ViewPaper?Volume=15&Issue=1&Code=IJACSA&SerialNo=115", "_blank");
  };
  return (
    <div className="flex flex-col lg:flex-row gap-12 items-center text-black">
      <div className="lg:w-1/2">
        <div className="bg-white rounded-xl shadow-lg p-8 border border-gray-100 relative">
          <div className="absolute -top-4 -right-4 bg-hust text-white text-sm font-medium py-1 px-3 rounded-full">ISI Q3 Journal</div>

          <div className="flex items-start mb-6">
            <Award className="h-8 w-8 text-hust mr-4 flex-shrink-0" />
            <div>
              <h3 className="text-xl font-bold text-start">Research Publication</h3>
              <div className="text-gray-600 mt-1 text-start">Our research on the YOLOv8 framework for automated exam grading</div>
            </div>
          </div>

          <div className="border-t border-gray-200 pt-6 mt-6">
            <h4 className="font-semibold mb-2">Research Highlights:</h4>
            <ul className="space-y-3">
              <li className="flex items-center">
                <div className="bg-hust-10 rounded-full p-1 mr-3 mt-1">
                  <FileText className="h-4 w-4 text-hust" />
                </div>
                <span className="text-gray-700">Novel application of computer vision for exam grading</span>
              </li>
              <li className="flex items-center">
                <div className="bg-hust-10 rounded-full p-1 mr-3 mt-1">
                  <FileText className="h-4 w-4 text-hust" />
                </div>
                <span className="text-gray-700">Optimized YOLOv8 model for answer sheet recognition</span>
              </li>
              <li className="flex items-center">
                <div className="bg-hust-10 rounded-full p-1 mr-3 mt-1">
                  <FileText className="h-4 w-4 text-hust" />
                </div>
                <span className="text-gray-700">Integration of LLMs for essay assessment methodology</span>
              </li>
            </ul>
          </div>

          <AppButton className="mt-6 w-full flex items-center justify-center" onClick={handleClick}>
            View Publication <ArrowUpRight className="ml-2 h-4 w-4" />
          </AppButton>
        </div>
      </div>

      <div className="lg:w-1/2 space-y-6">
        <h2 className="text-3xl md:text-4xl font-bold mb-4 relative pb-2 after:content-[''] after:absolute after:bottom-0 after:left-1/4 after:w-1/2 after:h-1 after:bg-hust">
          Research-Backed Excellence
        </h2>
        <p className="text-gray-600 text-lg">Our system is built on rigorous academic research and validated through peer-reviewed publications.</p>

        <div className="space-y-6 mt-8">
          <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
            <h3 className="text-xl font-semibold mb-3">Academic Impact</h3>
            <p className="text-gray-700">
              Our research on the YOLOv8 framework for automated exam grading has been published in an ISI Q3 Journal, validating the effectiveness
              and innovation of our approach.
            </p>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
            <h3 className="text-xl font-semibold mb-3">Future Development</h3>
            <p className="text-gray-700">
              We&apos;re continuing to enhance our AI models and expand our research into new areas of educational technology, with plans for
              additional publications and academic collaborations.
            </p>
          </div>

          <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
            <h3 className="text-xl font-semibold mb-3">Collaboration Opportunities</h3>
            <p className="text-gray-700">
              We&apos;re actively seeking academic and industry partners interested in advancing AI-powered educational assessment technologies.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Research;
