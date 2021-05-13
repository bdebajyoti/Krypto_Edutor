package com.selfowner.krypto_tutor;

public class RegisterHelperClass {
    String email;
    String contact;
    String Select;
    String Name;
    RegisterHelperClass(){

    }

    public RegisterHelperClass(String email, String contact, String select,String name) {
        this.email = email;
        this.contact = contact;
        Select = select;
        this.Name=name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getSelect() {
        return Select;
    }

    public String getNAme() {
        return Name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setSelect(String select) {
        Select = select;
    }

    public void setName(String name) {
        Name = name;
    }
}
