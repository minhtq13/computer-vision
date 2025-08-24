import {
    postConfiguredRequest,
} from "../api/apiCaller";
import { apiPath } from "../config/apiPath";

// upload a image
export const uploadImageService = async (file, successCallback, errorCallback) => {
    await postConfiguredRequest(
        apiPath.uploadImage,
        file,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        },
        successCallback,
        errorCallback
    );
};