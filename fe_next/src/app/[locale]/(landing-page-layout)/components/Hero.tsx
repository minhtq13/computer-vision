import AppButton from "@/components/app-button";
import { PATH_ROUTER } from "@/constants/router";
import { useRouter } from "next/navigation";
import { BsArrowRight } from "react-icons/bs";
import PreviewImage from "../../(main-layout)/scoring/_components/PreviewImage";

const Hero = () => {
  const router = useRouter();
  return (
    <div className="pt-24 md:pt-32 pb-16 md:pb-24">
      <div className="container mx-auto px-4 md:px-6">
        <div className="flex flex-col md:flex-row items-center justify-between gap-10">
          <div className="md:w-1/2 space-y-6">
            <div>
              <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold leading-tight text-gray-900">
                AI-Powered <span className="text-hust">Exam Grading</span> and Management
              </h1>
              <p className="mt-6 text-lg text-gray-600 leading-relaxed">
                Revolutionizing education with 99.5% accurate automated grading. Process 50 exams in 60 seconds and reduce manual workload by 90%.
              </p>
            </div>

            <div className="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4 pt-4">
              <AppButton
                className="bg-primary hover:bg-primary/90 text-white px-8 py-6 text-lg rounded-md"
                onClick={() => router.push(PATH_ROUTER.PUBLIC.LOGIN)}
              >
                Request a Demo
              </AppButton>
              <AppButton
                className="border-primary text-hust hover:text-hust hover:bg-primary/5 px-8 py-6 text-lg rounded-md"
                onClick={() => router.push(PATH_ROUTER.PUBLIC.LOGIN)}
              >
                Learn More <BsArrowRight className="ml-2 h-5 w-5" />
              </AppButton>
            </div>

            <div className="pt-6">
              <div className="text-gray-500 text-sm text-start">Trusted by leading institutions, including</div>
              <div className="flex items-center space-x-6 mt-4">
                <span className="text-gray-800 font-semibold">HUST</span>
                <div className="h-6 w-px bg-gray-300"></div>
                <span className="text-gray-800 font-semibold">School of Electrical & Electronic Engineering</span>
              </div>
            </div>
          </div>

          <div className="md:w-1/2 relative flex justify-center items-center">
            <div className="bg-white rounded-lg shadow-xl p-4 md:p-6 transform rotate-1 max-w-[500px]">
              <div className="mx-auto">
                <PreviewImage srcImage={"/input-image-prediction.png"} imageName={"image-prediction"} />
              </div>
            </div>

            <div className="absolute -bottom-6 left-8 bg-hust rounded-lg shadow-lg p-4 text-white">
              <div className="text-lg font-bold">99.5%</div>
              <div className="text-xs">Grading Accuracy</div>
            </div>

            <div className="absolute -top-4 right-6 bg-white rounded-lg shadow-lg p-4 text-gray-800">
              <div className="text-lg font-bold">90%</div>
              <div className="text-xs">Workload Reduction</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Hero;
