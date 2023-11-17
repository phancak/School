package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Student {

    private SimpleStringProperty studentId;
    private SimpleStringProperty studentLastName;
    private SimpleStringProperty studentFirstName;
    private SimpleStringProperty studentDateOfBirth;
    private SimpleStringProperty studentHomeTown;
    private SimpleStringProperty studentHomeCountry;
    private SimpleStringProperty studentHighSchoolAverage;

    //bean - the bean of this StringProperty
    //name - the name of this StringProperty (2nd argument)
    //initial value (3rd argument)
    //getBean() Returns the Object that contains this property.
    //getName() Returns the name of this property.
    public Student() {
        this.studentId = new SimpleStringProperty(this, "studentId","n/a");
        this.studentLastName = new SimpleStringProperty(this, "studentLastName","n/a");
        this.studentFirstName = new SimpleStringProperty(this, "studentFirstName","n/a");
        this.studentDateOfBirth = new SimpleStringProperty(this, "studentDateOfBirth","n/a");
        this.studentHomeTown = new SimpleStringProperty(this, "studentHomeTown","n/a");
        this.studentHomeCountry = new SimpleStringProperty(this, "studentHomeCountry","n/a");
        this.studentHighSchoolAverage = new SimpleStringProperty(this, "studentHighSchoolAverage","n/a");
    }

    //Updates ObservableList<Babysitter> dataBBS
    public static void processStudentTable(ResultSet rs, ObservableList<Student> studentList){
        Student testStudent=null;
        Integer studentIdInt = 0;
        String studentDofBString;

        try {
            while (rs.next()) {
                String studentId = new String(rs.getString(1));
                String studentFirstName =  new String(rs.getString(2));
                String studentLastName =  new String(rs.getString(3));
                String studentDateOfBirth =  new String(rs.getString(4));
                String studentHomeTown =  new String(rs.getString(5));
                String studentHomeCountry =  new String(rs.getString(6));
                String studentHighSchoolAverage =  new String(rs.getString(7));
                System.out.println("Student Id:" + studentId);
                System.out.println("studentFirstName:" + studentFirstName);
                System.out.println("studentLastName:" + studentLastName);
                System.out.println("studentDofB:" + studentDateOfBirth);
                System.out.println("studentHomeTown:" + studentHomeTown);
                System.out.println("studentHomeCountry:" + studentHomeCountry);
                System.out.println("studentHighSchoolAverage:" + studentHighSchoolAverage);
                System.out.println("");

                //Check if dataBBS array already contains the babysitter
                System.out.println("Creating testStudent");
                testStudent = new Student();
                testStudent.setStudentId(studentId);
                testStudent.setStudentFirstName(studentFirstName);
                testStudent.setStudentLastName(studentLastName);
                testStudent.setStudentDateOfBirth(studentDateOfBirth);
                testStudent.setStudentHomeTown(studentHomeTown);
                testStudent.setStudentHomeCountry(studentHomeCountry);
                testStudent.setStudentHighSchoolAverage(studentHighSchoolAverage);
                //studentId,studentLastName,studentFirstName,studentDofB,studentHomeTown);
                System.out.println("Created testStudent");
                if (studentList.contains(testStudent)==false){
                    System.out.println("Adding student to list");
                    studentList.add(testStudent);
                    System.out.println("Added student to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //When student is selected in the student tab table
    public static void processStudentTableSelect(ResultSet rs, ObservableList<String> studentEnrolledSections){
        String testString = null;

        try {
            while (rs.next()) {
                //Subjects.Subject_Name, Subjects.Subject_Number, Sections.Section_Time, Sections.Section_Room
                //Subjects.Subject_Name, Subjects.Subject_Number
                String Subject_Name = new String(rs.getString(1));
                String Subject_Number = new String(rs.getString(2));

                System.out.println("Subject_Name: " + Subject_Name);
                System.out.println("Subject_Number: " + Subject_Number);
                System.out.println("");

                // Check if subjectList already contains the subject
                System.out.println("Creating testSubject");
                testString = new String(Subject_Name + " " + Subject_Number);
                System.out.println("Created testSubject");

                if (!studentEnrolledSections.contains(testString)) {
                    System.out.println("Adding enrolled class to list");
                    studentEnrolledSections.add(testString);
                    System.out.println("Added testString to list");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        if (studentId != student.studentId) return false;
        if (!Objects.equals(studentLastName, student.studentLastName))
            return false;
        if (!Objects.equals(studentFirstName, student.studentFirstName))
            return false;
        if (!Objects.equals(studentDateOfBirth, student.studentDateOfBirth))
            return false;
        return Objects.equals(studentHomeTown, student.studentHomeTown);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (studentLastName != null ? studentLastName.hashCode() : 0);
        result = 31 * result + (studentFirstName != null ? studentFirstName.hashCode() : 0);
        result = 31 * result + (studentDateOfBirth != null ? studentDateOfBirth.hashCode() : 0);
        result = 31 * result + (studentHomeTown != null ? studentHomeTown.hashCode() : 0);
        return result;
    }

    public String getStudentId() {
        return studentId.get();
    }

    public StringProperty studentIdProperty(){
        return studentId;
    }

    public void setStudentId(String studentId){
        this.studentId.set(studentId);
    }

    public String getStudentLastName() {
        return studentLastName.get();
    }

    public StringProperty studentLastNameProperty(){
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName){
        this.studentLastName.set(studentLastName);
    }

    public String getStudentFirstName() {
        return studentFirstName.get();
    }

    public StringProperty studentFirstNameProperty(){
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName){
        this.studentFirstName.set(studentFirstName);
    }

    public String getStudentDateOfBirth() {
        return studentDateOfBirth.get();
    }

    public StringProperty studentDateOfBirthProperty(){
        return studentDateOfBirth;
    }

    public void setStudentDateOfBirth(String studentDateOfBirth){
        this.studentDateOfBirth.set(studentDateOfBirth);
    }

    public String getStudentHomeTown() {
        return studentHomeTown.get();
    }

    public StringProperty studentHomeTownProperty(){
        return studentHomeTown;
    }

    public void setStudentHomeTown(String studentHomeTown){
        this.studentHomeTown.set(studentHomeTown);
    }

    public String getStudentHomeCountry() {
        return studentHomeCountry.get();
    }

    public StringProperty studentHomeCountryProperty(){
        return studentHomeCountry;
    }

    public void setStudentHomeCountry(String studentHomeCountry){
        this.studentHomeCountry.set(studentHomeCountry);
    }

    public String getStudentHighSchoolAverage() {
        return studentHighSchoolAverage.get();
    }

    public StringProperty studentHighSchoolAverageProperty(){
        return studentHighSchoolAverage;
    }

    public void setStudentHighSchoolAverage(String studentHighSchoolAverage){
        this.studentHighSchoolAverage.set(studentHighSchoolAverage);
    }
}
