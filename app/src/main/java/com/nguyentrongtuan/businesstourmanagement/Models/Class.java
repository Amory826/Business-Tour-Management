package com.nguyentrongtuan.businesstourmanagement.Models;

public class Class {
    private long id;
    private String code;
    private String idTeacher;
    private String name;

    public Class() {
    }

    public Class(long id, String code, String idTeacher, String name) {
        this.id = id;
        this.code = code;
        this.idTeacher = idTeacher;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
