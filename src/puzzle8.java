/**
 * Created by Justin Ordonez
 */

import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class puzzle8 {
    static TreeNode current = new TreeNode(true);
    static Comparator<TreeNode> nodeComparator = new TreeNode();
    static PriorityQueue<TreeNode> frontier = new PriorityQueue(10, nodeComparator);
    static boolean useHeuristicOne = true;
    static boolean statisticMode = false;

    public static void main(String[] args) throws FileNotFoundException {
        userOptions();
    }

    private static void userOptions() throws FileNotFoundException {
        Scanner kb = new Scanner(System.in);
        System.out.println("What would you like to do?" +
                "\n1) Randomly generated puzzle." +
                "\n2) Input puzzle." +
                "\n3) Randomly generate 101 puzzles." +
                "\n4) Read 200 predefined test cases.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                heuristicChoose();
                randomPuzzle();
                break;
            case "2":
                userPuzzle();
                break;
            case "3":
                statisticMode = true;
                heuristicChoose();
                oneHundredAndOnePuzzles();
                break;
            case "4":
                statisticMode = true;
                heuristicChoose();
                twoHundredReadInPuzzles();
                break;
            default:
                System.out.println("Invalid Input.");
                userOptions();
        }
    }

    private static void heuristicChoose(){
        Scanner kb = new Scanner(System.in);
        System.out.println("Which heuristic would you lke to use?" +
                "\n1) The number of misplaced tiles." +
                "\n2) The sum of distances of the tiles from their goal positions.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                System.out.println("Using heuristic one.");
                break;
            case "2":
                System.out.println("Using heuristic two.");
                useHeuristicOne = false;
                break;
            default:
                System.out.println("Invalid Input.");
                heuristicChoose();
        }
    }

    static void randomPuzzle(){
        Stack puzzlePieces = new Stack();
        String puzzleString = "";

        for(int i= 0; i < 9; i++){
            puzzlePieces.add(Integer.toString(i));
        }
        Collections.shuffle(puzzlePieces);

        for(int i= 0; i < 9; i++){
            puzzleString += puzzlePieces.pop();
        }

        current.state = stringToState(puzzleString);


        if(!solvableCheck(current)){
            randomPuzzle();
        }else if(current.getStateString().contains("012345678")){
            System.out.println("I somehow managed to randomly generate an already-complete puzzle. C R A Z Y ! ! !");
            randomPuzzle();
        }else{
            System.out.println("\nGenerated Puzzle: ");
            printState(current);
            puzzleSolve();
        }
    }

    static void userPuzzle(){
        Scanner kb = new Scanner(System.in);
        Stack stringChecklist = new Stack();
        boolean stringAccepted = false;
        String input = "";

        while(stringAccepted == false){
            stringChecklist.clear();
            for(int i= 0; i < 9; i++){
                stringChecklist.add(i);
            }
            stringAccepted = true;

            System.out.println("Please input your puzzle as a 9-character string where" +
                    " each character is a number between 0-8, and there are no repeated characters." +
                    "\nFor example: \"012345678\"");
            input = kb.nextLine();

            if(input.length() != 9){
                System.out.println("String is not 9 characters long.");
                stringAccepted = false;
            }else if(input.contains("012345678")){
                System.out.println("That is an already completed puzzle!");
                stringAccepted = false;
            }else{
                for(int i = 0; i < 9; i++){
                    String checkCharacter = stringChecklist.pop().toString();
                    if(!input.contains(checkCharacter)){
                        stringAccepted = false;
                        System.out.println("String does not contain " + checkCharacter + ".");
                    }
                }
            }
        }

        heuristicChoose();
        current.state = stringToState(input);
        System.out.println("\nGenerated Puzzle: ");
        printState(current);
        System.out.println("Solving puzzle: ");
        puzzleSolve();
    }

    private static int[][] stringToState(String stateString){
        int[][] state = new int[3][3];

        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                state[i][j] = Character.getNumericValue(stateString.charAt((i*3)+j));
            }
        }

        return state;
    }

    private static void printState(TreeNode node){
        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                System.out.print(node.state[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void puzzleSolve(){
        Stack<TreeNode> pathStack = new Stack();
        int solutionDepth;
        int numOfMoves = 0;

        do {
            expandNode(current);
            addChildrenToFrontier(current);
            current = frontier.poll();
        } while(!current.isGoal && frontier.size() > 0);

        solutionDepth = current.depth;

        if(!statisticMode){
            while(!current.isRoot){
                pathStack.add(current);
                current = current.parent;
            }
            pathStack.add(current);

            while(pathStack.size() > 0){
                numOfMoves++;
                System.out.println("Move #" + numOfMoves + ":");
                printState(pathStack.pop());
            }
        }

        System.out.println("Search Cost: " + current.searchCost);
        System.out.println("Solution Depth: " + solutionDepth);
    }

    private static void expandNode(TreeNode nodeToExpand){
        int zeroCol = 0, zeroRow = 0;

        //locate the empty tile
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (nodeToExpand.state[i][j] == 0){
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }

        if(zeroCol > 0) {      //expand left movement
            TreeNode leftExpand = nodeToExpand.clone();
            leftExpand.state[zeroRow][zeroCol] = leftExpand.state[zeroRow][zeroCol - 1];
            leftExpand.state[zeroRow][zeroCol - 1] = 0;
            if(!nodeToExpand.explored.contains(leftExpand.getStateString()))nodeToExpand.addChild(leftExpand);
        }
        if(zeroCol < 2) {          //expand right movement
            TreeNode rightExpand = nodeToExpand.clone();
            rightExpand.state[zeroRow][zeroCol] = rightExpand.state[zeroRow][zeroCol + 1];
            rightExpand.state[zeroRow][zeroCol + 1] = 0;
            if(!nodeToExpand.explored.contains(rightExpand.getStateString()))nodeToExpand.addChild(rightExpand);
        }
        if(zeroRow > 0){            //expand up movement
            TreeNode upExpand = nodeToExpand.clone();
            upExpand.state[zeroRow][zeroCol] = upExpand.state[zeroRow-1][zeroCol];
            upExpand.state[zeroRow - 1][zeroCol] = 0;
            if(!nodeToExpand.explored.contains(upExpand.getStateString()))nodeToExpand.addChild(upExpand);
        }
        if(zeroRow < 2){            //expand down movement
            TreeNode downExpand = nodeToExpand.clone();
            downExpand.state[zeroRow][zeroCol] = downExpand.state[zeroRow+1][zeroCol];
            downExpand.state[zeroRow + 1][zeroCol] = 0;
            if(!nodeToExpand.explored.contains(downExpand.getStateString()))nodeToExpand.addChild(downExpand);
        }
    }

    private static void addChildrenToFrontier(TreeNode parentNode){
        for(Object x: parentNode.children){
            frontier.add((TreeNode)x);
        }
    }

    private static boolean solvableCheck(TreeNode node){
        int invertedTiles = 0;
        String stateString = node.getStateString();

        for(int i = 0; i < stateString.length(); i++){
            for(int j = i+1; j < stateString.length(); j++){
                if(Character.getNumericValue(stateString.charAt(j)) <
                        Character.getNumericValue(stateString.charAt(i))
                        && Character.getNumericValue(stateString.charAt(j)) != 0) {
                    invertedTiles++;
                }
            }
        }

        if(invertedTiles % 2 == 0) return true;
        else return false;
    }

    private static void oneHundredAndOnePuzzles(){
        int avgSearchCost = 0;
        long startTime, endTime;
        long avgExecTime = 0;

        for(int i=0 ; i <= 101; i++){
            startTime = System.currentTimeMillis();
            randomPuzzle();
            endTime = System.currentTimeMillis();
            avgExecTime += endTime - startTime;
            avgSearchCost += TreeNode.searchCost;
            TreeNode.searchCost = 0;
            frontier.clear();
            current = new TreeNode(true);
        }

        avgSearchCost = avgSearchCost/101;
        avgExecTime = avgExecTime/101;
        System.out.println("Average Search Cost for 101 cases: " + avgSearchCost
                    + "\nAverage Execution Time for 101 cases: " + avgExecTime + "ms");
    }

    private static void twoHundredReadInPuzzles() throws FileNotFoundException{
        Scanner in = new Scanner(new FileReader("200 Scrambled Puzzles.txt"));
        int depthLevel = 0;
        int amtInDepthLvl = 0;
        int avgSearchCost = 0;
        long avgExecTime = 0;
        long startTime, endTime;
        String currentLine;

        while(in.hasNext()){
            currentLine = in.next();
            if(currentLine.contains("Depth")){
                if(depthLevel != 0){
                    avgSearchCost = avgSearchCost/amtInDepthLvl;
                    avgExecTime = avgExecTime/amtInDepthLvl;
                    System.out.println("Average Search Cost for " + amtInDepthLvl + " cases in Depth " + depthLevel + ": "
                            + avgSearchCost
                            + "\nAverage Execution Time: " + avgExecTime + "ms");
                }

                depthLevel = in.nextInt();
                System.out.println("\nDepth: " + depthLevel);

                amtInDepthLvl = 0;
                avgSearchCost = 0;
                avgExecTime = 0;
            } else {
                startTime = System.currentTimeMillis();
                puzzleReader(currentLine);
                endTime = System.currentTimeMillis();
                avgExecTime += endTime - startTime;
                amtInDepthLvl++;
            }

            avgSearchCost += TreeNode.searchCost;
            TreeNode.searchCost = 0;
            frontier.clear();
            current = new TreeNode(true);
        }

        avgSearchCost = avgSearchCost/amtInDepthLvl;
        avgExecTime = avgExecTime/amtInDepthLvl;
        System.out.println("Average Search Cost for " + amtInDepthLvl + " cases in Depth " + depthLevel + ": "
                + avgSearchCost
                + "\nAverage Execution Time: " + avgExecTime + "ms");
    }

    private static void puzzleReader(String input){
        current.state = stringToState(input);
        System.out.println("\nGenerated Puzzle: ");
        printState(current);
        puzzleSolve();
    }
}
