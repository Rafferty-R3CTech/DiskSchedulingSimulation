/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskreadersimulation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import javax.swing.*;
import diskreadersimulation.DiscReading;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.collections.FXCollections.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 *
 * @author zabuz
 */
public class DiskReaderSimulation extends Application {
  int discSize;
  int headPosition;
  ArrayList<Integer> sectorsToRead = new ArrayList<>(30);

  TextField txtDiscSize = new TextField();
  TextField txtHeadPosition = new TextField();
  TextField txtSectorToRead = new TextField();
  TextArea sectorList = new TextArea();
  Button addSectorToRead = new Button("Add Sector");
  Button executeAlgorithms = new Button("Execute Algorithms");
  Button drawDiscGraphic = new Button("Draw Disc");
  TableView<DiscAlgorithmResults> algorithmTable = new TableView<DiscAlgorithmResults>();
  
  @Override
  public void start(Stage primaryStage) {
    sectorList.insertText(sectorList.getLength(), "SECTOR SEARCHING LIST\n----------------------------\n");
    sectorList.setMaxWidth(160);
    sectorList.setMaxHeight(200);
    sectorList.setEditable(false);
    algorithmTable.setMaxSize(1200,200);
    
    Canvas discAnim = new Canvas(450,350);  
    GraphicsContext gc = discAnim.getGraphicsContext2D();
    gc.strokeRect(0,0,450,350);
    gc.strokeOval(75,20,290,290);
    gc.strokeOval(95,40,250,250);
    gc.strokeOval(120,65,200,200);
    gc.strokeOval(140,85,160,160);
    gc.strokeOval(160,105,120,120);
    
    drawDiscGraphic.setOnAction((new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        int numLines = Integer.parseInt(txtDiscSize.getText());
        double a = 360/(double)numLines;

        for (int i = 0; i < numLines; i +=10) {
          double x1 = 220 + 60*Math.cos(a*i);
          double x2 = 220 + 145*Math.cos(a*i);
          double y1 = 165 + 60*Math.sin(a*i);
          double y2 = 165 + 145*Math.sin(a*i);
          gc.strokeLine(x1,y1,x2,y2);
        }
        gc.fillText("Sector 0", 200, 15);
        gc.fillText("Sector " + numLines/8, 340, 65);
        gc.fillText("Sector " + 2*numLines/8, 380, 165);
        gc.fillText("Sector " + 3*numLines/8, 345, 270);
        gc.fillText("Sector " + numLines/2, 200, 325);
        gc.fillText("Sector " + 5*numLines/8, 35, 270);
        gc.fillText("Sector " + 6*numLines/8, 10, 165);
        gc.fillText("Sector " + 7*numLines/8, 25, 65);
      }
    }));
    
    addSectorToRead.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (Integer.parseInt(txtSectorToRead.getText()) > -1 && Integer.parseInt(txtSectorToRead.getText()) < Integer.parseInt(txtDiscSize.getText())){
          sectorsToRead.add(Integer.parseInt(txtSectorToRead.getText()));
          sectorList.insertText(sectorList.getLength(), txtSectorToRead.getText() + '\n');
        } else {
          System.out.println("NOT Adding Sector = " + txtSectorToRead.getText());
        }
      }
    });
    
    executeAlgorithms.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          if (sectorsToRead.size() > 0) {
            for (int j = 0; j < discSize/2; j++){
              gc.strokeLine(100,100,100,100);
            }
            
            System.out.println("Beginning Algorithms now...");
            discSize = Integer.parseInt(txtDiscSize.getText());
            headPosition = Integer.parseInt(txtHeadPosition.getText());
            DiscReading disc = new DiscReading(discSize, sectorsToRead, headPosition);
            System.out.println("------ DISC INFORMATION -------");
            System.out.println("-------------------------------");
            System.out.println("Disc Size :" + discSize);
            System.out.println("Head Position  : " + headPosition);
            System.out.println("Sectors to Read (In Order Entered) : ");
            for (int sector : sectorsToRead) {
              System.out.println("  Sector # : " + sector);
            }
            algorithmTable.getItems().add(0,disc.fcfsAlgorithm());
            algorithmTable.getItems().add(1,disc.sstfAlorithm());
            algorithmTable.getItems().add(2,disc.lookAlgorithm());
            algorithmTable.getItems().add(3,disc.clookAlgorithm());
            algorithmTable.getItems().add(4,disc.scanAlgorithm());
            algorithmTable.getItems().add(5,disc.cscanAlgorithm());
          }
        } catch (Exception exc) {
          System.out.println(exc.toString());
        }
      }
    });

    algorithmTable.setEditable(false);

    TableColumn<DiscAlgorithmResults,String> algorithmName = new TableColumn("Algorithm Name");
      algorithmName.setResizable(false);
      algorithmName.setMinWidth(200.00);
      algorithmName.setCellValueFactory(new PropertyValueFactory("algorithmName"));
      
    TableColumn<DiscAlgorithmResults,Integer> sectorsTraversed = new TableColumn("# Sectors Traversed");
      sectorsTraversed.setResizable(false);
      sectorsTraversed.setMinWidth(200.00);
      sectorsTraversed.setCellValueFactory(new PropertyValueFactory("sectorsTraversed"));
      
    TableColumn<DiscAlgorithmResults,Integer> sectorsAccessed = new TableColumn("# Sectors Accessed");
      sectorsAccessed.setResizable(false);
      sectorsAccessed.setMinWidth(200.00);
      sectorsAccessed.setCellValueFactory(new PropertyValueFactory("sectorsAccessed"));
      
    TableColumn<DiscAlgorithmResults,Double> avgSectorLatency = new TableColumn("Avg. Latency per Sector");
      avgSectorLatency.setResizable(false);
      avgSectorLatency.setMinWidth(200.00);
      avgSectorLatency.setCellValueFactory(new PropertyValueFactory("avgLatency"));
      
    TableColumn header = new TableColumn("Algorithms' Output");
      header.setResizable(false);
      header.setMinWidth(200.00);
      header.getColumns().addAll(algorithmName, sectorsTraversed, sectorsAccessed, avgSectorLatency);
    
    algorithmTable.getColumns().addAll(header);  
    
    BorderPane root = new BorderPane();
    GridPane grid1 = new GridPane();
    Label lblDiscSize = new Label("Disc Size: ");
    Label lblPosition = new Label("Head Position: ");
    Label lblSector = new Label("Sector to Read: ");
    grid1.setConstraints(lblDiscSize,0,0);
    grid1.setConstraints(txtDiscSize,1,0);
    grid1.setConstraints(lblPosition,0,1);
    grid1.setConstraints(txtHeadPosition,1,1);
    grid1.setConstraints(lblSector,0,2);
    grid1.setConstraints(txtSectorToRead,1,2);
    grid1.setConstraints(addSectorToRead,0,3);
    grid1.setConstraints(executeAlgorithms,1,3);
    grid1.setConstraints(drawDiscGraphic,0,4);
    grid1.getChildren().addAll(drawDiscGraphic, txtDiscSize, txtHeadPosition, txtSectorToRead, lblDiscSize, lblPosition, lblSector, addSectorToRead, executeAlgorithms);
    
    root.setLeft(sectorList);
    root.setCenter(algorithmTable);
    root.setRight(grid1);
    root.setBottom(discAnim);
    
    Scene scene = new Scene(root, 1200, 600);
    
    primaryStage.setTitle("Disk Simulator 5000");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}


