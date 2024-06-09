package funnyJsonExplorer.composite;

import java.util.ArrayList;
import java.util.List;

public class InnerNode extends TreeNode {
    List<TreeNode> children = new ArrayList<>();

    public InnerNode(String name, int level, boolean isTail) {
        super(name, level, isTail);
    }

    public void add(TreeNode node) {
        children.add(node);
    }

    public List<TreeNode> getChildren() {
        return children;
    }
}