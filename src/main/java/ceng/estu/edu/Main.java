package ceng.estu.edu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

class Main {

    static List<Node> terminated = new ArrayList<>();
    static List<Node> waiting = new ArrayList<>();
    static List<Node> started = new ArrayList<>();
    static List<Node> threads = new ArrayList<>();
    static Map<String, String> map = new HashMap<>();

    @Option(name = "-i")
    private String path = "input1.txt";

    public static void main(String[] args) {

        new Main().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
        }

        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("->");
                try {
                    map.put(split[0], split[1]);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    map.put(split[0], "");
                }
            }
            br.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String[] split = entry.getKey().split(",");

            keys.addAll(Arrays.asList(split));
            if (!entry.getValue().equals(""))
                keys.add(entry.getValue());
        }

        List<String> keysList = new ArrayList<>(keys);
        Collections.shuffle(keysList);

        for (String key : keysList) {
            Node node = new Node();
            node.setName(key);
            threads.add(node);
        }

        while (terminated.size() <= threads.size()) {
            for (Node k : threads) {
                k.perform();
            }
        }
    }

    public static Node findNode(String name, List<Node> threads) {
        for (Node n : threads) {
            if (n.getName().equals(name))
                return n;
        }
        return null;
    }

}



