package com.elearning.elearning_support.services.semester.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.entities.semester.Semester;
import com.elearning.elearning_support.repositories.postgres.semester.SemesterRepository;
import com.elearning.elearning_support.services.semester.SemesterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    @Transactional
    @Override
    public void autoGenerateSemester() {
        LocalDateTime current = LocalDateTime.now();
        int currentYear = current.getYear();
        // auto generate semesters: ex 20241, 20242, 20243
        List<Semester> generatedSemesters = new ArrayList<>();
        for (int idx = 1; idx <= 3; idx++) {
            String genCode = currentYear + "" + idx;
            if (!semesterRepository.existsByCode(genCode)) {
                generatedSemesters.add(Semester.builder()
                    .code(genCode)
                    .name(genCode)
                    .schoolYear(String.format("%s-%s", currentYear, currentYear + 1))
                    .build());
            }
        }
        semesterRepository.saveAll(generatedSemesters);
    }
}
