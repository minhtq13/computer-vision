package com.example.multiplechoiceexam.Api;

import com.example.multiplechoiceexam.dto.auth.AuthenticationRequest;
import com.example.multiplechoiceexam.dto.auth.ProfileUserDTO;
import com.example.multiplechoiceexam.dto.auth.RefreshTokenResDTO;
import com.example.multiplechoiceexam.dto.auth.RegisterRequest;
import com.example.multiplechoiceexam.dto.auth.AuthenticationResponse;
import com.example.multiplechoiceexam.dto.auth.UserTypeEnum;
import com.example.multiplechoiceexam.dto.chapter.ChapterRequest;
import com.example.multiplechoiceexam.dto.chapter.ChapterResponse;
import com.example.multiplechoiceexam.dto.common.ICommonIdCodeName;
import com.example.multiplechoiceexam.dto.examClass.ClassRequest;
import com.example.multiplechoiceexam.dto.examClass.ClassResponse;
import com.example.multiplechoiceexam.dto.examClass.ExamClassSaveReqDTO;
import com.example.multiplechoiceexam.dto.examClass.ICommonIdCode;
import com.example.multiplechoiceexam.dto.examClass.IExamClassParticipantDTO;
import com.example.multiplechoiceexam.dto.examClass.UserExamClassDTO;
import com.example.multiplechoiceexam.dto.examClass.UserExamClassRoleEnum;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;
import com.example.multiplechoiceexam.dto.question.ImportResponseDTO;
import com.example.multiplechoiceexam.dto.question.MultiQuestionRequest;
import com.example.multiplechoiceexam.dto.question.QuestionLevelEnum;
import com.example.multiplechoiceexam.dto.question.QuestionListDTO;
import com.example.multiplechoiceexam.dto.question.QuestionResponse;
import com.example.multiplechoiceexam.dto.question.QuestionUpdateDTO;
import com.example.multiplechoiceexam.dto.student.StudentRequest;
import com.example.multiplechoiceexam.dto.student.StudentResponse;
import com.example.multiplechoiceexam.dto.student.StudentUpdateRequest;
import com.example.multiplechoiceexam.dto.studentTest.ExamClassResultStatisticsDTO;
import com.example.multiplechoiceexam.dto.studentTest.HandledImagesDeleteDTO;
import com.example.multiplechoiceexam.dto.studentTest.StudentTestSetResultDTO;
import com.example.multiplechoiceexam.dto.subject.SubjectRequest;
import com.example.multiplechoiceexam.dto.subject.SubjectResponse;
import com.example.multiplechoiceexam.dto.teacher.TeacherRequest;
import com.example.multiplechoiceexam.dto.teacher.TeacherResponse;
import com.example.multiplechoiceexam.dto.teacher.TeacherUpdateRequest;
import com.example.multiplechoiceexam.dto.test.TestClassRequest;
import com.example.multiplechoiceexam.dto.test.TestClassResponse;
import com.example.multiplechoiceexam.dto.test.TestSetDetailResponse;
import com.example.multiplechoiceexam.dto.test.TestSetGenerateReqDTO;
import com.example.multiplechoiceexam.dto.test.TestSetPreviewDTO;
import com.example.multiplechoiceexam.dto.test.TestSetSearchReqDTO;
import com.example.multiplechoiceexam.dto.test.semeter.SemesterBox;
import com.example.multiplechoiceexam.dto.upload.ResponseMessage;
import com.example.multiplechoiceexam.dto.upload.ScoringPreviewResDTO;

import java.util.List;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // signin, signup
    @POST("api/auth/login")
    Call<AuthenticationResponse> authenticateUser(@Body AuthenticationRequest request);
    @POST("/api/v1/auth/signup")
    Call<AuthenticationResponse> registerUser(@Body RegisterRequest registerRequest);
    @POST("api/auth/token/refresh")
    Call<RefreshTokenResDTO> refreshToken(@Header("refreshToken") String refreshToken);

    @GET("api/auth/profile")
    Call<ProfileUserDTO> userProfile();

    @GET("api/subject/list")
    Call<SubjectResponse> getListSubject(
            @Query("search") String search,
            @Query("departmentId") Long departmentId,
            @Query("departmentName") String departmentName,
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("sort") String sort
    );


    @POST("api/subject")
    Call<Void> addSubject(@Body SubjectRequest subjectRequest);

    @PUT("api/subject/{subjectId}")
    Call<Void> updateSubject(@Path("subjectId") Long subjectId, @Body SubjectRequest updateDTO);

    @DELETE("api/subject/{id}")
    Call<ResponseMessage> deleteSubjectById(@Path("id") Long subjectId);


    // chapter
    @GET("api/combobox/subject/chapter")
    Call<List<ICommonIdCodeName>> getAllSubjectChapters(
            @Query("subjectId") Long subjectId
    );

    @POST("api/chapter")
    Call<Void> addSubjectChapter(
            @Body ChapterRequest request
    );

    @PUT("api/chapter/{chapterId}")
    Call<Void> updateChapterById(@Path("chapterId") Long chapterId,@Body ChapterRequest.ChapterSaveReqDTO chapterRequest);

    @DELETE("api/chapter/{id}")
    Call<ChapterResponse> deleteChapterById(@Path("id") Long chapterId);

    // question

    @GET("api/question")
    Call<List<QuestionListDTO>> getListQuestion(
            @Query("subjectId") Long subjectId,
            @Query("subjectCode") String subjectCode,
            @Query("chapterCode") String chapterCode,
            @Query("chapterIds") Long chapterIds,
            @Query("level") QuestionLevelEnum level
    );
    @GET("api/question/detail/{questionId}")
    Call<QuestionResponse> getQuestionById(@Path("questionId") Long questionId);



    @POST("api/question")
    Call<Void> createQuestion(@Body MultiQuestionRequest createDTO);
    @PUT("api/question/{questionId}")
    Call<Void> updateQuestion(@Path("questionId") Long questionId,
                              @Body QuestionUpdateDTO questionUpdateDTO);

    @Multipart
    @POST("api/question/import")
    Call<ImportResponseDTO> importQuestions(@Part MultipartBody.Part file);
    @DELETE("api/question/{questionId}")
    Call<Void> disableQuestion(@Path("questionId") Long questionId);

    // test

    @GET("api/test")
    Call<TestClassResponse> getTestList(
            @Query("subjectId") Long subjectId,
            @Query("subjectCode") String subjectCode,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime,
            @Query("semesterId") Long semesterId,
            @Query("semesterCode") String semesterCode,
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("sort") String sort
    );


    @POST("api/test/create/random")
    Call<Long> createRandomTest(@Body TestClassRequest request);

    // class
    @GET("api/exam-class/page")
    Call<ClassResponse> getListExamClass(
            @Query("code") String code,
            @Query("semesterId") Long semesterId,
            @Query("subjectId") Long subjectId,
            @Query("testId") Long testId,
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("sort") String sort
    );

    @GET("api/exam-class/participant/list/{examClassId}")
    Call<List<IExamClassParticipantDTO>> getListExamClassParticipant(
            @Path("examClassId") Long examClassId,
            @Query("roleType") UserExamClassRoleEnum roleType
    );

    @PUT("api/exam-class/participant")
    Call<Void> updateParticipantToExamClass(@Body UserExamClassDTO userExamClassDTO);

    @Multipart
    @POST("api/exam-class/participant/student/import/{examClassId}")
    Call<Set<Long>> importStudents(
            @Path("examClassId") Long examClassId,
            @Part MultipartBody.Part file
    );
    @POST("api/exam-class")
    Call<Long> createClass(@Body ClassRequest classRequest);
    @PUT("api/exam-class/{id}")
    Call<Void> updateExamClass(@Path("id") Long id, @Body ExamClassSaveReqDTO updateDTO);


    @GET("api/exam-class/participant/export/{examClassId}")
    Call<ResponseBody> exportStudentTestExcel(@Path("examClassId") Long examClassId,
                                              @Query("roleType") UserExamClassRoleEnum roleType);

    @DELETE("api/v1/class/disable/{id}")
    Call<ResponseMessage> disableClassRoomExam(@Path("id") Long classRoom);




    // test-set
    @POST("api/test-set/generate")
    Call<List<TestSetPreviewDTO>> createTestSetFromTest(
            @Body TestSetGenerateReqDTO generateDTO
    );
    @POST("api/test-set/detail")
    Call<TestSetDetailResponse> getTestSetDetail(
            @Body TestSetSearchReqDTO searchReqDTO
    );
    @POST("api/test-set/scoring/result/save")
    Call<Void> saveScoringResult(
            @Query("tempFileCode") String tempFileCode,
            @Query("option") String option
    );

    @Multipart
    @POST("api/test-set/handled-answers/upload/{examClassCode}")
    Call<Void> uploadStudentTestImages(
            @Path ("examClassCode") String examClassCode,
            @Part List<MultipartBody.Part> files
    );
    
    @GET("api/test-set/scoring/exam-class/{exClassCode}")
    Call<ScoringPreviewResDTO> loadScoredStudentTestSet(@Path("exClassCode") String exClassCode);

    @GET("api/std-test-set/result/{examClassCode}")
    Call<ExamClassResultStatisticsDTO> getStudentTestSetResult(@Path("examClassCode") String examClassCode);

    @GET("api/std-test-set/result/export/{examClassCode}")
    Call<ResponseBody> exportStudentTestScoring(@Path("examClassCode") String examClassCode);

    @GET("api/test-set/handled-answers/uploaded/{examClassCode}")
    Call<List<FileAttachDTO>> getListFileInExFolder(@Path("examClassCode") String examClassCode);

    @POST("api/test-set/handled-answers/delete")
    Call<Void> deleteImagesInClassFolder(@Body HandledImagesDeleteDTO deleteDTO);
    // student

    @GET("api/user/student/list")
    Call<List<StudentResponse>> getListStudent(
            @Query("search") String search,
            @Query("courseNums") Integer courseNums
    );
    @Multipart
    @POST("api/user/student/import")
    Call<ImportResponseDTO> importStudent(@Part MultipartBody.Part file);
    @GET("api/user/student/export")
    Call<ResponseBody> exportExcelFileStudent(@Query("search") String search,
                                              @Query("courseNums") Integer courseNums);


    @DELETE("api/user/hard/{id}")
    Call<Void> disableStudentById(
            @Query("userType") UserTypeEnum userType,
            @Path("id") Long id
    );
    @PUT("api/user/{userId}")
    Call<Void> updateStudentById(@Path("userId") Long studentId,
                                            @Body StudentUpdateRequest studentUpdateRequest);


    @POST("api/v1/student/add")
    Call<ResponseMessage> addStudent(@Body StudentRequest studentRequest);

    @PUT("api/v1/student/update/profile")
    Call<ResponseMessage> updateStudentProfile(@Body StudentUpdateRequest request);


    //teacher
    @GET("api/user/teacher/list")
    Call<List<TeacherResponse>> getListTeacher(
            @Query("search") String search
    );
    @Multipart
    @POST("api/user/teacher/import")
    Call<ImportResponseDTO> importTeacher(@Part MultipartBody.Part file);
    @GET("api/user/teacher/export")
    Call<ResponseBody> exportExcelFileTeacher(
            @Query("search") String search
    );
    @PUT("api/user/{userId}")
    Call<Void> updateTeacherById(@Path("userId") Long teacherId,
                                            @Body TeacherUpdateRequest teacherUpdateRequest);
    @DELETE("api/user/hard/{id}")
    Call<Void> disableTeacherById(
            @Query("userType") UserTypeEnum userType,
            @Path("id") Long id
    );
    @POST("api/v1/teacher/add")
    Call<ResponseMessage> addTeacher(@Body TeacherRequest teacherRequest);


    @PUT("api/v1/teacher/update/profile")
    Call<ResponseMessage> updateTeacherProfile(@Body TeacherUpdateRequest request);


    @GET("api/combobox/semester")
    Call<List<SemesterBox>> getListSemester(@Query("search") String search);

    @GET("api/combobox/exam-class")
    Call<List<ICommonIdCode>> getListExamClass(
            @Query("semesterId") Long semesterId,
            @Query("subjectId") Long subjectId,
            @Query("search") String search
    );




    @DELETE("api/v1/test/disable/{id}")
    Call<ResponseMessage> disableTestById(@Path("id") Integer testId);
}
