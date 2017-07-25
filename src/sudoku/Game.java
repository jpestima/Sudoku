/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.Timer;

/**
 *
 * @author zsdaking
 */
public class Game implements Initializable {

    @FXML
    private GridPane game;

    @FXML
    private Button pause;

    @FXML
    private Label tempo, lvl;

    @FXML
    private ImageView p;

    int[][] sudoku;

    Timer t;
    int seconds = 0;
    
    //Iniciar timer
    public void start() {
        if (seconds != 0) { // se existir um timer ativo
            seconds = 0;
            t.restart();
        } else {
            t = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent ae) {
                    seconds++;
                    Platform.runLater(() -> {
                        int hours = seconds / 3600;
                        int minutes = (seconds % 3600) / 60;
                        int sec = seconds % 60;

                        String timeString = String.format("%02d:%02d:%02d", hours, minutes, sec);
                        tempo.setText("Tempo: " + timeString);

                    });
                }
            });
            t.start();
        }
    }
    //logout
    public void logout(ActionEvent event) throws IOException {
        final Node source = (Node) event.getSource();
        Stage s = (Stage) source.getScene().getWindow();
        s.close();
        //abrir nova janela
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    int x;
    //pausar e resumir o jogo
    public void pauseGame() {
        if (pause.getText().equals("Pause")) {
            t.stop();
            game.setVisible(false);
            pause.setText("Resume");
            p.setVisible(true);
            return;
        }

        if (pause.getText().equals("Resume")) {
            t.start();
            p.setVisible(false);
            game.setVisible(true);
            pause.setText("Pause");
        }
    }
    
    //obter nodo dado uma row e uma column
    @FXML
    public TextField getNodeByRowColumnIndex(final int row,
            final int column, GridPane gridPane
    ) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            try {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            } catch (Exception e) {

            }
        }
        return (TextField) result;
    }

    //Game time
    public void lmao() throws IOException {
        int flag = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                try {
                    TextField t = getNodeByRowColumnIndex(row, column, game);
                    if (t.getText().equals("") || t.getText().length() > 1 || t.getText().equals(" ") || t.getText().charAt(0) > '9' || t.getText().charAt(0) < '1') {
                        t.setText("");
                        flag = 1;
                        //return;
                    }
                    int v = Integer.valueOf(t.getText());
                    if (v != sudoku[row][column]) {
                        /* System.out.println("Linha " + row + " Coluna " + column);
                        System.out.println("Tem:" + v);
                        System.out.println("Devia ter:" + sudoku[row][column]);*/
                        flag = 1;
                        //return;
                    }
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                }
            }
        }
        if (flag == 1) {
            return;
        }
        //End Game condition
        game.setDisable(true);
        t.stop();
        tempo.setVisible(false);
        pause.setVisible(false);
        lvl.setVisible(false);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, sec);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Window primaryStage = null;
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        switch (x) {
            case 50:
                dialogVbox.getChildren().add(new Text("\n\tParabéns, acabaste o Sudoku Fácil em " + timeString + " !"));
                break;
            case 35:
                dialogVbox.getChildren().add(new Text("\n\tParabéns, acabaste o Sudoku Médio em " + timeString + " !"));
                break;
            case 20:
                dialogVbox.getChildren().add(new Text("\n\tParabéns, acabaste o Sudoku Difícil em " + timeString + " !"));
                break;
        }
        Scene dialogScene = new Scene(dialogVbox, 400, 50);
        dialog.setScene(dialogScene);
        dialog.show();

        //Guardar as coisinhas
        File t = new File("Times");
        FileWriter fw = new FileWriter(t, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter writer = new PrintWriter(bw);
        switch (x) {
            case 50:
                writer.append("Fácil - ");
                break;
            case 35:
                writer.append("Médio - ");
                break;
            case 20:
                writer.append("Difícil - ");
                break;
        }

        writer.append(timeString + "\n");
        writer.close();
        bw.close();
        fw.close();

    }
    //criar tabuleiro 
    public void board(int d) {
        game.getChildren().clear();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                Random rand = new Random();
                int n = rand.nextInt(100) + 0;
                TextField t = new TextField();
                t.setPrefSize(30, 100);
                t.setAlignment(Pos.CENTER);
                if (n >= d) {
                    t.setText("");
                    t.setStyle("-fx-font-weight: bold;-fx-border-color:orange;");
                } else {
                    t.setText(String.valueOf(sudoku[row][column]));
                    t.setDisable(true);
                    t.setStyle("-fx-text-fill:red;-fx-border-color:red;-fx-opacity:1.0;-fx-font-weight:bold;");

                }
                t.setEditable(true);
                game.add(t, column, row);
            }
        }
        pause.setVisible(true);
        tempo.setVisible(true);
        game.setVisible(true);
        start();
    }

    public void clickEasy(ActionEvent event) {
        sudoku = new int[][]{
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        lvl.setText("Nível Fácil");
        lvl.setVisible(true);
        game.setVisible(true);
        game.setDisable(false);
        x = 50;
        board(x);
    }

    public void clickNormal(ActionEvent event) {
        sudoku = new int[][]{
            {5, 6, 2, 4, 7, 9, 1, 8, 3},
            {3, 9, 4, 8, 6, 1, 2, 5, 7},
            {1, 7, 8, 5, 3, 2, 9, 4, 6},
            {7, 3, 9, 1, 4, 5, 8, 6, 2},
            {6, 4, 1, 3, 2, 8, 7, 9, 5},
            {2, 8, 5, 6, 9, 7, 3, 1, 4},
            {9, 2, 6, 7, 8, 4, 5, 3, 1},
            {4, 1, 7, 9, 5, 3, 6, 2, 8},
            {8, 5, 3, 2, 1, 6, 4, 7, 9},};
        lvl.setText("Nível Médio");
        lvl.setVisible(true);
        game.setVisible(true);
        game.setDisable(false);
        x = 35;
        board(x);
    }

    public void clickHard(ActionEvent event) {
        sudoku = new int[][]{
            {9, 6, 2, 5, 4, 8, 1, 3, 7},
            {8, 3, 7, 9, 2, 1, 4, 6, 5},
            {1, 5, 4, 3, 6, 7, 2, 8, 9},
            {6, 7, 3, 2, 8, 5, 9, 1, 4},
            {5, 1, 8, 4, 9, 3, 7, 2, 6},
            {2, 4, 9, 7, 1, 6, 3, 5, 8},
            {4, 9, 6, 1, 5, 2, 8, 7, 3},
            {3, 2, 5, 8, 7, 4, 6, 9, 1},
            {7, 8, 1, 6, 3, 9, 5, 4, 2}
        };
        lvl.setText("Nível Difícil");
        lvl.setVisible(true);
        game.setVisible(true);
        game.setDisable(false);
        x = 20;
        board(x);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        game.getChildren().clear();
        game.setVisible(true);
        game.setDisable(true);
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                TextField t = new TextField();
                t.setPrefSize(30, 100);
                t.setStyle("-fx-border-color:orange;-fx-opacity:1.0;");
                game.add(t, column, row);
            }
        }
    }
}
