import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Sonia on 11/28/2017.
 */
public class Huffman {

    public static void compress(String textFileName) throws IOException {

        //Build Tree

        //Make map of all character counts (excludes \n, includes \s as ' ')
        BufferedReader in = new BufferedReader(new FileReader(textFileName));
        HashMap<Character,Integer> map = new HashMap<Character,Integer>();

        String line = in.readLine();
        //assumes each file ends with a new line
        int newLines = 0;
        while(line != null){
            for(int i = 0; i < line.length(); i++){
                char c = line.charAt(i);
                Integer count = map.get(c);
                if(count != null){
                    map.put(c,count + 1);
                }else{
                    map.put(c,1);
                }
            }
            newLines++;
            line = in.readLine();
        }

        //Convert charc and counts to Huffman nodes in priority queue
        PriorityQueueLinkedList <HuffmanTree> queue = new PriorityQueueLinkedList<HuffmanTree>();
        HuffmanNode newLinesNode = new HuffmanNode(newLines, "\n");
        HuffmanTree newLinesTree = new HuffmanTree(newLinesNode);
        queue.PriorityEnqueue(newLinesTree);
        Set entries = map.entrySet();
        Iterator entriesIterator = entries.iterator();
        PrintWriter writer = new PrintWriter(textFileName + "Frequencies.txt", "UTF-8");
        writer.println(map.size());
        writer.println("\\n " + newLines);
        //Put in queue and simultaneously build file
        while(entriesIterator.hasNext()){
            Map.Entry mapping = (Map.Entry) entriesIterator.next();
            if((Character) mapping.getKey() == ' '){
                writer.println("\\s " + mapping.getValue());
            }
            else{writer.println(mapping.getKey() + " " + mapping.getValue());}
            HuffmanNode node = new HuffmanNode((int) mapping.getValue(), Character.toString((Character) mapping.getKey()));
            HuffmanTree tree = new HuffmanTree(node);
            queue.PriorityEnqueue(tree);
        }
        writer.close();

        while(queue.list.size() > 1){
            HuffmanTree right = queue.PriorityDequeue();
            HuffmanTree left = queue.PriorityDequeue();
            HuffmanTree newTree = left.mergeTrees(right);
            queue.PriorityEnqueue(newTree);
        }

        HuffmanTree finalTree = queue.PriorityDequeue();

        //Use tree to encode Document

        //finalTree.printAllCodes(finalTree.getRoot(), "");
        HashMap <String, String> codeMap = new HashMap<String, String>();
        codeMap = finalTree.getAllCodes(codeMap, finalTree.getRoot(), "");
        BufferedReader in2 = new BufferedReader(new FileReader(textFileName));

        String encoded = "";
        line = in2.readLine();
        while(line != null){
            for (int i = 0; i < line.length(); i++) {
                encoded = encoded+codeMap.get(Character.toString(line.charAt(i)));
            }
            encoded=encoded+codeMap.get("\n");
            line = in2.readLine();
        }
        //System.out.println(encoded);
        int addZeros = encoded.length() % 8;
        char[] zeroChars = new char[addZeros];
        Arrays.fill(zeroChars, '0');
        String zeros = new String(zeroChars);
        encoded = encoded + zeros;

        int numBytes = encoded.length() / 8;
        byte [] arr = new byte[numBytes];
        int idx = 0;
        for (int i = 0; i < encoded.length()-8; i+=8) {
            byte b = 0x00;
            String sub = encoded.substring(i,i+8);
            for(int j = 0;j < 8;++j){
                if(sub.charAt(j) == '1'){
                    b = (byte)(b | 0b00000001);
                }
                if(j != 7) b = (byte)(b << 1);

            }
            arr[idx] = b;
            idx++;

        }

        FileOutputStream out = new FileOutputStream(textFileName+".compressed");
        out.write(arr);
        out.close();
        /*
        PrintWriter writer2 = new PrintWriter(textFileName+".compressed", "UTF-8");
        writer2.println(encoded);
        writer2.close();
        */

    }

    public static void decompress(String textFileName) throws IOException {
        //rebuild tree
        BufferedReader in = new BufferedReader(new FileReader(textFileName + "Frequencies.txt"));
        PriorityQueueLinkedList <HuffmanTree> queue = new PriorityQueueLinkedList<HuffmanTree>();

        String line = in.readLine();
        int numChars = Integer.parseInt(line);
        int newLines = Integer.parseInt(in.readLine().split(" ")[1]);
        HuffmanNode newLinesNode = new HuffmanNode(newLines, "\n");
        HuffmanTree newLinesTree = new HuffmanTree(newLinesNode);
        queue.PriorityEnqueue(newLinesTree);

        line = in.readLine();
        while(line != null){
            HuffmanNode node = new HuffmanNode(Integer.parseInt(line.split(" ")[1]),line.split(" ")[0]);
            HuffmanTree tree = new HuffmanTree(node);
            queue.PriorityEnqueue(tree);
            line = in.readLine();
        }
        while(queue.list.size() > 1){
            HuffmanTree right = queue.PriorityDequeue();
            HuffmanTree left = queue.PriorityDequeue();
            HuffmanTree newTree = left.mergeTrees(right);
            queue.PriorityEnqueue(newTree);
        }

        HuffmanTree finalTree = queue.PriorityDequeue();


        //decompress file
        Path fileLocation = Paths.get(textFileName+".compressed");
        byte[] arr = Files.readAllBytes(fileLocation);
        line = "";
        for (int j = 0; j < arr.length ; j++) {
            // decode the String back out of the byte:
            byte b = arr[j];
            for(int i = 0;i < 8;++i){
                if((b & 0b10000000) != 0){
                    line=line+"1";
                }else{
                    line = line+"0";
                }
                b = (byte)(b << 1);
            }
        }


        HuffmanNode current = finalTree.getRoot();
        String output = "";
        for (int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == '0'){
                current = current.getLeftChild();
                if(current.getLeftChild() == null){
                    if(current.getValue().equals("\\s")){
                        output = output+" ";
                    }
                    else{output = output+current.getValue();}
                    current = finalTree.getRoot();
                }
            }

            else{
                current = current.getRightChild();
                if(current.getLeftChild() == null){
                    if(current.getValue().equals("\\s")){
                        output = output+" ";
                    }
                    else{output = output+current.getValue();}
                    current = finalTree.getRoot();
                }
            }
        }
        PrintWriter writer = new PrintWriter(textFileName+"decompressed.txt", "UTF-8");
        writer.print(output);
        writer.close();
        //System.out.println(output);



    }
}
