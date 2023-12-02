package com.example.demo;

import javafx.beans.value.ObservableValue;
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

    public void on_ChoiceBox_selection_change(){
        //Update the section selection list
        this.schoolController.sectionSelectedItems.set(0,
                this.schoolController.getSectionList().get(
                        this.sectionChoiceBox.getSelectionModel().getSelectedIndex()));

        //Update the section enrolled student table
        this.update_section_student_data();
    }

    public void update_section_student_data(){
        //Request data from database
        Database.getDatabaseData("CALL get_students_from_section(" + this.schoolController.sectionSelectedItems.get(0).getSectionId() + ");",
                "sectionStudents", this.schoolController.get_login_info(), this);
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

        this.initialize_Student_Enrolment(); //Initializes the tab
        Database.initialize_Section_ChoiceBox(this.sectionChoiceBox, this.schoolController.getSectionList(), this.schoolController.sectionIdList);

        //Initialize section selection
        this.sectionChoiceBox.getSelectionModel().select(this.schoolController.getSectionList().indexOf(this.schoolController.sectionSelectedItems.get(0)));

        //Adding action to the choice box
        this.sectionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    //Do this on ChoiceBox selection change
                    String selectedSectionId = this.schoolController.sectionIdList.get(new_val.intValue());

                    //Request data from database
                    Database.getDatabaseData("CALL get_students_from_section(" + selectedSectionId + ");",
                            "sectionStudents", this.schoolController.get_login_info(), this);
                });
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
