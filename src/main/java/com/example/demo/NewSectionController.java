package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NewSectionController implements Connectable{

    SchoolController schoolController; //Caller - SchoolController object

    private boolean newSection; //Is new student being generated or existing student being edited

    private ArrayList<String> subjectNameList = new ArrayList<String>();
    private ArrayList<String> subjectNumberList = new ArrayList<String>();
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> instructorList = new ArrayList<String>();
    private ArrayList<String> daysList = new ArrayList<String>();

    //Pane title text
    @FXML private Text title = new Text();

    //Section Information Fields
    @FXML private ChoiceBox subjectNameChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox subjectNumberChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox startTimeChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox endTimeChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox instructorChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox daysChoiceBox = new ChoiceBox();

    //Pass SchoolController caller object
    public void initData(SchoolController schoolController, boolean newSection){
        this.schoolController = schoolController;

        this.newSection = newSection; //New Section or Edit Section

        //Must initialize components from initData - login info
        this.getSubjectNameData();
        this.populateSubjectNameChoiceBox();
        this.getSubjectNumberData();
        this.populateSubjectNumberChoiceBox();
        this.getStartTimeData();
        this.populateStartTimeChoiceBox();
        this.populateEndTimeChoiceBox();
        this.getInstructorData();
        this.populateInstructorChoiceBox();
        this.getDaysData();
        this.populateDaysChoiceBox();
    }

    @FXML
    public void initialize() {
        //Initialize in initData after receiving schoolController
        //Due to database login
    }

    @Override
    public void ProcessData(ResultSet rs, String opCode) {
        switch (opCode) {
            case "requestSubjectName":
                //System.out.println("Processing Countries table");
                this.processSubjectNameData(rs, this.subjectNameList);
                this.schoolController.updateStatusTextFlow("Processed Subject Name Data");
                break;
            case "requestSubjectNumber":
                //System.out.println("Processing Countries table");
                this.processSubjectNumberData(rs, this.subjectNumberList);
                this.schoolController.updateStatusTextFlow("Processed Subject Number Data");
                break;
            case "requestInstructorData":
                //System.out.println("Processing Countries table");
                this.processInstructorData(rs, this.instructorList);
                this.schoolController.updateStatusTextFlow("Processed Subject Number Data");
                break;
        }
    }

    private void getSubjectNameData(){
        //Request data from database
        Database.getDatabaseData("SELECT DISTINCT Subjects.Subject_Name FROM Subjects;",
                "requestSubjectName", this.schoolController.get_login_info(), this);

    }

    public void processSubjectNameData(ResultSet rs, ArrayList<String> subjectNameList){
        String string=null;

        try {
            while (rs.next()) {
                string = new String(rs.getString(1));
                subjectNameList.add(string);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateSubjectNameChoiceBox(){
        for(String subjectName: subjectNameList) {
            this.subjectNameChoiceBox.getItems().add(subjectName);
        }
    }

    private void getSubjectNumberData() {
        // Request data from the database for subject numbers
        Database.getDatabaseData("SELECT DISTINCT Subjects.Subject_Number FROM Subjects;",
                "requestSubjectNumber", this.schoolController.get_login_info(), this);
    }

    public void processSubjectNumberData(ResultSet rs, ArrayList<String> subjectNumberList) {
        String string = null;

        try {
            while (rs.next()) {
                // Assuming SubjectNumber is a String, modify the data type accordingly
                string = new String(rs.getString(1));
                subjectNumberList.add(string);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateSubjectNumberChoiceBox() {
        for (String subjectNumber : subjectNumberList) {
            this.subjectNumberChoiceBox.getItems().add(subjectNumber);
        }
    }

    public void populateStartTimeChoiceBox() {
        for (String startTime : startTimeList) {
            this.startTimeChoiceBox.getItems().add(startTime);
        }
    }

    public void populateEndTimeChoiceBox() {
        //Is populated with same data as start time
        for (String startTime : startTimeList) {
            this.endTimeChoiceBox.getItems().add(startTime);
        }
    }

    public void getStartTimeData(){
        for (int i=0; i<17;i++) {
            String timeString = "06:00:00"; //Earliest class
            LocalTime parsedTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime newTime = parsedTime.plusMinutes(30 * i);

            // Define a DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Format LocalTime to String
            String formattedTime = newTime.format(formatter);

            //Add time to the list
            startTimeList.add(formattedTime);
        }
    }

    private void getInstructorData() {
        // Request data from the database
        Database.getDatabaseData("SELECT DISTINCT Instructors.Instructor_Last_Name, Instructors.Instructor_First_Name, Instructors.Instructor_Date_Of_Birth FROM Instructors;",
                "requestInstructorData", this.schoolController.get_login_info(), this);
    }

    public void processInstructorData(ResultSet rs, ArrayList<String> instructorNameList) {
        String string = null;

        try {
            while (rs.next()) {
                String instructorLastName = new String(rs.getString(1));
                String instructorFirstName =  new String(rs.getString(2));
                String instructorDateOfBirth =  new String(rs.getString(3));
                instructorList.add(instructorLastName + ", " + instructorFirstName + " DofB: " + instructorDateOfBirth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateInstructorChoiceBox() {
        for (String instructorName : instructorList) {
            this.instructorChoiceBox.getItems().add(instructorName);
        }
    }

    private void getDaysData() {
        daysList.add("MWF");
        daysList.add("TTh");
    }

    public void populateDaysChoiceBox() {
        for (String day : daysList) {
            this.daysChoiceBox.getItems().add(day);
        }
    }

    @FXML
    public void onCloseNewSectionButton() {
        Stage currentStage = (Stage) this.title.getScene().getWindow();
        currentStage.close();
    }

    public void onAddSectionButton(){
        //Test for empty data fields
        if (this.subjectNameChoiceBox.getSelectionModel().isEmpty() || this.subjectNumberChoiceBox.getSelectionModel().isEmpty() ||
                this.startTimeChoiceBox.getSelectionModel().isEmpty() || this.endTimeChoiceBox.getSelectionModel().isEmpty() ||
                this.instructorChoiceBox.getSelectionModel().isEmpty() || this.daysChoiceBox.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Section Data.");

            alert.showAndWait();
        } else {
            if (this.newSection) {
                //Format date data
                LocalDate studentDateOfBirthLocalDate = studentDateOfBirth.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String studentDateOfBirthString = studentDateOfBirthLocalDate.format(formatter);

                //Request data from database
                Database.getDatabaseData("INSERT INTO Students (Student_First_Name, Student_Last_Name, Student_Date_Of_Birth, " +
                                "Student_Home_Town, Student_Home_Country, Student_High_School_Average) " +
                                "VALUES ('" + studentFirstName.getText() + "', '" +
                                studentLastName.getText() + "', '" + studentDateOfBirthString + "', '" + studentHomeTown.getText() + "', '" +
                                studentHomeCountry.getValue() + "', '" + studentHighschoolAverage.getText() + "');",
                        "newStudents", this.schoolController.get_login_info(), this);

                //Close the new student window
                Stage currentStage = (Stage) this.studentFirstName.getScene().getWindow();
                currentStage.close();
                this.schoolController.onTabStudentSelection(); //Reacquire Student list
            } else {
                //Edit existing student
                LocalDate studentDateOfBirthLocalDate = studentDateOfBirth.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String studentDateOfBirthString = studentDateOfBirthLocalDate.format(formatter);

                //Request data from database
                Database.getDatabaseData("UPDATE Students " +
                                "SET Student_First_Name='" + studentFirstName.getText() +
                                "', Student_Last_Name='" + studentLastName.getText() + "', Student_Date_Of_Birth='" + studentDateOfBirthString +
                                "', Student_Home_Town='" + studentHomeTown.getText() + "', Student_Home_Country='" + studentHomeCountry.getValue() +
                                "', Student_High_School_Average='" + studentHighschoolAverage.getText() + "'" +
                                "WHERE Student_Id=" + schoolController.studentSelectedItems.get(0).getStudentId() + ";",
                        "update", this.schoolController.get_login_info(), this);

                //Close the new student window
                Stage currentStage = (Stage) this.studentFirstName.getScene().getWindow();
                currentStage.close();
                this.schoolController.onTabStudentSelection(); //Reacquire Student list
            }
        }
    }

    @Override
    public SchoolController getSchoolController() {
        return this.schoolController;
    }
}
