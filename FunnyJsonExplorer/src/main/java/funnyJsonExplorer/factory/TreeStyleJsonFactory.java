package funnyJsonExplorer.factory;

import funnyJsonExplorer.renderer.TreeStyleRenderer;

import java.util.Map;

public class TreeStyleJsonFactory extends AbstractJsonFactory {
    public void buildTree(Map<String, Object> map, String InnerNodeIcon, String LeafNodeIcon) {
        buildRoot(map);
        buildIconSet(InnerNodeIcon, LeafNodeIcon);
        tree.setRenderer(new TreeStyleRenderer());
    }
}
