package com.selfowner.krypto_tutor;

public class RegisterHelperClassTeacher {
    String Name;
    String Email;
    String Contact;
    String Select;
    String TeacherName;
    String TeacherCode;
    String TSID;
    String Semester;
    String Stream;
    String Type;
    RegisterHelperClassTeacher(){

    }
    public RegisterHelperClassTeacher(String name, String email, String contact, String select, String teacherName, String teacherCode,String TSID,String semester,String stream,String type) {
        Name = name;
        Email = email;
        Contact = contact;
        Select = select;
        TeacherName = teacherName;
        TeacherCode = teacherCode;
        this.TSID=TSID;
        Semester=semester;
        Stream=stream;
        Type=type;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public void setStream(String stream) {
        Stream = stream;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSemester() {
        return Semester;
    }

    public String getStream() {
        return Stream;
    }

    public String getType() {
        return Type;
    }

    public void setTSID(String TSID) {
        this.TSID = TSID;
    }

    public String getTSID() {
        return TSID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    public String getSelect() {
        return Select;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public String getTeacherCode() {
        return TeacherCode;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setSelect(String select) {
        Select = select;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public void setTeacherCode(String teacherCode) {
        TeacherCode = teacherCode;
    }
}
