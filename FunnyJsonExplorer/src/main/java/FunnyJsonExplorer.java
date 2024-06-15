import java.util.*;
import java.io.*;
import org.json.*;


class TreeNode {
    String name;
    int level;
    boolean isTail;
    TreeNode next;

    public TreeNode(String name, int level, boolean isTail) {
        this.name = name;
        this.level = level;
        this.isTail = isTail;
    }

    public void add(TreeNode node) {
        throw new UnsupportedOperationException();
    }

    public void setNext(TreeNode next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public boolean isTail() {
        return isTail;
    }

    public TreeNode getNext() {
        return next;
    }
}

class Leaf extends TreeNode {
    boolean parentIsTail;

    public Leaf(String name, int level, boolean isTail) {
        super(name, level, isTail);
    }

    public void setParentIsTail(boolean parentIsTail) {
        this.parentIsTail = parentIsTail;
    }

    public boolean isParentTail() {
        return parentIsTail;
    }
}

class InnerNode extends TreeNode {
    List<TreeNode> children = new ArrayList<>();

    public InnerNode(String name, int level, boolean isTail) {
        super(name, level, isTail);
    }

    public void add(TreeNode node) {
        children.add(node);
    }
}

class IconSet {
    String leafIcon;
    String innerNodeIcon;

    public IconSet(String leafIcon, String innerNodeIcon) {
        this.leafIcon = leafIcon;
        this.innerNodeIcon = innerNodeIcon;
    }

    public String getLeafIcon() {
        return leafIcon;
    }

    public String getInnerNodeIcon() {
        return innerNodeIcon;
    }
}

class JsonTree {
    private TreeNode root;
    private IconSet iconSet;

    public JsonTree(TreeNode root, IconSet iconSet) {
        this.root = root;
        this.iconSet = iconSet;
    }

    public TreeNode getRoot() {
        return root;
    }

    public String getLeafIcon() {
        return iconSet.getLeafIcon();
    }

    public String getInnerNodeIcon() {
        return iconSet.getInnerNodeIcon();
    }
}

interface Builder {
    void buildTree(Map<String, Object> map);
    void buildIconSet(String innerNodeIcon, String leafIcon);
    JsonTree getTree();
}

class JsonTreeBuilder implements Builder {
    private JsonTree tree;
    private TreeNode root;
    private IconSet iconSet;

    @Override
    public void buildIconSet(String innerNodeIcon, String leafIcon) {
        iconSet =  new IconSet(leafIcon, innerNodeIcon);
    }

    @Override
    public void buildTree(Map<String, Object> map) {
        if (map == null) {
            return ;
        }
        root = buildInnerNode("root", map, 0, true);
        buildNext();
    }

    private TreeNode buildInnerNode(String name, Map<String, Object> map, int level, boolean isTail) {
        InnerNode node = new InnerNode(name, level, isTail);
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry<String, Object> entry = it.next();
            TreeNode child = buildNode(entry.getKey(), entry.getValue(), level+1, i == map.size()-1);
            if (child instanceof Leaf) {
                ((Leaf) child).setParentIsTail(isTail);
            }
            node.add(child);
        }
        return node;
    }

    private TreeNode buildNode(String name, Object value, int level, boolean isTail) {
        if (value instanceof Map) {
            return buildInnerNode(name, (Map<String, Object>) value, level, isTail);
        } else if (value == null) {
            return new Leaf(name, level, isTail);
        } else {
            return new Leaf(name + ": " + value, level, isTail);
        }
    }

    private void buildNext() {
        final TreeNode[] prev = {null};
        class DFS {
            void dfs(TreeNode node) {
                if (prev[0] != null) {
                    prev[0].setNext(node);
                }
                prev[0] = node;
                if (node instanceof InnerNode innerNode) {
                    for (TreeNode child : innerNode.children) {
                        dfs(child);
                    }
                }
            }
        }
        new DFS().dfs(root);
    }

    @Override
    public JsonTree getTree() {
        if (root != null && iconSet != null) {
            tree = new JsonTree(root, iconSet);
        }
        return tree;
    }
}

class Director {
    Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public void build(Map<String, Object> map) {
        builder.buildTree(map);
        builder.buildIconSet(" ", " ");
    }

    public void build(Map<String, Object> map, String innerNodeIcon, String leafIcon) {
        builder.buildTree(map);
        builder.buildIconSet(innerNodeIcon, leafIcon);
    }

    public JsonTree getTree() {
        return builder.getTree();
    }
}

// 工厂方法
abstract class Viewer {
    protected JsonTree tree;
    protected ArrayList<Integer> skip = new ArrayList<>();
    protected TreeNode prev;

    public Viewer(JsonTree tree) {
        this.tree = tree;
        this.prev = tree.getRoot();
    }

    protected ArrayList<Integer> updatedSkip(TreeNode nextNode) {
        if (prev.getLevel() < nextNode.getLevel()) {
            if (nextNode.getLevel() > 1) {
                if (prev.isTail()) {
                    skip.add(0);
                } else {
                    skip.add(1);
                }
            }
        } else if (prev.getLevel() > nextNode.getLevel()) {
            for (int i = 0; i < prev.getLevel() - nextNode.getLevel(); i++) {
                skip.remove(skip.size()-1);
            }
        }
        prev = nextNode;
        return skip;
    }

    protected void reset() {
        skip.clear();
        prev = tree.getRoot();
    }

    public abstract void show();
}

class TreeShapeViewer extends Viewer {
    final private String nonTail = "├─";
    final private String tail = "└─";
    final private String vertical = "│  ";
    final private String space = "   ";

    public TreeShapeViewer(JsonTree tree) {
        super(tree);
    }

    @Override
    public void show() {
        if (prev != null) {
            TreeNode node = prev.getNext();
            while (node != null) {
                ArrayList<Integer> skip = updatedSkip(node);
                for (Integer i : skip) {
                    if (i == 1) {
                        System.out.print(vertical);
                    } else {
                        System.out.print(space);
                    }
                }
                System.out.print(node.isTail() ? tail : nonTail);
                System.out.print(node instanceof Leaf ? tree.getLeafIcon() : tree.getInnerNodeIcon());
                System.out.println(node.getName());
                node = node.getNext();
            }
            reset();
        }
    }
}

class RectShapeViewer extends Viewer {
    final private String firstNodeBegin = "┌─";
    final private String firstNodeEnd = "┐";
    final private String MiddleBegin = "├─";
    final private String MiddleEnd = "┤";
    final private String lastNodeDirectBegin = "└─";
    final private String lastNodeIndirectBegin = "┴─";
    final private String lastNodeEnd = "┘";
    final private String lastLineBegin = "└──";
    final private String lastLinePadding = "───";
    final private String vertical = "│  ";
    final private String padding = "─";
    private int maxLength;
    private int totalLine;

    public RectShapeViewer(JsonTree tree) {
        super(tree);
        calcMaxLength();
        maxLength += 10;
    }

    private void calcMaxLength() {
        TreeNode node = prev.getNext();
        while (node != null) {
            maxLength = Math.max(maxLength, node.getName().length() + node.getLevel() * 3);
            node = node.getNext();
            totalLine++;
        }
    }

    @Override
    public void show() {
        if (prev != null) {
            TreeNode node = prev.getNext();
            int line = 0;
            while (node != null) {
                if (line == 0) {
                    System.out.print(firstNodeBegin);
                } else if (line == totalLine - 1) {
                    if (node.getLevel() > 1) {
                        System.out.print(lastLineBegin);
                        System.out.print(lastLinePadding.repeat(node.getLevel() - 2));
                        System.out.print(lastNodeIndirectBegin);
                    } else {
                        System.out.print(lastNodeDirectBegin);
                    }
                } else {
                    if (node.getLevel() > 1)
                        System.out.print(vertical.repeat(node.getLevel()-1));
                    System.out.print(MiddleBegin);
                }
                System.out.print(node instanceof Leaf ? tree.getLeafIcon() : tree.getInnerNodeIcon());
                System.out.print(node.getName());
                System.out.print(" ");
                System.out.print(padding.repeat(maxLength - node.getName().length() - node.getLevel() * 3));
                if (line == 0) {
                    System.out.println(firstNodeEnd);
                } else if (line == totalLine - 1) {
                    System.out.println(lastNodeEnd);
                } else {
                    System.out.println(MiddleEnd);
                }
                line++;
                node = node.getNext();
            }
        }
    }
}

abstract class ViewerFactory {
    public abstract Viewer createViewer(JsonTree tree);
}

class TreeShapeViewerFactory extends ViewerFactory {
    @Override
    public Viewer createViewer(JsonTree tree) {
        return new TreeShapeViewer(tree);
    }
}

class RectShapeViewerFactory extends ViewerFactory {
    @Override
    public Viewer createViewer(JsonTree tree) {
        return new RectShapeViewer(tree);
    }
}

public class FunnyJsonExplorer {
    public String readJsonFile(String filename) {
        StringBuilder json = new StringBuilder();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return json.toString();
    }


    public FunnyJsonExplorer() {
        String jsonString = readJsonFile("data.json");
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> map = jsonObject.toMap();


        Builder builder = new JsonTreeBuilder();
        Director director = new Director(builder);
        director.build(map);
        // director.build(map, "♢", "♤");
        JsonTree tree = director.getTree();

        // ViewerFactory factory = new TreeShapeViewerFactory();
        ViewerFactory factory = new RectShapeViewerFactory();
        Viewer viewer = factory.createViewer(tree);
        viewer.show();
    }

    public static void main(String[] args) {
        new FunnyJsonExplorer();
    }
}
