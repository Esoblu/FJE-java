package funnyJsonExplorer.factory;

import funnyJsonExplorer.composite.*;
import funnyJsonExplorer.tree.JsonTree;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractJsonFactory {
    JsonTree tree;

    abstract public void buildTree(Map<String, Object> map, String InnerNodeIcon, String LeafNodeIcon);

    protected void buildRoot(Map<String, Object> map) {
        if (map == null) {
            return ;
        }
        tree = new JsonTree( buildInnerNode("root", map, 0, true));
    }

    private TreeNode buildInnerNode(String name, Map<String, Object> map, int level, boolean isTail) {
        InnerNode node = new InnerNode(name, level, isTail);
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry<String, Object> entry = it.next();
            TreeNode child = buildNode(entry.getKey(), entry.getValue(), level+1, i == map.size()-1);
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

    public void buildIconSet(String InnerNodeIcon, String LeafNodeIcon) {
        tree.setIcon(InnerNodeIcon, LeafNodeIcon);
    }

    public JsonTree getTree() {
        return tree;
    }
}
