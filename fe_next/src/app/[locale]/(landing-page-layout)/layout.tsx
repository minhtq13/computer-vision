import { Metadata } from "next";

const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || "https://mpec.exam.io.vn";

export const generateMetadata = async (): Promise<Metadata> => {
  return {
    title: "Automated Exam Management and Grading System",
    description:
      "Revolutionary AI-powered exam management solution with 99.5% scoring accuracy. Automated grading for multiple-choice and essay exams. Deployed at HUST. Request a demo today!",
    keywords: "AI grading, automated exam management, multiple-choice grading, essay grading, YOLOv8, LLMs, HUST, educational technology",
    openGraph: {
      title: "Automated Exam Management and Grading System",
      description: "AI-powered exam grading with 99.5% accuracy, processing 50 exams in 60 seconds",
      url: `${baseUrl}/landing-page`,
      siteName: "Automated Exam Management and Grading System",
      images: [
        {
          url: `${baseUrl}/pwa/screenshotPC.png`,
          width: 1200,
          height: 630,
          alt: "Automated Exam Management and Grading System Screenshot",
        },
      ],
      locale: "en_US",
      type: "website",
    },
    twitter: {
      card: "summary_large_image",
      title: "Automated Exam Management and Grading System",
      description: "AI-powered exam grading with 99.5% accuracy, processing 50 exams in 60 seconds",
      images: [`${baseUrl}/pwa/screenshotPC.png`],
    },
    alternates: {
      canonical: `${baseUrl}/landing-page`,
      languages: {
        en: `${baseUrl}/en/landing-page`,
        vi: `${baseUrl}/vi/landing-page`,
      },
    },
    robots: {
      index: true,
      follow: true,
      nocache: true,
      googleBot: {
        index: true,
        follow: true,
        "max-image-preview": "large",
        "max-video-preview": -1,
        "max-snippet": -1,
      },
    },
    metadataBase: new URL(baseUrl),
    verification: {
      google: "google-site-verification-code", // Replace with your actual verification code
    },
    other: {
      author: "Ta Quang Minh",
      publisher: "Hanoi University of Science and Technology",
      "apple-mobile-web-app-capable": "yes",
      "apple-mobile-web-app-status-bar-style": "black",
      "format-detection": "telephone=no",
      "revisit-after": "7 days",
      rating: "general",
    },
    applicationName: "Automated Exam Management and Grading System",
    referrer: "origin-when-cross-origin",
    creator: "Ta Quang Minh",
    publisher: "Hanoi University of Science and Technology",
    category: "Education Technology",
  };
};
export default async function LandingPageLayout({ children }: { children: React.ReactNode }) {
  return <div>{children}</div>;
}
