package node.singlelinked;

public class Main {

    public static void main(String[] args) throws Exception {

        Node node = new Node(3);
        for (int i = 0; i < 4; i++) {
            node = node.addNode(node);
        }
        node = node.insertNodeByIndex(2, node);
    }
}
