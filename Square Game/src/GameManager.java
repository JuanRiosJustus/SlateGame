
public class GameManager {
	
	
	public GameManager() {
	}

	public static int bombDetection (boolean[][] bombArray, int columnToScan, int rowToScan, boolean xAxis) {
	int bombsDetected = 0;
	if (xAxis) {
		// Checks from Top-to-bottom on a column
		for (int rowIndexValue = 0; rowIndexValue < rowToScan; rowIndexValue = rowIndexValue + 1 ) {
			if (bombArray[rowIndexValue][columnToScan] == true) {
				bombsDetected = bombsDetected + 1;
			}
		}
	} else {
		for (int columnIndexValue = 0; columnIndexValue < columnToScan; columnIndexValue = columnIndexValue + 1 ) {
			if (bombArray[rowToScan][columnIndexValue] == true) {
				bombsDetected = bombsDetected + 1;
			}
		}
	}
	return bombsDetected;
	}
	
	public static int pointDetection(int[][] scoreArray, int columnToScan, int rowToScan, boolean xAxis) {
		int pointsDetected = 0;

		if (xAxis) {
			//Checks from top-to-bottom on a column
			for (int rowIndexValue = 0; rowIndexValue < rowToScan; rowIndexValue = rowIndexValue + 1 ) {
				if (scoreArray[rowIndexValue][columnToScan] > 0) {
					pointsDetected = pointsDetected + scoreArray[rowIndexValue][columnToScan];
				}
			}
		} else {
			for (int columnIndexValue = 0; columnIndexValue < columnToScan; columnIndexValue = columnIndexValue + 1 ) {
				if (scoreArray[rowToScan][columnIndexValue] > 0) {
					pointsDetected = pointsDetected + scoreArray[rowToScan][columnIndexValue];
				}
			}
		}
		return pointsDetected;
	}
	
    public static int totalPoints(int[][] nums) {
        int total = 0;
        for (int row = 0; row < nums[0].length; row = row + 1) {
            for (int col = 0; col < nums[0].length; col = col + 1) {
                total = total + nums[row][col];
            }
        }
        total = total - 1;
        return total;
    }
    
    public static boolean[][] allFalseArray(int gridDimension) {
    	
    	boolean[][] noPoints = new boolean[gridDimension][gridDimension];
        for (int row = 0; row < gridDimension; row = row + 1) {
            for (int col = 0; col < gridDimension; col = col + 1) {
            	noPoints[row][col] = false;
            }
        }
        return noPoints;
    }
}
