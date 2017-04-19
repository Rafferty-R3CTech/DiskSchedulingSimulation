package diskreadersimulation;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class DiscReading
{
	public static int head;
	public static ArrayList<Integer> sectorsToRead;
	public static int distance;
	public static int seekTime;
	public static float averageSeek;
	public static int diskSize;
	public static char direction;
  
  public DiscReading(int size, ArrayList<Integer> sectors, int headPosition) {
    this.diskSize = size;
    this.sectorsToRead = sectors; 
    this.head = headPosition;
  }

	// First Come First Serve algorithm
	public DiscAlgorithmResults fcfsAlgorithm() {
		seekTime = 0;
		distance = Math.abs(sectorsToRead.get(0) - head);
		seekTime = seekTime + distance;

		// System.out.println("Move from " + head + " to " + sectorsToRead.get(0) + " with distance " + distance);

		for(int i = 0; i < sectorsToRead.size() - 1; i++) {
			distance = Math.abs(sectorsToRead.get(i+1) - sectorsToRead.get(i));
			seekTime = seekTime + distance;

			// System.out.println("Move from " + sectorsToRead.get(i) + " to " + sectorsToRead.get(i+1) + " with distance " + distance);
		}

		System.out.println("---FCFS ALGORITHM---");
		System.out.println("FCFS total seek time: " + seekTime);
		averageSeek = seekTime / (sectorsToRead.size() + 1);
		System.out.println("FCFS average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "FCFS");
	}

	// Shortest Seek Time First algorithm
	public DiscAlgorithmResults sstfAlorithm() {
		seekTime = 0;
		int[] tmpSects = new int[sectorsToRead.size()];
		int index;
		int start = head;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpSects[i] = sectorsToRead.get(i);
		}

		for(int i = 0; i < sectorsToRead.size(); i++) {
			index = findShortestSeek(tmpSects, start);
			seekTime = seekTime + Math.abs(start - tmpSects[index]);
			start = tmpSects[index];
			tmpSects[index] = -1;
		}

		System.out.println("---SSTF ALGORITHM---");
		System.out.println("SSTF total seek time: " + seekTime);
		averageSeek = seekTime / (sectorsToRead.size() + 1);
		System.out.println("SSTF average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "SSTF");
	}

	// LOOK algorithm
	public DiscAlgorithmResults lookAlgorithm() {
		seekTime = 0;
		int[] tmpSects = new int[sectorsToRead.size() + 1];
		int index = -1;
		int start = head;

		tmpSects[sectorsToRead.size()] = start;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpSects[i] = sectorsToRead.get(i);
		}

		tmpSects = sortQueue(tmpSects);

		for(int i = 0; i < tmpSects.length; i++) {
			if(start == tmpSects[i]) { 
				index = i;
			}
		}
    direction = (index == 0)? 'r': (tmpSects[index] - tmpSects[index--] > tmpSects[index++] - tmpSects[index])? 'r':'l';
		if(direction == 'l') {
			// reading to the left (descending)
			for(int i = index; i > 0; i--) {
				seekTime += Math.abs(tmpSects[i] - tmpSects[i-1]);
			}

			// change direction
			seekTime += Math.abs(tmpSects[index+1] - tmpSects[0]);

			// reading to the right (ascending)
			for(int i = index+1; i < tmpSects.length-1; i++) {
				seekTime += Math.abs(tmpSects[i+1] - tmpSects[i]);
			}
		} else if (direction == 'r') {
			// reading to the right (ascending)
			for(int i = index; i < tmpSects.length-1; i++) {
				seekTime += Math.abs(tmpSects[i] - tmpSects[i+1]);
			}

			// change direction
			seekTime += Math.abs(tmpSects[tmpSects.length-1] - tmpSects[index-1]);

			// reading to the left (descending)
			for(int i = index - 1; i > 0; i--) {
				seekTime += Math.abs(tmpSects[i] - tmpSects[i-1]);
			}
		}

		System.out.println("---LOOK ALGORITHM---");
		System.out.println("LOOK total seek time: " + seekTime);
		averageSeek = seekTime / tmpSects.length;
		System.out.println("LOOK average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "LOOK");
}

	// Circular LOOK algorithm 
	public DiscAlgorithmResults clookAlgorithm() {
		seekTime = 0;
		int[] tmpSects = new int[sectorsToRead.size() + 1];
		int index = -1;
		int start = head;

		tmpSects[sectorsToRead.size()] = start;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpSects[i] = sectorsToRead.get(i);
		}

		sortQueue(tmpSects);

		for(int i = 0; i < tmpSects.length; i++) {
			if(start == tmpSects[i]) { 
				index = i;
			}
		}
    
    direction = (index == 0)? 'r': (tmpSects[index] - tmpSects[index--] > tmpSects[index++] - tmpSects[index])? 'r':'l';
		if(direction == 'l') {
			// reading to the left (descending)
			for(int i = index; i > 0; i--) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i-1]);
			}

			// change direction
			// doesn't actually count this: seekTime = seekTime + Math.abs(tmpSects[0) - tmpSects[tmpSects.length-1));

			// descending again from other side
			for(int i = tmpSects.length - 1; i > index + 1; i--) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i-1]);
			}
		} else if(direction == 'r') {
			// reading to the right (ascending)
			for (int i = index; i < tmpSects.length-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}

			// change direction
			// doesn't actually count this: seekTime = seekTime + Math.abs(tmpSects[tmpSects.length-1) - tmpSects[0));

			// ascending again from the other side
			for(int i = 0; i < index-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}
		}

		System.out.println("---C-LOOK ALGORITHM---");
		System.out.println("C-LOOK total seek time: " + seekTime);
		averageSeek = seekTime / tmpSects.length;
		System.out.println("C-LOOK average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "C-LOOK");
	}

	// SCAN aka Elevator algorithm
	public DiscAlgorithmResults scanAlgorithm() {
		seekTime = 0;
		int[] tmpSects = new int[sectorsToRead.size() + 1];
		int index = -1;
		int start = head;

		tmpSects[sectorsToRead.size()] = start;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpSects[i] = sectorsToRead.get(i);
		}

		sortQueue(tmpSects);

		for(int i = 0; i < tmpSects.length; i++) {
			if(start == tmpSects[i]) { 
				index = i;
			}
		}
    direction = (index == 0)? 'r': (tmpSects[index] - tmpSects[index--] > tmpSects[index++] - tmpSects[index])? 'r':'l';

		if(direction == 'l') {
			// reading to the left (descending)
			for(int i = index; i > 0; i--) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i-1]);
			}
			
			// reading to the left (descending): 0
			seekTime = seekTime + Math.abs(tmpSects[0] - 0);
			seekTime = seekTime + Math.abs(tmpSects[index+1] - 0);

			for(int i = index+1; i < tmpSects.length - 1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i+1] - tmpSects[i]);
			}
		} else if(direction == 'r') {
			// reading to the right (ascending)
			for(int i = index; i < tmpSects.length-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}

			// reading to the right (asending): diskSize-1
			seekTime = seekTime + Math.abs(tmpSects[tmpSects.length-1] - (diskSize-1));
			seekTime = seekTime + Math.abs(0 - tmpSects[0]);

			// reading to the the right (ascending)
			for(int i = 0; i < index-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}
		}
		
		System.out.println("---SCAN ALGORITHM---");
		System.out.println("SCAN total seek time: " + seekTime);
		averageSeek = seekTime / tmpSects.length;
		System.out.println("SCAN average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "SCAN");
	}

	// Circular SCAN algorithm
	public DiscAlgorithmResults cscanAlgorithm() {
		seekTime = 0;
		int[] tmpSects = new int[sectorsToRead.size() + 2];
		int index = -1;
		int start = head;

		tmpSects[tmpSects.length-1] = start;
		tmpSects[tmpSects.length-2] = diskSize - 1;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpSects[i] = sectorsToRead.get(i);
		}

		sortQueue(tmpSects);

		for(int i = 0; i < tmpSects.length; i++) {
			if(start == tmpSects[i]) { 
				index = i;
			}
		}
    direction = (index == 0)? 'r': (tmpSects[index] - tmpSects[index--] > tmpSects[index++] - tmpSects[index])? 'r':'l';

		if(direction == 'l') {
			// reading to the left (descending)
			for(int i = index; i > 0; i--) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i-1]);
			}

			// reading to the left (descending) to 0
			seekTime = seekTime + Math.abs(tmpSects[0] - 0);

			// reading from the left (descending) from diskSize-1
			for(int i = tmpSects.length-1; i > index+1; i--) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i-1]);
			}
		} else if(direction == 'r') {
			// reading to the right (ascending)
			for(int i = index; i < tmpSects.length-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}

			// reading to the right (ascending) from 0
			seekTime = seekTime + Math.abs(0 - tmpSects[0]);

			// reading to the right (ascending)
			for(int i = 0; i < index-1; i++) {
				seekTime = seekTime + Math.abs(tmpSects[i] - tmpSects[i+1]);
			}
		}

		System.out.println("---C-SCAN ALGORITHM---");
		System.out.println("C-SCAN total seek time: " + seekTime);
		averageSeek = seekTime / (tmpSects.length-1);
		System.out.println("C-SCAN average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "C-SCAN");
	}

	// other --------------------------------------------------------------------
	public int findShortestSeek(int[] tmpSects, int start) {
		int min = 999999; // large initial value to check for min
		int index = -1;

		for(int i = 0; i < tmpSects.length; i++) {
			if(tmpSects[i] != -1) {
				distance = Math.abs(start - tmpSects[i]);
				if(min > distance) {
					min = distance;
					index = i;
				}
			}
		}

		return index;
	}

	public int[] sortQueue(int[] tmpSects) {
		int temp;

		for(int i = 0; i < tmpSects.length; i++) {
			for(int j = i; j < tmpSects.length; j++) {
				if(tmpSects[i] > tmpSects[j]) {
					temp = tmpSects[i];
					tmpSects[i] = tmpSects[j];
					tmpSects[j] = temp;
				}
			}
		}
    return tmpSects;
	}
}