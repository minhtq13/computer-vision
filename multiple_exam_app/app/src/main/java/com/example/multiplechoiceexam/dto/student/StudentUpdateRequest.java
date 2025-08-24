package com.example.multiplechoiceexam.dto.student;

public class StudentUpdateRequest {

    private String email;

    private String firstName;

    private String lastName;

    private String code;
    private Long departmentId;

    private String birthDate;

    private String genderType;

    private String phoneNumber;

    Integer userType;

    Integer courseNum;
    public boolean isValid() {
        return isValidEmail() && isValidFirstName() && isValidLastName() && isValidCode() && isValidBirthday()
                && isValidGender() && isValidPhoneNumber();
    }

    private boolean isValidEmail() {
        return true;
    }
    private boolean isValidFirstName(){return firstName != null && !firstName.trim().isEmpty();}
    private boolean isValidLastName(){return lastName != null && !lastName.trim().isEmpty();}

    private boolean isValidCode() {
        return code != null && !code.trim().isEmpty();
    }

    private boolean isValidBirthday() {
        return birthDate != null && !birthDate.trim().isEmpty();
    }

    private boolean isValidGender() {
        return genderType != null && !genderType.trim().isEmpty();
    }

    private boolean isValidPhoneNumber() {
        return phoneNumber != null && phoneNumber.matches("(84|0[3|5789])+([0-9]{8})\\b");
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(Integer courseNum) {
        this.courseNum = courseNum;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthday() {
        return birthDate;
    }

    public void setBirthday(String birthday) {
        this.birthDate = birthday;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
