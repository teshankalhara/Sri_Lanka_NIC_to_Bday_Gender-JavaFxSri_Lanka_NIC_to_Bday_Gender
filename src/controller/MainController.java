package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.Period;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class MainController {
    @FXML
    private Label lblAge;

    @FXML
    private Label lblBday;

    @FXML
    private Label lblGender;

    @FXML
    private TextField txtNicNumber;

    @FXML
    void btnGenerateOnAction(ActionEvent event) {
        boolean isValid = isValid(txtNicNumber.getText());
        if (isValid) {
            System.out.println("Generate btn Click Valid NIC Number :" + txtNicNumber.getText());
            nicData();
        } else {
            System.out.println("Generate btn Click Invalid NIC Number :" + txtNicNumber.getText());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error:: Invalid NIC Number");
            alert.setHeaderText("Invalid NIC Number!!!!");
            alert.setContentText("An error occurred. Please Entere Validate NIC Number.");
            alert.showAndWait();
        }
    }

    // validation for NIC
    boolean isValid(String nicNumber) {
        if (nicNumber.length() == 12 || (nicNumber.length() == 10 && nicNumber.toLowerCase().endsWith("v"))) {
            return true;
        }
        return false;
    }
    // validation for NIC

    void nicData() {
        String newtxtNicNumber = txtNicNumber.getText().substring(0, 4);
        if (txtNicNumber.getText().toLowerCase().endsWith("v")) {
            newtxtNicNumber = "19" + txtNicNumber.getText().toLowerCase().replace("V", "");
            oldNicGender();
        } else {
            newNicGender();
        }
        boolean isLeapYear = isLeapYear(Integer.parseInt("19" + txtNicNumber.getText().substring(0, 2)));
        LocalDate birthYear;
        if (isLeapYear) {
            birthYear = extractBirthDate(txtNicNumber.getText());
            lblBday.setText(String.valueOf(birthYear));
        } else {
            int dayOfYear = Integer.parseInt(newtxtNicNumber.substring(4, 7));
            if (dayOfYear >= 0 && dayOfYear < 60) {
                birthYear = extractBirthDate(txtNicNumber.getText());
                lblBday.setText(String.valueOf(birthYear));
            } else if (dayOfYear >= 61 && dayOfYear <= 366) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String dateStr = String.valueOf(extractBirthDate(txtNicNumber.getText()));

                LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                LocalDate newDate = date.minusDays(1);
                lblBday.setText(String.valueOf(newDate));
            } else if (dayOfYear >= 500 && dayOfYear < 560) {
                birthYear = extractBirthDate(txtNicNumber.getText());
                lblBday.setText(String.valueOf(birthYear));
            } else if (dayOfYear >= 561 && dayOfYear < 867) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                String dateStr = String.valueOf(extractBirthDate(txtNicNumber.getText()));

                LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                LocalDate newDate = date.minusDays(1);
                lblBday.setText(String.valueOf(newDate));
            }
        }
        int age = calculateAge(txtNicNumber.getText());
        lblAge.setText(String.valueOf(age));
    }

    void newNicGender() {
        String gender = Integer.parseInt(txtNicNumber.getText().substring(4, 7)) > 500 ? "Female" : "Male";
        lblGender.setText(gender);
    }

    void oldNicGender() {
        String gender = Integer.parseInt(txtNicNumber.getText().substring(2, 5)) > 500 ? "Female" : "Male";
        lblGender.setText(gender);
    }

    int calculateAge(String nicNumber) {
        LocalDate birthDate = extractBirthDate(nicNumber);
        if (birthDate != null) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
        return -1; // invalid NIC number
    }

    LocalDate extractBirthDate(String nicNumber) {
        int birthYear;
        int dayOfYear;

        if (nicNumber.length() == 12) {
            birthYear = Integer.parseInt(nicNumber.substring(0, 4));
            dayOfYear = Integer.parseInt(nicNumber.substring(4, 7));
        } else if (nicNumber.length() == 10) {
            birthYear = Integer.parseInt("19" + nicNumber.substring(0, 2));
            dayOfYear = Integer.parseInt(nicNumber.substring(2, 5));
        } else {
            return null;
        }

        if (dayOfYear > 500) {
            dayOfYear -= 500; // female NIC numbers
        }

        try {
            return LocalDate.ofYearDay(birthYear, dayOfYear);
        } catch (Exception e) {
            return null; // invalid date
        }
    }

    Month extractBirthMonth(String nicNumber) {
        LocalDate birthDate = extractBirthDate(nicNumber);
        if (birthDate != null) {
            return birthDate.getMonth();
        }
        return null; // invalid NIC number
    }

    public static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return true; // leap year
                } else {
                    return false; // not leap year
                }
            } else {
                return true; // leap year
            }
        } else {
            return false; // not leap year
        }
    }
}
