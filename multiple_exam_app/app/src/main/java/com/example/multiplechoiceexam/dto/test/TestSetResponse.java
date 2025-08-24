package com.example.multiplechoiceexam.dto.test;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TestSetResponse implements Parcelable {
    private int id;

    private String testNo;

    private int duration;

    private String subjectTitle;

    private String subjectCode;

    private int questionQuantity;

    private String createdAt;

    private String updatedAt;

    private String testDay;

    public TestSetResponse() {
    }

    protected TestSetResponse(Parcel in) {
        id = in.readInt();
        testNo = in.readString();
        duration = in.readInt();
        subjectTitle = in.readString();
        subjectCode = in.readString();
        questionQuantity = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        testDay = in.readString();
    }

    public static final Creator<TestSetResponse> CREATOR = new Creator<TestSetResponse>() {
        @Override
        public TestSetResponse createFromParcel(Parcel in) {
            return new TestSetResponse(in);
        }

        @Override
        public TestSetResponse[] newArray(int size) {
            return new TestSetResponse[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getQuestionQuantity() {
        return questionQuantity;
    }

    public void setQuestionQuantity(int questionQuantity) {
        this.questionQuantity = questionQuantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTestDay() {
        return testDay;
    }

    public void setTestDay(String testDay) {
        this.testDay = testDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(testNo);
        parcel.writeInt(duration);
        parcel.writeString(subjectTitle);
        parcel.writeString(subjectCode);
        parcel.writeInt(questionQuantity);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(testDay);
    }
}
