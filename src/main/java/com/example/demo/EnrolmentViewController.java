package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class EnrolmentViewController implements Connectable{

    SchoolController schoolController; //Caller - SchoolController object

    @FXML
    private TableView<Student> enrolmentViewStudentTableView = new TableView<>();

    //Object observable list setup
    @FXML private ObservableList<Student> enrolmentViewStudentList = FXCollections.observableArrayList();

    @FXML private TableColumn<Student, String> studentIdColumn = new TableColumn<>("Student Id");
    @FXML private TableColumn<Student, String> studentLastNameColumn = new TableColumn<>("Last Name");
    @FXML private TableColumn<Student, String> studentFirstNameColumn = new TableColumn<>("First Name");
    @FXML private TableColumn<Student, String> studentDateOfBirthColumn = new TableColumn<>("Date of Birth");
    @FXML private TableColumn<Student, String> studentHomeTownColumn = new TableColumn<>("Home Town");
    @FXML private TableColumn<Student, String> studentHomeCountryColumn = new TableColumn<>("Home Country");
    @FXML private TableColumn<Student, String> studentHighSchoolAverageColumn = new TableColumn<>("High School Average");

    //Choice Box (Section choice box)
    @FXML ChoiceBox sectionChoiceBox = new ChoiceBox();

    //Enrolment View Buttons
    @FXML private Button sectionEnrolmentViewButton;

    public void initialize_Section_ChoiceBox() {

        for (Section section: this.schoolController.getSectionList()){
            this.sectionChoiceBox.getItems().add("Id:" + section.getSectionId() +
                    " " + section.getSectionSubject() +
                    " " + section.getSectionInstructor() +
                    " " + section.getSectionDays() +
                    " Start:" + section.getSectionStartTime() +
                    " End:" + section.getSectionEndTime());
        }

        //Initialize section selection
        this.sectionChoiceBox.getSelectionModel().select(this.schoolController.getSectionList().indexOf(this.schoolController.sectionSelectedItems.get(0)));
    }

    public void on_ChoiceBox_selection_change(){
        //Update the section selection list
        this.schoolController.sectionSelectedItems.set(0,
                this.schoolController.getSectionList().get(
                        this.sectionChoiceBox.getSelectionModel().getSelectedIndex()));

        //Update the section enrolled student table
        this.update_section_student_data();
    }

    public void update_section_student_data(){
        //Must clear the table or it keeps previous entries
        this.enrolmentViewStudentTableView.getItems().clear(); //Empty table

        //Request data from database
        Database.getDatabaseData("SELECT Students.Student_Id, Students.Student_First_Name ,Students.Student_Last_Name ,Students.Student_Date_Of_Birth, " +
                "Students.Student_Home_Town ,Students.Student_Home_Country ,Students.Student_High_School_Average " +
                "FROM Students " +
                "INNER JOIN (SELECT * " +
                "FROM Enrolment " +
                "WHERE Enrolment.Section_Id=" + this.schoolController.sectionSelectedItems.get(0).getSectionId() + ") AS Selection " +
                "ON Students.Student_Id=Selection.Student_Id;", "sectionStudents", this.schoolController.get_login_info(), this);
    }

    public void initialize_Student_Enrolment() {

        this.update_section_student_data();

        //Initialize student table
        this.studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        this.studentLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentLastName"));
        this.studentFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentFirstName"));
        this.studentDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("studentDateOfBirth"));
        this.studentHomeTownColumn.setCellValueFactory(new PropertyValueFactory<>("studentHomeTown"));
        this.studentHomeCountryColumn.setCellValueFactory(new PropertyValueFactory<>("studentHomeCountry"));
        this.studentHighSchoolAverageColumn.setCellValueFactory(new PropertyValueFactory<>("studentHighSchoolAverage"));
        this.enrolmentViewStudentTableView.setItems(this.enrolmentViewStudentList);
    }

    @FXML
    public void initialize() {

        this.sectionEnrolmentViewButton = new Button();
    }

    @FXML
    public void onCloseEnrolmentViewButton() {
        Stage currentStage = (Stage) this.enrolmentViewStudentTableView.getScene().getWindow();
        currentStage.close();
    }

    //Pass SchoolController caller object
    public void initData(SchoolController schoolController){
        this.schoolController = schoolController;

        this.initialize_Student_Enrolment();
        this.initialize_Section_ChoiceBox();
    }

    @Override
    public void ProcessData(ResultSet rs, String opCode) {
        switch (opCode) {
            case "sectionStudents":
                System.out.println("Processing Enrolment View table");
                Student.processStudentTable(rs, enrolmentViewStudentList);
                this.schoolController.updateStatusTextFlow("Finished processing Student Table");
                break;
        }
    }

    @Override
    public SchoolController getSchoolController() {
        return this.schoolController;
    }
}
