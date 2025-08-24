import os
import platform
import cv2
import numpy as np
from ultralytics import YOLO
import argparse
import json
import time
from tool import generate_output, get_parameter_number_anwser, remove_elements_info, remove_elements_answer, remove_elements_marker
from tool import get_class_answer, get_class_info, get_coordinates, get_coordinates_info, get_remainder, orient_image_by_angle, get_class_marker
from tool import warning_color, green_color, blue_color, threshold_warning

# Shared data folder
HOME_DIR = os.path.expanduser('~')
WINDOWS_SHARED_DATA_DIR = HOME_DIR + "/AppData/Local/ELearningSupport/data"
LINUX_SHARED_DATA_DIR = HOME_DIR + "/usr/local/app/ELearningSupport/data"
SHARED_DATA_DIR = WINDOWS_SHARED_DATA_DIR if "windows" in platform.system().lower() else LINUX_SHARED_DATA_DIR



# ============================================ HANDLE MARKER =======================================

def get_marker(image, model, filename, folder_code = ""):
    try:
        results = model.predict(image)
        data = results[0].boxes.data
        list_marker = []
        marker_coordinates = []
        validate_marker = []
        count_marker2 = 0
        count_maker1 = 0
        for i, data in enumerate(data):
            validate_marker.append(data)
        validate_marker = remove_elements_marker(validate_marker)
        for i, marker in enumerate(validate_marker):
            x1 = int(marker[0])
            y1 = int(marker[1])
            x2 = int(marker[2])
            y2 = int(marker[3])
            conf = round(float(data[4]), 3)
            class_marker = int(marker[5])
            if (class_marker == 1):
                count_marker2 += 1
                marker2 = [x1, y1]
            if (class_marker == 0):
                count_maker1 += 1
            if (class_marker == 0 or class_marker == 1):
                list_marker.append([x1, y1])
                marker_coordinates.append([x1, y1, x2, y2])
            if (class_marker >= 0):
                cv2.rectangle(image, (x1, y1), (x2, y2), green_color if conf > threshold_warning else warning_color,
                    1 if conf > threshold_warning else 2)
                cv2.putText(image, str(get_class_marker(class_marker)) if conf > threshold_warning else str(f"{get_class_marker(class_marker)}-{conf}"),
                    (x1, y1), cv2.FONT_HERSHEY_SIMPLEX, 0.4 if conf > threshold_warning else 0.5,
                    blue_color if conf > threshold_warning else warning_color, 1,cv2.LINE_AA)
     
            
            # print("list_marker", list_marker)

        # Handle errors
        # if count_marker2 != 1 or count_maker1 != 3:
        #     error_message = f"Ảnh {filename} không nhận diện được đủ marker hoặc có thể bị thiếu góc"
        #     maybe_wrong_marker.append(error_message)
        #     # with open(f"{SHARED_DATA_DIR}/AnsweredSheets/{folder_code}/MayBeWrong/error.txt", "w", encoding="utf-8") as f:
        #     #     for string in maybe_wrong_marker:
        #     #         f.write(string + "\n")
        #     raise Exception(error_message)
        # print("list_marker", list_marker)
        oriented, marker_coordinates_true = orient_image_by_angle(list_marker, marker_coordinates)
        # print(oriented)
        oritentation = ''
        # Ảnh bị xoay 90 độ về bên phải => marker 2 ở bottom-left
        if (marker2 == oriented[3]):
            oritentation = 'bl'
        # Ảnh không bị xoay => marker 2 ở bottom-right    
        if (marker2 == oriented[2]):  
            oritentation = 'br'
        # Ảnh bị xoay 90 độ về bên trái => marker 2 ở top-right    
        if (marker2 == oriented[1]):  
            oritentation = 'tr'
        # Ảnh bị xoay 180 => marker 2 ở top-left    
        if (marker2 == oriented[0]):
            oritentation = 'tl'
        corners = sorted(np.concatenate(marker_coordinates_true).tolist())
        output = generate_output(image, corners)

        # Show ảnh đã xử lý với marker
        # imgResize = cv2.resize(output, (506, 800), interpolation=cv2.INTER_AREA)
        # cv2.imshow("imgResize", imgResize)
        # cv2.waitKey(0)

        return output, oritentation, maybe_wrong_marker
    except Exception as e:
        return None, None, maybe_wrong_marker
# ============================================ CUT IMAGE COLUMN ANSWER =======================================

def crop_image_answer(img, numberAnswer):
    ans_blocks = []
    arrayX = [30, 350, 660]
    for i in range(3):
        y = 480
        width = 350
        height = 896
        cropped_image = img[y: y + height, arrayX[i]: arrayX[i] + width]
        ans_blocks.append(cropped_image)
    sorted_ans_blocks = ans_blocks
    if numberAnswer == 20 or numberAnswer == 40 or numberAnswer == 60:
        numbers_question_pictures = get_parameter_number_anwser(numberAnswer)
    else:
        numbers_question_pictures = get_parameter_number_anwser(numberAnswer) + 1
    sorted_ans_blocks = sorted_ans_blocks[:numbers_question_pictures]
    sorted_ans_blocks_resize = []
    coord_array = []
    size_array = []
    for i, sorted_ans_block in enumerate(sorted_ans_blocks):
        img2 = sorted_ans_block[0]
        width, height = img2.shape
        size_array.append((350, 896))
        img_resize = cv2.resize(sorted_ans_block, (250, 640), interpolation=cv2.INTER_AREA)
        # cv2.imshow("answersheet", img_resize)
        # cv2.waitKey(0)
        sorted_ans_blocks_resize.append(img_resize)
        coord_array.append((arrayX[i], 480))

    return sorted_ans_blocks_resize, size_array, coord_array

# ============================================ PREDICT IMAGE COLUMN ANSWER =======================================

def predictAnswer(img, model, index, numberAnswer):
    results = model.predict(img)
    data = results[0].boxes.data
    list_label = []
    for i, data in enumerate(data):
        list_label.append(data)
    list_label = sorted(list_label, key=lambda x: x[1])
    list_label = remove_elements_answer(list_label)
    array_answer = []
    maybe_wrong_answer = []
    for i, answer in enumerate(list_label):
        if index == get_parameter_number_anwser(numberAnswer) and i == get_remainder(numberAnswer):
            break
        class_answer = get_class_answer(int(answer[5]))
        array_answer.append(class_answer)
        x1 = int(answer[0])
        y1 = int(answer[1])
        x2 = int(answer[2])
        y2 = int(answer[3])
        conf = round(float(answer[4]), 3)
        class_answer = int(answer[5])
        if conf < threshold_warning:
            maybe_wrong_answer.append(f'Nhãn "{get_class_answer(class_answer)}" câu {index * 20 + i+1}, ảnh {filename}, xác suất: {conf}')
            
        for char in str(get_class_answer(class_answer)):
            point1, point2, point3, point4 = get_coordinates(x1, y1, x2, y2, char)
            # Vẽ hình chữ nhật lên các nhãn không khoanh chỉ khi conf < threshold_warning
            if char == "x":
                if conf < threshold_warning:
                    cv2.rectangle(img, (point1, point2), (point3, point4), warning_color, 2)
                    cv2.putText(img, str(f"{get_class_answer(class_answer)}-{conf}"),
                        (point1, point2), cv2.FONT_HERSHEY_SIMPLEX, 0.5, warning_color, 1,cv2.LINE_AA)
            else:
                cv2.rectangle(img, (point1, point2), (point3, point4), green_color if conf > threshold_warning else warning_color,
                    1 if conf > threshold_warning else 2)
                cv2.putText(img, str(char) if conf > threshold_warning else str(f"{get_class_answer(class_answer)}-{conf}"),
                    (point1, point2), cv2.FONT_HERSHEY_SIMPLEX, 0.4 if conf > threshold_warning else 0.5,
                    blue_color if conf > threshold_warning else warning_color, 1,cv2.LINE_AA)
        img_graft = cv2.resize(img, (350, 896), interpolation=cv2.INTER_AREA)
        
    return array_answer, img_graft, maybe_wrong_answer


# ============================================ CUT IMAGE INFO =======================================

def crop_image_info(img):
    left = 550
    top = 0
    right = 1056
    bottom = 500
    cropped_image = img[top:bottom, left:right]
    cropped_image = cv2.convertScaleAbs(cropped_image * 255)
    img_resize = cv2.resize(cropped_image, (640, 640), interpolation=cv2.INTER_AREA)
    return img_resize

def predictInfo(img, model, filename):
    results = model.predict(img)
    data = results[0].boxes.data
    numberClassRecognition = len(data)
    list_label = []
    for i, data in enumerate(data):
        list_label.append(data)
    list_label = sorted(list_label, key=lambda x: x[0])
    list_label = remove_elements_info(list_label)
    dict_info = {}
    for i, info in enumerate(list_label):
        class_info = get_class_info(int(info[5]))
        dict_info[f"{i+1}"] = class_info
        x1 = int(info[0])
        y1 = int(info[1])
        x2 = int(info[2])
        y2 = int(info[3])
        conf = round(float(info[4]), 3)
        class_info = int(info[5])
        if conf < threshold_warning:
            maybe_wrong_info.append(f'Nhãn thứ {i} từ trái sang phải: "{get_class_info(class_info)}", xác suất: {conf}, ảnh {filename}')

        point1, point2, point3, point4 = get_coordinates_info(x1, y1, x2, y2, get_class_info(class_info))
        cv2.rectangle(img, (point1, point2), (point3, point4), 
            green_color if conf > threshold_warning else warning_color, 1 if conf > threshold_warning else 2)
        cv2.putText(img, str(get_class_info(class_info)) if conf > threshold_warning else str(f"{get_class_info(class_info)}-{conf}"), 
            (point1, point2),cv2.FONT_HERSHEY_SIMPLEX,
            0.4 if conf > threshold_warning else 0.5, blue_color if conf > threshold_warning else warning_color, 1 ,cv2.LINE_AA,)
    if numberClassRecognition > 5:
        class_code = "".join([dict_info[str(i)] if str(i) not in dict_info or dict_info[str(i)] != "unchoice" else "x" for i in range(1, 7)])
        student_code = "".join(list(dict_info.values())[6:-3])
        exam_code = "".join(list(dict_info.values())[-3:])
        result_info = { "class_code": class_code, "student_code": student_code, "exam_code": exam_code}
        imgResize = cv2.resize(img, (506, 500), interpolation=cv2.INTER_AREA)
    elif numberClassRecognition <= 5:
        result_info = { "class_code": "", "student_code": "", "exam_code": ""}
        imgResize = cv2.resize(img, (506, 500), interpolation=cv2.INTER_AREA)

    return result_info, imgResize, numberClassRecognition, maybe_wrong_info

# ============================================ MERGE IMAGES =======================================
def mergeImages(filename, coord_array, array_img_graft, background_image, imgInfo):
    filename_cut = filename.split(".")[0]
    height, width, _ = imgInfo.shape
    background_image[0: 0 + height, 550: 550 + width] = imgInfo / 255
    
    for i in range(len(coord_array)):
        x_coord = coord_array[i][0]
        y_coord = coord_array[i][1]
        height, width, _ = array_img_graft[i].shape
        background_image[y_coord: y_coord + height, x_coord: x_coord + width] = array_img_graft[i] / 255
    handled_scored_img = f"{SHARED_DATA_DIR}/AnsweredSheets/{args.input}/HandledSheets//handled_{filename_cut}.jpg"
    cv2.imwrite(handled_scored_img, background_image * 255)
    return handled_scored_img


if __name__ == "__main__":
    # ========================== Đo thời gian ====================================
    # start_time = time.time()
    # ===================== Khai báo và load model ==============================
    pWeight_marker = "./Model/marker_11.pt"
    pWeight_answer = "./Model/answer_m_11.pt"
    pWeight_info = "./Model/info_m_11.pt"
    model_info = YOLO(pWeight_info)
    model_marker = YOLO(pWeight_marker)
    model_answer = YOLO(pWeight_answer)
    # ======================= Khai báo tham số truyền vào cmd  ===============================
    parser = argparse.ArgumentParser(description="Process some integers.")
    parser.add_argument("input", help="input")
    args = parser.parse_args()

    # ================= Tạo folder ảnh gốc và ảnh đã qua xử lý===============================
    folder_path = f"{SHARED_DATA_DIR}/AnsweredSheets/{args.input}"
    folder_path_handle = f"{SHARED_DATA_DIR}/AnsweredSheets/{args.input}/HandledSheets"
    folder_scored_path = f"{SHARED_DATA_DIR}/AnsweredSheets/{args.input}/ScoredSheets"
    folder_maybe_wrong = f"{SHARED_DATA_DIR}/AnsweredSheets/{args.input}/MayBeWrong"
    if not os.path.exists(folder_path):
        try:
            os.makedirs(folder_path)
        except OSError:
            print(f"Lỗi: Không thể tạo thư mục {folder_path}.")
            
    if not os.path.exists(folder_path_handle):
        try:
            os.makedirs(folder_path_handle)
        except OSError:
            print(f"Lỗi: Không thể tạo thư mục {folder_path_handle}.")
            
    if not os.path.exists(folder_scored_path):
        try:
            os.makedirs(folder_scored_path)
        except OSError:
            print(f"Lỗi: Không thể tạo thư mục {folder_scored_path}.")
            
    if not os.path.exists(folder_maybe_wrong):
        try:
            os.makedirs(folder_maybe_wrong)
        except OSError:
            print(f"Lỗi: Không thể tạo thư mục {folder_maybe_wrong}.")            
            
    maybe_wrong_info = []
    maybe_wrong_answer_array = []
    maybe_wrong_marker = []


    # ================================= Chương trình chính=================================
    file_names = os.listdir(folder_path)
    file_names.sort()
    for filename in file_names:
        if filename.lower().endswith((".jpg", ".jpeg", ".png")):
            image_path = os.path.join(folder_path, filename)
            image = cv2.imread(image_path)
            document, oritentation, maybe_wrong_marker = get_marker(image, model_marker, filename, args.input)
            if (document is None):
                continue
            if oritentation == 'bl':
                document = cv2.rotate(document, cv2.ROTATE_90_COUNTERCLOCKWISE)
                document = cv2.resize(document, (1056, 1500), interpolation=cv2.INTER_AREA)
            elif oritentation == 'br':
                document = cv2.resize(document, (1056, 1500), interpolation=cv2.INTER_AREA)
            elif oritentation == 'tr':
                document = cv2.rotate(document, cv2.ROTATE_90_CLOCKWISE)
                document = cv2.resize(document, (1056, 1500), interpolation=cv2.INTER_AREA)
            elif oritentation == 'tl':
                document = cv2.rotate(document, cv2.ROTATE_180)
                document = cv2.resize(document, (1056, 1500), interpolation=cv2.INTER_AREA)
            document = document / 255
            # ========================== Cắt ảnh sbd và mdt ===============================
            img_resize = crop_image_info(document)
            result_info, imgResize, numberClassRecognition, maybe_wrong_info = predictInfo(img=img_resize, model=model_info, filename=filename)
            numberAnswer = 60
            # =================================== Lấy đáp án ==============================
            result_answer, size_array, coord_array = crop_image_answer(cv2.convertScaleAbs(document * 255), numberAnswer)
            list_answer = []
            array_img_graft = []
            for i, answer in enumerate(result_answer):
                selected_answer, img_graft, maybe_wrong_answer = predictAnswer(img=answer, model=model_answer, index=i, numberAnswer=numberAnswer)
                list_answer = list_answer + selected_answer
                array_img_graft.append(img_graft)
                maybe_wrong_answer_array += maybe_wrong_answer
            # ================================= Format file json =============================
            array_result = []
            for key, value in enumerate(list_answer):
                item = {"questionNo": int(key) + 1, "selectedAnswers": value}
                array_result.append(item)
                if key == (numberAnswer - 1):
                    break
               
            if len(result_info) == 3:
                result = {
                    "examClassCode": result_info["class_code"],
                    "studentCode": result_info["student_code"],
                    "testSetCode": result_info["exam_code"],
                    "answers": array_result
                }
            
            # ============================= Ghép ảnh =====================================
            handled_scored_img = mergeImages(filename, coord_array, array_img_graft, background_image=document, imgInfo=imgResize)
            result["handledScoredImg"] = handled_scored_img
            result["originalImg"] = image_path
            result["originalImgFileName"] = filename
                
            # =============================== Ghi file json ==========================

            orig_file_name = filename.split(".")[0]
            file_path = f"{folder_scored_path}/{orig_file_name}_data.json"
            dir_path = os.path.dirname(file_path)

            if not os.path.exists(dir_path):
                os.makedirs(dir_path)
            # Ghi dữ liệu từ điển vào tệp tin JSON
            with open(file_path, "w") as file:
                json.dump(result, file)
            # =============================== Ghi file cảnh báo có thể sai ==========================

        # ========================================= Đo thời gian ==========================
        # print("Thời gian thực thi: ", time.time() - start_time, " giây")
    if len(maybe_wrong_info) > 0 or len(maybe_wrong_answer_array) > 0 or len(maybe_wrong_marker) > 0:
        with open(f"{folder_maybe_wrong}/may_be_wrong.txt", "w", encoding="utf-8") as f:
            if len(maybe_wrong_marker) > 0:
                for string in maybe_wrong_marker:
                    f.write(string + "\n")
            if len(maybe_wrong_info) > 0:
                for string in maybe_wrong_info:
                    f.write(string + "\n")
            if len(maybe_wrong_answer_array) > 0:
                for string in maybe_wrong_answer_array:
                    f.write(string + "\n")
      # Reset data 
    maybe_wrong_marker = []
    maybe_wrong_info = []
    maybe_wrong_answer_array = []