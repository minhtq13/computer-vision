package com.example.multiplechoiceexam.dto.test;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.multiplechoiceexam.Utils.HtmlUtils;
import com.example.multiplechoiceexam.Utils.ObjectMapperUtil;
import com.example.multiplechoiceexam.dto.fileAttach.FileAttachDTO;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionAnswerResDTO implements Parcelable {
    private Long id;
    private String content;
    private Integer level;
    private List<FileAttachDTO> images = new ArrayList<>();
    private Integer questionNo;
    private List<TestSetAnswerResDTO> answers;

    public TestQuestionAnswerResDTO(ITestQuestionAnswerResDTO iTestQuestionAnswerResDTO) {
        this.id = iTestQuestionAnswerResDTO.getId();
        this.content = iTestQuestionAnswerResDTO.getContent();
        this.level = iTestQuestionAnswerResDTO.getLevel();
        this.questionNo = iTestQuestionAnswerResDTO.getQuestionNo();
        this.answers = ObjectMapperUtil.listMapper(iTestQuestionAnswerResDTO.getLstAnswerJson(), TestSetAnswerResDTO.class);
        this.images = ObjectMapperUtil.listMapper(iTestQuestionAnswerResDTO.getLstImageJson(), FileAttachDTO.class);
    }

    public List<String> getCorrectAnswers() {
        List<String> correctAnswers = new ArrayList<>();

        for (TestSetAnswerResDTO answer : answers) {
            if (Boolean.TRUE.equals(answer.getCorrect())) {
                correctAnswers.add(cleanHtmlTags(answer.getContent()));
            }
        }

        return correctAnswers;
    }

    private String cleanHtmlTags(String content) {
        return HtmlUtils.getTextFromHtml(content);
    }
    public String getCorrectAnswersString() {
        List<String> correctAnswers = getCorrectAnswers();
        return TextUtils.join(", ", correctAnswers);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<FileAttachDTO> getImages() {
        return images;
    }

    public void setImages(List<FileAttachDTO> images) {
        this.images = images;
    }

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public List<TestSetAnswerResDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<TestSetAnswerResDTO> answers) {
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
// Check for null before writing to Parcel
        if (id != null) {
            parcel.writeLong(id);
        } else {
            parcel.writeLong(-1);
        }

        if (content != null) {
            parcel.writeString(content);
        } else {
            parcel.writeString("");
        }

        if (level != null) {
            parcel.writeInt(level);
        } else {
            parcel.writeInt(-1);
        }

        if (images != null) {
            parcel.writeList(images);
        } else {
            parcel.writeList(new ArrayList<>());
        }

        if (questionNo != null) {
            parcel.writeInt(questionNo);
        } else {
            parcel.writeInt(-1);
        }

        if (answers != null) {
            parcel.writeList(answers);
        } else {
            parcel.writeList(new ArrayList<>());
        }

    }

    public static final Creator<TestQuestionAnswerResDTO> CREATOR = new Creator<TestQuestionAnswerResDTO>() {
        @Override
        public TestQuestionAnswerResDTO createFromParcel(Parcel in) {
            return new TestQuestionAnswerResDTO(in);
        }

        @Override
        public TestQuestionAnswerResDTO[] newArray(int size) {
            return new TestQuestionAnswerResDTO[size];
        }
    };

    private TestQuestionAnswerResDTO(Parcel in) {
        id = in.readLong();
        content = in.readString();
        level = in.readInt();

        images = new ArrayList<>();
        in.readList(images, FileAttachDTO.class.getClassLoader());

        questionNo = in.readInt();

        answers = new ArrayList<>();
        in.readList(answers, TestSetAnswerResDTO.class.getClassLoader());
    }
}
