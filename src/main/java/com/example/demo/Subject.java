package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Subject {

    private SimpleStringProperty subjectId;
    private SimpleStringProperty subjectName;
    private SimpleStringProperty subjectNumber;
    private SimpleStringProperty subjectDescription;
    private SimpleStringProperty subjectNoSections;

    public Subject() {
        this.subjectId = new SimpleStringProperty(this, "subjectId", "n/a");
        this.subjectName = new SimpleStringProperty(this, "subjectName", "n/a");
        this.subjectNumber = new SimpleStringProperty(this, "subjectNumber", "n/a");
        this.subjectDescription = new SimpleStringProperty(this, "subjectDescription", "n/a");
        this.subjectNoSections = new SimpleStringProperty(this, "subjectNoSections", "n/a");
    }

    public static void processSubjectsTable(ResultSet rs, ObservableList<Subject> subjectList){
        Subject testSubject = null;

        try {
            while (rs.next()) {
                String subjectId = new String(rs.getString(1));
                String subjectLastName = new String(rs.getString(2));
                String subjectNumber = new String(rs.getString(3));
                String subjectDescription = new String(rs.getString(4));
                String subjectNoSections = new String(rs.getString(5));

                System.out.println("Subject Id: " + subjectId);
                System.out.println("Subject Last Name: " + subjectLastName);
                System.out.println("Subject Number: " + subjectNumber);
                System.out.println("Subject Description: " + subjectDescription);
                System.out.println("Subject Number of Sections: " + subjectNoSections);
                System.out.println("");

                // Check if subjectList already contains the subject
                System.out.println("Creating testSubject");
                testSubject = new Subject();
                testSubject.setSubjectId(subjectId);
                testSubject.setSubjectName(subjectLastName);
                testSubject.setSubjectNumber(subjectNumber);
                testSubject.setSubjectDescription(subjectDescription);
                testSubject.setSubjectNoSections(subjectNoSections);
                System.out.println("Created testSubject");

                if (!subjectList.contains(testSubject)) {
                    System.out.println("Adding subject to list");
                    subjectList.add(testSubject);
                    System.out.println("Added subject to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //When instructor is selected in the instructor tab table
    public static void processSubjectsTableSelect(ResultSet rs, ObservableList<String> subjectSections){
        //Sections.Section_Id, Sections.Section_Time Sections.Section_Room
        String testString = null;

        try {
            while (rs.next()) {
                String Section_Id = new String(rs.getString(1));
                String Section_Time = new String(rs.getString(2));
                String Section_Room = new String(rs.getString(3));

                System.out.println("Section_Id: " + Section_Id);
                System.out.println("Section_Time: " + Section_Time);
                System.out.println("Section_Room: " + Section_Room);
                System.out.println("");

                // Check if subjectList already contains the subject
                System.out.println("Creating testSubject");
                testString = new String(Section_Id + " Time: " + Section_Time + " Room: " + Section_Room);
                System.out.println("Created testSubject");

                if (!subjectSections.contains(testString)) {
                    //System.out.println("Adding enrolled class to list");
                    subjectSections.add(testString);
                    //System.out.println("Added testString to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSubjectId() {
        return subjectId.get();
    }

    public SimpleStringProperty subjectIdProperty() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId.set(subjectId);
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName.set(subjectName);
    }

    public String getSubjectNumber() {
        return subjectNumber.get();
    }

    public SimpleStringProperty subjectNumberProperty() {
        return subjectNumber;
    }

    public void setSubjectNumber(String subjectNumber) {
        this.subjectNumber.set(subjectNumber);
    }

    public String getSubjectDescription() {
        return subjectDescription.get();
    }

    public SimpleStringProperty subjectDescriptionProperty() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription.set(subjectDescription);
    }

    public String getSubjectNoSections() {
        return subjectNoSections.get();
    }

    public SimpleStringProperty subjectNoSectionsProperty() {
        return subjectNoSections;
    }

    public void setSubjectNoSections(String subjectNoSections) {
        this.subjectNoSections.set(subjectNoSections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (!subjectId.equals(subject.subjectId)) return false;
        if (!subjectName.equals(subject.subjectName)) return false;
        if (!subjectNumber.equals(subject.subjectNumber)) return false;
        return subjectDescription.equals(subject.subjectDescription);
    }

    @Override
    public int hashCode() {
        int result = subjectId.hashCode();
        result = 31 * result + subjectName.hashCode();
        result = 31 * result + subjectNumber.hashCode();
        result = 31 * result + subjectDescription.hashCode();
        return result;
    }
}

