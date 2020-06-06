package LL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/6  20:28
 */
public class AnalyzeTable {
    private String[][] analyzeTable;
    private ArrayList<String> terminal;
    private ArrayList<String> nonTerminal;
    private HashMap<String,ArrayList<String>> select;
    public String[][] getAnalyzeTable(ArrayList<String> terminal,
                                ArrayList<String> nonTerminal,
                                HashMap<String, ArrayList<String>> select){
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
        this.select = select;
        Object[] terminalArray = terminal.toArray();
        Object[] nonterminalArray = nonTerminal.toArray();
        this.analyzeTable = new String[nonterminalArray.length+1][terminalArray.length+1];

        analyzeTable[0][0] = "Vn\\Vt";
        //初始化首行
        for(int i=0;i<terminalArray.length;i++){
            if (terminalArray[i].equals("ε")){
                terminalArray[i] = "#";
            }
            analyzeTable[0][i+1] = terminalArray[i] +"";
        }

        for (int i=0;i<nonterminalArray.length;i++){
            //初始化首列
            analyzeTable[i+1][0] = nonterminalArray[i]+"";
        }

        for(int i=1;i<nonterminalArray.length+1;i++){
            for(int j=1;j<terminalArray.length+1;j++){
                String str = findGrammar(analyzeTable[i][0],analyzeTable[0][j]);
                if(str != "")
                    analyzeTable[i][j] = str;
                else
                    analyzeTable[i][j] = "";

            }
        }
        return analyzeTable;
    }
    private String findGrammar(String Vn,String Vt){
        String result ="";
        Set<String> selectKey =select.keySet();
        for(String sk : selectKey){
            if(sk.split("->")[0].equals(Vn)){
                if(select.get(sk).contains(Vt)){
                    result = sk;
                    break;
                }
            }
        }
        return result;
    }

}
