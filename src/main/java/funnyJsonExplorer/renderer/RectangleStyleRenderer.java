package funnyJsonExplorer.renderer;

import java.util.ArrayList;
import java.util.List;

import funnyJsonExplorer.composite.*;
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
    private List<String> buffer = new ArrayList<>();

    private int maxLength = 0;
    private int lineLength = 40;
    private int guildLineLength = Vertical.length();

    // render the tree with buffer
    public void render(JsonTree tree) {
        this.InnerNodeIcon = tree.getInnerNodeIcon();
        this.LeafNodeIcon = tree.getLeafNodeIcon();
        List<String> prefix = new ArrayList<>();
        for (TreeNode child : ((InnerNode) tree.getRoot()).getChildren()) {
            renderNode(child, prefix);
        }
        renderBuffer();
    }

    private void renderNode(TreeNode node, List<String> prefix) {
        // render the InnerNode
        if (node instanceof InnerNode innerNode) {
            renderLine(prefix, innerNode);

            prefix.add(Vertical);

            for (TreeNode child : innerNode.getChildren()) {
                renderNode(child, prefix);
            }
            prefix.remove(prefix.size()-1);
        } else {
            // render the LeafNode
            renderLine(prefix, node);
        }
    }

    // render the line with prefix, add the line to buffer
    private void renderLine(List<String> prefix, TreeNode node) {
        StringBuilder line = new StringBuilder();
        for (String pre : prefix) {
            line.append(pre);
        }
        line.append(MiddleNodeBegin);
        if (node instanceof InnerNode) {
            line.append(InnerNodeIcon);
        } else {
            line.append(LeafNodeIcon);
        }
        line.append(node.getName());
        maxLength = Math.max(maxLength, line.length());
        buffer.add(line.toString());
    }

    // render the buffer
    private void renderBuffer() {
        if (lineLength < maxLength) {
            lineLength = maxLength;
        }
        for (int i = 0; i < buffer.size(); i++) {
            if (i == 0) {
                // modify the first line
                buffer.set(i, FirstNodeBegin + buffer.get(i).substring(FirstNodeBegin.length()));
            } else if (i == buffer.size()-1) {
                // modify the last line
                // check if the last node is direct child

                int numGuildLine = 0;
                while (numGuildLine * (1 + guildLineLength) < buffer.get(i).length() &&
                        buffer.get(i).substring(numGuildLine * guildLineLength, numGuildLine * guildLineLength + Vertical.length()).equals(Vertical)) {
                    numGuildLine++;
                }
                String name = buffer.get(i).substring(numGuildLine * guildLineLength + MiddleNodeBegin.length() + InnerNodeIcon.length());
                if (numGuildLine == 0) {
                    buffer.set(i, LastNodeDirectBegin + name);
                } else {
                    buffer.set(i, LastLineBegin + LastLinePadding.repeat(numGuildLine-1)
                            + LastNodeIndirectBegin + LeafNodeIcon + name);
                }
            }
            System.out.print(buffer.get(i));
            System.out.print(Padding.repeat(lineLength-buffer.get(i).length()));
            if (i == 0) {
                System.out.println(FirstNodeEnd);
            } else if (i == buffer.size()-1) {
                System.out.println(LastNodeEnd);
            } else {
                System.out.println(MiddleNodeEnd);
            }
        }
    }
}
