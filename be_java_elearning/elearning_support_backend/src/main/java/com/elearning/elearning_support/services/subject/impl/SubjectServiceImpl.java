package com.elearning.elearning_support.services.subject.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.subject.ISubjectDetailDTO;
import com.elearning.elearning_support.dtos.subject.ISubjectListDTO;
import com.elearning.elearning_support.dtos.subject.SubjectDetailDTO;
import com.elearning.elearning_support.dtos.subject.SubjectSaveReqDTO;
import com.elearning.elearning_support.entities.subject.Subject;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.subject.SubjectRepository;
import com.elearning.elearning_support.services.subject.SubjectService;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final ExceptionFactory exceptionFactory;

    @Override
    public Subject findById(Long subjectId) {
        return subjectRepository.findByIdAndIsEnabled(subjectId, Boolean.TRUE)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Subject.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, Resources.SUBJECT,
                        ErrorKey.Subject.ID, String.valueOf(subjectId)));
    }

    @Override
    public void createSubject(SubjectSaveReqDTO createDTO) {
        if (subjectRepository.existsByCode(createDTO.getCode())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Subject.EXISTED_CODE, Resources.SUBJECT,
                    MessageConst.RESOURCE_EXISTED,
                    ErrorKey.Subject.CODE, createDTO.getCode());
        }

        // create new subject
        Subject newSubject = new Subject();
        BeanUtils.copyProperties(createDTO, newSubject);
        newSubject.setCreatedAt(new Date());
        newSubject.setCreatedBy(AuthUtils.getCurrentUserId());
        newSubject.setIsEnabled(Boolean.TRUE);
        subjectRepository.save(newSubject);
    }

    @Override
    public void updateSubject(Long subjectId, SubjectSaveReqDTO updateDTO) {
        // Check subject existed
        Subject subject = findById(subjectId);
        // Check existed code
        if (subjectRepository.existsByCodeAndIdNot(updateDTO.getCode(), subject.getId())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Subject.EXISTED_CODE, Resources.SUBJECT,
                    MessageConst.RESOURCE_EXISTED,
                    ErrorKey.Subject.CODE, updateDTO.getCode());
        }

        // update subject
        BeanUtils.copyProperties(updateDTO, subject);
        subject.setModifiedBy(AuthUtils.getCurrentUserId());
        subject.setModifiedAt(new Date());
        subjectRepository.save(subject);
    }

    @Override
    public Page<ISubjectListDTO> getListSubject(String search, Long departmentId, String departmentName,
            Pageable pageable) {
        Set<Long> viewableSubjectIds = getListViewableSubjectIds();
        return subjectRepository.getListSubject(viewableSubjectIds, search, departmentId, departmentName, pageable);
    }

    @Override
    public SubjectDetailDTO getSubjectDetail(Long subjectId) {
        ISubjectDetailDTO iSubjectDetail = subjectRepository.getDetailSubject(subjectId);
        if (Objects.isNull(iSubjectDetail.getId())) {
            throw exceptionFactory.resourceNotFoundException(MessageConst.Subject.NOT_FOUND,
                    MessageConst.RESOURCE_NOT_FOUND, Resources.SUBJECT,
                    ErrorKey.Subject.ID, String.valueOf(subjectId));
        }
        return new SubjectDetailDTO(iSubjectDetail);
    }

    @Override
    public Set<Long> getListViewableSubjectIds() {
        Long currentUserId = AuthUtils.getCurrentUserId();
        Set<Long> viewableSubjectIds = new HashSet<>();

        if (AuthUtils.isAdmin() || AuthUtils.isBaseStudent()) {
            viewableSubjectIds.add(-1L);
        } else if (AuthUtils.isDepartmentAdmin()) {
            viewableSubjectIds = subjectRepository
                    .getListSubjectIdByDepartmentIdIn(AuthUtils.getCurrentUserDepartmentIds());
        } else { // teacher - subject
            viewableSubjectIds.addAll(subjectRepository.getListViewableSubjectId(Boolean.FALSE, currentUserId));
        }
        return viewableSubjectIds;
    }
}
