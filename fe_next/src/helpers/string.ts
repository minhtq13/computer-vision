// get text content from a question (not precise in all cases)
export const getQuestionMainContent = (content: any) => {
  let firstOpenPTagIdx = content.indexOf("<p>");
  let firstClosedPTagIdx = content.indexOf("</p>");
  return content.substring(firstOpenPTagIdx + 1, firstClosedPTagIdx);
};

export default function pathsToArray(paths: Record<string, string | Record<string, string>>): string[] {
  const result: string[] = [];

  const extractPaths = (obj: Record<string, string | Record<string, string>>) => {
    for (const key in obj) {
      const value = obj[key];
      if (typeof value === "string") {
        result.push(value);
      } else {
        extractPaths(value);
      }
    }
  };

  extractPaths(paths);
  return result;
}
