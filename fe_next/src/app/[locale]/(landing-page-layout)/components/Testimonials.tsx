import { useState } from "react";
import { ChevronLeft, ChevronRight, Quote } from "lucide-react";
import AppButton from "@/components/app-button";

const testimonials = [
  {
    quote:
      "The AI-powered grading system has revolutionized our examination process. We've seen remarkable time savings while maintaining grading quality.",
    author: "Nguyen Huy Hoang",
    role: "Professor, Computer Science, HUST",
  },
  {
    quote: "This system has transformed how we approach large-scale assessments. The accuracy and efficiency are truly impressive.",
    author: "Nguyen Thanh Binh",
    role: "Dean, School of Electrical Engineering, HUST",
  },
  {
    quote:
      "Integrating this system with our existing LMS was seamless. The time saved on grading has allowed our faculty to focus more on student engagement.",
    author: "Pham Thi Huong",
    role: "School of Electrical Engineering, HUST",
  },
];

const Testimonials = () => {
  const [current, setCurrent] = useState(0);

  const prev = () => {
    setCurrent((curr) => (curr === 0 ? testimonials.length - 1 : curr - 1));
  };

  const next = () => {
    setCurrent((curr) => (curr === testimonials.length - 1 ? 0 : curr + 1));
  };

  return (
    <div className="container mx-auto px-4 md:px-6">
      <div className="text-center max-w-3xl mx-auto mb-12">
        <h2 className="text-3xl md:text-4xl font-bold mb-4 relative pb-2 after:content-[''] after:absolute after:bottom-0 after:left-1/4 after:w-1/2 after:h-1 after:bg-hust">
          What Educators Are Saying
        </h2>
        <p className="text-gray-600 text-lg">Hear from faculty members who have implemented our AI grading system at their institutions.</p>
      </div>

      <div className="relative max-w-4xl mx-auto">
        <div className="bg-white rounded-xl shadow-lg p-8 md:p-10 relative">
          <Quote className="h-12 w-12 text-hust-20 absolute top-6 left-6" />

          <div className="relative z-10">
            <blockquote className="text-xl md:text-2xl text-gray-700 mb-6 relative pl-10">&quot;{testimonials[current].quote}&quot;</blockquote>

            <div className="flex flex-col">
              <span className="font-bold text-gray-900">{testimonials[current].author}</span>
              <span className="text-gray-600">{testimonials[current].role}</span>
            </div>
          </div>
        </div>

        <div className="flex justify-center mt-8 space-x-4">
          <AppButton className="rounded-full" onClick={prev} aria-label="Previous testimonial">
            <ChevronLeft className="h-5 w-5" />
          </AppButton>

          {testimonials.map((_, index) => (
            <AppButton
              key={index}
              className={`w-2 h-2 p-0 rounded-full ${current === index ? "!bg-hust !text-white" : "bg-gray-300"}`}
              onClick={() => setCurrent(index)}
              aria-label={`Go to testimonial ${index + 1}`}
              classChildren={`${current === index ? "!text-white" : "text-hust"}`}
            >
              {index + 1}
            </AppButton>
          ))}

          <AppButton className="rounded-full" onClick={next} aria-label="Next testimonial">
            <ChevronRight className="h-5 w-5" />
          </AppButton>
        </div>
      </div>
    </div>
  );
};

export default Testimonials;
