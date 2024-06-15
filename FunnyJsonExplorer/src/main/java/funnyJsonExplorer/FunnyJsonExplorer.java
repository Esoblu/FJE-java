package funnyJsonExplorer;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

import funnyJsonExplorer.factory.*;
import funnyJsonExplorer.tree.JsonTree;
import funnyJsonExplorer.commandLineParser.CLP;
import funnyJsonExplorer.config.Config;
import jdk.jshell.spi.ExecutionControl;
import org.json.*;


public class FunnyJsonExplorer {
    private JsonTree tree;

    // 读取json文件
    public String readJsonFile(String filename) {
        StringBuilder json = new StringBuilder();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public FunnyJsonExplorer(String[] args) throws ExecutionControl.NotImplementedException {
        // 解析命令行参数
        Map<String, String> cmdMap = CLP.parseCommandLine(args);
        assert cmdMap != null && cmdMap.containsKey("path") && cmdMap.containsKey("iconFamily") && cmdMap.containsKey("style");
        // 从配置文件中获取iconFamily
        Map<String, String> iconFamily = Config.getIconFamilyFromConfig(cmdMap.get("iconFamily"));
        assert iconFamily != null && iconFamily.containsKey("InnerNodeIcon") && iconFamily.containsKey("LeafNodeIcon");
        // 读取json文件，转换为 map
        String jsonString = readJsonFile(cmdMap.get("path"));
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> map = jsonObject.toMap();
        // 根据style选择不同的工厂
        AbstractJsonFactory factory;
        if (cmdMap.get("style").equals("tree")) {
            factory = new TreeStyleJsonFactory();
        } else if (cmdMap.get("style").equals("rectangle")) {
            factory = new RectangleStyleJsonFactory();
        } else {
            throw new ExecutionControl.NotImplementedException("Invalid style");
        }
        // 构建树
        factory.buildTree(map, iconFamily.get("InnerNodeIcon"), iconFamily.get("LeafNodeIcon"));
        tree = factory.getTree();
    }

    // 渲染
    public void render() {
        tree.render();
    }

    public static void main(String[] args) throws ExecutionControl.NotImplementedException {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        new FunnyJsonExplorer(args).render();
    }
}
