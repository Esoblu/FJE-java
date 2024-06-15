package funnyJsonExplorer.composite;

public class TreeNode {
    private String name;
    private int level;
    private boolean isTail;

    public TreeNode(String name, int level, boolean isTail) {
        this.name = name;
        this.level = level;
        this.isTail = isTail;
    }

    public void add(TreeNode node) {
        throw new UnsupportedOperationException();
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

}
