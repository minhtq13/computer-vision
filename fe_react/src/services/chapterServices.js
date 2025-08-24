import { putRequest } from "../api/apiCaller";
import { apiPath } from "../config/apiPath";

export const updateChapterService = async (chapterId, params, successCallback, errorCallback) => {
  await putRequest(`${apiPath.updateChapter}/${chapterId}`, params, successCallback, errorCallback);
};