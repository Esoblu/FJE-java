package funnyJsonExplorer.renderer;

import funnyJsonExplorer.composite.*;
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
        for (TreeNode child : ((InnerNode) tree.getRoot()).getChildren()) {
            renderNode(child, prefix);
        }
    }

    // prepare data for rendering the inner node and leaf node
    private void renderNode(TreeNode node, List<String> prefix) {
        if (node instanceof InnerNode innerNode) {
            renderLine(prefix, innerNode);

            if (innerNode.isTail()) {
                prefix.add(Skip);
            } else {
                prefix.add(Vertical);
            }

            for (TreeNode child : innerNode.getChildren()) {
                renderNode(child, prefix);
            }
            prefix.remove(prefix.size()-1);
        } else {
            renderLine(prefix, node);
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
