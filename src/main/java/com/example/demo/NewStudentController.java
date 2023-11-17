package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NewStudentController implements Connectable{

    SchoolController schoolController; //Caller - SchoolController object

    private ArrayList<String> countriesList = new ArrayList<String>();

    private boolean newStudent; //Is new student being generated or existing student being edited

    //Pane title text
    @FXML private Text title = new Text();

    //Student Information Fields
    @FXML private TextField studentFirstName = new TextField();
    @FXML private TextField studentLastName = new TextField();
    @FXML private TextField studentHomeTown = new TextField();
    @FXML private ChoiceBox studentHomeCountry = new ChoiceBox();
    @FXML private TextField studentHighschoolAverage = new TextField();
    @FXML private DatePicker studentDateOfBirth = new DatePicker();

    //Window buttons
    @FXML private Button addStudentButton = new Button();

    //Pass SchoolController caller object
    public void initData(SchoolController schoolController, boolean newStudent){
        this.schoolController = schoolController;

        this.newStudent = newStudent;

        //Must initialize components from initData - login info
        this.getCountryData();
        this.populateStudentHomeCountryChoiceBox();

        //Existing student is being edited
        if(!newStudent){
            this.title.setText("Edit Student");
            this.addStudentButton.setText("Modify Student Entry");
            this.studentFirstName.setText(schoolController.studentSelectedItems.get(0).getStudentFirstName());
            this.studentLastName.setText(schoolController.studentSelectedItems.get(0).getStudentLastName());
            this.studentHomeTown.setText(schoolController.studentSelectedItems.get(0).getStudentHomeTown());
            this.studentHomeCountry.getSelectionModel().select(this.countriesList.indexOf(this.schoolController.studentSelectedItems.get(0).getStudentHomeCountry()));
            this.studentHighschoolAverage.setText(schoolController.studentSelectedItems.get(0).getStudentHighSchoolAverage());

            //Student date of birth
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate studentDofB = LocalDate.parse(schoolController.studentSelectedItems.get(0).getStudentDateOfBirth(), formatter);
            this.studentDateOfBirth.setValue(studentDofB);
        }
    }

    public void populateStudentHomeCountryChoiceBox(){
        for(String country: countriesList) {
            this.studentHomeCountry.getItems().add(country);
        }
    }

    @FXML
    public void initialize() {

        //this.getCountryData();
        //this.populateStudentHomeCountryChoiceBox();
    }

    public void getCountryData(){
        //Request data from database
        Database.getDatabaseData("SELECT Countries.Country_Name FROM Countries;",
                "requestCountries", this.schoolController.get_login_info(), this);
    }

    public void onAddStudentButton(){
        //Test for empty data fields
        if (studentFirstName.getText()=="" || studentLastName.getText()=="" ||
                studentHomeTown.getText()=="" || studentHomeCountry.getSelectionModel().isEmpty() ||
                studentDateOfBirth.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            //alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
            alert.setContentText("Invalid student data.");

            alert.showAndWait();
        } else {
            if (newStudent) {
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

    public void processCountriesTable(ResultSet rs, ArrayList<String> countriesList){
        String country=null;

        try {
            while (rs.next()) {
                String countryName = new String(rs.getString(1));
                System.out.println("countryName:" + countryName);
                System.out.println("");

                //if (countriesList.contains(country)==false){
                    System.out.println("Adding country to list");
                    countriesList.add(countryName);
                    System.out.println("Added country to list");
                //}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCloseNewStudentButton() {
        Stage currentStage = (Stage) this.studentFirstName.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void ProcessData(ResultSet rs, String opCode) {
        switch (opCode) {
            case "sectionStudents":
                System.out.println("Processing Enrolment View table");
                //Student.processStudentTable(rs, enrolmentViewStudentList);
                this.schoolController.updateStatusTextFlow("Finished processing Student Table");
                break;
            case "requestCountries":
                System.out.println("Processing Countries table");
                this.processCountriesTable(rs, countriesList);
                this.schoolController.updateStatusTextFlow("Finished processing Countries Table");
                break;
        }
    }

    @Override
    public SchoolController getSchoolController() {
        return this.schoolController;
    }
}
