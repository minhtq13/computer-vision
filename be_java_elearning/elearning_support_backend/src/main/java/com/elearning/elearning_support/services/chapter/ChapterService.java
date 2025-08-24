package com.elearning.elearning_support.services.chapter;

import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.chapter.ChapterSaveReqDTO;
import com.elearning.elearning_support.dtos.chapter.SubjectChapterCreateDTO;

@Service
public interface ChapterService {

    /**
     * Tạo danh sách các chương của môn học
     */
    void createListChapter(SubjectChapterCreateDTO createDTO);


    /**
     * Cập nhật chương
     */
    void updateChapter(Long chapterId, ChapterSaveReqDTO updateDTO);

}
