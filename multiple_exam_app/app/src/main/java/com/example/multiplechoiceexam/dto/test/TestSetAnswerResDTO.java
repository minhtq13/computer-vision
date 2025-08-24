package com.example.multiplechoiceexam.dto.test;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TestSetAnswerResDTO implements Parcelable {
    Long answerId;

    Integer answerNo;

    String answerNoMask;
    Boolean isCorrect;

    String content;

    public TestSetAnswerResDTO() {
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Integer getAnswerNo() {
        return answerNo;
    }

    public void setAnswerNo(Integer answerNo) {
        this.answerNo = answerNo;
    }

    public String getAnswerNoMask() {
        return answerNoMask;
    }

    public void setAnswerNoMask(String answerNoMask) {
        this.answerNoMask = answerNoMask;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(answerId != null ? answerId : -1);
        parcel.writeInt(answerNo != null ? answerNo : -1);
        parcel.writeString(answerNoMask != null ? answerNoMask : "");
        parcel.writeString(content != null ? content : "");

    }

    // Parcelable CREATOR
    public static final Parcelable.Creator<TestSetAnswerResDTO> CREATOR = new Parcelable.Creator<TestSetAnswerResDTO>() {
        @Override
        public TestSetAnswerResDTO createFromParcel(Parcel in) {
            return new TestSetAnswerResDTO(in);
        }

        @Override
        public TestSetAnswerResDTO[] newArray(int size) {
            return new TestSetAnswerResDTO[size];
        }
    };

    // Constructor for reading from Parcel
    protected TestSetAnswerResDTO(Parcel in) {
        // Read your fields from the parcel
        answerId = in.readLong();
        answerNo = in.readInt();
        answerNoMask = in.readString();
        content = in.readString();
    }
}
