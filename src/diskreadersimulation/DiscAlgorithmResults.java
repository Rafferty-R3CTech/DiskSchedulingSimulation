/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diskreadersimulation;

import java.util.ArrayList;

/**
 *
 * @author carothersrr
 */
public class DiscAlgorithmResults {
  public int discSize, headPosition, sectorsTraversed, sectorsAccessed;
  public double avgLatency;
  public String algorithmName;
  
  public DiscAlgorithmResults(int traversed, int accessed, String name) {
    this.algorithmName = name;
    this.sectorsAccessed = accessed;
    this.sectorsTraversed = traversed;
    this.avgLatency = traversed/accessed;
  }
  
  public int getSectorsTraversed(){
    return this.sectorsTraversed;
  }
  public int getSectorsAccessed(){
    return this.sectorsAccessed;
  }
  public String getAlgorithmName() {
    return this.algorithmName;
  }
  public Double getAvgLatency() {
    return this.avgLatency;
  }
}
