package com.example.multiplechoiceexam.dto.student;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StudentResponse implements Parcelable{
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    private String address;
    private String phoneNumber;
    private String email;
    private Long departmentId;
    private String departmentName;
    private String userType;
    private Integer courseNum;

    // Constructors, getters, and setters

    public StudentResponse() {
        // Default constructor
    }

    public StudentResponse(Long id, String code, Integer identityType, String gender,
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

    protected StudentResponse(Parcel in) {
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

    public static final Creator<StudentResponse> CREATOR = new Creator<StudentResponse>() {
        @Override
        public StudentResponse createFromParcel(Parcel in) {
            return new StudentResponse(in);
        }

        @Override
        public StudentResponse[] newArray(int size) {
            return new StudentResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        if (id != null) {
            parcel.writeLong(id);
        } else {
            parcel.writeLong(-1); // Hoặc bất kỳ giá trị đặc biệt nào thích hợp
        }
        if (code != null) {
            parcel.writeString(code);
        } else {
            parcel.writeString("");
        }

        if (identityType != null) {
            parcel.writeInt(identityType);
        } else {
            parcel.writeInt(-1); // Hoặc bất kỳ giá trị đặc biệt nào thích hợp
        }
        if (gender != null) {
            parcel.writeString(gender);
        } else {
            parcel.writeString("");
        }
        if (identificationNumber != null) {
            parcel.writeString(identificationNumber);
        } else {
            parcel.writeString("");
        }
        if (avatarPath != null) {
            parcel.writeString(avatarPath);
        } else {
            parcel.writeString("");
        }
        if (avatarId != null) {
            parcel.writeLong(avatarId);
        } else {
            parcel.writeLong(-1); // Hoặc bất kỳ giá trị đặc biệt nào thích hợp
        }
        if (firstName != null) {
            parcel.writeString(firstName);
        } else {
            parcel.writeString("");
        }
        if (lastName != null) {
            parcel.writeString(lastName);
        } else {
            parcel.writeString("");
        }

        if (birthDate != null) {
            parcel.writeString(birthDate);
        } else {
            parcel.writeString("");
        }

        if (address != null) {
            parcel.writeString(address);
        } else {
            parcel.writeString("");
        }
        if (phoneNumber != null) {
            parcel.writeString(phoneNumber);
        } else {
            parcel.writeString("");
        }
        if (email != null) {
            parcel.writeString(email);
        } else {
            parcel.writeString("");
        }

        // Kiểm tra và xử lý Long có thể là null
        if (departmentId != null) {
            parcel.writeLong(departmentId);
        } else {
            parcel.writeLong(-1); // Hoặc bất kỳ giá trị đặc biệt nào thích hợp
        }

        if (departmentName != null) {
            parcel.writeString(departmentName);
        } else {
            parcel.writeString("");
        }
        if (userType != null) {
            parcel.writeString(userType);
        } else {
            parcel.writeString("");
        }
        if (courseNum != null) {
            parcel.writeInt(courseNum);
        } else {
            parcel.writeInt(-1); // Hoặc bất kỳ giá trị đặc biệt nào thích hợp
        }
    }
}
