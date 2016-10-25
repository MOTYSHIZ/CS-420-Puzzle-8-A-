/**
 * Created by Justin Ordonez
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Comparator;

public class TreeNode implements Comparator<TreeNode>{
    HashSet explored = new HashSet();

    protected ArrayList children = new ArrayList();
    protected int[][]state;
    protected int displacedTiles = 0;
    protected int distanceSum = 0;
    protected int depth = 0;
    protected boolean isGoal = false;
    protected boolean isRoot = false;
    protected static int searchCost = 0;
    protected TreeNode parent;

    TreeNode(){
        searchCost++;
    }

    TreeNode(boolean isRoot){
        this.isRoot = isRoot;
        searchCost++;
    }

    public void addChild(TreeNode child){
        children.add(child);
    }

    public String getStateString(){
        String stateString = "";

        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                stateString += state[i][j];
            }
        }

        return stateString;
    }

    public TreeNode clone(){
        TreeNode clone = new TreeNode();
        clone.state = arrayCopy(this.state);
        clone.parent = this;
        clone.explored = new HashSet(this.explored);
        clone.explored.add(this.getStateString());
        clone.depth = clone.explored.size();
        return clone;
    }

    public int heuristicOne(){
        displacedTiles = 0;

        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                if(state[i][j] != ((i*3)+j))displacedTiles++;
            }
        }

        if(displacedTiles == 0)isGoal = true;
        return displacedTiles + depth;
    }

    public int heuristicTwo(){
        distanceSum = 0;

        for(int i = 0; i < state.length ; i++){
            for(int j = 0; j < state[i].length; j++){
                if(state[i][j] != 0){
                    distanceSum += Math.abs((state[i][j] / 3) - i) + Math.abs((state[i][j] % 3) - j);
                }
            }
        }

        if(distanceSum == 0)isGoal = true;
        return distanceSum + depth;
    }

    public int[][] arrayCopy(int[][] array){
        int[][] copy = new int[3][3];

        for(int i=0; i < 3 ;i++){
            for(int j = 0; j < 3 ;j++){
                copy[i][j] = array[i][j];
            }
        }

        return copy;
    }

    @Override
    public int compare(TreeNode o1, TreeNode o2) {
        if(puzzle8.useHeuristicOne){
            return o1.heuristicOne() - o2.heuristicOne();
        }else{
            return o1.heuristicTwo() - o2.heuristicTwo();
        }
    }
}
