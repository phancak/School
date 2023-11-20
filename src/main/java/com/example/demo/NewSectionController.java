package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NewSectionController implements Connectable{

    SchoolController schoolController; //Caller - SchoolController object

    private boolean newSection; //Is new student being generated or existing student being edited

    private ArrayList<String> subjectList = new ArrayList<String>();
    private ArrayList<String> subjectIdList = new ArrayList<String>();
    private ArrayList<String> startTimeList = new ArrayList<String>();
    private ArrayList<String> instructorList = new ArrayList<String>();
    private ArrayList<String> instructorIdList = new ArrayList<String>();
    private ArrayList<String> daysList = new ArrayList<String>();

    //Pane title text
    @FXML private Text title = new Text();

    //Section Information Fields
    @FXML private ChoiceBox subjectChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox startTimeChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox endTimeChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox instructorChoiceBox = new ChoiceBox();
    @FXML private ChoiceBox daysChoiceBox = new ChoiceBox();
    @FXML private TextField roomTextField = new TextField();

    //Window buttons
    @FXML private Button addSectionButton = new Button();

    //Pass SchoolController caller object
    public void initData(SchoolController schoolController, boolean newSection){
        this.schoolController = schoolController;

        this.newSection = newSection; //New Section or Edit Section

        //Must initialize components from initData - login info
        this.getSubjectData();
        this.populateSubjectChoiceBox();
        this.getStartTimeData();
        this.populateStartTimeChoiceBox();
        this.populateEndTimeChoiceBox();
        this.getInstructorData();
        this.populateInstructorChoiceBox();
        this.getDaysData();
        this.populateDaysChoiceBox();

        //Existing student is being edited
        if(!newSection){
            this.title.setText("Edit Section");
            this.addSectionButton.setText("Modify Section Entry");
            this.subjectChoiceBox.getSelectionModel().select(this.subjectIdList.indexOf(this.schoolController.sectionSelectedItems.get(0).getSectionId()));
            this.startTimeChoiceBox.getSelectionModel().select(this.startTimeList.indexOf(this.schoolController.sectionSelectedItems.get(0).getSectionStartTime()));
            this.endTimeChoiceBox.getSelectionModel().select(this.startTimeList.indexOf(this.schoolController.sectionSelectedItems.get(0).getSectionEndTime()));
            this.instructorChoiceBox.getSelectionModel().select(this.instructorIdList.indexOf(this.schoolController.sectionSelectedItems.get(0).getSectionInstructorId()));
            this.roomTextField.setText(this.schoolController.sectionSelectedItems.get(0).getSectionRoom());
            this.daysChoiceBox.getSelectionModel().select(this.daysList.indexOf(this.schoolController.sectionSelectedItems.get(0).getSectionDays()));
        }
    }

    @FXML
    public void initialize() {
        //Initialize in initData after receiving schoolController
        //Due to database login
    }

    @Override
    public void ProcessData(ResultSet rs, String opCode) {
        switch (opCode) {
            case "requestSubjectData":
                //System.out.println("Processing Countries table");
                this.processSubjectData(rs, this.subjectList);
                this.schoolController.updateStatusTextFlow("Processed Subject Data");
                break;
            case "requestInstructorData":
                //System.out.println("Processing Countries table");
                this.processInstructorData(rs, this.instructorList);
                this.schoolController.updateStatusTextFlow("Processed Subject Number Data");
                break;
        }
    }

    private void getSubjectData(){
        //Request data from database
        Database.getDatabaseData("SELECT DISTINCT Subjects.Subject_Id, Subjects.Subject_Name, Subjects.Subject_Number FROM Subjects;",
                "requestSubjectData", this.schoolController.get_login_info(), this);

    }

    public void processSubjectData(ResultSet rs, ArrayList<String> subjectNameList){
        String string=null;

        try {
            while (rs.next()) {
                String subjectId = new String(rs.getString(1));
                String subjectName = new String(rs.getString(2));
                String subjectNumber = new String(rs.getString(3));
                this.subjectList.add(subjectName + " " + subjectNumber);
                this.subjectIdList.add(subjectId); //Will coincide with the position in the ChoiceBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateSubjectChoiceBox(){
        for(String subjectName: subjectList) {
            this.subjectChoiceBox.getItems().add(subjectName);
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
        for (int i=0; i<34;i++) {
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
        Database.getDatabaseData("SELECT DISTINCT Instructors.Instructor_Id, Instructors.Instructor_Last_Name, Instructors.Instructor_First_Name, Instructors.Instructor_Date_Of_Birth FROM Instructors;",
                "requestInstructorData", this.schoolController.get_login_info(), this);
    }

    public void processInstructorData(ResultSet rs, ArrayList<String> instructorNameList) {
        String string = null;

        try {
            while (rs.next()) {
                String instructorId = new String(rs.getString(1));
                String instructorLastName = new String(rs.getString(2));
                String instructorFirstName =  new String(rs.getString(3));
                String instructorDateOfBirth =  new String(rs.getString(4));
                this.instructorIdList.add(instructorId); //Will coincide with the position in the ChoiceBox
                this.instructorList.add(instructorLastName + ", " + instructorFirstName + " DofB: " + instructorDateOfBirth);
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
        if (this.subjectChoiceBox.getSelectionModel().isEmpty() ||
                this.startTimeChoiceBox.getSelectionModel().isEmpty() || this.endTimeChoiceBox.getSelectionModel().isEmpty() ||
                this.instructorChoiceBox.getSelectionModel().isEmpty() || this.daysChoiceBox.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Section Data.");

            alert.showAndWait();
        } else {
            if (this.newSection) {
                //Request data from database
                Database.getDatabaseData("INSERT INTO Sections (Instructor_Id, Subject_Id, " +
                                "Section_Start_Time, Section_End_Time, Section_Room, Section_Days) " +
                                "VALUES ('" + this.instructorIdList.get(this.instructorChoiceBox.getSelectionModel().getSelectedIndex()) + "', '" +
                                this.subjectIdList.get(this.subjectChoiceBox.getSelectionModel().getSelectedIndex()) + "', '" +
                                this.startTimeChoiceBox.getValue() + "', '" + this.endTimeChoiceBox.getValue() + "', '" +
                                this.roomTextField.getText() + "', '" + this.daysChoiceBox.getValue() + "');",
                        "newSection", this.schoolController.get_login_info(), this);

                //Close the new student window
                Stage currentStage = (Stage) this.subjectChoiceBox.getScene().getWindow();
                currentStage.close();
                this.schoolController.onTabSectionsSelection(); //Reacquire Section list
            } else {
                //Request data from database
                Database.getDatabaseData("UPDATE Sections " +
                                "SET Subject_Id='" + this.subjectIdList.get(this.subjectChoiceBox.getSelectionModel().getSelectedIndex()) + "', Section_Start_Time='" + this.startTimeChoiceBox.getValue() +
                                "', Section_End_Time='" + this.endTimeChoiceBox.getValue() + "', Section_Room='" + this.roomTextField.getText() +
                                "', Section_Days='" + this.daysChoiceBox.getValue() + "'" +
                                "WHERE Instructor_Id=" + schoolController.sectionSelectedItems.get(0).getSectionId() + ";",
                        "update", this.schoolController.get_login_info(), this);

                //Close the new student window
                Stage currentStage = (Stage) this.subjectChoiceBox.getScene().getWindow();
                currentStage.close();
                this.schoolController.onTabSectionsSelection(); //Reacquire Section list
            }
        }
    }

    @Override
    public SchoolController getSchoolController() {
        return this.schoolController;
    }
}
