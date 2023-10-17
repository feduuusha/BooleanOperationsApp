package com.fyodor.booleanframe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class BoolController {
    @FXML
    public TextField writeText;
    public CheckBox buttonTupic;
    public CheckBox buttonMinimum;
    @FXML
    private Button button1, button2, button3, button4, buttonN1, buttonN2, buttonN3, buttonN4, buttonDis, buttonCon,
            buttonLeft, buttonRight, buttonNegative, buttonDown, buttonUp, buttonend;
    @FXML
    private CheckBox buttonSdnf, buttonSknf, tablet, abbrDNF;
    @FXML

    protected void onButtonButtonBuild() {
        String expression = writeText.getText();
        expression = converter(expression);
        BoolOperator operator = null;
        try {
            operator = new BoolOperator(expression);
        } catch (InvalidInputStringException ex) {
            Stage stageEx = new Stage();
            stageEx.setTitle("Boolean Operations by Fyodor Voropaev");
            Parent root2;
            try {
                root2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("exepScene.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stageEx.setScene(new Scene(root2,217,176));
            stageEx.show();

        }
        if (operator != null) {
            Stage stage = new Stage();
            stage.setTitle("Boolean Operations by Fyodor Voropaev");
            Parent root2;
            try {
                root2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("resultScene.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AnchorPane anchorPane = (AnchorPane) root2;
            Text textick = new Text();
            textick.setLayoutX(14);
            textick.setLayoutY(65);
            textick.setFont(new Font("size", 18));
            textick.setVisible(true);
            if (tablet.isSelected()) {
                Label tablet = new Label();
                tablet.setVisible(true);
                tablet.setLayoutX(850);
                tablet.setLayoutY(200);
                tablet.setText(operator.table());
                tablet.setStyle("-fx-font-family: sans-serif;");
                tablet.setFont(new Font("size", 18));
                anchorPane.getChildren().add(tablet);
            }
            if (buttonSdnf.isSelected()) {
                textick.setText("СДНФ:\n" + operator.sdnf());
            }
            if (buttonSknf.isSelected()) {
                textick.setText(textick.getText() + "СКНФ:\n" + operator.scnf() + "\n");
            }
            if (abbrDNF.isSelected()) {
                textick.setText(textick.getText() + "Cокращенная ДНФ:\n" + operator.abbrDNF() + "\n");
            }
            if (buttonTupic.isSelected()) {
                textick.setText(textick.getText() + "Тупиковые ДНФ: \n" + operator.tupicDNF() + "\n");
            }
            if (buttonMinimum.isSelected()) {
                textick.setText(textick.getText() + "Минимальные ДНФ: \n" + operator.minimumDNF() + "\n");
            }
            anchorPane.getChildren().add(textick);
            stage.setScene(new Scene(root2, 1000, 600));
            stage.show();
        }
    }
    @FXML
    protected void buttonClose() {
        Stage stage = (Stage) buttonend.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onButtonButton1() {
        writeText.setText(writeText.getText() + button1.getText());
    }
    @FXML
    protected void onButtonButton2() {
        writeText.setText(writeText.getText() + button2.getText());
    }
    @FXML
    protected void onButtonButton3() {
        writeText.setText(writeText.getText() + button3.getText());
    }
    @FXML
    protected void onButtonButton4() {
        writeText.setText(writeText.getText() + button4.getText());
    }
    @FXML
    protected void onButtonButtoN1() {
        writeText.setText(writeText.getText() + buttonN1.getText());
    }
    @FXML
    protected void onButtonButtoN2() {
        writeText.setText(writeText.getText() + buttonN2.getText());
    }
    @FXML
    protected void onButtonButtoN3() {
        writeText.setText(writeText.getText() + buttonN3.getText());
    }
    @FXML
    protected void onButtonButtoN4() {
        writeText.setText(writeText.getText() + buttonN4.getText());
    }
    @FXML
    protected void onButtonButtonCon() {
        writeText.setText(writeText.getText() + buttonCon.getText());
    }
    @FXML
    protected void onButtonButtonDis() {
        writeText.setText(writeText.getText() + buttonDis.getText());
    }
    @FXML
    protected void onButtonLeft() {
        writeText.setText(writeText.getText() +  buttonLeft.getText());
    }
    @FXML
    protected void onButtonRight() {
        writeText.setText(writeText.getText() + buttonRight.getText());
    }
    @FXML
    protected void onButtonNegative() {
        writeText.setText(writeText.getText() + buttonNegative.getText());
    }
    @FXML
    protected void onButtonDown() {
        writeText.setText(writeText.getText() + buttonDown.getText());
    }
    @FXML
    protected void onButtonUp() {
        writeText.setText(writeText.getText() + buttonUp.getText());
    }

    private static String converter(String a) {
        a = a.toLowerCase();
        a = a.replace(" ", "");
        a = a.replace("¬", "!");
        a = a.replace("∧", "&");
        a = a.replace("∨", "|");
        return a;
    }
}