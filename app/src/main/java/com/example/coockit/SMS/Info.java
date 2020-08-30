package com.example.coockit.SMS;

public class Info {
    private String email;
    private String name;
    private String id;
    private String img;
    private String pass;
    private String phone;

    public Info() {
    }

    public Info(String email, String name, String id, String img, String pass, String phone) {
        this.email = email;
        this.name = name;
        this.id = id;
        this.img = img;
        this.pass = pass;
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getPass() {
        return pass;
    }

    public String getPhone() {
        return phone;
    }
}

