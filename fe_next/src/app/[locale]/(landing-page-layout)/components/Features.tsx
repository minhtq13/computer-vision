import { CheckCircle2, FileCheck, FileText, BrainCircuit, BarChart, ServerCog } from "lucide-react";
import FeatureCard from "./FeatureCard";

const Features = () => {
  const features = [
    {
      icon: CheckCircle2,
      title: "Multiple-Choice Grading",
      description: "Uses YOLOv8 for automated scoring with 99.5% accuracy, processing 50 exams in just 60 seconds.",
    },
    {
      icon: FileText,
      title: "Essay Examination",
      description: "Leverages LLMs to evaluate content, logic, grammar, and creativity with human-like understanding.",
    },
    {
      icon: BrainCircuit,
      title: "AI-Powered Analysis",
      description: "Combines YOLOv8 for object detection and LLMs for sophisticated text analysis and grading.",
    },
    {
      icon: ServerCog,
      title: "LMS Integration",
      description: "Seamlessly compatible with existing Learning Management Systems at your institution.",
    },
    {
      icon: FileCheck,
      title: "Exam Generation",
      description: "Automatically creates and manages question banks while maintaining academic integrity.",
    },
    {
      icon: BarChart,
      title: "Performance Analytics",
      description: "Comprehensive analytics and insights on student performance and learning patterns.",
    },
  ];

  return (
    <div className="py-16 md:py-24">
      <div className="container mx-auto px-4 md:px-6">
        <div className="text-center max-w-3xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4 relative pb-2 after:content-[''] after:absolute after:bottom-0 after:left-1/4 after:w-1/2 after:h-1 after:bg-hust">
            Advanced AI-Powered Features
          </h2>
          <p className="text-gray-600 text-lg">
            Our system combines cutting-edge AI technology with intuitive management tools to transform the educational assessment process.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <FeatureCard key={index} icon={feature.icon} title={feature.title} description={feature.description} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Features;
