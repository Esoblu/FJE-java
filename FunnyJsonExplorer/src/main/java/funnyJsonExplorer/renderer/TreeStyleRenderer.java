package funnyJsonExplorer.renderer;

import funnyJsonExplorer.composite.*;
import funnyJsonExplorer.iterator.TreeIterator;
import funnyJsonExplorer.tree.JsonTree;

import java.util.ArrayList;
import java.util.List;

public class TreeStyleRenderer implements Renderer {
    final private String Skip = "   ";
    final private String Vertical = "│  ";
    final private String Tail = "└─";
    final private String NonTail = "├─";
    private String InnerNodeIcon;
    private String LeafNodeIcon;

    // render the tree
    public void render(JsonTree tree) {
        this.InnerNodeIcon = tree.getInnerNodeIcon();
        this.LeafNodeIcon = tree.getLeafNodeIcon();
        List<String> prefix = new ArrayList<>();
        TreeIterator iterator = tree.createDepthFirstIterator();
        TreeNode prev = iterator.next();
        // iterate and render the tree
        while (iterator.hasNext()) {
            TreeNode node = iterator.next();
            updatePrefix(prefix, prev, node);
            renderLine(prefix, node);
            prev = node;
        }
    }

    // update the prefix
    // if the node is the child of the prev node and the prev node is not the tail of its parent, add vertical line
    // if the node is the child of the prev node and the prev node is the tail of its parent, add skip line
    // if the node level is less than the prev node level, remove the prefix until the level is equal to the node level
    private void updatePrefix(List<String> prefix, TreeNode prev, TreeNode node) {
        if (node.getLevel() > prev.getLevel()) { // don't need to update prefix if prev is the root
            if (prev.getLevel() != 0) {
                if (prev.isTail()) {
                    prefix.add(Skip);
                } else {
                    prefix.add(Vertical);
                }
            }
        } else if (node.getLevel() < prev.getLevel()) {
            for (int i = 0; i < prev.getLevel() - node.getLevel(); i++) {
                prefix.remove(prefix.size() - 1);
            }
        }
    }

    // actual rendering the line
    private void renderLine(List<String> prefix, TreeNode node) {
        for (String pre : prefix) {
            System.out.print(pre);
        }
        if (node.isTail()) {
            System.out.print(Tail);
        } else {
            System.out.print(NonTail);
        }
        if (node instanceof InnerNode) {
            System.out.println(InnerNodeIcon + node.getName());
        } else {
            System.out.println(LeafNodeIcon + node.getName());
        }
    }
}
