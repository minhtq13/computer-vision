package com.example.multiplechoiceexam.dto.answer;
import android.os.Parcel;
import android.os.Parcelable;


public class AnswerRequest implements Parcelable {
    String content;

    Boolean isCorrect;

    Long imageId;

    public AnswerRequest() {
    }

    public AnswerRequest(String content, Boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    // Parcelable implementation
    protected AnswerRequest(Parcel in) {
        content = in.readString();
        byte tmpIsCorrect = in.readByte();
        isCorrect = tmpIsCorrect == 0 ? null : tmpIsCorrect == 1;
        if (in.readByte() == 0) {
            imageId = null;
        } else {
            imageId = in.readLong();
        }
    }

    public static final Creator<AnswerRequest> CREATOR = new Creator<AnswerRequest>() {
        @Override
        public AnswerRequest createFromParcel(Parcel in) {
            return new AnswerRequest(in);
        }

        @Override
        public AnswerRequest[] newArray(int size) {
            return new AnswerRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeByte((byte) (isCorrect == null ? 0 : isCorrect ? 1 : 2));
        if (imageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(imageId);
        }
    }
}
