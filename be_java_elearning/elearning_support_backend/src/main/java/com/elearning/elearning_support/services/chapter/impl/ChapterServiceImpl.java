package com.elearning.elearning_support.services.chapter.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.chapter.ChapterSaveReqDTO;
import com.elearning.elearning_support.dtos.chapter.SubjectChapterCreateDTO;
import com.elearning.elearning_support.entities.chapter.Chapter;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.chapter.ChapterRepository;
import com.elearning.elearning_support.repositories.postgres.subject.SubjectRepository;
import com.elearning.elearning_support.services.chapter.ChapterService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final SubjectRepository subjectRepository;

    private final ChapterRepository chapterRepository;

    private final ExceptionFactory exceptionFactory;

    @Transactional
    @Override
    public void createListChapter(SubjectChapterCreateDTO createDTO) {
        // Check subject existed
        if (!subjectRepository.existsByIdAndIsEnabled(createDTO.getSubjectId(), Boolean.TRUE)) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Subject.NOT_FOUND, MessageConst.RESOURCE_NOT_FOUND, Resources.SUBJECT,
                ErrorKey.Subject.ID, String.valueOf(createDTO.getSubjectId()));
        }

        // Get list current chapter order in subject
        Set<Integer> lstCurrentOrderInSubject = chapterRepository.getListExistedChapterOrderInSubject(createDTO.getSubjectId());
        createDTO.getLstChapter().forEach(chapter -> {
            if (lstCurrentOrderInSubject.contains(chapter.getOrders())) {
                throw exceptionFactory.resourceExistedException(MessageConst.Chapter.EXISTED_ORDERS, Resources.CHAPTER,
                    MessageConst.RESOURCE_EXISTED, ErrorKey.Chapter.ORDER, String.valueOf(chapter.getOrders()));
            }
        });
        // New chapter save to DB
        List<Chapter> lstNewChapter = createDTO.getLstChapter().stream().map(chapter -> new Chapter(createDTO.getSubjectId(), chapter)).collect(
            Collectors.toList());
        chapterRepository.saveAll(lstNewChapter);
    }

    @Override
    public void updateChapter(Long chapterId, ChapterSaveReqDTO updateDTO) {
        // Check chapter existed
        Chapter chapter = findChapterById(chapterId);
        // Check existed the same order in subject
        if (chapterRepository.existsOtherByOrderInSubject(chapter.getSubjectId(), updateDTO.getOrders(), chapter.getId())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Chapter.EXISTED_ORDERS, Resources.CHAPTER,
                MessageConst.RESOURCE_EXISTED, ErrorKey.Chapter.ORDER);
        }
        //Update chapter
        BeanUtils.copyProperties(updateDTO, chapter);
        chapter.setModifiedAt(new Date());
        chapter.setModifiedBy(AuthUtils.getCurrentUserId());
        chapterRepository.save(chapter);
    }

    /**
     * find by id and is_enabled
     */
    private Chapter findChapterById(Long chapterId) {
        return chapterRepository.findByIdAndIsEnabled(chapterId, Boolean.TRUE).orElseThrow(() ->
            exceptionFactory.resourceExistedException(MessageConst.Chapter.NOT_FOUND, MessageConst.RESOURCE_NOT_FOUND, Resources.CHAPTER,
                ErrorKey.Chapter.ID, String.valueOf(chapterId)));
    }
}
