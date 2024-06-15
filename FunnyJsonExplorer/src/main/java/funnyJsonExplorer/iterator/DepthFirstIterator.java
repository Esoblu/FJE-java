package funnyJsonExplorer.iterator;

import funnyJsonExplorer.composite.*;

import java.util.Stack;

public class DepthFirstIterator implements TreeIterator{
    private Stack<TreeNode> stack;

    public DepthFirstIterator(TreeNode root) {
        stack = new Stack<>();
        stack.push(root);
    }
    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public TreeNode next() {
        if (!hasNext()) {
            throw new RuntimeException("No more elements");
        }
        TreeNode node = stack.pop();
        // 将子节点压入栈中，保持深度优先遍历顺序
        if (node instanceof InnerNode innerNode) {
            for (int i = innerNode.getChildren().size() - 1; i >= 0; i--) {
                stack.push(innerNode.getChildren().get(i));
            }
        }
        return node;
    }
}
