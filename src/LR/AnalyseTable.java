package LR;

import LL.AnalyzeTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/8  21:55
 */
public class AnalyseTable {
    private ArrayList<String> terminal;
    private ArrayList<String> nonTerminal;
    private ArrayList<ArrayList<Map<String, Object>>> projectUnions;
    private ArrayList<Map<String, Object>> analyseTableAction;
    private ArrayList<Map<String, Object>> analyseTableGoto;
    private ArrayList<Map<String, Object>> DFA;
    private ArrayList<Map<String, String>> production;
    private HashMap<String, ArrayList<String>> follow;

    public AnalyseTable(ArrayList<String> terminal,
                        ArrayList<String> nonTerminal,
                        ArrayList<ArrayList<Map<String, Object>>> projectUnions,
                        ArrayList<Map<String, Object>> analyseTableAction,
                        ArrayList<Map<String, Object>> analyseTableGoto,
                        ArrayList<Map<String, Object>> DFA,
                        ArrayList<Map<String, String>> production,
                        HashMap<String, ArrayList<String>> follow){
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
        this.projectUnions = projectUnions;
        this.analyseTableAction = analyseTableAction;
        this.analyseTableGoto = analyseTableGoto;
        this.DFA = DFA;
        this.production = production;
        this.follow = follow;
    }


    public boolean createLR0AnalyseTable() {
        int terminalCount = terminal.size();
        int nonterminalCount = nonTerminal.size();
        int stateCount = projectUnions.size();

        analyseTableAction.clear();
        analyseTableGoto.clear();

        for (int i = 0; i < stateCount; i++) {
            ArrayList<Map<String, Object>> begin = projectUnions.get(i);

            for (int j = 0; j < terminalCount; j++) {
                for (Map<String, Object> dfa : DFA) {
                    if (dfa.get("begin").equals(begin)
                            && dfa.get("edge").equals(terminal.get(j))) {
                        ArrayList<Map<String, Object>> end = (ArrayList<Map<String, Object>>) dfa.get("end");
                        int endPos = projectUnions.indexOf(end);

                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", terminal.get(j));
                        ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                        if (al == null)
                            al = new ArrayList<String>();
                        al.add("S" + endPos);
                        newMap.put("value", al);

                        analyseTableAction.add(newMap);
                    }
                }
            }

            for (Map<String, Object> map : begin) {
                Map<String, String> pro = (Map<String, String>) map.get("project");
                String right = pro.get("right");
                String left = pro.get("left");

                if (right.endsWith(".")) {

                    Map<String, String> prod = new HashMap<String, String>();
                    prod.put("right", right.replaceFirst("\\.", ""));
                    prod.put("left", left);

                    if (production.indexOf(prod) == 0) {
                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", "#");
                        ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                        if (al == null)
                            al = new ArrayList<String>();
                        al.add("acc");
                        newMap.put("value", al);

                        analyseTableAction.add(newMap);
                    } else {
                        int pos = production.indexOf(prod);

                        for (int k = 0; k < terminalCount + 1; k++) {
                            String ter;
                            if (k == terminalCount)
                                ter = "#";
                            else
                                ter = terminal.get(k);

                            Map<String, Object> newMap = null;
                            for (Map<String, Object> temp : analyseTableAction) {
                                if (temp.get("state").equals(i)
                                        && temp.get("terminal").equals(ter)) {
                                    newMap = temp;
                                    break;
                                }
                            }
                            if (newMap == null) {
                                newMap = new HashMap<String, Object>();
                                newMap.put("state", i);
                                newMap.put("terminal", ter);
                                analyseTableAction.add(newMap);
                            }
                            ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                            if (al == null)
                                al = new ArrayList<String>();
                            al.add("r" + (pos + 1));
                            newMap.put("value", al);
                        }
                    }
                }
            }

            for (int j = 1; j < nonterminalCount; j++) {
                for (Map<String, Object> dfa : DFA) {
                    if (dfa.get("begin").equals(begin)
                            && dfa.get("edge").equals(nonTerminal.get(j))) {
                        ArrayList<Map<String, Object>> end = (ArrayList<Map<String, Object>>) dfa.get("end");
                        int endPos = projectUnions.indexOf(end);

                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", nonTerminal.get(j));
                        newMap.put("value", endPos);

                        analyseTableGoto.add(newMap);
                    }
                }
            }
        }

        boolean isLR0 = true;
        for (Map<String, Object> map : analyseTableAction) {
            ArrayList<String> al = (ArrayList<String>) map.get("value");
            if (al.size() > 1) {
                isLR0 = false;
                break;
            }
        }

        return isLR0;
    }
    public boolean createSLR1AnalyseTable() {
        int terminalCount = terminal.size();
        int nonterminalCount = nonTerminal.size();
        int stateCount = projectUnions.size();

        analyseTableAction.clear();
        analyseTableGoto.clear();

        for (int i = 0; i < stateCount; i++) {
            ArrayList<Map<String, Object>> begin = projectUnions.get(i);

            for (int j = 0; j < terminalCount; j++) {
                for (Map<String, Object> dfa : DFA) {
                    if (dfa.get("begin").equals(begin)
                            && dfa.get("edge").equals(terminal.get(j))) {
                        ArrayList<Map<String, Object>> end = (ArrayList<Map<String, Object>>) dfa.get("end");
                        int endPos = projectUnions.indexOf(end);

                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", terminal.get(j));
                        ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                        if (al == null)
                            al = new ArrayList<String>();
                        al.add("S" + endPos);
                        newMap.put("value", al);

                        analyseTableAction.add(newMap);
                    }
                }
            }

            for (Map<String, Object> map : begin) {
                Map<String, String> pro = (Map<String, String>) map.get("project");
                String right = pro.get("right");
                String left = pro.get("left");

                if (right.endsWith(".")) {
                    Map<String, String> prod = new HashMap<String, String>();
                    prod.put("right", right.replaceFirst("\\.", ""));
                    prod.put("left", left);
                    if (production.indexOf(prod) == 0) {
                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", "#");
                        ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                        if (al == null)
                            al = new ArrayList<String>();
                        al.add("acc");
                        newMap.put("value", al);

                        analyseTableAction.add(newMap);
                    } else {
                        int pos = production.indexOf(prod);
                        ArrayList<String> fo = null;


                        fo = follow.get(left);
//                        for (Map<String, Object> f : follow) {
//                            if (f.get("nonTerminal").equals(left)) {
//                                fo = (ArrayList<String>) f.get("follow");
//                            }
//                        }

                        for (int k = 0; k < terminalCount + 1; k++) {
                            String ter;
                            if (k == terminalCount)
                                ter = "#";
                            else
                                ter = terminal.get(k);

                            if (fo.contains(ter)) {

                                Map<String, Object> newMap = null;
                                for (Map<String, Object> temp : analyseTableAction) {
                                    if (temp.get("state").equals(i)
                                            && temp.get("terminal").equals(ter))
                                        newMap = temp;
                                }
                                if (newMap == null) {
                                    newMap = new HashMap<String, Object>();
                                    newMap.put("state", i);
                                    newMap.put("terminal", ter);
                                    analyseTableAction.add(newMap);
                                }
                                ArrayList<String> al = (ArrayList<String>) newMap.get("value");
                                if (al == null)
                                    al = new ArrayList<String>();
                                al.add("r" + (pos + 1));
                                newMap.put("value", al);
                            }
                        }
                    }
                }
            }

            for (int j = 1; j < nonterminalCount; j++) {
                for (Map<String, Object> dfa : DFA) {
                    if (dfa.get("begin").equals(begin)
                            && dfa.get("edge").equals(nonTerminal.get(j))) {
                        ArrayList<Map<String, Object>> end = (ArrayList<Map<String, Object>>) dfa.get("end");
                        int endPos = projectUnions.indexOf(end);

                        Map<String, Object> newMap = new HashMap<String, Object>();
                        newMap.put("state", i);
                        newMap.put("terminal", nonTerminal.get(j));
                        newMap.put("value", endPos);

                        analyseTableGoto.add(newMap);
                    }
                }
            }
        }

        boolean isSLR1 = true;
        for (Map<String, Object> map : analyseTableAction) {
            ArrayList<String> al = (ArrayList<String>) map.get("value");
            if (al.size() > 1) {
                isSLR1 = false;
                break;
            }
        }

        return isSLR1;
    }


    public String printActionTable() {
        int terminalCount = terminal.size();
        int stateCount = projectUnions.size();
        String action = "";
        action = action + "S\\Vn\t";

        String str = "";
        for (int i = 0; i < terminalCount; i++)
            str = str + terminal.get(i)  + "\t";
        action = action + str + "#\t\n";
        for (int i = 0; i < stateCount; i++) {
            action = action + i + "\t";
            for (int j = 0; j < terminalCount + 1; j++) {
                String temp;
                if (j != terminalCount)
                    temp = terminal.get(j);
                else
                    temp = "#";

                int flag = -1;
                for (Map<String, Object> map : analyseTableAction) {
                    if (map.get("state").equals(i) && map.get("terminal").equals(temp)) {
                        ArrayList<String> al = (ArrayList<String>) map.get("value");
                        for (int k = 0; k < al.size(); k++) {
                            if (k == 0)
                                action = action + al.get(k) + "\t";
                            if (k > 0)
                                action = action + "," + al.get(k) + "\t";
                            if (k == al.size() - 1)
                                action = action + " ";
                        }
                        flag = 1;
                    }
                }

                if (flag == -1)
                    action = action + " "+ "\t";
            }
            action = action + "\n";
        }
        return action;

    }
    public String printGotoTable(){
        int nonterminalCount = nonTerminal.size();
        int stateCount = projectUnions.size();
        String goto_ = "";
        goto_ = goto_ + "S\\Vt\t";

        String str = "";
        for (int i = 1; i < nonterminalCount; i++)
            str = str + nonTerminal.get(i) + "\t";
        goto_ = goto_ + str + "\n";

        for (int i = 0; i < stateCount; i++) {
            goto_ = goto_ + i + "\t";
            for (int j = 1; j < nonterminalCount; j++) {
                String temp = nonTerminal.get(j);

                int flag = -1;
                for (Map<String, Object> map : analyseTableGoto) {
                    if (map.get("state").equals(i) && map.get("terminal").equals(temp)) {
                        goto_ = goto_ + map.get("value") + "\t";
                        flag = 1;
                    }
                }
                if (flag == -1)
                    goto_ = goto_ + " \t";
            }
            goto_ = goto_ + "\n";
        }
        return goto_;
    }


    public static void print(String s) {
        System.out.print(s);
    }


}
