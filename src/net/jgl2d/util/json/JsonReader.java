package net.jgl2d.util.json;

import java.io.*;

public class JsonReader {
    private String data;
    private Node.ListNode topNode;

    public JsonReader(File file) throws FileNotFoundException {
        FileInputStream fis;
        BufferedReader reader;

        fis = new FileInputStream(file);
        reader = new BufferedReader(new InputStreamReader(fis));
        data = "";
        String line;
        try {
            while((line = reader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        topNode = new Node.ListNode(data);
    }

    public Node.ListNode getTopNode() {
        return topNode;
    }
}