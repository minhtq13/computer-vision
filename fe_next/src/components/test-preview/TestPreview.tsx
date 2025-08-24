"use client";
import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from "react";
import HtmlRenderer from "../html-render";
import NewTestHeader from "./NewTestHeader";
import { downloadHTML } from "./downloadHTML";
import TestHeaderEn from "./TestHeaderEn";

const TestPreview = forwardRef(
  ({ questions, testDetail, testNo, testLanguage = "vi" }: { questions: any; testDetail: any; testNo: any; testLanguage?: string }, ref) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const [pages, setPages] = useState<any[]>([]);

    const waitForImagesToLoad = async (element: HTMLElement): Promise<void> => {
      const imgElements = Array.from(element.getElementsByTagName("img"));

      const promises = imgElements.map((img) => {
        if (img.complete) return Promise.resolve();
        return new Promise<void>((resolve) => {
          img.onload = () => resolve();
          img.onerror = () => resolve();
        });
      });

      return Promise.all(promises).then(() => {});
    };
    useEffect(() => {
      const calculatePages = async () => {
        const pageHeight = 969;
        const firstPageHeight = 969 - 260;

        let currentPage: any[] = [];
        const totalPages: any[] = [];
        let currentHeight = 0;
        let availableHeight = firstPageHeight; //
        if (!Array.isArray(questions)) return;
        for (const question of questions) {
          const questionRef = document.createElement("div");
          questionRef.style.width = "716px";
          questionRef.innerHTML = `
        <div style="font-size: 14px; padding-bottom: 12px; line-height: 1.2; font-family: 'Times New Roman', Times, serif;">
          <div style="margin-bottom: 4px; display: flex; gap: 4px; align-items: start;">
            <span style="font-weight: bold; flex-shrink: 0;">Câu ${question.questionNo}:</span>
            <div>${question.content}</div>
          </div>
          <div style="display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px;">
            ${question.answers
              .map(
                (ans: any, ansNo: number) =>
                  `<div class="${ans.isCorrect ? "flex gap-1 font-bold" : "gap-1 flex "}">${String.fromCharCode(65 + ansNo)}. ${ans.content}</div>`
              )
              .join("")}
          </div>
        </div>
      `;
          await waitForImagesToLoad(questionRef);
          document.body.appendChild(questionRef);
          const questionHeight = questionRef.offsetHeight;
          document.body.removeChild(questionRef);
          currentHeight += questionHeight;

          if (currentHeight > availableHeight) {
            totalPages.push(currentPage);
            currentPage = [];
            currentHeight = questionHeight;
            availableHeight = pageHeight;
          }

          currentPage.push(question);
        }

        if (currentPage.length > 0) {
          totalPages.push(currentPage);
        }

        setPages(totalPages);
      };

      calculatePages();
    }, [questions]);

    useImperativeHandle(ref, () => ({
      downloadHTML: onDownloadHTML,
    }));

    const onDownloadHTML = () => {
      downloadHTML(containerRef, testDetail);
    };

    return (
      <div>
        <div ref={containerRef} className="test-preview-wrapper">
          {pages.map((page, pageIndex) => (
            <div
              key={pageIndex}
              className="test-preview-page !mb-2"
              style={{
                fontFamily: "'Times New Roman', Times, serif ",
                width: "210mm",
                height: "297mm",
                margin: "0 auto",
                padding: "10mm",
                border: "1px solid #ccc",
                boxSizing: "border-box",
                position: "relative",
                pageBreakAfter: pageIndex < pages.length - 1 ? "always" : "auto",
              }}
            >
              {/* Header */}
              {pageIndex === 0 && (
                <div className="test-header" style={{ marginBottom: "20px" }}>
                  {testLanguage === "vi" ? (
                    <NewTestHeader testDetail={testDetail} testNo={testNo} />
                  ) : (
                    <TestHeaderEn testDetail={testDetail} testNo={testNo} />
                  )}
                </div>
              )}

              {/* Questions */}
              <div className="test-questions">
                {page.map((question: any, index: number) => (
                  <div style={{ fontSize: "14px", paddingBottom: "12px", lineHeight: "1.2" }} key={`page-${pageIndex}-q-${index}`}>
                    <div style={{ display: "flex", gap: "4px", marginBottom: "4px" }}>
                      <span style={{ fontWeight: "bold", flexShrink: "0" }}>{`Câu ${question.questionNo}:`}</span>
                      <HtmlRenderer htmlContent={question.content} />
                    </div>
                    <div style={{ display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: "8px" }}>
                      {question.answers &&
                        question.answers.map((ans: any, ansNo: number) => (
                          <div className={ans.isCorrect ? "font-bold" : ""} key={`answer-${index}-${ansNo}`} style={{ display: "flex", gap: "4px" }}>
                            <span>{`${String.fromCharCode(65 + ansNo)}.`}</span>
                            <HtmlRenderer htmlContent={ans.content} />
                          </div>
                        ))}
                    </div>
                  </div>
                ))}
              </div>

              {/* Page Number */}
              <div
                className="page-number"
                style={{
                  position: "absolute",
                  bottom: "15mm",
                  right: "15mm",
                  fontSize: "12px",
                }}
              >
                {testLanguage === "vi" ? "Trang" : "Page"} {pageIndex + 1}/{pages.length}
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
);

TestPreview.displayName = "TestPreview";

export default TestPreview;
