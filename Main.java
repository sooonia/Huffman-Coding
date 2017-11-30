import java.io.IOException;

/**
 * Created by Sonia on 11/28/2017.
 */
public class Main {

    public static void main(String[] args) {
        Huffman huff = new Huffman();
        try {
            huff.compress("small.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            huff.decompress("small.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
