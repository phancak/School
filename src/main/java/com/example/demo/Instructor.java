package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Instructor {

    private SimpleStringProperty instructorId;
    private SimpleStringProperty instructorLastName;
    private SimpleStringProperty instructorFirstName;
    private SimpleStringProperty instructorDateOfBirth;
    private SimpleStringProperty instructorSpecialization;
    private SimpleStringProperty instructorEducation;

    public Instructor() {
        this.instructorId = new SimpleStringProperty(this, "instructorId","n/a");
        this.instructorLastName = new SimpleStringProperty(this, "instructorLastName","n/a");;
        this.instructorFirstName = new SimpleStringProperty(this, "instructorFirstName","n/a");;
        this.instructorDateOfBirth = new SimpleStringProperty(this, "instructorDateOfBirth","n/a");;
        this.instructorSpecialization = new SimpleStringProperty(this, "instructorSpecialization","n/a");
        this.instructorEducation = new SimpleStringProperty(this, "instructorEducation","n/a");;
    }

    public static void processInstructorsTable(ResultSet rs, ObservableList<Instructor> instructorList){
        Instructor testInstructor=null;

        System.out.println("Entered processInstructorsTable");

        try {
            while (rs.next()) {
                String instructorId = new String(rs.getString(1));
                String instructorFirstName =  new String(rs.getString(2));
                String instructorLastName =  new String(rs.getString(3));
                String instructorDofB =  new String(rs.getString(4));
                String instructorSpecialization =  new String(rs.getString(5));
                String instructorEducation =  new String(rs.getString(6));
                System.out.println("Instructor Id:" + instructorId);
                System.out.println("Instructor FirstName:" + instructorFirstName);
                System.out.println("Instructor LastName:" + instructorLastName);
                System.out.println("Instructor Date of Birth:" + instructorDofB);
                System.out.println("Instructor Specialization:" + instructorSpecialization);
                System.out.println("Instructor Education:" + instructorEducation);
                System.out.println("");

                // Check if instructorList array already contains the instructor
                System.out.println("Creating testInstructor");
                testInstructor = new Instructor();
                testInstructor.setInstructorId(instructorId);
                testInstructor.setInstructorFirstName(instructorFirstName);
                testInstructor.setInstructorLastName(instructorLastName);
                testInstructor.setInstructorDateOfBirth(instructorDofB);
                testInstructor.setInstructorSpecialization(instructorSpecialization);
                testInstructor.setInstructorEducation(instructorEducation);
                System.out.println("Created testInstructor");
                if (instructorList.contains(testInstructor) == false) {
                    System.out.println("Adding instructor to list");
                    instructorList.add(testInstructor);
                    System.out.println("Added instructor to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //When instructor is selected in the instructor tab table
    public static void processInstructorTableSelect(ResultSet rs, ObservableList<String> instructorSections){
        String testString = null;

        try {
            while (rs.next()) {
                //Subjects.Subject_Name, Subjects.Subject_Number, Sections.Section_Time, Sections.Section_Room
                //Subjects.Subject_Name, Subjects.Subject_Number
                String Subject_Name = new String(rs.getString(1));
                String Subject_Number = new String(rs.getString(2));
                String Section_Time = new String(rs.getString(3));
                String Section_Room = new String(rs.getString(4));

                System.out.println("Subject_Name: " + Subject_Name);
                System.out.println("Subject_Number: " + Subject_Number);
                System.out.println("");

                // Check if subjectList already contains the subject
                System.out.println("Creating testSubject");
                testString = new String(Subject_Name + " " + Subject_Number + " Time: " + Section_Time + " Room: " + Section_Room);
                System.out.println("Created testSubject");

                if (!instructorSections.contains(testString)) {
                    //System.out.println("Adding enrolled class to list");
                    instructorSections.add(testString);
                    //System.out.println("Added testString to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getInstructorId() {
        return instructorId.get();
    }

    public StringProperty instructorIdProperty(){
        return instructorId;
    }

    public void setInstructorId(String instructorId){
        this.instructorId.set(instructorId);
    }

    public String getInstructorLastName() {
        return instructorLastName.get();
    }

    public StringProperty instructorLastNameProperty(){
        return instructorLastName;
    }

    public void setInstructorLastName(String instructorLastName){
        this.instructorLastName.set(instructorLastName);
    }

    public String getInstructorFirstName() {
        return instructorFirstName.get();
    }

    public StringProperty instructorFirstNameProperty(){
        return instructorFirstName;
    }

    public void setInstructorFirstName(String instructorFirstName){
        this.instructorFirstName.set(instructorFirstName);
    }

    public String getInstructorDateOfBirth() {
        return instructorDateOfBirth.get();
    }

    public StringProperty instructorDateOfBirthProperty(){
        return instructorDateOfBirth;
    }

    public void setInstructorDateOfBirth(String instructorDateOfBirth){
        this.instructorDateOfBirth.set(instructorDateOfBirth);
    }

    public String getInstructorSpecialization() {
        return instructorSpecialization.get();
    }

    public StringProperty instructorSpecializationProperty(){
        return instructorSpecialization;
    }

    public void setInstructorSpecialization(String instructorSpecialization){
        this.instructorSpecialization.set(instructorSpecialization);
    }

    public String getInstructorEducation() {
        return instructorEducation.get();
    }

    public StringProperty instructorEducationProperty(){
        return instructorEducation;
    }

    public void setInstructorEducation(String instructorEducation){
        this.instructorEducation.set(instructorEducation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instructor that)) return false;

        if (!instructorId.equals(that.instructorId)) return false;
        if (!instructorLastName.equals(that.instructorLastName)) return false;
        if (!instructorFirstName.equals(that.instructorFirstName)) return false;
        if (!Objects.equals(instructorDateOfBirth, that.instructorDateOfBirth))
            return false;
        if (!Objects.equals(instructorSpecialization, that.instructorSpecialization))
            return false;
        return Objects.equals(instructorEducation, that.instructorEducation);
    }

    @Override
    public int hashCode() {
        int result = instructorId.hashCode();
        result = 31 * result + instructorLastName.hashCode();
        result = 31 * result + instructorFirstName.hashCode();
        result = 31 * result + (instructorDateOfBirth != null ? instructorDateOfBirth.hashCode() : 0);
        result = 31 * result + (instructorSpecialization != null ? instructorSpecialization.hashCode() : 0);
        result = 31 * result + (instructorEducation != null ? instructorEducation.hashCode() : 0);
        return result;
    }
}
