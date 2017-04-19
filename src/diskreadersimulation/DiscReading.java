package diskreadersimulation;

import java.util.ArrayList;

public class DiscReading
{	
  private int head;
	private ArrayList<Integer> sectorsToRead;
	private int distance;
	private int seekTime;
	private float averageSeek;
	private int diskSize;
  
  public DiscReading(int size, ArrayList<Integer> sectors, int position){
    this.head = position;
    this.diskSize = size;
    this.sectorsToRead = sectors;
  }


	// First Come First Serve
	public DiscAlgorithmResults fcfsAlgorithm() {
		seekTime = 0;
		distance = Math.abs(sectorsToRead.get(0) - head);
		seekTime = seekTime + distance;

		// System.out.println("Move from " + head + " to " + queue[0] + " with distance " + distance);

		for(int i = 0; i < sectorsToRead.size() - 1; i++) {
			distance = Math.abs(sectorsToRead.get(i+1) - sectorsToRead.get(i));
			seekTime = seekTime + distance;

			// System.out.println("Move from " + queue[i] + " to " + queue[i+1] + " with distance " + distance);
		}

		System.out.println("---FCFS ALGORITHM---");
		System.out.println("FCFS total seek time: " + seekTime);
		averageSeek = seekTime / (sectorsToRead.size());
		System.out.println("FCFS average seek time: " + averageSeek + "\n");
    
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "FCFS");
	}

	// Shortest Seek Time First
	public DiscAlgorithmResults sstfAlorithm() {
		seekTime = 0;
		int[] tmpQueue = new int[sectorsToRead.size()];
		int index;
		int start = head;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpQueue[i] = sectorsToRead.get(i);
		}

		for(int i = 0; i < sectorsToRead.size(); i++) {
			index = findShortestSeek(tmpQueue, start);
			seekTime = seekTime + Math.abs(start - tmpQueue[index]);
			start = tmpQueue[index];
			tmpQueue[index] = -1;
		}

		System.out.println("---SSTF ALGORITHM---");
		System.out.println("SSTF total seek time: " + seekTime);
		averageSeek = seekTime / sectorsToRead.size();
		System.out.println("SSTF average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "SSTF");
  }

	public DiscAlgorithmResults lookAlgorithm() {
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "LOOK");
	}

	public DiscAlgorithmResults clookAlgorithm() {
		seekTime = 0;
		int[] tmpQueue = new int[sectorsToRead.size()];
		int index;
		int start = head;
		boolean begin = false;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpQueue[i] = sectorsToRead.get(i);
		}

		// distance = Math.abs(tmpQueue[index] - head);

		index = clookSort(tmpQueue, start);

		// System.out.println("Move from " + head + " to " + tmpQueue[index] + " with distance " + distance);

		for(int i = index-1; i > 0; i--) {
			distance = Math.abs(tmpQueue[i] - tmpQueue[i-1]);
			seekTime = seekTime + distance;

			// System.out.println("Move from " + queue[i] + " to " + queue[i+1] + " with distance " + distance);
		}

		System.out.println("---CLOOK ALGORITHM---");
		System.out.println("CLOOK total seek time: " + seekTime);
		averageSeek = seekTime / sectorsToRead.size();
		System.out.println("CLOOK average seek time: " + averageSeek + "\n");
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "CLOOK");
	}

	public DiscAlgorithmResults scanAlgorithm() {
		seekTime = 0;
		int[] tmpQueue = new int[sectorsToRead.size()];
		int index = -1;
		int start = head;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			tmpQueue[i] = sectorsToRead.get(i);
		}

		sortQueue(tmpQueue);

		for(int i=0; i < sectorsToRead.size(); i++) {
			if(start > tmpQueue[i]) { 
				index = i; 
				break;  
			}
		}
		for(int i = index; i >= 0; i--) {
			System.out.print(" --> " + tmpQueue[i]);
		}
		System.out.print("0 --> ");
		for(int i = index+1; i < sectorsToRead.size(); i++) {
			System.out.print("--> " + tmpQueue[i]);
		}
		
		// seekTime = start + max;
		System.out.println("---SCAN ALGORITHM---");
    System.out.println("\nmovement of total cylinders: " + seekTime);

    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "SCAN");
	}

	public DiscAlgorithmResults cscanAlgorithm() {
    
    return new DiscAlgorithmResults(seekTime, sectorsToRead.size(), "CSCAN");
	}

	// --------------------------------------------------------------------
	public int findShortestSeek(int[] tmpQueue, int start) {
		int min = 999999; // large initial value to check for min
		int index = -1;

		for(int i = 0; i < tmpQueue.length; i++) {
			if(tmpQueue[i] != -1) {
				distance = Math.abs(start - tmpQueue[i]);
				if(min > distance) {
					min = distance;
					index = i;
				}
			}
		}

		return index;
	}

	public int clookSort(int[] tmpQueue, int start) {
		int temp;

		for(int i = 0; i < sectorsToRead.size() - 1; i++) {
			for(int j = 0; j < sectorsToRead.size() - 1; j++) {
				if(tmpQueue[j] > tmpQueue[j+1]) {
					temp = tmpQueue[j];
					tmpQueue[j] = tmpQueue[j+1];
					tmpQueue[j+1] = temp;
				}
			}
		}

		for(int i = 0; i < sectorsToRead.size(); i++) {
			if(tmpQueue[i] > start) {
				return i;
			}
		}

		return sectorsToRead.size();
	}

	public void sortQueue(int[] tmpQueue) {
		int temp;

		for(int i = 0; i < sectorsToRead.size(); i++) {
			for(int j = i; j < sectorsToRead.size(); j++) {
				if(tmpQueue[i] > tmpQueue[j]) {
					temp = tmpQueue[i];
					tmpQueue[i] = tmpQueue[j];
					tmpQueue[j] = temp;
				}
			}
		}
	}
}