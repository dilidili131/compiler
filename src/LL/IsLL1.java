package LL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/6  20:11
 */
public class IsLL1 {

    public boolean isLL1(HashMap<String, ArrayList<String>> select1){
        HashMap<String, ArrayList<String>> select =select1;
        boolean isIntersect = false;

        outer:for (String P1 : select.keySet()){

            String left1 = P1.split("->")[0];
            ArrayList<String> set1 = select.get(P1);
            Set<String> leftKeySet = new HashSet<>(select.keySet());
            leftKeySet.remove(P1);

            for (String P2 : leftKeySet){
                String left2 = P2.split("->")[0];
                // 如果两条产生式的左部相等的话
                if (left1.equals(left2)){
                    ArrayList<String> set2 = select.get(P2);
                    // 判断是否相交
                    if (judge(set1,set2)){
                        isIntersect = true;
                        break outer;
                    }
                }
            }
        }

        return isIntersect;
    }

    private boolean judge(ArrayList<String> set1,ArrayList<String> set2){
        boolean temp = true;
        for(String str :set1){
            if(set2.contains(str)){
                temp = false;
                break;
            }
        }
        for (String str :set2){
            if(set1.contains(str)){
                temp = false;
            }
        }

        return temp;
    }
}
