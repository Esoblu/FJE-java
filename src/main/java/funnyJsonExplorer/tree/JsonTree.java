package funnyJsonExplorer.tree;

import funnyJsonExplorer.composite.TreeNode;
import funnyJsonExplorer.renderer.Renderer;

public class JsonTree {
    private TreeNode root;
    private String InnerNodeIcon;
    private String LeafNodeIcon;
    private Renderer renderer;

    public JsonTree(TreeNode root) {
        this.root = root;
    }

    public void setIcon(String InnerNodeIcon, String LeafNodeIcon) {
        this.InnerNodeIcon = InnerNodeIcon;
        this.LeafNodeIcon = LeafNodeIcon;
    }

    public String getInnerNodeIcon() {
        return InnerNodeIcon;
    }

    public String getLeafNodeIcon() {
        return LeafNodeIcon;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void render() {
        renderer.render(this);
    }
}
