/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author zsdaking
 */
public class Login implements Initializable {

    @FXML
    private Label status;

    @FXML
    private TextField user;

    @FXML
    private PasswordField pwd;

    @FXML
    private Button register, login;

    @FXML
    private void Register(ActionEvent event) throws FileNotFoundException, IOException {
        String username = user.getText();
        String password = pwd.getText();
        if (username.equals("") || password.equals("")) {
            status.setText("Não pode haver campos vazios");
            return;
        }
        File o = new File("users");
        if (!o.exists()) {
            o.createNewFile();
        }
        FileReader f = new FileReader("users");
        BufferedReader br = new BufferedReader(f);
        String sCurrentLine;
        boolean flag = false;

        while ((sCurrentLine = br.readLine()) != null) {
            if (sCurrentLine.equals(username)) {
                flag = true;
                break;
            } else {
                sCurrentLine = br.readLine();
            }
        }
        if (!flag) {
            try {
                FileWriter fw = new FileWriter(o, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter writer = new PrintWriter(bw);
                writer.append(username + "\n");
                writer.append(password + "\n");
                writer.close();
            } catch (IOException e) {
                // do something
            }
            status.setText("Registo feito com sucesso");
        } else {
            status.setText("Username já existe");
        }

    }

    @FXML
    private void Login(ActionEvent event) throws FileNotFoundException, IOException {
        String username = user.getText();
        String password = pwd.getText();
        if (username.equals("") || password.equals("")) {
            status.setText("Não pode haver campos vazios");
            return;
        }
        File o = new File("users");
        if (!o.exists()) {
            o.createNewFile();
        }
        FileReader f = new FileReader("users");
        BufferedReader br = new BufferedReader(f);
        String sCurrentLine;
        boolean flag = false;

        while ((sCurrentLine = br.readLine()) != null) {
            if (sCurrentLine.equals(username)) {
                flag = true;
                sCurrentLine = br.readLine();
                if (sCurrentLine.equals(password)) {
                    status.setText("Login feito com sucesso");
                    //fechar janela atual
                    File t = new File("Times");
                    if (!t.exists()) {
                        t.createNewFile();
                    }
                    FileWriter fw = new FileWriter(t, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter writer = new PrintWriter(bw);
                    writer.append("Tempos para o jogador "+username+"\n");
                    writer.close();
                    bw.close();
                    fw.close();
                    final Node source = (Node) event.getSource();
                    Stage s = (Stage) source.getScene().getWindow();
                    s.close();
                    //abrir nova janela
                    Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle("Sudoku");
                    stage.setScene(scene);
                    stage.show();

                } else {
                    status.setText("Password errada");
                    return;
                }
            }
        }
        if (!flag) {
            status.setText("Username não existe");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
