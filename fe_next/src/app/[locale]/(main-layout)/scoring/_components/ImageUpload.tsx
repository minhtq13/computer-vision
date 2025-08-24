import { useAppNotification } from "@/hooks/useAppNotification";
import { useUploadImageMutation } from "@/stores/test-set/api";
import { useHandleError } from "@/hooks/useHandleError";
import AppButton from "@/components/app-button";
import { useRef } from "react";
import { useTranslations } from "next-intl";

interface IImageUpload {
  selectedImages: any[];
  setSelectedImages: (images: any[]) => void;
  examClassCode: any;
}

const ImageUpload = ({ selectedImages, setSelectedImages, examClassCode }: IImageUpload) => {
  const tScoring = useTranslations("scoring");
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [uploadImage, { isLoading }] = useUploadImageMutation();
  const isImage = (file) => {
    return file.type.startsWith("image/");
  };
  const inputUpload = useRef(null);
  const handleImageChange = (e) => {
    const files = e.target.files;
    if (files) {
      const imageFiles = Array.from(files).filter(isImage);
      setSelectedImages(imageFiles);
    } else {
      setSelectedImages([]);
    }
  };
  const handleImageUpload = async () => {
    if (selectedImages.length === 0) {
      return;
    }
    try {
      const formData = new FormData();
      selectedImages.forEach((image) => {
        formData.append("files", image);
      });
      await uploadImage({ formData, examClassCode }).unwrap();
      inputUpload.current.value = null;
      notification.success({
        description: tScoring("uploadSuccess"),
      });
    } catch (error: any) {
      handleError(error);
    }
  };
  return (
    <div className="image-upload-component flex gap-2 my-4 flex-col ">
      <div className="text-[14px] font-bold">{tScoring("uploadImage")}:</div>
      <div className="flex items-center gap-2">
        <input
          ref={inputUpload}
          type="file"
          id="input-import"
          onChange={handleImageChange}
          accept="image/*"
          multiple
          className="input-upload-scoring"
        />
        {selectedImages.length > 0 && (
          <AppButton className="upload-btn" onClick={handleImageUpload} loading={isLoading}>
            {tScoring("upload")}
          </AppButton>
        )}
      </div>
    </div>
  );
};

export default ImageUpload;
