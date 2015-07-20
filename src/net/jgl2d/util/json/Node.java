package net.jgl2d.util.json;

import net.jgl2d.sys.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by peter on 7/19/15.
 */
public abstract class Node<T> {
    protected String body;
    public Node(String body) {
        this.body = body;
    }

    public abstract String toString();

    public abstract T build();

    private static abstract class SingleNode<E> extends Node<E> {

        public SingleNode(String body) {
            super(body);
        }
        public abstract E getValue();

        public E build() {
            return getValue();
        }
    }

    private static class FloatNode extends SingleNode<Float> {

        float value = 0f;
        public FloatNode(String body) {
            super(body);
            value = Float.parseFloat(body);
        }

        @Override
        public String toString() {
            return value + "";
        }

        @Override
        public Float getValue() {
            return value;
        }
    }

    private static class IntegerNode extends SingleNode<Integer> {

        int value = 0;
        public IntegerNode(String body) {
            super(body);
            value = Integer.parseInt(body);
        }

        @Override
        public String toString() {
            return value + "";
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    private static class StringNode extends SingleNode<String> {

        private String content;

        public StringNode(String body) {
            super(body);
            content = body.substring(1, body.length()-1);
        }

        @Override
        public String getValue() {
            return content;
        }

        @Override
        public String toString() {
            return body;
        }
    }

    public static class DictNode extends Node<HashMap<Object,Object>> {
        private HashMap<Node, Node> content = new HashMap<>();

        public DictNode(String body) {
            super(body);
            int i = 0;
            Node key = null;
            String subBody = "";
            while(i < body.length()) {
                char c = body.charAt(i);
                if(subBody.length() > 0 || (c != ' ' && c != ':' && c != ',')) {
                    subBody += c;
                }

                if(subBody.length() > 1 && subBody.startsWith("\"") && subBody.endsWith("\"")) {
                    if(key == null) {
                        key = new StringNode(subBody);
                        subBody = "";
                    } else {
                        content.put(key, new StringNode(subBody));
                        subBody = "";
                        key = null;
                    }
                } else if(subBody.startsWith("{L") && subBody.endsWith("}") && count(subBody, '{') == count(subBody, '}')) {
                    if(key == null) {
                        key = new ListNode(subBody.substring(2, subBody.length() - 1));
                        subBody = "";
                    } else {
                        content.put(key, new ListNode(subBody.substring(2, subBody.length() - 1)));
                        subBody = "";
                        key = null;
                    }
                } else if(subBody.startsWith("{D") && subBody.endsWith("}") && count(subBody, '{') == count(subBody, '}')) {
                    if(key == null) {
                        key = new DictNode(subBody.substring(2, subBody.length() - 1));
                        subBody = "";
                    } else {
                        content.put(key, new DictNode(subBody.substring(2, subBody.length() - 1)));
                        subBody = "";
                        key = null;
                    }
                } else if((i == body.length() - 1 || !isInt(body.charAt(i+1))) && subBody.length() > 0 && isInt(subBody.charAt(0)) && isInt(subBody.charAt(subBody.length()-1))) {
                    if(key == null) {
                        key = (subBody.contains(".") ? new FloatNode(subBody) : new IntegerNode(subBody));
                        subBody = "";
                    } else {
                        content.put(key, (subBody.contains(".") ? new FloatNode(subBody) : new IntegerNode(subBody)));
                        subBody = "";
                        key = null;
                    }
                }
                i++;
            }
        }

        @Override
        public String toString() {
            String result = "{D ";
            for(Node key : content.keySet()) {
                result += key.toString() + " : " + content.get(key).toString() + ", ";
            }
            return result + "}";
        }

        @Override
        public HashMap<Object, Object> build() {
            HashMap<Object, Object> build = new HashMap<>();
            for(Node key : content.keySet()) {
                build.put(key.build(), content.get(key).build());
            }
            return build;
        }
    }

    public static class ListNode extends Node<List<Object>> {
        private List<Node> content = new ArrayList<>();

        public ListNode(String body) {
            super(body);
            int i = 0;
            String subBody = "";
            while(i < body.length()) {
                char c = body.charAt(i);
                if(subBody.length() > 0 || (c != ' ' && c != ':' && c != ',')) {
                    subBody += c;
                }

                if(subBody.length() > 1 && subBody.startsWith("\"") && subBody.endsWith("\"")) {
                    content.add(new StringNode(subBody));
                    subBody = "";
                } else if(subBody.startsWith("{L") && subBody.endsWith("}") && count(subBody, '{') == count(subBody, '}')) {
                    content.add(new ListNode(subBody.substring(2, subBody.length() - 1)));
                    subBody = "";
                } else if(subBody.startsWith("{D") && subBody.endsWith("}") && count(subBody, '{') == count(subBody, '}')) {
                    content.add(new DictNode(subBody.substring(2, subBody.length() - 1)));
                    subBody = "";
                } else if((i == body.length() - 1 || !isInt(body.charAt(i+1))) && subBody.length() > 0 && isInt(subBody.charAt(0)) && isInt(subBody.charAt(subBody.length()-1))) {
                    if(subBody.contains(".")) {
                        content.add(new FloatNode(subBody));
                    } else {
                        content.add(new IntegerNode(subBody));
                    }
                    subBody = "";
                }
                i++;
            }
        }

        @Override
        public String toString() {
            String result = "{L ";
            for(Node node : content) {
                result += node.toString() + ", ";
            }
            return result + "}";
        }

        @Override
        public List<Object> build() {
            ArrayList<Object> build = new ArrayList<>();
            for(Node node : content) {
                build.add(node.build());
            }
            return build;
        }
    }

    private static int count(String string, char c) {
        int i = 0;
        for(int j = 0; j < string.length(); j++) {
            if(string.charAt(j) == c) i++;
        }
        return i;
    }

    private static boolean isInt(char c) {
        return "0123456789.".contains(c + "");
    }
}
