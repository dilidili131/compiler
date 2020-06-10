package LR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/8  21:16
 */
public class DFA {
    private ArrayList<String> terminal;
    private ArrayList<Map<String, String>> projects;
    private ArrayList<Map<String, String>> production;
    private ArrayList<ArrayList<Map<String, Object>>> projectUnions;
    private ArrayList<Map<String, Object>> DFA;
    public DFA(ArrayList<Map<String, String>> projects,
               ArrayList<Map<String, String>> production,
               ArrayList<ArrayList<Map<String, Object>>> projectUnions,
               ArrayList<Map<String, Object>> DFA,
               ArrayList<String> terminal){
        this.projects = projects;
        this.terminal = terminal;
        this.production = production;
        this.projectUnions = projectUnions;
        this.DFA = DFA;
    }

    public ArrayList<Map<String, Object>> createDFA(
            ArrayList<Map<String, Object>> al,
            ArrayList<Map<String, Object>> parent) {
        ArrayList<Map<String, Object>> Union = new ArrayList<Map<String, Object>>();

        if (al == null) {
            al = new ArrayList<>();
            Map<String, Object> first = new HashMap<>();
            first.put("project", projects.get(0));
            first.put("pos", 1);

            al.add(first);
        }

        for (Map<String, Object> project : al) {
            Union.add(project);
        }

        int pos = 0;
        while (true) {
            if (pos == Union.size())
                break;
            Map<String, Object> m = Union.get(pos);
            pos++;
            Map<String, String> project = (Map<String, String>) m
                    .get("project");
            String right = project.get("right");
            if (right.endsWith("."))
                continue;
            int pointPos = right.indexOf(".");
            String nextCh = right.substring(pointPos + 1, pointPos + 2);

            if (terminal.contains(nextCh))
                continue;
            for (Map<String, String> p : production) {
                String pl = p.get("left");
                if (pl.equals(nextCh)) {
                    Map<String, String> newPro = new HashMap<String, String>();
                    newPro.put("left", pl);
                    if (p.get("right").equals("$"))
                        newPro.put("right", ".");
                    else
                        newPro.put("right", "." + p.get("right"));

                    Map<String, Object> newproj = new HashMap<String, Object>();

                    newproj.put("project", newPro);
                    newproj.put("pos",
                            production.indexOf(projectToProduction(newPro)));

                    if (!Union.contains(newproj))
                        Union.add(newproj);
                }
            }
        }

        ArrayList<Map<String, Object>> next = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> project : Union) {
            Map<String, String> pro = (Map<String, String>) project
                    .get("project");
            String right = pro.get("right");

            if (!right.endsWith(".")) {
                int pointpos = right.indexOf(".");
                String edge = right.substring(pointpos + 1, pointpos + 2);

                ArrayList<Map<String, Object>> newAl = null;
                for (Map<String, Object> map : next) {
                    if (map.get("edge").equals(edge)) {
                        newAl = (ArrayList<Map<String, Object>>) map
                                .get("value");
                        break;
                    }
                }

                if (newAl == null) {
                    Map<String, Object> newMap = new HashMap<String, Object>();
                    newAl = new ArrayList<>();

                    newMap.put("edge", edge);
                    newMap.put("value", newAl);

                    next.add(newMap);
                }

                Map<String, Object> nextPros = new HashMap<>();
                nextPros.put("project", projects.get(projects.indexOf(pro) + 1));
                nextPros.put("pos", project.get("pos"));
                newAl.add(nextPros);
            }
        }

        if (!projectUnions.contains(Union))
            projectUnions.add(Union);

        for (Map<String, Object> map : next) {
            Map<String, Object> node = new HashMap<String, Object>();
            node.put("begin", Union);
            node.put("edge", map.get("edge"));
            if (Union.equals(parent))
                return Union;
            ArrayList<Map<String, Object>> nextUnion = createDFA(
                    (ArrayList<Map<String, Object>>) map.get("value"), Union);
            node.put("end", nextUnion);

            if (!DFA.contains(node))
                DFA.add(node);
        }

        return Union;
    }
    //从项目反向生成产生式
    private Map<String, String> projectToProduction(Map<String, String> proj) {
        String left = proj.get("left");
        String right = proj.get("right");

        Map<String, String> map = new HashMap<String, String>();
        map.put("left", left);
        map.put("right", right.replaceFirst("\\.", ""));

        return map;
    }
}
