// get text content from a question (not precise in all cases)
export const getQuestionMainContent = (content) => {
    let firstOpenPTagIdx = content.indexOf("<p>");
    let firstClosedPTagIdx = content.indexOf("</p>");
    return content.substring(firstOpenPTagIdx + 1, firstClosedPTagIdx);
};