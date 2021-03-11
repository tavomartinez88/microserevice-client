package com.gustavomartinez.getclients.model;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Client {

    private String firstName;
    private String lastName;
    private Integer age;
    private String birthDate;
    private LocalDate possibleDeadDate;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getPossibleDeadDate() {
        return possibleDeadDate;
    }

    public void setPossibleDeadDate(LocalDate possibleDeadDate) {
        this.possibleDeadDate = possibleDeadDate;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age='" + age + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", possibleDeadDate='" + possibleDeadDate + '\'' +
                '}';
    }
}
