import stanford.karel.SuperKarel;

public class Homework extends SuperKarel {

    private int movesCounter;
    private int beepersCounter;

    private int widthMap = 1;
    private int heightMap = 1;
    private boolean canDivideMap = true;

    public void run() {
        calculateDimensions();
        divideGrid();
        printAndReset();
    }

    private void printAndReset(){
        System.out.println("Map width: "+widthMap); widthMap = 1;
        System.out.println("Map height: "+heightMap); heightMap = 1;
        System.out.println("Total moves: "+ movesCounter); movesCounter = 0;
        if(!canDivideMap) System.out.println("The map Can't be divided"); canDivideMap = true;
        System.out.println("Beepers used: "+ beepersCounter +"\n**************\n"); beepersCounter = 0;
    }

    private void calculateDimensions(){
        widthMap = calculateOneDimension();
        turnLeft();
        heightMap = calculateOneDimension();
        turnLeft();
    }

    private int calculateOneDimension(){
        int dimension = 1;
        while(frontIsClear()){
            moveCounted();
            dimension++;
        }
        return dimension;
    }

    private void moveCounted(){
        move();
        movesCounter++;
        System.out.println("Current moves: " + movesCounter);
    }

    private void divideGrid() {
        if(widthMap == heightMap && (widthMap % 2 == 0)){ // optimization for equal even dimensions
            if(widthMap == 2){
                putBeepersDiagonal();
            }
            else{
                putBeepersDiagonal();
                turnRight();
                while(frontIsClear()) moveCounted();
                turnAround();
                putBeepersDiagonal();
            }
        }
        else if(widthMap <= 2 || heightMap <= 2){ // if one of dimensions is 2 or 1
            if(!((widthMap <= 2) && (heightMap <= 2))){ // if only one dimension is 2 or less
                if(widthMap >= 3) divideLinearGrid(widthMap); // if the width has to be divided
                else divideLinearGrid(heightMap); // if the height has to be divided
            }
            else canDivideMap = false;
        }
        else{ // if both dimensions is 3 or more
            divideOneDimension(widthMap, false);
            divideOneDimension(heightMap, true);
        }
    }

    private void putBeepersDiagonal(){
        putBeeperCounted();
        while(frontIsClear()){
            moveCounted();
            if(leftIsClear()){
                turnLeft();
                moveCounted();
                putBeeperCounted();
                turnRight();
            }
        }
    }

    private void divideLinearGrid(int dimension){
        int dividedPartitionLength = calculatePartitionLength(dimension);
        if(dividedPartitionLength == 0){ // divide the grid into 2 halves
            divideLinearByHalf(dimension);
        }
        else{ // divide the grid into 3 or 4 partitions
            justifyLinearHeight();
            boolean onRight = true; // logical flag to help with moves optimization
            int currentCorner = 1;
            while(frontIsClear()){
                if(currentCorner == dividedPartitionLength+1){
                    putBeepersZigZag(onRight);
                    onRight = !onRight;
                    currentCorner = 0;
                }
                moveCounted();
                currentCorner++;
            }
        }
    }

    private void putBeepersZigZag(boolean onRight){
        if(onRight){
            turnLeft();
            putSingleLineBeepers();
            turnRight();
        } else {
            turnRight();
            putSingleLineBeepers();
            turnLeft();
        }
    }

    private void divideLinearByHalf(int dimension){
        justifyLinearHeight();
        divideOneDimension(dimension, true);
    }

    private void justifyLinearHeight(){
        if (heightMap > widthMap){
            while(frontIsClear()){
                moveCounted();
            }
            turnLeft();
        }
    }

    private int calculatePartitionLength(int dimension){
        if ((dimension - 3) % 4 == 0){
            return ((dimension - 3) / 4); // divide to four parts
        } else if ((dimension - 2) % 3 == 0) {
            return ((dimension - 2) / 3); // divide to three parts
        }
        else return 0;
    }

    private void divideOneDimension(int dimension, boolean isLastLine){
        if (dimension % 2 == 0){
            int startCorner = dimension / 2;
            putBeeperLines(startCorner, true, isLastLine);
        }
        else{
            int startCorner = dimension / 2 + 1;
            putBeeperLines(startCorner, false, isLastLine);
        }
    }

    private void putBeeperLines(int startCorner, boolean isDoubleLine, boolean isLastline){
        int currentCorner = 1;
        while(currentCorner < startCorner){
            moveCounted();
            currentCorner++;
        }
        turnLeft();
        if(isDoubleLine) putDoubleLineBeepers();
        else putSingleLineBeepers();
        if(!isLastline){
            turnLeft();
            while(frontIsClear()){
                moveCounted();
            }
            turnLeft();
        }
    }

    private void putDoubleLineBeepers(){
        putSingleLineBeepers();
        turnRight();
        moveCounted();
        turnRight();
        putSingleLineBeepers();
    }

    private void putSingleLineBeepers(){
        putBeeperCounted();
        while(frontIsClear()){
            moveCounted();
            putBeeperCounted();
        }
    }

    private void putBeeperCounted(){
        if(noBeepersPresent()){
            putBeeper();
            beepersCounter++;
        }
    }
}