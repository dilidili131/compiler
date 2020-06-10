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
    private HashMap<String, ArrayList<String>> expressionMap;
    public LL_Pretreatment(ArrayList<String> grammer, HashMap<String, ArrayList<String>> expressionMap){
        this.grammer = grammer;
        this.expressionMap = expressionMap;
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
}
