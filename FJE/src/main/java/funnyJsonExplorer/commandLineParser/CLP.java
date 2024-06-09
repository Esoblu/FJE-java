package funnyJsonExplorer.commandLineParser;

import org.apache.commons.cli.*;
import java.util.*;

// 命令行解析
// 定义了三个参数，-f <json file path> [-s <style>] [-i <icon family>]
// -f: json文件路径
// -s: json explorer的样式，'tree'/'rectangle'
// -i: icon family
// -h: 帮助
// -v: 版本
public class CLP {
    static final private String[] styles = {"tree", "rectangle"};

    public static Map<String, String> parseCommandLine(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "show help");
        options.addOption("v", "version", false, "show version");
        options.addOption("f", "file", true, "json file path");
        options.addOption("s", "style", true, "style of the json explorer, 'tree'/'rectangle'");
        options.addOption("i", "iconFamily", true, "icon family of the json explorer");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("-f <json file path> [-s <style>] [-i <icon family>]", options);
            }
            if (cmd.hasOption("v")) {
                System.out.println("Version 1.0");
            }
            Map<String, String> map = new HashMap<>();
            if (cmd.hasOption("f")) {
                map.put("path", cmd.getOptionValue("f"));
            } else {
                map.put("path", "src/main/resources/data.json");
            }
            if (cmd.hasOption("s")) {
                if (!Arrays.asList(styles).contains(cmd.getOptionValue("s"))) {
                    System.out.println("Invalid style, use 'tree' or 'rectangle'");
                    return null;
                }
                map.put("style", cmd.getOptionValue("s"));
            } else {
                map.put("style", "tree");
            }
            if (cmd.hasOption("i")) {
                map.put("iconFamily", cmd.getOptionValue("i"));
            } else {
                map.put("iconFamily", "plain");
            }
            return map;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, String> map = parseCommandLine(args);
        if (map != null) {
            System.out.println(map);
        }
    }
}
