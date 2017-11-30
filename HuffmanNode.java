/**
 * Created by Sonia on 11/28/2017.
 */
public class HuffmanNode implements Comparable <HuffmanNode> {
    int weight;
    String value;
    HuffmanNode leftChild;
    HuffmanNode rightChild;
    HuffmanNode(int weight, String value){
        this.weight = weight;
        this.value = value;
    }

    public void setLeftChild(HuffmanNode child){
        this.leftChild=child;
    }

    public void setRightChild(HuffmanNode child){
        this.rightChild=child;
    }

    public HuffmanNode getLeftChild() {
        return leftChild;
    }

    public HuffmanNode getRightChild() {
        return rightChild;
    }

    public int getWeight(){
        return this.weight;
    }

    public String getValue() {
        return value;
    }

    public int compareTo(HuffmanNode o) {
        int ret = 0;
        if (this.weight < o.getWeight()){
            ret = -1;
        }
        else if(this.weight > o.getWeight()){
            ret = 1;
        }
        return ret;
    }
}
