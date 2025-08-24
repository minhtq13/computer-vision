"use client";

import AppButton from "@/components/app-button";
import { PATH_ROUTER } from "@/constants/router";
import { GraduationCap } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface MenuItem {
  id: string;
  label: string;
}

const LandingNav = () => {
  const router = useRouter();
  //   const tCommon = useTranslations("common");
  const [isScrolled, setIsScrolled] = useState(false);
  const [activeSection, setActiveSection] = useState("hero");

  const menuItems: MenuItem[] = [
    { id: "hero", label: "Home" },
    { id: "features", label: "Features" },
    { id: "benefits", label: "Benefits" },
    { id: "testimonials", label: "Testimonials" },
    { id: "research", label: "Research" },
  ];

  // Handle scroll event to change navbar style and active section
  useEffect(() => {
    const handleScroll = () => {
      // Change navbar style when scrolled
      setIsScrolled(window.scrollY > 10);

      // Update active section based on scroll position
      const sections = menuItems.map((item) => document.getElementById(item.id));
      const scrollPosition = window.scrollY + 100; // Offset for better detection

      for (let i = sections.length - 1; i >= 0; i--) {
        const section = sections[i];
        if (section && section.offsetTop <= scrollPosition) {
          setActiveSection(menuItems[i].id);
          break;
        }
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [menuItems]);

  // Smooth scroll to section
  const scrollToSection = (id: string) => {
    const element = document.getElementById(id);
    if (element) {
      window.scrollTo({
        top: element.offsetTop - 80, // Offset for navbar height
        behavior: "smooth",
      });
    }
  };

  return (
    <nav className={`fixed top-0 left-0 w-full z-50 transition-all duration-300 ${isScrolled ? "bg-white shadow-md py-2" : "bg-transparent py-4"}`}>
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <GraduationCap className="w-8 h-8 text-hust" />
            <span className={`font-bold text-xl ${isScrolled ? "text-hust" : "text-gray-700"}`}>ELearning System</span>
          </div>

          <div className="hidden md:flex items-center space-x-8">
            {menuItems.map((item) => (
              <button
                key={item.id}
                onClick={() => scrollToSection(item.id)}
                className={`text-base font-medium transition-all duration-300 hover:text-hust relative ${
                  isScrolled ? "text-gray-700" : "text-gray-700"
                } ${activeSection === item.id ? "font-semibold" : ""}
                `}
              >
                {item.label}
                <span
                  className={`absolute bottom-[-6px] left-0 w-full h-0.5 bg-hust transform scale-x-0 transition-transform duration-300 ${
                    activeSection === item.id ? "scale-x-100" : ""
                  }`}
                ></span>
              </button>
            ))}
          </div>

          <div className="flex items-center space-x-2">
            <AppButton
              type="primary"
              size="large"
              onClick={() => router.push(PATH_ROUTER.PUBLIC.LOGIN)}
              className="bg-hust hover:bg-hust-80 text-white font-medium py-2 px-4 rounded-md transition-all duration-300 transform hover:translate-y-[-2px] hover:shadow-lg"
            >
              Login
            </AppButton>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default LandingNav;
