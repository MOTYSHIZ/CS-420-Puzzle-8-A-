/**
 * Created by Justin Ordonez on 10/22/2016.
 */
import java.util.ArrayList;
import java.util.HashSet;

public class TreeNode {
    HashSet explored = new HashSet();
    private TreeNode parent;
    private ArrayList children = new ArrayList();
    protected int[][]state;

    public void setParent(TreeNode parent){
        this.parent = parent;
    }

    public void addChild(TreeNode child){
        children.add(child);
    }

    public void setState(int[][] state){
        this.state = state;
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
        clone.state = this.state.clone();
        clone.parent = this;
        clone.explored = (HashSet)this.explored.clone();
        clone.explored.add(this.getStateString());

        return clone;
    }
}
