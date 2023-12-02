package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class SchoolController implements Connectable{
    private Login login_info = new Login("root","12345678"); //Initialize login info //Database login information

    Queue<String> statusMessageQueue = new LinkedList<>(); //Tracks status message

    @FXML private TextFlow statusTextflow1 = new TextFlow();
    @FXML private Text statusText1 = new Text("\n");
    @FXML private Text statusText2 = new Text("\n");
    @FXML private Text statusText3 = new Text("\n");
    @FXML private Text statusText4 = new Text("\n");
    @FXML private Text statusText5 = new Text("\n");

    //Tabs list
    @FXML private TabPane tabPaneSchool = new TabPane();
    @FXML private Tab tabStudents = new Tab();
    @FXML private Tab tabInstructors = new Tab();
    @FXML private Tab tabSections = new Tab();
    @FXML private Tab tabSubjects = new Tab();
    @FXML private Tab tabEnrolment = new Tab();
    @FXML private Tab tabGradedItems = new Tab();
    @FXML private Tab tabGradeBook = new Tab();

    @FXML private ObservableList<String> systemMessages = FXCollections.observableArrayList();
    @FXML private ObservableList<String> studentEnrolledSections = FXCollections.observableArrayList();
    @FXML private ObservableList<String> instructorSections = FXCollections.observableArrayList();
    @FXML private ObservableList<String> subjectSections = FXCollections.observableArrayList();
    @FXML private TableView<Student> studentTableView = new TableView<>();
    @FXML private TableView<Instructor> instructorTableView = new TableView<>();
    @FXML private TableView<Subject> subjectsTableView = new TableView<>();
    @FXML private TableView<Section> sectionTableView = new TableView<>();
    @FXML private TableView<Student> enrolmentEnrolledTableView = new TableView<>();
    @FXML private TableView<Student> enrolmentNotenrolledTableView = new TableView<>();
    @FXML private ListView<String> studentTabListView = new ListView<>();
    @FXML private ListView<String> instructorTabListView = new ListView<>();
    @FXML private ListView<String> subjectTabListView = new ListView<>();
    @FXML private TableView<GradedItem> gradedItemTableView = new TableView<>();

    //Tableview selection models
    private TableView.TableViewSelectionModel<Student> studentSelectionModel;
    private TableView.TableViewSelectionModel<Instructor> instructorSelectionModel;
    private TableView.TableViewSelectionModel<Subject> subjectsSelectionModel;
    private TableView.TableViewSelectionModel<Section> sectionSelectionModel;
    private TableView.TableViewSelectionModel<Student> enrolmentEnrolledSelectionModel;
    private TableView.TableViewSelectionModel<Student> enrolmentNotenrolledSelectionModel;
    private TableView.TableViewSelectionModel<GradedItem> gradedItemSelectionModel;

    //Tableview selection list
    ObservableList<Student> studentSelectedItems;
    ObservableList<Instructor> instructorSelectedItems;
    ObservableList<Subject> subjectsSelectedItems;
    ObservableList<Section> sectionSelectedItems;
    ObservableList<Student> enrolmentEnrolledSelectedItems;
    ObservableList<Student> enrolmentNotenrolledSelectedItems;
    ObservableList<GradedItem> gradedItemSelectedItems;

    //Object observable list setup
    @FXML private ObservableList<Student> studentList = FXCollections.observableArrayList();
    @FXML private ObservableList<Instructor> instructorList = FXCollections.observableArrayList();
    @FXML private ObservableList<Subject> subjectList = FXCollections.observableArrayList();
    @FXML private ObservableList<Section> sectionList = FXCollections.observableArrayList();
    @FXML private ObservableList<Student> enrolmentEnrolledList = FXCollections.observableArrayList();
    @FXML private ObservableList<Student> enrolmentNotenrolledList = FXCollections.observableArrayList();
    @FXML private ObservableList<GradedItem> gradedItemList = FXCollections.observableArrayList();

    //TableView Column Setup
    @FXML private TableColumn studentTableViewIdColumn = new TableColumn<>("Id");
    @FXML private TableColumn studentTableViewFirstNameColumn = new TableColumn("First Name");
    @FXML private TableColumn studentTableViewLastNameColumn = new TableColumn("Last Name");
    @FXML private TableColumn studentTableViewDofBColumn = new TableColumn("DofB");
    @FXML private TableColumn instructorTableViewIdColumn = new TableColumn<>("Instructor Id");
    @FXML private TableColumn instructorTableViewFirstNameColumn = new TableColumn("Last Name");
    @FXML private TableColumn instructorTableViewLastNameColumn = new TableColumn("Last Name");
    @FXML private TableColumn instructorTableViewDofBColumn = new TableColumn("DofB");
    @FXML private TableColumn subjectTableViewIdColumn = new TableColumn("Id");
    @FXML private TableColumn subjectTableViewNameColumn = new TableColumn("Name");
    @FXML private TableColumn subjectTableViewNumberColumn = new TableColumn("Number");
    @FXML private TableColumn subjectTableViewNoSectionsColumn = new TableColumn("Sections");
    @FXML private TableColumn sectionTableViewIdColumn = new TableColumn<>("Id");
    @FXML private TableColumn sectionTableViewSubjectColumn = new TableColumn<>("Subject");
    @FXML private TableColumn sectionTableViewStartTimeColumn = new TableColumn<>("Start Time");
    @FXML private TableColumn sectionTableViewEndTimeColumn = new TableColumn<>("End Time");
    @FXML private TableColumn sectionTableViewDaysColumn = new TableColumn<>("Days");
    @FXML private TableColumn sectionTableViewInstructorColumn = new TableColumn<>("Instructor");
    @FXML private TableColumn sectionTableViewRoomColumn = new TableColumn<>("Room");
    @FXML private TableColumn sectionTableViewEnrolmentColumn = new TableColumn<>("Enrolment");
    @FXML private TableColumn enrolmentEnrolledTableViewStudentIdColumn = new TableColumn<>("Id");
    @FXML private TableColumn enrolmentNotenrolledTableViewStudentIdColumn = new TableColumn<>("Student Id");
    @FXML private TableColumn enrolmentEnrolledTableViewStudentNameColumn = new TableColumn<>("Name");
    @FXML private TableColumn enrolmentNotenrolledTableViewStudentNameColumn = new TableColumn<>("Name");
    @FXML private TableColumn enrolmentEnrolledTableViewStudentDofBColumn = new TableColumn<>("DofB");
    @FXML private TableColumn enrolmentNotenrolledTableViewStudentDofBColumn = new TableColumn<>("DofB");
    @FXML private TableColumn gradedItemTableViewId = new TableColumn<>("Id");
    @FXML private TableColumn gradedItemTableViewTitle = new TableColumn<>("Title");
    @FXML private TableColumn gradedItemTableViewDate = new TableColumn<>("Date");
    @FXML private TableColumn gradedItemTableViewAverage = new TableColumn<>("Average");

    // Students Tab Personal Information Pane
    @FXML private TextFlow studentTab_PersonalInfo_LeftTextFlow = new TextFlow();
    @FXML private TextFlow studentTab_PersonalInfo_RightTextFlow = new TextFlow();
    @FXML private Text studentId_text_1 = new Text("Student Id:\n");
    @FXML private Text studentFirstName_text_2 = new Text("First Name:\n");
    @FXML private Text studentLastName_text_3 = new Text("Last Name:\n");
    @FXML private Text studentDofB_text_4 = new Text("Date of Birth:\n");
    @FXML private Text studentHomeTown_text_5 = new Text("Home Town:\n");
    @FXML private Text studentHomeCountry_text_6 = new Text("Home Country:\n");
    @FXML private Text studentHSAverage_text_7 = new Text("High School Average:\n");
    @FXML private Text studentIdData_text_1 = new Text("\n");
    @FXML private Text studentFirstNameData_text_2 = new Text("\n");
    @FXML private Text studentLastNameData_text_3 = new Text("\n");
    @FXML private Text studentDofBData_text_4 = new Text("\n");
    @FXML private Text studentHomeTownData_text_5 = new Text("\n");
    @FXML private Text studentHomeCountryData_text_6 = new Text("\n");
    @FXML private Text studentHSAverageData_text_7 = new Text("\n");

    // Instructors Tab Personal Information Pane
    @FXML private TextFlow instructorTab_PersonalInfo_LeftTextFlow = new TextFlow();
    @FXML private TextFlow instructorTab_PersonalInfo_RightTextFlow = new TextFlow();
    @FXML private Text instructorId_text_1 = new Text("Instructor Id:\n");
    @FXML private Text instructorFirstName_text_2 = new Text("First Name:\n");
    @FXML private Text instructorLastName_text_3 = new Text("Last Name:\n");
    @FXML private Text instructorDofB_text_4 = new Text("Date of Birth:\n");
    @FXML private Text instructorSpecialization_text_5 = new Text("Specialization:\n");
    @FXML private Text instructorEducation_text_6 = new Text("Education:\n");
    @FXML private Text instructorIdData_text_1 = new Text("\n");
    @FXML private Text instructorFirstNameData_text_2 = new Text("\n");
    @FXML private Text instructorLastNameData_text_3 = new Text("\n");
    @FXML private Text instructorDofBData_text_4 = new Text("\n");
    @FXML private Text instructorSpecializationData_text_5 = new Text("\n");
    @FXML private Text instructorEducationData_text_6 = new Text("\n");

    // Subjects Tab Personal Information Pane
    @FXML private TextFlow subjectTab_Info_LeftTextFlow = new TextFlow();
    @FXML private TextFlow subjectTab_Info_RightTextFlow = new TextFlow();
    @FXML private Text subjectId_text_1 = new Text("Subject Id:\n");
    @FXML private Text subjectName_text_2 = new Text("Subject Name:\n");
    @FXML private Text subjectNumber_text_3 = new Text("Subject Number:\n");
    @FXML private Text subjectDescription_text_4 = new Text("Subject Description:\n");
    @FXML private Text subjectNoSections_text_5 = new Text("Number of Sections:\n");
    @FXML private Text subjectIdData_text_1 = new Text("\n");
    @FXML private Text subjectNameData_text_2 = new Text("\n");
    @FXML private Text subjectNumberData_text_3 = new Text("\n");
    @FXML private Text subjectDescriptionData_text_4 = new Text("\n");
    @FXML private Text subjectNoSectionsData_text_5 = new Text("\n");

    //Enrolment Pane
    @FXML ChoiceBox enrolmentSectionChoiceBox = new ChoiceBox(); //Choice Box (Section choice box)
    @FXML private Button enrolButton = new Button();
    @FXML private Button unenrolButton = new Button();
    public ArrayList<String> sectionIdList = new ArrayList<String>();

    //Graded Item Pane
    @FXML ChoiceBox gardedItemSectionChoiceBox = new ChoiceBox(); //Choice Box (Section choice box)
    //@FXML private Button enrolButton = new Button();
    //@FXML private Button unenrolButton = new Button();

    //Section Tab Buttons
    @FXML public Button sectionEnrolmentViewButton = new Button();

    //Student tab buttons
    @FXML private Button removeStudentButton = new Button();
    @FXML private Button editStudentButton = new Button();

    //Instructor tab buttons
    @FXML private Button removeInstructorButton = new Button();
    @FXML private Button editInstructorButton = new Button();

    //Subjects tab buttons
    @FXML private Button removeSubjectButton = new Button();
    @FXML private Button editSubjectButton = new Button();

    //Sections tab buttons
    @FXML public Button removeSectionButton = new Button();
    @FXML public Button editSectionButton = new Button();

    //Controller Constructor
    public SchoolController(){
        super();
        updateStatusTextFlow("HelloController Constructor Executed");
    }

    public void initializeSchoolTabPane(){
        this.tabPaneSchool.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        System.out.println("Tab Selection changed");

                        switch (newTab.getId()) {
                            case "tabStudents":
                                onTabStudentSelection();
                                updateStatusTextFlow("Selected Students tab");
                                break;
                            case "tabInstructors":
                                onTabInstructorsSelection();
                                updateStatusTextFlow("Selected Instructors tab");
                                break;
                            case "tabSubjects":
                                onTabSubjectsSelection();
                                updateStatusTextFlow("Selected Subjects tab");
                                break;
                            case "tabSections":
                                onTabSectionsSelection();
                                updateStatusTextFlow("Selected Sections tab");
                                break;
                            case "tabEnrolment":
                                onTabEnrolmentSelection();
                                updateStatusTextFlow("Selected Enrolment tab");
                                break;
                            case "tabGradedItems":
                                onTabGradedItemsSelection();
                                updateStatusTextFlow("Selected Graded Items tab");
                                break;
                            case "tabGradeBook":
                                onTabGradeBookSelection();
                                updateStatusTextFlow("Selected Grade Book tab");
                                break;
                        }
                    }
                }
        );
    }

    @FXML
    public void unenrolButtonClick(){
        String selectedSectionId = this.sectionIdList.get(this.enrolmentSectionChoiceBox.getSelectionModel().getSelectedIndex());
        String selectedStudentId = this.enrolmentEnrolledSelectedItems.get(0).getStudentId();

        //Send database request for student enrolment
        Database.getDatabaseData("CALL unenrol_student(" + selectedSectionId + ", " + selectedStudentId + ");", "na", this.get_login_info(), this);

        //Request data from database for Enrolled Tables
        Database.getDatabaseData("CALL get_students_from_section(" + selectedSectionId + ");", "sectionEnrolledStudents", this.get_login_info(), this);

        //Request data from database for Not Enrolled Tables
        Database.getDatabaseData("CALL get_students_not_from_section(" + selectedSectionId + ");", "sectionNotenrolledStudents", this.get_login_info(), this);
    }

    @FXML
    public void enrolButtonClick(){
        String selectedSectionId = this.sectionIdList.get(this.enrolmentSectionChoiceBox.getSelectionModel().getSelectedIndex());
        String selectedStudentId = this.enrolmentNotenrolledSelectedItems.get(0).getStudentId();

        //Send database request for student enrolment
        Database.getDatabaseData("CALL enrol_student(" + selectedSectionId + ", " + selectedStudentId + ");", "na", this.get_login_info(), this);

        //Request data from database for Enrolled Tables
        Database.getDatabaseData("CALL get_students_from_section(" + selectedSectionId + ");", "sectionEnrolledStudents", this.get_login_info(), this);

        //Request data from database for Not Enrolled Tables
        Database.getDatabaseData("CALL get_students_not_from_section(" + selectedSectionId + ");", "sectionNotenrolledStudents", this.get_login_info(), this);
    }

    @FXML
    public void onEditStudentButtonClick(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(SchoolApplication.class.getResource("new-student.fxml"));
        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load()); //, 320, 640);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Pass data to controller
        NewStudentController newStudentController = fxmlLoader2.getController();
        newStudentController.initData(this, false);

        Stage stage2 = new Stage();
        stage2.setTitle("New Student");
        stage2.setScene(scene2);
        // Specifies the modality for new window.
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
    }

    @FXML
    public void onNewStudentButtonClick(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(SchoolApplication.class.getResource("new-student.fxml"));
        Scene scene2 = null;
        try {
            //Pass data to controller
            //NewStudentController newStudentController = fxmlLoader2.getController();
            //newStudentController.initData(this);
            scene2 = new Scene(fxmlLoader2.load()); //, 320, 640);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Pass data to controller
        NewStudentController newStudentController = fxmlLoader2.getController();
        newStudentController.initData(this, true);

        Stage stage2 = new Stage();
        stage2.setTitle("New Student");
        stage2.setScene(scene2);
        // Specifies the modality for new window.
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
    }

    @FXML
    public void onSectionEnrolmentViewButtonClick(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(SchoolApplication.class.getResource("enrolment-view.fxml"));
        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load()); //, 320, 640);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Pass data to controller
        EnrolmentViewController enrolmentViewController = fxmlLoader2.getController();
        enrolmentViewController.initData(this);

        Stage stage2 = new Stage();
        stage2.setTitle("Enrolment View");
        stage2.setScene(scene2);
        // Specifies the modality for new window.
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
    }

    @FXML
    public void onNewSectionButtonClick(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(SchoolApplication.class.getResource("new-section.fxml"));
        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load()); //, 320, 640);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Pass data to controller
        NewSectionController newSectionController = fxmlLoader2.getController();
        newSectionController.initData(this, true);

        Stage stage2 = new Stage();
        stage2.setTitle("New Section");
        stage2.setScene(scene2);
        // Specifies the modality for new window.
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
    }

    @FXML
    public void onEditSectionButtonClick(){
        FXMLLoader fxmlLoader2 = new FXMLLoader(SchoolApplication.class.getResource("new-section.fxml"));
        Scene scene2 = null;
        try {
            scene2 = new Scene(fxmlLoader2.load()); //, 320, 640);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Pass data to controller
        NewSectionController newSectionController = fxmlLoader2.getController();
        newSectionController.initData(this, false);

        Stage stage2 = new Stage();
        stage2.setTitle("Edit Section");
        stage2.setScene(scene2);
        // Specifies the modality for new window.
        stage2.initModality(Modality.APPLICATION_MODAL);
        stage2.show();
    }

    public void initialize_Student_Tab(){
        System.out.println("Initializing student tab");

        //Init buttons
        removeStudentButton.setDisable(true);
        editStudentButton.setDisable(true);

        //Selection Model Initialization
        studentSelectionModel = studentTableView.getSelectionModel();
        studentSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        studentSelectedItems = studentSelectionModel.getSelectedItems();

        //Adding listener to the selection selected item list
        studentSelectedItems.addListener(
                new ListChangeListener<Student>() {
                    @Override
                    public void onChanged(
                            Change<? extends Student> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("studentSelectedItems size: " + studentSelectedItems.size());
                        removeStudentButton.setDisable(false);
                        editStudentButton.setDisable(false);
                        onStudentTableSelection();
                    }
                });

        //Initialize student table
        studentTableViewIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentTableViewFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentFirstName"));
        studentTableViewLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentLastName"));
        studentTableViewDofBColumn.setCellValueFactory(new PropertyValueFactory<>("studentDateOfBirth"));
        studentTableView.setItems(studentList);

        //Student tab listview (lists enrolled Sections for selected student)
        studentTabListView.setItems(studentEnrolledSections);
        studentTabListView.setPlaceholder(new Label("No Class Enrolled"));

        //Students Tab Personal Information Pane
        studentTab_PersonalInfo_LeftTextFlow.setTextAlignment(TextAlignment.RIGHT);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentId_text_1);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentFirstName_text_2);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentLastName_text_3);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentDofB_text_4);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentHomeTown_text_5);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentHomeCountry_text_6);
        studentTab_PersonalInfo_LeftTextFlow.getChildren().add(studentHSAverage_text_7);

        studentTab_PersonalInfo_RightTextFlow.setTextAlignment(TextAlignment.LEFT);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentIdData_text_1);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentFirstNameData_text_2);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentLastNameData_text_3);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentDofBData_text_4);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentHomeTownData_text_5);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentHomeCountryData_text_6);
        studentTab_PersonalInfo_RightTextFlow.getChildren().add(studentHSAverageData_text_7);
    }

    public void initialize_Instructor_Tab(){
        //Init buttons
        removeInstructorButton.setDisable(true);
        editInstructorButton.setDisable(true);

        //Initialize Tableview selection models
        instructorSelectionModel = instructorTableView.getSelectionModel();
        instructorSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        instructorSelectedItems = instructorSelectionModel.getSelectedItems();

        instructorSelectedItems.addListener(
                new ListChangeListener<Instructor>() {
                    @Override
                    public void onChanged(
                            Change<? extends Instructor> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("instructorSelectedItems size: " + instructorSelectedItems.size());
                        removeInstructorButton.setDisable(false);
                        editInstructorButton.setDisable(false);
                        onInstructorTableSelection();
                    }
                });

        //Initialize instructor table
        instructorTableViewIdColumn.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        instructorTableViewFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("instructorFirstName"));
        instructorTableViewLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("instructorLastName"));
        instructorTableViewDofBColumn.setCellValueFactory(new PropertyValueFactory<>("instructorDateOfBirth"));
        instructorTableView.setItems(instructorList);

        //Instructor tab listview (lists Sections taught by the selected instructor)
        instructorTabListView.setItems(instructorSections);
        instructorTabListView.setPlaceholder(new Label("No Class Taught"));

        //Instructors Tab Personal Information Pane
        instructorTab_PersonalInfo_LeftTextFlow.setTextAlignment(TextAlignment.RIGHT);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorId_text_1);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorFirstName_text_2);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorLastName_text_3);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorDofB_text_4);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorSpecialization_text_5);
        instructorTab_PersonalInfo_LeftTextFlow.getChildren().add(instructorEducation_text_6);

        instructorTab_PersonalInfo_RightTextFlow.setTextAlignment(TextAlignment.LEFT);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorIdData_text_1);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorFirstNameData_text_2);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorLastNameData_text_3);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorDofBData_text_4);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorSpecializationData_text_5);
        instructorTab_PersonalInfo_RightTextFlow.getChildren().add(instructorEducationData_text_6);
    }

    public void initialize_Subject_Tab(){
        //Init buttons
        removeSubjectButton.setDisable(true);
        editSubjectButton.setDisable(true);

        //Initialize Tableview selection models
        subjectsSelectionModel = subjectsTableView.getSelectionModel();

        //Set selection model to one row
        subjectsSelectionModel.setSelectionMode(SelectionMode.SINGLE);

        //Tableview selection list (Items selected in the tables)
        subjectsSelectedItems = subjectsSelectionModel.getSelectedItems();

        subjectsSelectedItems.addListener(
                new ListChangeListener<Subject>() {
                    @Override
                    public void onChanged(
                            Change<? extends Subject> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("instructorSelectedItems size: " + subjectsSelectedItems.size());
                        removeSubjectButton.setDisable(false);
                        editSubjectButton.setDisable(false);
                        onSubjectsTableSelection();
                    }
                });

        //Initialize subjects table
        subjectTableViewIdColumn.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        subjectTableViewNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        subjectTableViewNumberColumn.setCellValueFactory(new PropertyValueFactory<>("subjectNumber"));
        subjectTableViewNoSectionsColumn.setCellValueFactory(new PropertyValueFactory<>("subjectNoSections"));
        subjectsTableView.setItems(subjectList);

        //Subjects tab listview (lists sections for a particular subject)
        subjectTabListView.setItems(subjectSections);
        subjectTabListView.setPlaceholder(new Label("No Sections Scheduled"));

        // Subjects Tab Personal Information Pane
        subjectTab_Info_LeftTextFlow.setTextAlignment(TextAlignment.RIGHT);
        subjectTab_Info_LeftTextFlow.getChildren().addAll(
                subjectId_text_1,
                subjectName_text_2,
                subjectNumber_text_3,
                subjectDescription_text_4,
                subjectNoSections_text_5
        );

        subjectTab_Info_RightTextFlow.setTextAlignment(TextAlignment.LEFT);
        subjectTab_Info_RightTextFlow.getChildren().addAll(
                subjectIdData_text_1,
                subjectNameData_text_2,
                subjectNumberData_text_3,
                subjectDescriptionData_text_4,
                subjectNoSectionsData_text_5
        );
    }

    public void initialize_Enrolment_Tab(){
        //Init buttons (Disable until student selected)
        this.enrolButton.setDisable(true);
        this.unenrolButton.setDisable(true);

        //Initialize Tableview selection models
        this.enrolmentEnrolledSelectionModel = this.enrolmentEnrolledTableView.getSelectionModel();
        this.enrolmentEnrolledSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        this.enrolmentEnrolledSelectedItems = this.enrolmentEnrolledSelectionModel.getSelectedItems();
        this.enrolmentNotenrolledSelectionModel = this.enrolmentNotenrolledTableView.getSelectionModel();
        this.enrolmentNotenrolledSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        this.enrolmentNotenrolledSelectedItems = this.enrolmentNotenrolledSelectionModel.getSelectedItems();

        this.enrolmentEnrolledSelectedItems.addListener(
                new ListChangeListener<Student>() {
                    @Override
                    public void onChanged(
                            Change<? extends Student> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("enrolmentEnrolledSelectedItems size: " + enrolmentEnrolledSelectedItems.size());
                        unenrolButton.setDisable(false);
                        //onInstructorTableSelection(); -------
                    }
                });

        this.enrolmentNotenrolledSelectedItems.addListener(
                new ListChangeListener<Student>() {
                    @Override
                    public void onChanged(
                            Change<? extends Student> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("enrolmentUnenrolledSelectedItems size: " + enrolmentNotenrolledSelectedItems.size());
                        enrolButton.setDisable(false);
                        //onInstructorTableSelection(); -------
                    }
                });

        //Adding action to the choice box
        this.enrolmentSectionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    System.out.println("enrolmentSectionChoiceBox listener");

                    //Disable buttons until student selected
                    this.enrolButton.setDisable(true);
                    this.unenrolButton.setDisable(true);

                    //This method is run at tab exit
                    //The sectionList is cleared which is treated as change in selection
                    //This new_val is -1 at some point
                    //It has value of -1 on tab exit and so the choice will not actually be seen
                    int sectionSelection = new_val.intValue();
                    if (sectionSelection==-1){
                        sectionSelection = 0;
                    }

                    //Do this on ChoiceBox selection change
                    String selectedSectionId = this.sectionIdList.get(sectionSelection);

                    //Request data from database for Enrolled Tables
                    Database.getDatabaseData("CALL get_students_from_section(" + selectedSectionId + ");", "sectionEnrolledStudents", this.get_login_info(), this);

                    //Request data from database for Not Enrolled Tables
                    Database.getDatabaseData("CALL get_students_not_from_section(" + selectedSectionId + ");", "sectionNotenrolledStudents", this.get_login_info(), this);
                });

        //Initialize Enrolled table
        this.enrolmentEnrolledTableViewStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        this.enrolmentEnrolledTableViewStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        this.enrolmentEnrolledTableViewStudentDofBColumn.setCellValueFactory(new PropertyValueFactory<>("studentDateOfBirth"));
        this.enrolmentEnrolledTableView.setItems(this.enrolmentEnrolledList);

        //Initialize Notenrolled table
        this.enrolmentNotenrolledTableViewStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        this.enrolmentNotenrolledTableViewStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        this.enrolmentNotenrolledTableViewStudentDofBColumn.setCellValueFactory(new PropertyValueFactory<>("studentDateOfBirth"));
        this.enrolmentNotenrolledTableView.setItems(this.enrolmentNotenrolledList);
    }

    public void initialize_GradeBook_Tab(){

    }

    public Login get_login_info(){
        return this.login_info;
    }

    public ObservableList<Section> getSectionList() {
        return sectionList;
    }

    public void initialize_Section_Tab() {
        //Initialize buttons
        sectionEnrolmentViewButton.setDisable(true);
        removeSectionButton.setDisable(true);
        editSectionButton.setDisable(true);

        // Selection Model Initialization
        sectionSelectionModel = sectionTableView.getSelectionModel();
        sectionSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        sectionSelectedItems = sectionSelectionModel.getSelectedItems();

        //Initialize Buttons
        //sectionEnrolmentViewButton = new Button();

        // Adding listener to the selected item list
        sectionSelectedItems.addListener(
                new ListChangeListener<Section>() {
                    @Override
                    public void onChanged(
                            Change<? extends Section> change) {
                        System.out.println(
                                "Selection changed: " + change.getList());
                        System.out.println("sectionSelectedItems size: " + sectionSelectedItems.size());
                        removeSectionButton.setDisable(false);
                        editSectionButton.setDisable(false);
                        sectionEnrolmentViewButton.setDisable(false);
                        //onSectionTableSelection();
                    }
                });

        // Initialize section table
        sectionTableViewIdColumn.setCellValueFactory(new PropertyValueFactory<>("sectionId"));
        sectionTableViewSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("sectionSubject"));
        sectionTableViewStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sectionStartTime"));
        sectionTableViewEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sectionEndTime"));
        sectionTableViewDaysColumn.setCellValueFactory(new PropertyValueFactory<>("sectionDays"));
        sectionTableViewInstructorColumn.setCellValueFactory(new PropertyValueFactory<>("sectionInstructor"));
        sectionTableViewEnrolmentColumn.setCellValueFactory(new PropertyValueFactory<>("sectionEnrolment"));
        sectionTableViewRoomColumn.setCellValueFactory(new PropertyValueFactory<>("sectionRoom"));
        sectionTableView.setItems(sectionList);
    }

    public void initialize_GradedItem_Tab(){
        //Init buttons (Disable until student selected)
        //this.enrolButton.setDisable(true);
        //this.unenrolButton.setDisable(true);

        //Initialize Tableview selection models
        this.gradedItemSelectionModel = this.gradedItemTableView.getSelectionModel();
        this.gradedItemSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        this.gradedItemSelectedItems = this.gradedItemSelectionModel.getSelectedItems();

        this.gradedItemSelectedItems.addListener(
                new ListChangeListener<GradedItem>() {
                    @Override
                    public void onChanged(
                            Change<? extends GradedItem> change) {
                        System.out.println("gradedItemSelectedItems size: " + gradedItemSelectedItems.size());
                        onGradedItemsTableSelection();
                    }
                });

        //Adding action to the choice box
        this.gardedItemSectionChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {

                    System.out.println("gardedItemSectionChoiceBox listener");

                    //Disable buttons until student selected
                    //this.enrolButton.setDisable(true);
                    //this.unenrolButton.setDisable(true);

                    //This method is run at tab exit
                    //The sectionList is cleared which is treated as change in selection
                    //This new_val is -1 at some point
                    //It has value of -1 on tab exit and so the choice will not actually be seen
                    int sectionSelection = new_val.intValue();
                    if (sectionSelection==-1){
                        sectionSelection = 0;
                    }

                    //Do this on ChoiceBox selection change
                    String selectedSectionId = this.sectionIdList.get(sectionSelection);

                    //Request data from database for Graded Items Table
                    Database.getDatabaseData("CALL get_graded_items_by_section(" + selectedSectionId + ");", "sectionGradedItems", this.get_login_info(), this);
                });

        //Initialize Enrolled table
        this.gradedItemTableViewId.setCellValueFactory(new PropertyValueFactory<>("gradedItemId"));
        this.gradedItemTableViewTitle.setCellValueFactory(new PropertyValueFactory<>("gradedItemTitle"));
        this.gradedItemTableViewDate.setCellValueFactory(new PropertyValueFactory<>("gradedItemDate"));
        this.gradedItemTableViewAverage.setCellValueFactory(new PropertyValueFactory<>("gradedItemDate"));
        this.gradedItemTableView.setItems(this.gradedItemList);
    }


    @FXML
    public void initialize(){
        //System.out.println("Starting intialize");
        //login_info = new Login("root","12345678"); //Initialize login info

        initializeSchoolTabPane(); //Initialize the TabPane change listeners

        //Initialize the individual tabs
        initialize_Student_Tab();
        initialize_Instructor_Tab();
        initialize_Subject_Tab();
        initialize_Section_Tab();
        initialize_Enrolment_Tab();
        initialize_GradedItem_Tab();

        //Initialize system message listView
        //listView1.setItems(systemMessages);
        statusTextflow1.setStyle("-fx-background-color: white");
        statusTextflow1.getChildren().add(statusText1);
        statusTextflow1.getChildren().add(statusText2);
        statusTextflow1.getChildren().add(statusText3);
        statusTextflow1.getChildren().add(statusText4);
        statusTextflow1.getChildren().add(statusText5);

        System.out.println("HelloController Initialize Executed");
        updateStatusTextFlow("SchoolController Initialize Executed");
    }

    void updateStatusTextFlow(String message){
        statusText1.setText(statusText2.getText());
        statusText2.setText(statusText3.getText());
        statusText3.setText(statusText4.getText());
        statusText4.setText(statusText5.getText());
        statusText5.setText("> " + message + "\n");
    }

    protected void onStudentTableSelection() {
        //Must clear the table or it keeps previous entries
        studentTabListView.getItems().clear(); //Empty list

        //Display data in the personal information pane on the student tab
        studentIdData_text_1.setText(" " + studentSelectedItems.get(0).getStudentId() + "\n");
        studentFirstNameData_text_2.setText(" " + studentSelectedItems.get(0).getStudentFirstName() + "\n");
        studentLastNameData_text_3.setText(" " + studentSelectedItems.get(0).getStudentLastName() + "\n");
        studentDofBData_text_4.setText(" " + studentSelectedItems.get(0).getStudentDateOfBirth() + "\n");
        studentHomeTownData_text_5.setText(" " + studentSelectedItems.get(0).getStudentHomeTown() + "\n");
        studentHomeCountryData_text_6.setText(" " + studentSelectedItems.get(0).getStudentHomeCountry() + "\n");
        studentHSAverageData_text_7.setText(" " + studentSelectedItems.get(0).getStudentHighSchoolAverage() + "\n");

        //Request data from database
        Database.getDatabaseData(
                "SELECT Subjects.Subject_Name, Subjects.Subject_Number " +
                "FROM (SELECT * FROM Enrolment WHERE Enrolment.Student_Id="+
                        studentSelectedItems.get(0).getStudentId() +") AS Selections " +
                        "INNER JOIN Sections ON Selections.Section_Id=Sections.Section_Id " +
                        "INNER JOIN Subjects ON Sections.Subject_Id=Subjects.Subject_Id", "onStudentTableSelection", login_info, this);
    }

    protected void onInstructorTableSelection() {
        // Must clear the table or it keeps previous entries
        instructorTabListView.getItems().clear(); // Empty list

        // Display data in the personal information pane on the instructor tab
        instructorIdData_text_1.setText(" " + instructorSelectedItems.get(0).getInstructorId() + "\n");
        instructorFirstNameData_text_2.setText(" " + instructorSelectedItems.get(0).getInstructorFirstName() + "\n");
        instructorLastNameData_text_3.setText(" " + instructorSelectedItems.get(0).getInstructorLastName() + "\n");
        instructorDofBData_text_4.setText(" " + instructorSelectedItems.get(0).getInstructorDateOfBirth() + "\n");
        instructorSpecializationData_text_5.setText(" " + instructorSelectedItems.get(0).getInstructorSpecialization() + "\n");
        instructorEducationData_text_6.setText(" " + instructorSelectedItems.get(0).getInstructorEducation() + "\n");

        // Request data from database
        Database.getDatabaseData(
                "SELECT Subjects.Subject_Name, Subjects.Subject_Number, Selections.Section_Start_Time, Selections.Section_Room " +
                        "FROM (SELECT * FROM Sections WHERE Sections.Instructor_Id=" +
                        instructorSelectedItems.get(0).getInstructorId() + ") AS Selections " +
                        "INNER JOIN Subjects ON Selections.Subject_Id=Subjects.Subject_Id", "onInstructorTableSelection", login_info, this);
    }

    protected void onSubjectsTableSelection() {
        // Must clear the list view or it keeps previous entries
        subjectTabListView.getItems().clear(); // Empty list

        // Display data in the personal information pane on the subject tab
        subjectIdData_text_1.setText(" " + subjectsSelectedItems.get(0).getSubjectId() + "\n");
        subjectNameData_text_2.setText(" " + subjectsSelectedItems.get(0).getSubjectName() + "\n");
        subjectNumberData_text_3.setText(" " + subjectsSelectedItems.get(0).getSubjectNumber() + "\n");
        subjectDescriptionData_text_4.setText(" " + subjectsSelectedItems.get(0).getSubjectDescription() + "\n");
        subjectNoSectionsData_text_5.setText(" " + subjectsSelectedItems.get(0).getSubjectNoSections() + "\n");

        // Request data from the database
        Database.getDatabaseData(
                "SELECT Sections.Section_Id, Sections.Section_Start_Time, Sections.Section_Room " +
                        "FROM Sections " +
                        "WHERE Sections.Subject_Id=" + subjectsSelectedItems.get(0).getSubjectId() + ";", "onSubjectsTableSelection", login_info, this);
    }

    protected void onGradedItemsTableSelection() {
        // Must clear the list view or it keeps previous entries
        subjectTabListView.getItems().clear(); // Empty list

        // Display data in the personal information pane on the subject tab
        subjectIdData_text_1.setText(" " + subjectsSelectedItems.get(0).getSubjectId() + "\n");
        subjectNameData_text_2.setText(" " + subjectsSelectedItems.get(0).getSubjectName() + "\n");
        subjectNumberData_text_3.setText(" " + subjectsSelectedItems.get(0).getSubjectNumber() + "\n");
        subjectDescriptionData_text_4.setText(" " + subjectsSelectedItems.get(0).getSubjectDescription() + "\n");
        subjectNoSectionsData_text_5.setText(" " + subjectsSelectedItems.get(0).getSubjectNoSections() + "\n");

        // Request data from the database
        Database.getDatabaseData(
                "CALL get_graded_items_by_section(" + subjectsSelectedItems.get(0).getSubjectId() + ";", "onSubjectsTableSelection", login_info, this);
    }

    @FXML
    //When Students tab is selected
    protected void onTabStudentSelection() {
        //Disables the remove student button until a student is selected in the table
        removeStudentButton.setDisable(true);
        editStudentButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        studentTableView.getItems().clear(); //Empty table

        //Request data from database
        Database.getDatabaseData("SELECT * FROM Students" + ";", "Students", login_info, this);
    }

    @FXML
    //When Instructors tab is selected
    protected void onTabInstructorsSelection() {
        //Disables the remove student button until a student is selected in the table
        removeInstructorButton.setDisable(true);
        editInstructorButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        instructorTableView.getItems().clear(); //Empty table

        //Request data from database
        Database.getDatabaseData("SELECT * FROM Instructors" + ";", "Instructors", login_info, this);
    }

    @FXML
    //When Subjects tab is selected
    protected void onTabSubjectsSelection() {
        //Disables the remove student button until a student is selected in the table
        removeStudentButton.setDisable(true);
        editSubjectButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        subjectsTableView.getItems().clear(); //Empty table

        //Request data from database
        Database.getDatabaseData("SELECT Subjects.Subject_Id, Subjects.Subject_Name, Subjects.Subject_Number, Subjects.Subject_Description, COALESCE(Selection.Subject_No_Sections, 0) as value " +
                "FROM Subjects " +
                "LEFT JOIN (SELECT Sections.Subject_Id, COUNT(*) AS Subject_No_Sections " +
                "FROM Sections " +
                "GROUP BY Sections.Subject_Id) AS Selection " +
                "ON Subjects.Subject_Id=Selection.Subject_Id;", "Subjects", login_info, this);
    }

    @FXML
    protected void onTabSectionsSelection() {
        //Disables the remove student button until a student is selected in the table
        removeSectionButton.setDisable(true);
        editSectionButton.setDisable(true);
        sectionEnrolmentViewButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        //sectionTableView.getItems().clear(); //Empty table

        //Request data from database
        Database.getDatabaseData("SELECT * FROM sections_info_view;", "Sections", login_info, this);
    }

    @FXML
    //When Subjects tab is selected
    protected void onTabEnrolmentSelection() {
        //Disables the remove enrolment button until a student is selected in the table
        enrolButton.setDisable(true);
        unenrolButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        this.enrolmentEnrolledList.clear();
        this.enrolmentNotenrolledList.clear();
        //this.enrolmentEnrolledTableView.getItems().clear(); //Empty table
        //this.enrolmentNotenrolledTableView.getItems().clear(); //Empty table

        //Request data from database for the sections ChoiceBox
        Database.getDatabaseData("SELECT * FROM sections_info_view;", "Enrolment", login_info, this);

        /*
        //Request data from database for the sections ChoiceBox
        Database.getDatabaseData("SELECT Sections.Section_Id, Subjects.Subject_Name, Subjects.Subject_Number, Subjects.Subject_Id, " +
                "Sections.Section_Start_Time, Sections.Section_End_Time, Sections.Section_Days, " +
                "Instructors.Instructor_First_Name, Instructors.Instructor_Last_Name, Instructors.Instructor_Id, Sections.Section_Room, " +
                "COALESCE(Selection.Section_Enrolment, 0) as value " +
                "FROM Sections " +
                "LEFT JOIN Subjects " +
                "ON Subjects.Subject_Id=Sections.Subject_Id " +
                "LEFT JOIN Instructors " +
                "ON Sections.Instructor_Id=Instructors.Instructor_Id " +
                "LEFT JOIN (SELECT Enrolment.Section_Id, COUNT(*) AS Section_Enrolment " +
                "FROM Enrolment " +
                "GROUP BY Enrolment.Section_Id) AS Selection " +
                "ON Sections.Section_Id=Selection.Section_Id;", "Enrolment", login_info, this);

         */
    }

    @FXML
    //When Subjects tab is selected
    protected void onTabGradedItemsSelection() {
        //Disables the remove enrolment button until a student is selected in the table
        //enrolButton.setDisable(true);
        //unenrolButton.setDisable(true);

        //Must clear the table or it keeps previous entries
        this.gradedItemList.clear();

        //Request data from database for the sections ChoiceBox
        Database.getDatabaseData("SELECT * FROM sections_info_view;", "GradedItems", login_info, this);

        /*
        //Request data from database for the sections ChoiceBox
        Database.getDatabaseData("SELECT Sections.Section_Id, Subjects.Subject_Name, Subjects.Subject_Number, Subjects.Subject_Id, " +
                "Sections.Section_Start_Time, Sections.Section_End_Time, Sections.Section_Days, " +
                "Instructors.Instructor_First_Name, Instructors.Instructor_Last_Name, Instructors.Instructor_Id, Sections.Section_Room, " +
                "COALESCE(Selection.Section_Enrolment, 0) as value " +
                "FROM Sections " +
                "LEFT JOIN Subjects " +
                "ON Subjects.Subject_Id=Sections.Subject_Id " +
                "LEFT JOIN Instructors " +
                "ON Sections.Instructor_Id=Instructors.Instructor_Id " +
                "LEFT JOIN (SELECT Enrolment.Section_Id, COUNT(*) AS Section_Enrolment " +
                "FROM Enrolment " +
                "GROUP BY Enrolment.Section_Id) AS Selection " +
                "ON Sections.Section_Id=Selection.Section_Id;", "Enrolment", login_info, this);

         */
    }

    @FXML
    protected void onTabGradeBookSelection() {

    }

    @Override
    public void ProcessData(ResultSet rs, String opCode) {
        switch (opCode) {
            case "Students":
                System.out.println("Processing Students table");
                Student.processStudentTable(rs, studentList);
                updateStatusTextFlow("Finished processing Student Table");
                break;
            case "Instructors":
                System.out.println("Processing Instructors table");
                Instructor.processInstructorsTable(rs, instructorList);
                updateStatusTextFlow("Finished processing Instructor Table");
                break;
            case "Subjects":
                System.out.println("Processing Subjects table");
                Subject.processSubjectsTable(rs, subjectList);
                updateStatusTextFlow("Finished processing Subject Table");
                break;
            case "Sections":
                System.out.println("Processing Sections table");
                Section.processSectionsTable(rs, sectionList);
                updateStatusTextFlow("Finished processing Sections Table");
                break;
            case "Enrolment":
                Section.processSectionsTable(rs, sectionList);
                Database.initialize_Section_ChoiceBox(this.enrolmentSectionChoiceBox, this.sectionList, this.sectionIdList); //Populates the Sections ChoiceBox
                updateStatusTextFlow("Finished processing Sections Table");
                break;
            case "GradedItems":
                Section.processSectionsTable(rs, sectionList);
                Database.initialize_Section_ChoiceBox(this.gardedItemSectionChoiceBox, this.sectionList, this.sectionIdList); //Populates the Sections ChoiceBox
                updateStatusTextFlow("Finished processing Sections Table");
                break;
            case "onStudentTableSelection":
                Student.processStudentTableSelect(rs, studentEnrolledSections);
                updateStatusTextFlow("Finished processing Selected Student Enrolled Sections Table");
                break;
            case "onInstructorTableSelection":
                Instructor.processInstructorTableSelect(rs, instructorSections);
                updateStatusTextFlow("Finished processing Instructor Sections Table");
                break;
            case "onSubjectsTableSelection":
                System.out.println("Processing Subjects table");
                Subject.processSubjectsTableSelect(rs, subjectSections);
                updateStatusTextFlow("Finished processing Selected Subject sections Table");
                break;
            case "sectionEnrolledStudents":
                //System.out.println("Processing Subjects table");
                Student.processStudentTable(rs, enrolmentEnrolledList);
                updateStatusTextFlow("Finished processing Selected Section Students");
                break;
            case "sectionNotenrolledStudents":
                //System.out.println("Processing Subjects table");
                Student.processStudentTable(rs, enrolmentNotenrolledList);
                updateStatusTextFlow("Finished processing Selected Section Not Enrolled Students");
                break;
            case "sectionGradedItems":
                GradedItem.processGradedItemTable(rs, gradedItemList);
                updateStatusTextFlow("Finished processing Graded Item list");
                break;
        }
    }

    @Override
    public SchoolController getSchoolController() {
        return this;
    }

    @FXML
    public void onQuitMenuItem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage currentStage = (Stage) this.studentTableView.getScene().getWindow();
            currentStage.close();
        } else {
            alert.close();
        }
    }
}