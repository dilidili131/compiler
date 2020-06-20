import LL.*;
import LR.AnalyseTable;
import LR.DFA;
import LR.LRPretraetment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  20:04
 */
public class MianWindow {
    private JTextArea textArea_input;
    private JTextArea textArea_first;
    private JTextArea textArea_resultll;
    private JTextArea textArea_follow;
    private JButton button1;
    private JPanel panel1;
    private JTextField textField1;
    private JTextArea textArea1;
    private JTextArea textArea_resultlr_action;
    private JTextArea textArea_result_goto;
    private JScrollPane jScrollPane;

    private ArrayList<String> grammer = new ArrayList<String>();                         //输入文法
    private ArrayList<String> exGrammer = new ArrayList<>();                        //拓广文法
    private ArrayList<String> nonTerminal = new ArrayList<String>();                     //非终结符
    private ArrayList<String> terminal = new ArrayList<String>();                        //终结符
    private String start;                                                           //开始符号
    private HashMap<String, ArrayList<String>> first = new HashMap<String, ArrayList<String>>();            //first集
    private HashMap<String, ArrayList<String>> follow = new HashMap<String, ArrayList<String>>();           //follow集
    private HashMap<String, ArrayList<String>> select = new HashMap<String, ArrayList<String>>();           //select集合
    private String[][] analyze_Table;                                               //预测分析表
    private HashMap<String, ArrayList<String>> expressionMap = new HashMap<String, ArrayList<String>>();    //表达式集和
    private ArrayList<Map<String, String>> prodution = new ArrayList<Map<String, String>>();          //产生式左部右部
    private ArrayList<Map<String, String>> projects = new ArrayList<Map<String, String>>();           //
    private ArrayList<Map<String, Object>> DFA = new ArrayList<Map<String, Object>>();
    private ArrayList<ArrayList<Map<String, Object>>> projectUnions = new ArrayList<ArrayList<Map<String, Object>>>();
    private ArrayList<Map<String, Object>> analyseTableAction = new ArrayList<Map<String, Object>>();
    private ArrayList<Map<String, Object>> analyseTableGoto = new ArrayList<Map<String, Object>>();


    public MianWindow(){
        String text = "*************Attention!*************\n" +
                "1.Please use ε for empty string\n" +
                "2.Non-terminal characters are pure capital letters, do not appear in the form of A'\n" +
                "3.Input grammar should not appear left recursive";
       
        textArea1.setText(text);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Input input = new Input(textArea_input);
                grammer = input.readGrammer();
                nonTerminal = input.spiltToNonterminal();
                terminal = input.spiltToTerminal();
                start = input.getStart();
                System.out.println(terminal);
                System.out.println(nonTerminal);

                //LL预处理
                LL_Pretreatment llPretreatment = new LL_Pretreatment(grammer,exGrammer,expressionMap,start);
                expressionMap = llPretreatment.initExpressionMaps();
                llPretreatment.ex_Grammer();//拓广文法
                System.out.println("拓广后的文法："+exGrammer);


                //LR预处理
                LRPretraetment lrPretraetment = new LRPretraetment(exGrammer,start,prodution,projects);
                prodution = lrPretraetment.initProduction();
                projects = lrPretraetment.productionChangeToProject();

                First first_ = new First();
                first_.getFirst(first,nonTerminal,terminal,expressionMap);
                Follow follow_ = new Follow();
                follow_.getFollow(follow,first,nonTerminal,terminal,grammer,expressionMap,start);
                Select select_ = new Select();
                select_.getSelect(grammer,first,follow,select,expressionMap);

                DFA.clear();
                projectUnions.clear();
                DFA dfa = new DFA(projects,prodution,projectUnions,DFA,terminal);
                dfa.createDFA(null,null);
                AnalyseTable analyseTable = new AnalyseTable(terminal,nonTerminal,projectUnions,analyseTableAction,analyseTableGoto,DFA,prodution,follow);
                boolean isll0 = analyseTable.createLR0AnalyseTable();
                boolean isslr1 = analyseTable.createSLR1AnalyseTable();
                System.out.println("isll0:"+isll0);
                System.out.println("isslr1:"+isslr1);


                String result = "";
                IsLL1 isLL1 = new IsLL1();
                if(isLL1.isLL1(select)){
                    AnalyzeTable analyzeTable = new AnalyzeTable();
                    analyze_Table = analyzeTable.getAnalyzeTable(terminal,nonTerminal,select);
                    String words = "";
                    for(int i=0;i<nonTerminal.size()+1;i++){
                        for(int j =0;j<terminal.size()+1;j++){
                            words = words + analyze_Table[i][j] + "\t";
                        }
                        words = words + "\n";
                    }
                    textArea_resultll.setText(words);
                    result = "LL1";
                }
                if(isll0){
                    String action = analyseTable.printActionTable();
                    textArea_resultlr_action.setText(action);
                    String goto_ = analyseTable.printGotoTable();
                    textArea_result_goto.setText(goto_);
                    result = result + " LR0";
                } else {
                    if(isslr1){
                        String action = analyseTable.printActionTable();
                        textArea_resultlr_action.setText(action);
                        String goto_ = analyseTable.printGotoTable();
                        textArea_result_goto.setText(goto_);
                        result = result + " SLR1";
                    }else {
                        result = "";
                    }
                }
                if(result.equals("")){
                    textField1.setText("This grammar does not belong to any of LL1, LR0, SLR1");
                }else {
                    textField1.setText("This grammer is "+result+" grammer");
                }

                //输出First集
                String firstLine = "";
                for(String str : nonTerminal){
                    firstLine = firstLine + "First( " + str + " )" + "=" + first.get(str) + "\n";
                }
                textArea_first.setText(firstLine);
                //输出follow集
                String followLine = "";
                for(String str : nonTerminal){
                    followLine = followLine + "follow( " + str + " )" + "=" + follow.get(str) + "\n";
                }
                textArea_follow.setText(followLine);
                //输出select集
                for(String gra: grammer){
                    System.out.println("Select("+gra+")="+select.get(gra));
                }

            }

        });
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("1120172774-李传赫");
        frame.setContentPane(new MianWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
