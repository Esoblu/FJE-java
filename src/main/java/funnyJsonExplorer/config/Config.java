package funnyJsonExplorer.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Config {
    // 从配置文件中获取iconFamily
    public static Map<String, String> getIconFamilyFromConfig(String iconFamilyName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.yaml not found");
            }
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            Map<String, Object> obj = yaml.load(reader);
            if (obj.containsKey("IconFamily") && obj.get("IconFamily") instanceof Map) {
                Map<String, Object> iconFamily = (Map<String, Object>) obj.get("IconFamily");
                if (iconFamily.containsKey(iconFamilyName) && iconFamily.get(iconFamilyName) instanceof Map) {
                    Map<String, String> result = (Map<String, String>) iconFamily.get(iconFamilyName);
                    if (result.containsKey("InnerNodeIcon") && result.containsKey("LeafNodeIcon")) {
                        return result;
                    } else {
                        throw new Exception("Invalid IconFamily config, require InnerNodeIcon and LeafNodeIcon in key: " + iconFamilyName);
                    }
                } else {
                    throw new Exception("Invalid IconFamily config, missing key in IconFamily: " + iconFamilyName);
                }
            } else {
                throw new Exception("Invalid IconFamily config, missing key: IconFamily");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getIconFamilyFromConfig("awesome"));
    }
}
