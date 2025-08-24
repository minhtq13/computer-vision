import { Clock, CheckCircle, Scale } from "lucide-react";
import StatCard from "./StatCard";
import AppButton from "@/components/app-button";
import { PATH_ROUTER } from "@/constants/router";
import { useRouter } from "next/navigation";

const Benefits = () => {
  const router = useRouter();
  return (
    <div className="py-16 md:py-24">
      <div className="container mx-auto px-4 md:px-6">
        <div className="flex flex-col lg:flex-row gap-12 items-center">
          <div className="lg:w-1/2 space-y-6">
            <h2 className="text-3xl md:text-4xl font-bold mb-4 ">Transforming Education Through AI Innovation</h2>
            <p className="text-gray-600 text-lg mb-8">
              Our system delivers substantial benefits for educational institutions, instructors, and administrators alike.
            </p>

            <div className="space-y-6">
              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <Clock className="h-6 w-6 text-primary mt-1" />
                </div>
                <div>
                  <h3 className="text-xl font-semibold mb-2">Saves Time</h3>
                  <p className="text-gray-600">
                    Reduces manual grading workload by 90%, freeing instructors to focus on teaching and student engagement.
                  </p>
                </div>
              </div>

              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <CheckCircle className="h-6 w-6 text-primary mt-1" />
                </div>
                <div>
                  <h3 className="text-xl font-semibold mb-2">Enhances Accuracy</h3>
                  <p className="text-gray-600">
                    Minimizes human errors in grading with 99.5% accuracy for multiple-choice and consistent evaluation for essays.
                  </p>
                </div>
              </div>

              <div className="flex gap-4">
                <div className="flex-shrink-0">
                  <Scale className="h-6 w-6 text-primary mt-1" />
                </div>
                <div>
                  <h3 className="text-xl font-semibold mb-2">Scalable Solution</h3>
                  <p className="text-gray-600">
                    Designed for seamless integration across universities with plans for expansion to 3+ institutions within 2 years.
                  </p>
                </div>
              </div>
            </div>

            <AppButton className="bg-primary hover:bg-primary/90 text-white mt-4" onClick={() => router.push(PATH_ROUTER.PUBLIC.LOGIN)}>
              Learn How It Works
            </AppButton>
          </div>

          <div className="lg:w-1/2 grid grid-cols-1 md:grid-cols-2 gap-6">
            <StatCard value="90%" label="Workload Reduction" description="Manual grading effort reduced, allowing educators to focus on teaching." />
            <StatCard value="99.5%" label="Grading Accuracy" description="Near-perfect accuracy in multiple-choice exam assessment." />
            <StatCard value="60s" label="Processing Time" description="For a batch of 50 exams on standard hardware (Core i5, 16GB RAM)." />
            <StatCard value="3+" label="University Expansion" description="Planned deployment across multiple institutions within 2 years." />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Benefits;
