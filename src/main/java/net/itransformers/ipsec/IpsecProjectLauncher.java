package net.itransformers.ipsec;

import net.itransformers.topologyviewer.gui.TopologyManagerFrame;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by niau on 10/15/16.
 */
public class IpsecProjectLauncher {

    public static void main(String[] args) throws IOException {

            Map<String, String> params = new HashMap<String, String>();
            String key = null;
            for (String arg : args) {
                if (key == null && arg.startsWith("-")) {
                    key = arg;
                } else {
                    params.put(key, arg);
                    key = null;
                }
            }
            if (!params.containsKey("-t")) {
                params.put("-t", "directed");
            }

            String dirStr = params.get("-d");
            String urlStr = params.get("-u");
            if (dirStr != null && urlStr != null) {
                printUsage("Can not specify -d or -u options simultaneously");
                return;
            }
            if (dirStr != null) {
                File dir = new File(dirStr);
                if (!dir.exists()) {
                    System.out.println(String.format("The specified directory '%s' does not exists", dirStr));
                    return;
                }
            }
            GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
            System.setProperty("base.dir", new File(".").getAbsolutePath());
            ctx.load("classpath:rightClick/rightClick.xml");
            ctx.load("classpath:rightClickAPI/rightClickAPI.xml");
            ctx.load("classpath:xmlResourceManager/xmlResourceManagerFactory.xml");
            ctx.load("classpath:csvConnectionDetails/csvConnectionDetailsFactory.xml");
            ctx.load("classpath:topologyViewer/topologyViewer.xml");
            ctx.load("classpath:xmlTopologyViewerConfig/xmlTopologyViewerConfig.xml");
            ctx.refresh();
            TopologyManagerFrame frame = (TopologyManagerFrame) ctx.getBean("topologyManagerFrame");
            frame.setTitle("IpSec Manager");
            frame.init();


    }
    private static void printUsage(String msg) {
        System.out.println("Error: "+msg);
        System.out.println("Usage:   topoManager.bat [-t <directed|undirected>] -d <local_dir> -u <remote_url> -g <graphml_dir> -f <viewer_config]");
        System.out.println(
                "-d <local_dir>              # relative or absolute path to local dir with 'graphml-dir' and 'device-data' dirs.\n"
        );
    }
}
