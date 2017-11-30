import java.util.HashMap;

/**
 * Created by Sonia on 11/28/2017.
 */
public class HuffmanTree implements Comparable <HuffmanTree> {
    HuffmanNode root;

    public HuffmanTree(HuffmanNode root){
        this.root = root;
    }
    public HuffmanNode getRoot() {
        return root;
    }

    public HuffmanTree mergeTrees(HuffmanTree right){
        int weight = this.root.getWeight() + right.getRoot().getWeight();
        HuffmanNode root = new HuffmanNode(weight, "???");
        root.setLeftChild(this.getRoot());
        root.setRightChild(right.getRoot());
        return new HuffmanTree(root);
    }

    public int compareTo(HuffmanTree o) {
        int ret = 0;
        if (this.root.getWeight() < o.root.getWeight()){
            ret = -1;
        }
        else if(this.root.getWeight() > o.root.getWeight()){
            ret = 1;
        }
        return ret;
    }

    public void printAllCodes(HuffmanNode node, String code){
        if(!node.getValue().equals("???")){
            if(node.getValue().equals("\n")){
                System.out.println("\\n " + code);
            }
            else if(node.getValue().equals(" ")){
                System.out.println("\\s " + code);
            }
            else{System.out.println(node.getValue() + " " + code);}
        }
        else{
            printAllCodes(node.getLeftChild(), code+"0");
            printAllCodes(node.getRightChild(), code+"1");
        }

    }

    public HashMap getAllCodes(HashMap <String, String> map, HuffmanNode node, String code){

        if(!node.getValue().equals("???")){
            map.put(node.getValue(), code);
        }
        else{
            getAllCodes(map, node.getLeftChild(), code + "0");
            getAllCodes(map, node.getRightChild(), code + "1");
        }
        return map;
    }
}
