"use client";

import { Col, Divider, Row, Typography } from "antd";
import { useEffect, useState } from "react";

import Link from "next/link";
import LandingNav from "./components/LandingNav";
import Hero from "./components/Hero";
import Features from "./components/Features";
import Benefits from "./components/Benefits";
import Testimonials from "./components/Testimonials";
import Research from "./components/Research";

const { Text } = Typography;

const LandingPage = () => {
  //   const tCommon = useTranslations("common");
  const [isVisible, setIsVisible] = useState({
    hero: false,
    metrics: false,
    features: false,
    benefits: false,
    testimonials: false,
    research: false,
  });

  useEffect(() => {
    const handleScroll = () => {
      const sections = ["hero", "features", "benefits", "testimonials", "research"];

      sections.forEach((section) => {
        const element = document.getElementById(section);
        if (element) {
          const rect = element.getBoundingClientRect();
          const isInView = rect.top <= window.innerHeight * 0.75;
          setIsVisible((prev) => ({ ...prev, [section]: isInView }));
        }
      });
    };

    // Initial check
    handleScroll();

    // Add event listener for scroll
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <div className="landing-page">
      <LandingNav />

      {/* Hero Section */}
      <section
        id="hero"
        className={`relative pt-[91px] text-center bg-white transition-all duration-1000 ${
          isVisible.hero ? "opacity-100 translate-y-0" : "opacity-0 translate-y-10"
        }`}
      >
        <Hero />
      </section>

      {/* Features Section */}
      <section
        id="features"
        className={`pt-24 bg-gradient-to-b to-gray-50 from-hust-10 transition-all duration-1000 ${
          isVisible.features ? "opacity-100 translate-y-0" : "opacity-0 translate-y-10"
        }`}
      >
        <Features />
      </section>

      {/* Benefits Section */}
      <section
        id="benefits"
        className={`py-24 px-4 bg-white transition-all duration-1000 ${
          isVisible.benefits ? "opacity-100 translate-y-0" : "opacity-0 translate-y-10"
        }`}
      >
        <div className="container mx-auto">
          <Benefits />
        </div>
      </section>

      {/* Testimonials */}
      <section
        id="testimonials"
        className={`py-24 px-4 bg-gradient-to-b to-gray-50 from-hust-10 transition-all duration-1000 ${
          isVisible.testimonials ? "opacity-100 translate-y-0" : "opacity-0 translate-y-10"
        }`}
      >
        <div className="container mx-auto">
          <Testimonials />
        </div>
      </section>

      {/* Research */}
      <section
        id="research"
        className={`py-24 px-4 bg-white text-center text-white transition-all duration-1000 ${
          isVisible.research ? "opacity-100 translate-y-0" : "opacity-0 translate-y-10"
        }`}
      >
        <div className="container mx-auto">
          <Research />
        </div>
      </section>

      {/* Footer */}
      <footer id="footer" className={`py-16 px-4 bg-gray-800 text-white`}>
        <div className="container mx-auto">
          <Row gutter={[24, 32]}>
            <Col xs={24} md={8}>
              <h4 className="text-xl font-bold text-white mb-4">About the Project</h4>
              <p className="text-gray-400 mb-4">
                The Automated Exam Management and Grading System is a research project completed as part of a master&apos;s thesis at Hanoi University
                of Science and Technology.
              </p>
              <div className="flex items-center space-x-4">
                <Link
                  href="https://github.com/chiendao1808/elearning-support-system"
                  className="text-white hover:text-hust-70 transition-colors"
                  target="_blank"
                >
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path
                      fillRule="evenodd"
                      d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                      clipRule="evenodd"
                    />
                  </svg>
                </Link>
                <Link href="https://www.facebook.com/qu4ngm1nhh/" className="text-white hover:text-hust-70 transition-colors" target="_blank">
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path
                      fillRule="evenodd"
                      d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z"
                      clipRule="evenodd"
                    />
                  </svg>
                </Link>
                <Link href="https://x.com/taminh596" className="text-white hover:text-hust-70 transition-colors" target="_blank">
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" />
                  </svg>
                </Link>
              </div>
            </Col>
            <Col xs={24} md={8}>
              <h4 className="text-xl font-bold text-white mb-4">Contact</h4>
              <p className="text-gray-400">
                Email:{" "}
                <a
                  href="mailto:
                "
                >
                  taminh596@gmail.com
                </a>
                <br />
                Phone: +84 979 047 751
                <br />
                Address: Hanoi University of Science and Technology, No. 1 Dai Co Viet, Hanoi, Vietnam
              </p>
            </Col>
            <Col xs={24} md={8}>
              <h4 className="text-xl font-bold text-white mb-4">Credits</h4>
              <p className="text-gray-400">
                MPEC LAB <br /> Dr. Pham Doan Tinh
                <br />
                Ta Quang Minh
                <br />
                Dao Huy Chien
                <br />
                HUST, School of Electrical & Electronic Engineering
                <br />
                Completed: April 2025
              </p>
            </Col>
          </Row>
          <Divider className="!border-white mt-8 mb-8" />
          <div className="text-center">
            <Text className="!text-white">© {new Date().getFullYear()} Copyright MPEC LAB - SEEE - HUST. All rights reserved.</Text>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;
