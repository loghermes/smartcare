package com.smartcare.smartcaredesktop.controller;

import com.smartcare.smartcaredesktop.model.Doctor;
import com.smartcare.smartcaredesktop.service.DoctorService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Random;

public class DoctorManagementController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField slugField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField specializationField;
    @FXML private TextField licenseField;
    @FXML private TextField phoneField;
    @FXML private TextField experienceField;
    @FXML private TextArea bioField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private Label photoNameLabel;

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor,String> firstNameColumn;
    @FXML private TableColumn<Doctor,String> lastNameColumn;
    @FXML private TableColumn<Doctor,String> slugColumn;
    @FXML private TableColumn<Doctor,String> specializationColumn;
    @FXML private TableColumn<Doctor,String> phoneColumn;

    private Doctor selectedDoctor;
    private String selectedPhotoPath;

    private final DoctorService doctorService = new DoctorService();

    @FXML
    public void initialize() {

        genderComboBox.setItems(FXCollections.observableArrayList("male","female","other"));

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        slugColumn.setCellValueFactory(new PropertyValueFactory<>("slug"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadDoctors();

        doctorTable.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)->{
            if(newVal!=null){
                fillForm(newVal);
            }
        });

        firstNameField.textProperty().addListener((obs,o,n)->generateSlug());
        lastNameField.textProperty().addListener((obs,o,n)->generateSlug());
    }

    private void generateSlug(){
        String first=firstNameField.getText().toLowerCase();
        String last=lastNameField.getText().toLowerCase();
        if(!first.isEmpty()&&!last.isEmpty()){
            slugField.setText("Dr."+first+"-"+last);
        }
    }

    private void fillForm(Doctor d){
        selectedDoctor=d;
        firstNameField.setText(d.getFirstName());
        lastNameField.setText(d.getLastName());
        slugField.setText(d.getSlug());
        specializationField.setText(d.getSpecialization());
        phoneField.setText(d.getPhone());
        experienceField.setText(String.valueOf(d.getYearsExperience()));
        bioField.setText(d.getBio());
    }

    private void loadDoctors(){
        List<Doctor> list=doctorService.findAll();
        doctorTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void handleBrowsePhoto(){
        FileChooser chooser=new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg")
        );
        File file=chooser.showOpenDialog(firstNameField.getScene().getWindow());
        if(file!=null){
            selectedPhotoPath=file.getAbsolutePath();
            photoNameLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleAddDoctor(){
        String password=generateRandomPassword(10);

        doctorService.createDoctor(
                emailField.getText(),
                usernameField.getText(),
                password,
                firstNameField.getText(),
                lastNameField.getText(),
                genderComboBox.getValue(),
                selectedPhotoPath,
                specializationField.getText(),
                licenseField.getText(),
                phoneField.getText(),
                Integer.parseInt(experienceField.getText()),
                bioField.getText(),
                slugField.getText()
        );

        new Alert(Alert.AlertType.INFORMATION,"Generated password: "+password).showAndWait();
        loadDoctors();
        clearForm();
    }

    @FXML
    private void handleUpdateDoctor(){
        if(selectedDoctor==null)return;

        selectedDoctor.setFirstName(firstNameField.getText());
        selectedDoctor.setLastName(lastNameField.getText());
        selectedDoctor.setSlug(slugField.getText());
        selectedDoctor.setSpecialization(specializationField.getText());
        selectedDoctor.setPhone(phoneField.getText());
        selectedDoctor.setYearsExperience(Integer.parseInt(experienceField.getText()));
        selectedDoctor.setBio(bioField.getText());

        doctorService.updateDoctor(selectedDoctor);
        loadDoctors();
        clearForm();
    }

    @FXML
    private void handleDeleteDoctor(){
        if(selectedDoctor==null)return;
        doctorService.deleteDoctor(selectedDoctor.getId());
        loadDoctors();
        clearForm();
    }

    private void clearForm(){
        selectedDoctor=null;
        firstNameField.clear();
        lastNameField.clear();
        slugField.clear();
        specializationField.clear();
        phoneField.clear();
        experienceField.clear();
        bioField.clear();
        photoNameLabel.setText("No file selected");
    }

    private String generateRandomPassword(int length){
        String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb=new StringBuilder();
        Random r=new Random();
        for(int i=0;i<length;i++){
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }
}