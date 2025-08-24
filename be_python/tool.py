
import numpy as np
import cv2


# ============================================ CONSTANT =======================================

warning_color = (78, 173, 240)
blue_color = (255, 0, 0)
red_color = (0, 0, 255)
green_color = (0, 255, 0)
threshold_warning = 0.79

# ============================================ FUNCTION =======================================

def order_points(pts):
    rect = np.zeros((4, 2), dtype="float32")
    pts = np.array(pts)
    s = pts.sum(axis=1)
    rect[0] = pts[np.argmin(s)]
    rect[2] = pts[np.argmax(s)]

    diff = np.diff(pts, axis=1)
    rect[1] = pts[np.argmin(diff)]
    rect[3] = pts[np.argmax(diff)]
    return rect.astype("int").tolist()


def find_dest(pts):
    (tl, tr, br, bl) = pts
    widthA = np.sqrt(((br[0] - bl[0]) ** 2) + ((br[1] - bl[1]) ** 2))
    widthB = np.sqrt(((tr[0] - tl[0]) ** 2) + ((tr[1] - tl[1]) ** 2))
    maxWidth = max(int(widthA), int(widthB))
    heightA = np.sqrt(((tr[0] - br[0]) ** 2) + ((tr[1] - br[1]) ** 2))
    heightB = np.sqrt(((tl[0] - bl[0]) ** 2) + ((tl[1] - bl[1]) ** 2))
    maxHeight = max(int(heightA), int(heightB))
    destination_corners = [[0, 0], [maxWidth, 0], [maxWidth, maxHeight], [0, maxHeight]]
    return order_points(destination_corners)


def generate_output(image: np.array, corners: list, scale: tuple = None, resize_shape: int = 640):
    corners = order_points(corners)
    if scale is not None:
        print(np.array(corners).shape, scale)
        corners = np.multiply(corners, scale)

    destination_corners = find_dest(corners)
    M = cv2.getPerspectiveTransform(np.float32(corners), np.float32(destination_corners))
    out = cv2.warpPerspective(image, M, (destination_corners[2][0], destination_corners[2][1]), flags=cv2.INTER_LANCZOS4)
    out = np.clip(out, a_min=0, a_max=255)
    out = out.astype(np.uint8)
    return out



def get_class_marker(argument):
    if argument == 0:
        return "marker1"
    elif argument == 1:
        return "marker2"
    else:
        return ""

# Get string class from number class
def get_class_answer(argument):
    if argument == 0:
        return "x"
    elif argument == 1:
        return "A"
    elif argument == 2:
        return "B"
    elif argument == 3:
        return "C"
    elif argument == 4:
        return "D"
    elif argument == 5:
        return "AB"
    elif argument == 6:
        return "AC"
    elif argument == 7:
        return "AD"
    elif argument == 8:
        return "BC"
    elif argument == 9:
        return "BD"
    elif argument == 10:
        return "CD"
    elif argument == 11:
        return "ABC"
    elif argument == 12:
        return "ABD"
    elif argument == 13:
        return "ACD"
    elif argument == 14:
        return "BCD"
    elif argument == 15:
        return "ACBD"
    else:
        return "x"
    

    # Get string class from number class
def get_class_info(argument):
    if argument == 0:
        return "0"
    elif argument == 1:
        return "1"
    elif argument == 2:
        return "2"
    elif argument == 3:
        return "3"
    elif argument == 4:
        return "4"
    elif argument == 5:
        return "5"
    elif argument == 6:
        return "6"
    elif argument == 7:
        return "7"
    elif argument == 8:
        return "8"
    elif argument == 9:
        return "9"
    elif argument == 10:
        return "x"
    elif argument == 16:
        return "0"
    elif argument == 17:
        return "1"
    elif argument == 18:
        return "2"
    elif argument == 19:
        return "3"
    elif argument == 20:
        return "4"
    elif argument == 21:
        return "5"
    elif argument == 22:
        return "6"
    elif argument == 23:
        return "7"
    elif argument == 24:
        return "8"
    elif argument == 25:
        return "9"
    elif argument == 26:
        return "x"
    else:
        return "x"



# Remove label duplicate
def remove_elements_info(arr):
    result = []
    i = 0
    while i < len(arr):
        item = arr[i]
        result.append(item)
        j = i + 1
        while j < len(arr) and abs(item[0] - arr[j][0]) <= 5:
            if arr[j][4] >= item[4]:
                result.pop()
                break
            j += 1
        i = j
    return result


def remove_elements_answer(arr):
    result = []
    i = 0
    while i < len(arr):
        item = arr[i]
        result.append(item)
        j = i + 1
        while j < len(arr) and abs(item[1] - arr[j][1]) <= 5:
            if arr[j][4] >= item[4]:
                result.pop()
                break
            j += 1
        i = j
    return result

def remove_elements_marker(arr):
    result = []
    i = 0
    while i < len(arr):
        item = arr[i]
        result.append(item)
        j = i + 1
        while j < len(arr) and abs(item[0] - arr[j][0]) <= 5 and abs(item[1] - arr[j][1]) <= 5:
            if arr[j][4] >= item[4]:
                result.pop()
                break
            j += 1
        i = j
    return result


#  Handles drawing rectangles over circled answers
def get_coordinates(x1, y1, x2, y2, class1):
    point1 = x1
    point2 = y1
    point3 = x2
    point4 = y2
    if class1 == "":
        point1 = x1
        point2 = y1
        point3 = x2
        point4 = y2
    elif class1 == "A":
        point1 = x1 - 5
        point2 = y1 - 2
        point3 = x1 + int((x2 - x1) / 4) - 15
        point4 = y1 + int((y2 - y1))
    elif class1 == "B":
        point1 = x1 + 37
        point2 = y1 - 2
        point3 = x1 + int((x2 - x1) / 4) + 25
        point4 = y1 + int((y2 - y1))
    elif class1 == "C":
        point1 = x1 + 75
        point2 = y1 - 2
        point3 = x1 + int((x2 - x1) / 4) + 68
        point4 = y1 + int((y2 - y1))
    elif class1 == "D":
        point1 = x1 + 118
        point2 = y1 - 2
        point3 = x1 + int((x2 - x1) / 4) + 108
        point4 = y1 + int((y2 - y1))
    return point1, point2, point3, point4


def get_coordinates_info(x1, y1, x2, y2, class1):
    point1 = x1
    point2 = y1
    point3 = x2
    point4 = y2
    if class1 == "0":
        point1 = x1
        point2 = y1
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9)
    elif class1 == "1":
        point1 = x1
        point2 = y1 + 38
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38
    elif class1 == "2":
        point1 = x1
        point2 = y1 + 38 * 2
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 2
    elif class1 == "3":
        point1 = x1
        point2 = y1 + 38 * 3
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 3
    elif class1 == "4":
        point1 = x1
        point2 = y1 + 38 * 4
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 4
    elif class1 == "5":
        point1 = x1
        point2 = y1 + 38 * 5
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 5
    elif class1 == "6":
        point1 = x1
        point2 = y1 + 38 * 6
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 6
    elif class1 == "7":
        point1 = x1
        point2 = y1 + 38 * 7
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 7
    elif class1 == "8":
        point1 = x1
        point2 = y1 + 38 * 8
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 8
    elif class1 == "9":
        point1 = x1
        point2 = y1 + 38 * 9
        point3 = x2
        point4 = y1 + int((y2 - y1) / 9) + 38 * 9
    elif class1 == "x":
        point1 = x1
        point2 = y1
        point3 = x2
        point4 = y2
    return point1, point2, point3, point4


#  Handle the number of questions is not fixed
def get_parameter_number_anwser(numberAnswer):
    naturalParts = numberAnswer // 20
    return naturalParts


def get_remainder(numberAnswer):
    remainder = numberAnswer % 20
    return remainder


def calculate_new_coordinates(marker_coordinates, rect, param1, param2):
    matching_indices = np.where((marker_coordinates[:, :2] == rect).all(axis=1))
    c = marker_coordinates[matching_indices]
    c = c.flatten()
    new_array = np.array([(c[0] + c[2]) / 2 + param1, (c[1] + c[3]) / 2 + param2])
    return new_array



def orient_image_by_angle(pts, marker_coordinates):
    rect = np.zeros((4, 2), dtype="float32")
    marker_coordinates_true = []
    param = 40
    pts = np.array(pts)
    marker_coordinates = np.array(marker_coordinates)
    s = pts.sum(axis=1)
    rect[0] = pts[np.argmin(s)] # top-left
    marker_coordinates_true.append(calculate_new_coordinates(marker_coordinates, rect[0], -param, -param))
    rect[2] = pts[np.argmax(s)] # bottom-right
    marker_coordinates_true.append(calculate_new_coordinates(marker_coordinates, rect[2], param, param))
    diff = np.diff(pts, axis=1)
    rect[1] = pts[np.argmin(diff)] # top-right
    marker_coordinates_true.append(calculate_new_coordinates(marker_coordinates, rect[1], param, -param))
    rect[3] = pts[np.argmax(diff)] # bottom-left
    marker_coordinates_true.append(calculate_new_coordinates(marker_coordinates, rect[3], -param, param))
    marker_coordinates_true = np.array([marker_coordinates_true]).reshape(-1, 1, 2)
    return rect.astype("int").tolist(), marker_coordinates_true

