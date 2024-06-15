package funnyJsonExplorer.renderer;

import java.util.ArrayList;
import java.util.List;

import com.sun.source.tree.Tree;
import funnyJsonExplorer.composite.*;
import funnyJsonExplorer.iterator.TreeIterator;
import funnyJsonExplorer.tree.JsonTree;

public class RectangleStyleRenderer implements Renderer {
    final private String FirstNodeBegin = "┌─";
    final private String FirstNodeEnd = "┐";
    final private String MiddleNodeBegin = "├─";
    final private String MiddleNodeEnd = "┤";
    final private String LastNodeDirectBegin = "└─";
    final private String LastNodeIndirectBegin = "┴─";
    final private String LastNodeEnd = "┘";
    final private String LastLineBegin = "└──";
    final private String LastLinePadding = "───";
    final private String Vertical = "│  ";
    final private String Padding = "─";
    private String InnerNodeIcon;
    private String LeafNodeIcon;

    final private int guideLineLength = LastLineBegin.length();
    private int maxLength = 0;
    private int lineLength = 40;
    private int total_line = 0;

    // render the tree with 2 pass: first pass to calculate the max length of the node name and the total line number
    //                              second pass to render the tree
    public void render(JsonTree tree) {
        this.InnerNodeIcon = tree.getInnerNodeIcon();
        this.LeafNodeIcon = tree.getLeafNodeIcon();
        List<String> prefix = new ArrayList<>();
        // first pass
        calcMaxLineLength(tree);

        TreeIterator iterator = tree.createDepthFirstIterator();
        TreeNode prev = iterator.next();
        int line = 0;
        // second pass
        while (iterator.hasNext()) {
            TreeNode node = iterator.next();
            updatePrefix(prefix, prev, node);
            if (line == 0) {
                renderFirstLine(node);
            } else if (line == total_line - 1) {
                renderLastLine(prefix, node);
            } else {
                renderMiddleLine(prefix, node);
            }
            line ++;
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
                prefix.add(Vertical);
            }
        } else if (node.getLevel() < prev.getLevel()) {
            for (int i = 0; i < prev.getLevel() - node.getLevel(); i++) {
                prefix.remove(prefix.size() - 1);
            }
        }
    }

    private void calcMaxLineLength(JsonTree tree) {
        TreeIterator iterator = tree.createDepthFirstIterator();
        iterator.next();
        while (iterator.hasNext()) {
            TreeNode node = iterator.next();
            if (node instanceof InnerNode) {
                maxLength = Math.max(maxLength, node.getName().length() + node.getLevel() * guideLineLength);
            }
            total_line++;
        }
        if (maxLength + 5 > lineLength) {
            lineLength = maxLength + 5;
        }
    }

    private void renderFirstLine(TreeNode node) {
        System.out.print(FirstNodeBegin);
        renderInfo(node);
        renderPadding(node.getLevel() * guideLineLength + node.getName().length());
        System.out.println(FirstNodeEnd);
    }

    private void renderMiddleLine(List<String> prefix, TreeNode node) {
        for (String pre : prefix) {
            System.out.print(pre);
        }
        System.out.print(MiddleNodeBegin);
        renderInfo(node);
        renderPadding(node.getLevel() * guideLineLength + node.getName().length());
        System.out.println(MiddleNodeEnd);
    }

    private void renderLastLine(List<String> prefix, TreeNode node) {
        if (prefix.isEmpty()) {
            System.out.print(LastNodeDirectBegin);
        } else {
            System.out.print(LastLineBegin);
            for (int i = 0; i < prefix.size() - 1; i++) {
                System.out.print(LastLinePadding);
            }
            System.out.print(LastNodeIndirectBegin);
        }
        renderInfo(node);
        renderPadding(node.getLevel() * guideLineLength + node.getName().length());
        System.out.println(LastNodeEnd);
    }

    private void renderInfo(TreeNode node) {
        if (node instanceof InnerNode) {
            System.out.print(InnerNodeIcon + node.getName());
        } else {
            System.out.print(LeafNodeIcon + node.getName());
        }
    }

    private void renderPadding(int length) {
        for (int i = 0; i < lineLength - length; i++) {
            System.out.print(Padding);
        }
    }
}
