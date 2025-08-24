package com.elearning.elearning_support.services.comboBox;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.common.CommonIdCodeNameDTO;
import com.elearning.elearning_support.dtos.common.CommonIdNameDTO;
import com.elearning.elearning_support.dtos.common.ICommonIdCode;
import com.elearning.elearning_support.dtos.common.ICommonIdCodeName;
import com.elearning.elearning_support.dtos.common.ICommonIdName;
import com.elearning.elearning_support.enums.system.SystemObjectEnum;
import com.elearning.elearning_support.enums.tag.TagObjectAppliedTypeEnum;
import com.elearning.elearning_support.enums.test.TestTypeEnum;
import com.elearning.elearning_support.enums.users.UserTypeEnum;

@Service
public interface ComboBoxService {


    /**
     * Danh sách (id, name, code) của các môn học
     */
    List<ICommonIdCodeName> getListSubject(String subjectTitle, String subjectCode, Set<Long> departmentIds);

    /**
     * Danh sách (id, name, code) của các môn học (có lọc theo exam_class tham gia)
     */
    List<ICommonIdCodeName> getListViewableSubject(String subjectTitle, String subjectCode, SystemObjectEnum targetObject);


    /**
     * Danh sách (id, name, code) của các chương trong môn học
     */
    List<ICommonIdCodeName> getListChapterInSubject(Long subjectId, String chapterTitle, String chapterCode);


    /**
     * Danh sách HSSV
     */
    List<ICommonIdCodeName> getListStudent(String studentName, String studentCode);


    /**
     * Danh sách GV
     */
    List<ICommonIdCodeName> getListTeacher(String teacherName, String teacherCode);

    /**
     * Danh sách vai trò
     */
    List<ICommonIdCodeName> getListRole(String search, UserTypeEnum userType);

    /**
     * Danh sách kỳ học
     */
    List<CommonIdNameDTO> getListSemester(String search);

    /**
     * Danh sách kỳ học
     */
    List<ICommonIdCodeName> getListTest(String search);

    /**
     * Danh sách lớp thi
     */
    List<ICommonIdCodeName> getListExamClass(Long semesterId, Long subjectId, TestTypeEnum testType, String search);

    /**
     * Danh sách lớp học
     */
    List<ICommonIdCode> getListCourse(Long semesterId, Long subjectId, String search);

    /**
     * Danh sách các đơn vị quản lý
     */
    List<CommonIdCodeNameDTO> getListDepartment(String search);

    /**
     * Danh sách các tag tương ứng với loại đối tượng áp dụng
     */
    List<ICommonIdName> getListTag(String filter, TagObjectAppliedTypeEnum objectAppliedType);



}
