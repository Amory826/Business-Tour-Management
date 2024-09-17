package com.nguyentrongtuan.businesstourmanagement.Models;

import java.util.List;

public class Tours {
    private long available;
    private String code;
    private String description;
    private long id;
    private String idCompany;
    private String idTeacher;
    private String name;
    private String startDate;
    private List<Students> studentsList;

    public Tours() {
    }

    public Tours(long available, String code, String description, long id, String idCompany,
                 String idTeacher, String name, String startDate, List<Students> studentsList) {
        this.available = available;
        this.code = code;
        this.description = description;
        this.id = id;
        this.idCompany = idCompany;
        this.idTeacher = idTeacher;
        this.name = name;
        this.startDate = startDate;
        this.studentsList = studentsList;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Students> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<Students> studentsList) {
        this.studentsList = studentsList;
    }
}
