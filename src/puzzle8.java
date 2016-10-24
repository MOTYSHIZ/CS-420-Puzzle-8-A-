/**
 * Created by pc on 10/20/2016.
 */

import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Collections;

public class puzzle8 {
    static TreeNode root = new TreeNode();
    PriorityQueue frontier = new PriorityQueue();

    public static void main(String[] args){
        userOptions();
    }

    private static void userOptions(){
        Scanner kb = new Scanner(System.in);
        System.out.println("What would you like to do?" +
                "\n1) Randomly generated puzzle." +
                "\n2) Input puzzle.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                randomPuzzle();
                break;
            case "2":
                userPuzzle();
                break;
            default:
                System.out.println("Invalid Input.");
                userOptions();
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

        root.state = stringToState(puzzleString);
        System.out.println("Generated Puzzle: ");
        printState(root.state);
        System.out.println("Cool and nice, kid.");
        puzzleSolve();
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

        root.state = stringToState(input);
        System.out.println("Generated Puzzle: ");
        printState(root.state);
        System.out.println("Nice, kid.");
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

    private static void printState(int[][] state){
        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void puzzleSolve(){
        expandNode(root);
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
            zeroCol--;
            if(!nodeToExpand.explored.contains(leftExpand.getStateString()))nodeToExpand.addChild(leftExpand);
        }
        if(zeroCol < 2) {          //expand right movement
            TreeNode rightExpand = nodeToExpand.clone();
            rightExpand.state[zeroRow][zeroCol] = rightExpand.state[zeroRow][zeroCol + 1];
            rightExpand.state[zeroRow][zeroCol + 1] = 0;
            zeroCol++;
            if(!nodeToExpand.explored.contains(rightExpand.getStateString()))nodeToExpand.addChild(rightExpand);
        }
        if(zeroRow > 0){            //expand up movement
            TreeNode upExpand = nodeToExpand.clone();
            upExpand.state[zeroRow][zeroCol] = upExpand.state[zeroRow-1][zeroCol];
            upExpand.state[zeroRow - 1][zeroCol] = 0;
            zeroRow--;
            if(!nodeToExpand.explored.contains(upExpand.getStateString()))nodeToExpand.addChild(upExpand);
        }
        if(zeroRow < 2){            //expand down movement
            TreeNode downExpand = nodeToExpand.clone();
            downExpand.state[zeroRow][zeroCol] = downExpand.state[zeroRow+1][zeroCol];
            downExpand.state[zeroRow + 1][zeroCol] = 0;
            zeroRow++;
            if(!nodeToExpand.explored.contains(downExpand.getStateString()))nodeToExpand.addChild(downExpand);
        }
    }
}
