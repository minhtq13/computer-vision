import { getRequest, postRequest } from "../api/apiCaller";
import { apiPath } from "../config/apiPath";

// Chấm điểm từ AI
export const getModelAIService = async (examClassCode, params, successCallback, errorCallback) => {
  await postRequest(`${apiPath.automaticScoring}/exam-class/${examClassCode}`, params, successCallback, errorCallback, 359000); // 359s
};
export const resetTableResultService = async (examClassCode, tempFileCode, params, successCallback, errorCallback) => {
  await postRequest(`${apiPath.resetTableResult}/save/${examClassCode}?tempFileCode=${tempFileCode}&option=DELETE`, params, successCallback, errorCallback);
};
export const saveTableResultService = async (examClassCode, tempFileCode, params, successCallback, errorCallback) => {
  await postRequest(`${apiPath.saveTableResult}/save/${examClassCode}?tempFileCode=${tempFileCode}&option=SAVE`, params, successCallback, errorCallback);
};
export const getImgInFolderService = async (examClassCode, params, successCallback, errorCallback) => {
  await getRequest(`${apiPath.imgInFolder}/${examClassCode}`, params, successCallback, errorCallback);
};

export const loadLatestTempScoredDataService = async (examClassCode, studentCodes, successCallback, errorCallback) => {
  await getRequest(`${apiPath.loadLatestTempScoredData}/${examClassCode}?studentCodes=${studentCodes.join(",")}`, {}, successCallback, errorCallback);
};
export const deleteImgInFolderService = async (params, successCallback, errorCallback) => {
  await postRequest(`${apiPath.deleteImgInFolder}`, params, successCallback, errorCallback);
};