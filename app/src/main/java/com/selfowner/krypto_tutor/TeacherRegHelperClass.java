package com.selfowner.krypto_tutor;

public class TeacherRegHelperClass {
    String TID;
    String TSID;
    String Name;
    String Contact;
    String Email;
    String Qualification;
    String Department;
    String Subject;
    String Identity;
    String SourceInfo;
    String ImageFile;
    String Students;
    String PaymentId;
    String PaymentAmount;
    String Date;
    String TotalPayment;
    String InitialTime;
    String PlanPolicy;
    String TCODE;
    TeacherRegHelperClass(){

    }

    public TeacherRegHelperClass(String TID, String TSID, String name, String contact, String email, String qualification, String department, String subject, String identity, String sourceInfo, String imageFile, String students, String paymentId, String paymentAmount,String date,String totalPayment,String initialTime,String planPolicy,String TCODE) {

        this.TID = TID;
        this.TSID = TSID;
        Name = name;
        Contact = contact;
        Email = email;
        Qualification = qualification;
        Department = department;
        Subject = subject;
        Identity = identity;
        SourceInfo = sourceInfo;
        ImageFile = imageFile;
        Students = students;
        PaymentId = paymentId;
        PaymentAmount = paymentAmount;
        Date=date;
        TotalPayment=totalPayment;
        InitialTime=initialTime;
        PlanPolicy=planPolicy;
        this.TCODE=TCODE;

    }

    public void setTCODE(String TCODE) {
        this.TCODE = TCODE;
    }

    public String getTCODE() {
        return TCODE;
    }

    public void setPlanPolicy(String planPolicy) {
        PlanPolicy = planPolicy;
    }

    public String getPlanPolicy() {
        return PlanPolicy;
    }

    public String getInitialTime() {
        return InitialTime;
    }

    public void setInitialTime(String initialTime) {
        InitialTime = initialTime;
    }

    public void setTotalPayment(String totalPayment) {
        TotalPayment = totalPayment;
    }

    public String getTotalPayment() {
        return TotalPayment;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public String getTID() {
        return TID;
    }

    public String getTSID() {
        return TSID;
    }

    public String getName() {
        return Name;
    }

    public String getContact() {
        return Contact;
    }

    public String getEmail() {
        return Email;
    }

    public String getQualification() {
        return Qualification;
    }

    public String getDepartment() {
        return Department;
    }

    public String getSubject() {
        return Subject;
    }

    public String getIdentity() {
        return Identity;
    }

    public String getSourceInfo() {
        return SourceInfo;
    }

    public String getImageFile() {
        return ImageFile;
    }

    public String getStudents() {
        return Students;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public String getPaymentAmount() {
        return PaymentAmount;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public void setTSID(String TSID) {
        this.TSID = TSID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public void setIdentity(String identity) {
        Identity = identity;
    }

    public void setSourceInfo(String sourceInfo) {
        SourceInfo = sourceInfo;
    }

    public void setImageFile(String imageFile) {
        ImageFile = imageFile;
    }

    public void setStudents(String students) {
        Students = students;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public void setPaymentAmount(String paymentAmount) {
        PaymentAmount = paymentAmount;
    }
}
