import LL.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  20:04
 */
public class MianWindow {
    private JTextArea textArea_input;
    private JTextArea textArea_first;
    private JTextArea textArea_result;
    private JTextArea textArea_follow;
    private JButton button1;
    private JPanel panel1;
    private JTextField textField1;

    private ArrayList<String> grammer = new ArrayList<String>();                         //输入文法
    private ArrayList<String> nonTerminal = new ArrayList<String>();                     //非终结符
    private ArrayList<String> terminal = new ArrayList<String>();                        //终结符
    private String start;                                                           //开始符号
    private HashMap<String, ArrayList<String>> first = new HashMap<String, ArrayList<String>>();            //first集
    private HashMap<String, ArrayList<String>> follow = new HashMap<String, ArrayList<String>>();           //follow集
    private HashMap<String, ArrayList<String>> select = new HashMap<String, ArrayList<String>>();           //select集合
    private String[][] analyze_Table;
    private HashMap<String, ArrayList<String>> expressionMap = new HashMap<String, ArrayList<String>>();    //表达式集和

    public MianWindow(){
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Input input = new Input();
                input.readGrammer(textArea_input,grammer);
                input.spiltToNonterminal(grammer,nonTerminal);
                input.spiltToTerminal(grammer,terminal,nonTerminal);
                start = input.getStart(grammer,nonTerminal);


                LL_Pretreatment llPretreatment = new LL_Pretreatment();
                llPretreatment.initExpressionMaps(grammer,expressionMap);


                First first_ = new First();
                first_.getFirst(first,nonTerminal,terminal,expressionMap);
                Follow follow_ = new Follow();
                follow_.getFollow(follow,first,nonTerminal,terminal,grammer,expressionMap,start);
                Select select_ = new Select();
                select_.getSelect(grammer,first,follow,select,expressionMap);
                IsLL1 isLL1 = new IsLL1();
                if(isLL1.isLL1(select)){
                    textField1.setText("此文法为LL1文法");
                    AnalyzeTable analyzeTable = new AnalyzeTable();
                    analyze_Table = analyzeTable.getAnalyzeTable(terminal,nonTerminal,select);
                    String words = "";
                    for(int i=0;i<nonTerminal.size()+1;i++){
                        for(int j =0;j<terminal.size()+1;j++){
                            words = words + analyze_Table[i][j] + "\t";
                        }
                        words = words + "\n";
                    }
                    textArea_result.setText(words);
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
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MianWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);




    }

}
