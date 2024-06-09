package funnyJsonExplorer.factory;

import java.util.Map;

import funnyJsonExplorer.renderer.*;

public class RectangleStyleJsonFactory extends AbstractJsonFactory {
    public void buildTree(Map<String, Object> map, String InnerNodeIcon, String LeafNodeIcon) {
        buildRoot(map);
        buildIconSet(InnerNodeIcon, LeafNodeIcon);
        tree.setRenderer(new RectangleStyleRenderer());
    }
}
