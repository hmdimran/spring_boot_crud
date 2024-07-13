package com.himran.crud.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class UserDto {
    @NotEmpty(message = "The Name Is Required")
    private String user_name;
    @NotEmpty(message = "The Designation Is Required")
    private String designation;
    @Min(0)
    private double salary;
    @Size(min = 10,message = "Minimum Text Size 10 is required")
    @Size(max = 2000,message = "Maximum Text Size 2000 is required")
    private String about;

    private MultipartFile imageFileName;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public MultipartFile getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(MultipartFile imageFileName) {
        this.imageFileName = imageFileName;
    }
}
