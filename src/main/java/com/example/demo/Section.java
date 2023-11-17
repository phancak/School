package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Section {

    private SimpleStringProperty sectionId;
    private SimpleStringProperty sectionSubject;
    private SimpleStringProperty sectionStartTime;
    private SimpleStringProperty sectionEndTime;
    private SimpleStringProperty sectionDays; //MWF, TTh
    private SimpleStringProperty sectionInstructor;
    private SimpleStringProperty sectionEnrolment;

    public Section() {
        this.sectionId = new SimpleStringProperty(this, "sectionId", "n/a");
        this.sectionSubject = new SimpleStringProperty(this, "sectionSubject", "n/a");
        this.sectionStartTime = new SimpleStringProperty(this, "sectionStartTime", "n/a");
        this.sectionEndTime = new SimpleStringProperty(this, "sectionEndTime", "n/a");
        this.sectionDays = new SimpleStringProperty(this, "sectionDays", "n/a");
        this.sectionInstructor = new SimpleStringProperty(this, "sectionInstructor", "n/a");
        this.sectionEnrolment = new SimpleStringProperty(this, "sectionEnrolment", "n/a");
    }

    public static void processSectionsTable(ResultSet rs, ObservableList<Section> sectionList){
        Section testSection = null;

        try {
            while (rs.next()) {
                String sectionId = rs.getString(1);
                String sectionSubjectName = rs.getString(2);
                String sectionSubjectNumber = rs.getString(3);
                String sectionStartTime = rs.getString(4);
                String sectionEndTime = rs.getString(5);
                String sectionDays = rs.getString(6);
                String sectionInstructorFirstName = rs.getString(7);
                String sectionInstructorLastName = rs.getString(8);
                String sectionEnrolment = rs.getString(9);

                System.out.println("Section Id: " + sectionId);
                System.out.println("Section Subject Name: " + sectionSubjectName);
                System.out.println("Section Subject Number: " + sectionSubjectNumber);
                System.out.println("Section Start Time: " + sectionStartTime);
                System.out.println("Section End Time: " + sectionEndTime);
                System.out.println("Section Days: " + sectionDays);
                System.out.println("Section Instructor: " + sectionInstructorLastName);
                System.out.println("sectionEnrolment: " + sectionEnrolment);
                System.out.println("");

                // Check if sectionList already contains the section
                testSection = new Section();
                testSection.setSectionId(sectionId);
                testSection.setSectionSubject(sectionSubjectName + " " + sectionSubjectNumber);
                testSection.setSectionStartTime(sectionStartTime);
                testSection.setSectionEndTime(sectionEndTime);
                testSection.setSectionDays(sectionDays);
                testSection.setSectionInstructor(sectionInstructorLastName + ", " + sectionInstructorFirstName);
                testSection.setSectionEnrolment(sectionEnrolment);

                if (!sectionList.contains(testSection)) {
                    sectionList.add(testSection);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getSectionId() {
        return sectionId.get();
    }

    public SimpleStringProperty sectionIdProperty() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId.set(sectionId);
    }

    public String getSectionSubject() {
        return sectionSubject.get();
    }

    public SimpleStringProperty sectionSubjectProperty() {
        return sectionSubject;
    }

    public void setSectionSubject(String sectionSubject) {
        this.sectionSubject.set(sectionSubject);
    }

    public String getSectionStartTime() {
        return sectionStartTime.get();
    }

    public SimpleStringProperty sectionStartTimeProperty() {
        return sectionStartTime;
    }

    public void setSectionStartTime(String sectionStartTime) {
        this.sectionStartTime.set(sectionStartTime);
    }

    public String getSectionEndTime() {
        return sectionEndTime.get();
    }

    public SimpleStringProperty sectionEndTimeProperty() {
        return sectionEndTime;
    }

    public void setSectionEndTime(String sectionEndTime) {
        this.sectionEndTime.set(sectionEndTime);
    }

    public String getSectionDays() {
        return sectionDays.get();
    }

    public SimpleStringProperty sectionDaysProperty() {
        return sectionDays;
    }

    public void setSectionDays(String sectionDays) {
        this.sectionDays.set(sectionDays);
    }

    public String getSectionInstructor() {
        return sectionInstructor.get();
    }

    public SimpleStringProperty sectionInstructorProperty() {
        return sectionInstructor;
    }

    public void setSectionInstructor(String sectionInstructor) {
        this.sectionInstructor.set(sectionInstructor);
    }

    public String getSectionEnrolment() {
        return sectionEnrolment.get();
    }

    public SimpleStringProperty sectionEnrolmentProperty() {
        return sectionEnrolment;
    }

    public void setSectionEnrolment(String sectionEnrolment) {
        this.sectionEnrolment.set(sectionEnrolment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;

        Section section = (Section) o;

        if (!sectionId.equals(section.sectionId)) return false;
        if (!sectionSubject.equals(section.sectionSubject)) return false;
        if (!sectionStartTime.equals(section.sectionStartTime)) return false;
        if (!sectionEndTime.equals(section.sectionEndTime)) return false;
        if (!sectionDays.equals(section.sectionDays)) return false;
        return sectionInstructor.equals(section.sectionInstructor);
    }

    @Override
    public int hashCode() {
        int result = sectionId.hashCode();
        result = 31 * result + sectionSubject.hashCode();
        result = 31 * result + sectionStartTime.hashCode();
        result = 31 * result + sectionEndTime.hashCode();
        result = 31 * result + sectionDays.hashCode();
        result = 31 * result + sectionInstructor.hashCode();
        return result;
    }
}

