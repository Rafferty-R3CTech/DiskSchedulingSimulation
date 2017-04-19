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
import javafx.scene.control.cell.PropertyValueFactory;
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
  Button addSectorToRead = new Button("Add Sector");
  Button executeAlgorithms = new Button("Execute Algorithms");
  TableView<DiscAlgorithmResults> algorithmTable = new TableView<DiscAlgorithmResults>();
  
  @Override
  public void start(Stage primaryStage) {
    addSectorToRead.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (Integer.parseInt(txtSectorToRead.getText()) > -1 && Integer.parseInt(txtSectorToRead.getText()) < Integer.parseInt(txtDiscSize.getText())){
          sectorsToRead.add(Integer.parseInt(txtSectorToRead.getText()));
        } else {
          System.out.println("NOT Adding Sector = " + txtSectorToRead.getText());
        }
      }
    });
    
    executeAlgorithms.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          System.out.println("Beginning Algorithms now...");
          DiscReading disc = new DiscReading(discSize, sectorsToRead, headPosition);
          System.out.println("------ DISC INFORMATION -------");
          System.out.println("-------------------------------");
          System.out.println("Disc Size :" + discSize);
          System.out.println("Head Position  : " + headPosition);
          System.out.println("Sectors to Read (In Order Entered) : ");
          for (int sector : sectorsToRead) {
            System.out.println("  Sector # : " + sector);
          }
          DiscAlgorithmResults result = disc.fcfsAlgorithm();
          algorithmTable.getItems().add(result); //done
          algorithmTable.getItems().add(1,disc.sstfAlorithm()); //
          algorithmTable.getItems().add(2,disc.lookAlgorithm());
          algorithmTable.getItems().add(3,disc.clookAlgorithm());
          algorithmTable.getItems().add(4,disc.scanAlgorithm());
          algorithmTable.getItems().add(5,disc.cscanAlgorithm());
        } catch (Exception exc) {
          System.out.println(exc.toString());
        }
      }
    });

    //algorithmTable.setEditable(false);

    TableColumn<DiscAlgorithmResults,String> algorithmName = new TableColumn("Algorithm Name");
      algorithmName.setResizable(false);
      algorithmName.setMinWidth(200.00);
      algorithmName.setCellValueFactory(new PropertyValueFactory("algorithmName"));
      ArrayList<String> algorithmNames = new ArrayList();
        algorithmNames.add("FCFS");
        algorithmNames.add("SSTF");
        algorithmNames.add("LOOK");
        algorithmNames.add("CLOOK");
        algorithmNames.add("SCAN");
        algorithmNames.add("CSCAN");
      
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
    FlowPane root = new FlowPane();
    root.getChildren().add(algorithmTable);
    root.getChildren().add(txtDiscSize);
    root.getChildren().add(txtHeadPosition);
    root.getChildren().add(txtSectorToRead);
    root.getChildren().add(addSectorToRead);
    root.getChildren().add(executeAlgorithms);
    
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


