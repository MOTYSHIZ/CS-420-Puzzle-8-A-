/**
 * Created by pc on 10/20/2016.
 */

import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Stack;

public class puzzle8 {
    static TreeNode root = new TreeNode();
    PriorityQueue frontier = new PriorityQueue();

    public static void main(String[] args){
        userOptions();
    }

    private static void userOptions(){
        Scanner kb = new Scanner(System.in);
        System.out.println("What would you like to do?" +
                "\n1)Input puzzle.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                userPuzzle();
                break;
            default:
                System.out.println("Invalid Input.");
                userOptions();
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
        printState(root.state);
        System.out.println("Nice, kid.");
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

}
