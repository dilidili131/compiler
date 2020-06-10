package LL;

import java.util.*;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/5  0:34
 */
public class Select {
    public void getSelect(ArrayList<String> grammer,
                           HashMap<String, ArrayList<String>> first,
                           HashMap<String, ArrayList<String>> follow,
                           HashMap<String, ArrayList<String>> select,
                           HashMap<String, ArrayList<String>> expressionMap){

        // 为true的话，说明右部可以推导出空串
        boolean situation;
        // 遍历产生式
        for (String gra: grammer){
            String[] items = gra.split("->");
            String leftItem = items[0];
            ArrayList<String> rightItems = expressionMap.get(leftItem);

            // 遍历右部
            for (String item : rightItems){
                situation = false;
                // 分解串为符号
                List<String> characters = disassemble(item);
                ArrayList<String> f = new ArrayList<>();

                // 计算串的select集
                for (int i=0; i<characters.size(); i++){
                    String character = characters.get(i);
                    ArrayList<String> currentFIRST = first.get(character);
                    if (currentFIRST.contains("ε")){
                        // 如果最后一个符号也能推导出空串，说明右部可以推导出空串
                        if (i == characters.size()-1){
                            situation = true;
                            f.addAll(first.get(character));
                            f.remove("ε");
                        } else{
                            f.addAll(first.get(character));
                            f.remove("ε");
                        }
                    } else{
                        f.addAll(first.get(character));
                        break;
                    }
                }

                String p = leftItem + "->" + item;

                if (situation){
                    ArrayList<String> fo = follow.get(leftItem);
                    f.addAll(fo);
                    select.put(p, f);
                } else{
                    select.put(p, f);

                }

            }
//            System.out.println(select);
        }
    }
    private List<String> disassemble(String value){
        char c;
        List<String> cList = new ArrayList<>();

        // 将右部分解为一个个的符号
        for (int i=0; i<value.length(); i++){
            if (value.equals("ε")){
                cList.add(value);
                break;
            }
            if (i+1<value.length() && value.charAt(i+1) == '\''){
                cList.add(value.substring(i, i+2));
                i+=1;
            } else {
                cList.add(value.substring(i, i+1));
            }
        }

        return cList;
    }

}
