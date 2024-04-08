package edu.univ.lab.utils;

import edu.univ.lab.config.Global;
import edu.univ.lab.entity.json.MethodJson;
import edu.univ.lab.service.ParseService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SootUtilsTest {

    @Test
    public void testUtils() {
        Global.downloadDependencies("org.apache.commons:commons-lang3:3.14.0");
        MethodJson mj = ParseService.parseHierarchy("org.apache.commons.lang3.text.StrBuilder.lastIndexOf(StrMatcher,int)");
//        MethodJson mj = ParseService.parseHierarchy("org.jfree.chart.axis.CyclicNumberAxis.refreshTicksHorizontal(Graphics2D,Rectangle2D,RectangleEdge)");
        JsonUtils.writeObjectIntoFile(mj);
    }

    @Test
    public void generateFunc() {
        Global.downloadDependencies("org.ocpsoft.prettytime:prettytime:5.0.7.Final");
        List<String> signs = new ArrayList<>();
        Global.SOOT_METHOD_MAP.forEach((k, v) -> {
            if (!k.contains("$") && !k.contains("()") && !k.contains("<clinit>") && !k.contains("<init>")) {
                if (v.getDeclaringClass().isPublic() && !v.getDeclaringClass().isAbstract() && !v.getDeclaringClass().isInterface() && v.isPublic()) {
                    if (k.startsWith("org.ocpsoft.prettytime"))
                        signs.add(k);
                }
            }
        });
        FileUtils.writeFile("sign_2", String.join("\n", signs), false);
    }

    @Test
    public void testUtils2() {
        FileUtils.writeFile("../sign", Arrays.stream(FileUtils.readFile("../sign").split("\n")).filter(n -> !n.startsWith("org.ocpsoft.prettytime.PrettyTime")).collect(Collectors.joining("\n")), false);
    }
}