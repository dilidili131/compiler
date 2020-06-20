package LL;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  21:33
 */
public class LL_Pretreatment {
    private ArrayList<String> grammer;
    private ArrayList<String> exGrammer;
    private HashMap<String, ArrayList<String>> expressionMap;
    private String start;
    public LL_Pretreatment(ArrayList<String> grammer, ArrayList<String> exGrammer,HashMap<String, ArrayList<String>> expressionMap,String start){
        this.grammer = grammer;
        this.exGrammer = exGrammer;
        this.expressionMap = expressionMap;
        this.start = start;
    }
    public HashMap<String, ArrayList<String>> initExpressionMaps(){
        expressionMap.clear();
        for (String gra : grammer) {
            String[] nvNtItem = gra.split("->");
            String charItemStr = nvNtItem[0];
            String charItemRightStr = nvNtItem[1];


            if (!expressionMap.containsKey(charItemStr)) {
                ArrayList<String> expArr = new ArrayList<String>();
                expArr.add(charItemRightStr);
                expressionMap.put(charItemStr, expArr);
            } else {
                ArrayList<String> expArr = expressionMap.get(charItemStr);
                expArr.add(charItemRightStr);
                expressionMap.put(charItemStr, expArr);
            }
        }
        return expressionMap;
    }
    public ArrayList<String> ex_Grammer(){
        exGrammer.clear();
        if (expressionMap.get(start).size() != 1){
            String str = start + "'->" +start;
            exGrammer.add(str);
            for(String gra : grammer){
                exGrammer.add(gra);
            }
        }else {
            for(String gra : grammer){
                exGrammer.add(gra);
            }
        }
        return exGrammer;
    }
}
