package LL;

import java.util.*;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  21:31
 */
public class Follow {
    private HashMap<String, ArrayList<String>> follow;
    private HashMap<String, ArrayList<String>> first;
    private ArrayList<String> nonTerminal;
    private ArrayList<String> terminal;
    private ArrayList<String> grammer;
    private HashMap<String, ArrayList<String>> expressionMap;
    private String start;
    private Map<String, Integer> status;    // 存储在查找FOLLOW集合时非终结符的状态
    public void getFollow(HashMap<String, ArrayList<String>> follow,
                          HashMap<String, ArrayList<String>> first,
                          ArrayList<String> nonTerminal,
                          ArrayList<String> terminal,
                          ArrayList<String> grammer,
                          HashMap<String, ArrayList<String>> expressionMap,
                          String start){
        this.first = first;
        this.follow = follow;
        this.nonTerminal = nonTerminal;
        this.terminal =terminal;
        this.grammer = grammer;
        this.expressionMap = expressionMap;
        this.start = start;
        status = new HashMap<>();

        for (String Vn : nonTerminal) {
            // 初始化非终结符的状态
            // 状态1表示还没找过
            // 状态2表示正在查找
            // 状态3表示已经结束查找
            status.put(Vn, 1);
            follow.put(Vn, new ArrayList<>());
            }
        ArrayList<String> startFollow = new ArrayList<>();
        startFollow.add("#");
        follow.put(start, startFollow);
        System.out.println("follow("+start+"):"+follow.get(start));

        for (String Vn : nonTerminal){
            // 如果已经查找完，就跳过
            if (status.get(Vn) == 3){
                continue;
            }
            calculate(Vn);
//            System.out.println("当前非终结符为："+Vn+"follow：" +follow.get(Vn));
        }

        HashMap<String, ArrayList<String>> follow_2 = follow;
//        follow.clear();
        for(String vn : nonTerminal){
            ArrayList<String> temp = getListSingle(follow_2.get(vn));
            follow.put(vn,temp);
        }
    }

    // 将某个符号集加入到某个非终结符的FOLLOW集合中
    private void addCharsToFOLLOW(ArrayList<String> characterSet, String nonEndChar){
        ArrayList<String> nonEndCharFollow = follow.get(nonEndChar);
        // 加入前先判断有没有在映射中了
        if (nonEndCharFollow != null){
            nonEndCharFollow.addAll(characterSet);
        } else{
            nonEndCharFollow = new ArrayList<>();
            nonEndCharFollow.addAll(characterSet);
            follow.put(nonEndChar, nonEndCharFollow);
        }
    }
    // 将某个符号加入到某个非终结符的FOLLOW集合中
    private void addCharToFOLLOW(String character, String nonEndChar){
        ArrayList<String> nonEndCharFollow = follow.get(nonEndChar);
        // 加入前先判断有没有在映射中了
        if (nonEndCharFollow != null){
            nonEndCharFollow.add(character);
        } else{
            nonEndCharFollow = new ArrayList<>();
            nonEndCharFollow.add(character);
            follow.put(nonEndChar, nonEndCharFollow);
        }
    }

    private ArrayList<String> calculate(String Vn){
        // 首先置当前查找的非终结符的状态为正在查找
        status.put(Vn, 2);

        // 在产生式中搜索所有非终结符出现的位置
        for (String character : nonTerminal){
            ArrayList<String> rightItems = expressionMap.get(character);

            RightItemLoop:for (String item : rightItems){
                // 获取符号列表
                List<String> cList = disassemble(item);

                // 接下来，搜索当前查找的非终结符的位置
                for (int i=0; i<cList.size(); i++){
                    String nonEndChar = cList.get(i);
                    if (nonEndChar.equals(Vn)){
                        // 判断是否处于最右的位置
                        if (i < cList.size() - 1){
                            // 如果没在最右边的位置
                            // 下面循环判断后一个符号是否是非终结符
                            for (int j=i+1; j<cList.size(); j++){
                                String nextChar = cList.get(j);
                                if (nonTerminal.contains(nextChar)){
                                    // 如果是非终结符，查看其FIRST集是否包含空串
                                    ArrayList<String> nextFirst = first.get(nextChar);
                                    // 如果包含空串，并且此时这个符号是最后一个符号
                                    // 就要将其FIRST除去空串的集合加入FOLLOW集，且左部的FOLLOW集加入FOLLOW集
                                    if (nextFirst.contains("ε")){
                                        // 判断是否是最后一个符号
                                        if (j == cList.size() - 1){
                                            // 这里首先判断一下要递归查找的非终结符的状态
                                            // 如果为正在查找，就会陷入死循环
                                            // 所以要略过这一条产生式
                                            // 在略过产生式之前，因为直接略过会遗漏掉之前正在查找的非终结符的FOLLOW集中的元素，所以要加上
                                            if (status.get(character) == 2){
                                                ArrayList<String> fol = follow.get(character);
                                                if (fol.size() != 0){
                                                    addCharsToFOLLOW(fol, Vn);
                                                }
                                                continue RightItemLoop;
                                            }
                                            ArrayList<String> leftFOLLOW = calculate(character);
                                            ArrayList<String> nextFirstExceptNULL = new ArrayList<>(nextFirst);
                                            nextFirstExceptNULL.remove("ε");
                                            addCharsToFOLLOW(leftFOLLOW, Vn);
                                            addCharsToFOLLOW(nextFirstExceptNULL, Vn);
                                        } else{
                                            // 如果不是最后一个符号，将FIRST集合加入
                                            ArrayList<String> nextFirstExceptNULL = new ArrayList<>(nextFirst);
                                            nextFirstExceptNULL.remove("ε");
                                            addCharsToFOLLOW(nextFirstExceptNULL, Vn);
                                        }
                                    } else{
                                        // 如果不包含空串加入FIRST之后跳出循环
                                        addCharsToFOLLOW(nextFirst, Vn);
                                        break;
                                    }
                                } else{
                                    // 如果不是非终结符，把此符号加入到当前查找的非终结符的FOLLOW集中
                                    addCharToFOLLOW(nextChar, nonEndChar);
                                    break;
                                }
                            }
                        }
                        // 如果在最右边，将FOLLOW（左部）加入到当前非终结符的FOLLOW集合
                        else{
                            // 这里首先判断一下要递归查找的非终结符的状态
                            // 如果为正在查找，就会陷入死循环
                            // 所以要略过这一条产生式
                            // 在略过产生式之前，因为直接略过会遗漏掉之前正在查找的非终结符的FOLLOW集中的元素，所以要加上
                            if (status.get(character) == 2){
                                ArrayList<String> fol = follow.get(character);
                                if (fol.size() != 0){
                                    addCharsToFOLLOW(fol, Vn);
                                }
                                continue RightItemLoop;
                            }
                            ArrayList<String> leftFOLLOW = calculate(character);
                            addCharsToFOLLOW(leftFOLLOW, Vn);
                        }
                    }
                }
            }
        }
        // 如果return，说明已经查找完
        status.put(Vn, 3);
        return follow.get(Vn);
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
    private ArrayList<String> getListSingle(ArrayList arr){
        ArrayList list = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()){
            Object obj = it.next();
            if(!list.contains(obj)){
                list.add(obj);
            }
        }
        return list;
    }
}
