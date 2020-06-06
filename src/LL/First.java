package LL;

import java.util.*;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  21:31
 */
public class First {
    private HashMap<String, ArrayList<String>> first;
    private ArrayList<String> nonTerminal;
    private ArrayList<String> terminal;
    private HashMap<String, ArrayList<String>> expressionMap;
    public void getFirst(HashMap<String, ArrayList<String>> first,
                         ArrayList<String> nonTerminal,
                         ArrayList<String> terminal,
                         HashMap<String, ArrayList<String>> expressionMap){
        this.first = first;
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
        this.expressionMap = expressionMap;
        first.clear();

        //终结符的first集就是本身
        for(String Vt : terminal){
            ArrayList<String> f = new ArrayList<>();
            f.add(Vt);
            first.put(Vt,f);
        }
        //非终结符
        for(String Vn : nonTerminal){
//            System.out.println("当前正在计算first  " +Vn+"-----------------------------------");
            first.put(Vn,calculateFirst(Vn));
        }

    }
    private ArrayList<String> calculateFirst(String Vn){
        ArrayList<String> f = new ArrayList<>();
        ArrayList<String> rightItems = expressionMap.get(Vn); //产生式右部集合
//        System.out.println("非终结符为："+Vn+" 产生式集合为："+ rightItems);
        for(String rightItem : rightItems){//遍历每一个右部
            List<String> cList = disassemble(rightItem);//分割右部为字符
            //如果长度等于1
            if(cList.size()==1&&nonTerminal.contains(cList)){
//                System.out.println("进入长度等于1情况");
                f.addAll(calculateFirst(cList.get(0)));
            }else{//如果有多个符号组成
                for(int i=0;i<cList.size();i++){
                    String character = cList.get(i);  //第i个字符
                    // 如果是终结符，直接把它加入first集合
                    if (terminal.contains(character)){
//                        System.out.println("我是一个终结符   "+character);
                        if(!f.contains(character))
                            f.add(character);
                        break;
                    } else {
                        // 如果是非终结符，就计算它的FIRST集
//                        System.out.println("我是一个非终结符   "+character);
                        ArrayList<String> cFirst = calculateFirst(character);
                        // 如果包含有空串，就要继续计算后一个符号的FIRST集
                        if (cFirst.contains("ε")){
                            // 如果此时是最后一个符号了
                            // 就要把空串页加入进first集合
                            if (i == cList.size() - 1){
                                if(!f.contains(cFirst))
                                    f.addAll(cFirst);
                            } else {
                                cFirst.remove("ε");
                                if(!f.contains(cFirst))
                                    f.addAll(cFirst);
                            }
                        } else {
                            // 如果不包含空串，就不需要继续计算了
                            if(!f.contains(cFirst))
                                f.addAll(cFirst);
                            break;
                        }
                    }
                }
            }

        }
        f = getListSingle(f);
        return f;
    }
    private ArrayList<String> getListSingle(List arr){
        List list = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()){
            Object obj = (Object)it.next();
            if(!list.contains(obj)){
                list.add(obj);
            }
        }
        return (ArrayList)list;
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
