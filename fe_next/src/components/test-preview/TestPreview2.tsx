"use client";
import { forwardRef, useEffect, useImperativeHandle, useRef, useState } from "react";
import HtmlRenderer from "../html-render";
import NewTestHeader from "./NewTestHeader";
import { downloadPDF } from "./downloadPDF";
import TestHeaderEn from "./TestHeaderEn";
import { useAppNotification } from "@/hooks/useAppNotification";

const TestPreview2 = forwardRef(
  ({ questions, testDetail, testNo, testLanguage = "vi" }: { questions: any; testDetail: any; testNo: any; testLanguage?: string }, ref) => {
    const CONTENT_PAGE_PIXEL = 1122 - 10 * 2 * 3.78;
    const containerRef = useRef<HTMLDivElement>(null);
    const [pages, setPages] = useState<any[]>([]);
    const notification = useAppNotification();

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
        const pageHeight = CONTENT_PAGE_PIXEL;
        const firstPageHeight = pageHeight - 260;

        let currentPage: any[] = [];
        const totalPages: any[] = [];
        let currentHeight = 0;
        let availableHeight = firstPageHeight; //
        if (!Array.isArray(questions)) return;
        for (const question of questions) {
          const questionRef = document.createElement("div");
          questionRef.style.width = "210mm";
          questionRef.innerHTML = `
        <div class="question-item text-sm pb-4">
          <div class="flex items-start gap-1 mb-1">
            <span class="font-bold flex-shrink-0">Câu ${question.questionNo}:</span>
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
      downloadPDF: onDownloadPDF,
    }));

    const onDownloadPDF = async () => {
      try {
        await downloadPDF(containerRef, testDetail, testLanguage);
      } catch (error) {
        console.log(error);
        notification.error({
          description: "Có lỗi xảy ra, hãy thử download trên trình duyệt khác ngoài Google Chrome",
        });
      }
    };

    return (
      <div>
        <div ref={containerRef} className="test-preview-wrapper hidden">
          {pages.map((page, pageIndex) => (
            <div
              key={pageIndex}
              className="test-preview-page !mb-2"
              style={{
                fontFamily: "'Times New Roman', Times, serif ",
                // width: "205mm",
                // height: "290mm",
                margin: "0 auto",
                // padding: "15mm",
                // border: "1px solid #ccc",
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
                  <div
                    className="question-item text-sm pb-4"
                    style={{ fontSize: "14px", paddingBottom: "16px" }}
                    key={`page-${pageIndex}-q-${index}`}
                  >
                    <div className="flex items-start gap-1 mb-1" style={{ display: "flex", gap: "4px", marginBottom: "4px" }}>
                      <span className="font-bold flex-shrink-0" style={{ fontWeight: "bold", flexShrink: "0" }}>{`Câu ${question.questionNo}:`}</span>
                      <HtmlRenderer htmlContent={question.content} />
                    </div>
                    <div style={{ display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: "8px" }}>
                      {question.answers &&
                        question.answers.map((ans: any, ansNo: number) => (
                          <div style={{ display: "flex", gap: "4px" }} key={`answer-${index}-${ansNo}`}>
                            <span>{`${String.fromCharCode(65 + ansNo)}.`}</span>
                            <HtmlRenderer htmlContent={ans.content} />
                          </div>
                        ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
);

TestPreview2.displayName = "TestPreview2";

export default TestPreview2;
