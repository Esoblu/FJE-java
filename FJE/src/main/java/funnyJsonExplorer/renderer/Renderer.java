package funnyJsonExplorer.renderer;

import funnyJsonExplorer.tree.JsonTree;

// 策略模式
public interface Renderer {
    void render(JsonTree tree);
}
