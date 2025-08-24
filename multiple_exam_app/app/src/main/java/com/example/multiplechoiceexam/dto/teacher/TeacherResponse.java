package com.example.multiplechoiceexam.dto.teacher;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TeacherResponse implements Parcelable {
    private Long id;
    private String code;
    private Integer identityType;
    private String gender;
    private String identificationNumber;
    private String avatarPath;
    private Long avatarId;
    private String firstName;
    private String lastName;

    private String birthDate;

    private String address;
    private String phoneNumber;
    private String email;
    private Long departmentId;
    private String departmentName;
    private String userType;
    private Integer courseNum;


    public TeacherResponse() {
    }

    public TeacherResponse(Long id, String code, Integer identityType, String gender,
                           String identificationNumber, String avatarPath, Long avatarId,
                           String firstName, String lastName, String birthDate, String address,
                           String phoneNumber, String email, Long departmentId, String departmentName,
                           String userType, Integer courseNum) {
        this.id = id;
        this.code = code;
        this.identityType = identityType;
        this.gender = gender;
        this.identificationNumber = identificationNumber;
        this.avatarPath = avatarPath;
        this.avatarId = avatarId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.userType = userType;
        this.courseNum = courseNum;
    }

    // Implement getters and setters for each field


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(Integer courseNum) {
        this.courseNum = courseNum;
    }

    protected TeacherResponse(Parcel in) {
        id = in.readLong();
        code = in.readString();
        identityType = in.readInt();
        gender = in.readString();
        identificationNumber = in.readString();
        avatarPath = in.readString();
        avatarId = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        birthDate = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        departmentId = in.readLong();
        departmentName = in.readString();
        userType = in.readString();
        courseNum = in.readInt();
    }

    public static final Creator<TeacherResponse> CREATOR = new Creator<TeacherResponse>() {
        @Override
        public TeacherResponse createFromParcel(Parcel in) {
            return new TeacherResponse(in);
        }

        @Override
        public TeacherResponse[] newArray(int size) {
            return new TeacherResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(id != null ? id : -1);
        parcel.writeString(code != null ? code : "");
        parcel.writeInt(identityType != null ? identityType : -1);
        parcel.writeString(gender != null ? gender : "");
        parcel.writeString(identificationNumber != null ? identificationNumber : "");
        parcel.writeString(avatarPath != null ? avatarPath : "");
        parcel.writeLong(avatarId != null ? avatarId : -1);
        parcel.writeString(firstName != null ? firstName : "");
        parcel.writeString(lastName != null ? lastName : "");
        parcel.writeString(birthDate != null ? birthDate:"");
        parcel.writeString(address != null ? address : "");
        parcel.writeString(phoneNumber != null ? phoneNumber : "");
        parcel.writeString(email != null ? email : "");
        parcel.writeLong(departmentId != null ? departmentId : -1);
        parcel.writeString(departmentName != null ? departmentName : "");
        parcel.writeString(userType != null ? userType : "");
        parcel.writeInt(courseNum != null ? courseNum : -1);

    }
}
