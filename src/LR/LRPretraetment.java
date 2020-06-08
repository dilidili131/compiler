package LR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/8  17:50
 */
public class LRPretraetment {
    private ArrayList<String> grammar;
    private ArrayList<Map<String, String>> prodution;
    private ArrayList<Map<String, String>> projects;

    public LRPretraetment(ArrayList<String> grammar,
                          ArrayList<Map<String, String>> prodution,
                          ArrayList<Map<String, String>> projects){

        this.prodution = prodution;
        this.grammar = grammar;
        this.projects = projects;
    }
    //将文法每个产生式拆分为左部和右部的形式保存在Map中
    public ArrayList<Map<String, String>>  initProduction(){
        for(String gra : grammar){
            String[] temp = gra.split("->");
            Map<String,String> map = new HashMap<String, String>();
            map.put("left",temp[0]);
            if (temp[1].equals("ε")){
                temp[1] = "";
            }
            map.put("right",temp[1]);
            prodution.add(map);
        }
        return prodution;
    }
    //将产生式转换成项目集合
    public ArrayList<Map<String, String>> productionChangeToProject() {
        for (Map<String, String> map : prodution) {
            String left = map.get("left");
            String right = map.get("right");

            for (int i = 0; i < right.length() + 1; i++) {
                String newString;
                if (right.equals("$")) {
                    newString = ".";
                    i = 2;
                } else {
                    String ltmp = right.substring(0, i);
                    String rtmp = right.substring(i, right.length());

                    newString = ltmp + "." + rtmp;
                }
                Map<String, String> newMap = new HashMap<String, String>();
                newMap.put("left", left);
                newMap.put("right", newString);

                projects.add(newMap);
            }
        }
        return projects;
    }
}
