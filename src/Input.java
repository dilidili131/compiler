import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @author: 李传赫
 * @time: 2020/6/4  20:11
 */
public class Input {
    private JTextArea textArea_input;
    private ArrayList<String> grammer;
    private ArrayList<String> nontermianl;
    private ArrayList<String> termianl;
    private String start;
    public Input(JTextArea textArea_input){
        this.textArea_input = textArea_input;
    }

    //将输入框中的内容保存
    public ArrayList<String> readGrammer(){
        grammer = new ArrayList<String>();
        ArrayList<String> grammer1 = new ArrayList<String>();
        for(String gra : textArea_input.getText().split("\n")){
            grammer1.add(gra);
        }
//        System.out.println(grammer1);
        String[] temp;
        String[] one;
        for(int i=0;i<grammer1.size();i++){
            String line = grammer1.get(i);
            temp = line.split("->");
            one = temp[1].split("\\|");
            for(String str : one){
                grammer.add(temp[0]+"->"+str);
            }
        }
        return grammer;
    }
    //获取非终结符
    public ArrayList<String> spiltToNonterminal(){
        nontermianl = new ArrayList<String>();
        for(String gra : grammer){
            String[] str = gra.split("->");
            if (!nontermianl.contains(str[0])){
                nontermianl.add(str[0]);
            }
        }
        return nontermianl;
    }
    //获取终结符
    public ArrayList<String> spiltToTerminal(){
        termianl = new ArrayList<String>();
        for(String gra : grammer){
            String[] str = gra.split("->");
            List<String> clist = disassemble(str[1]);
            for(String s : clist){
                if(!nontermianl.contains(s)){
                    if(!termianl.contains(s))
                        termianl.add(s);
                }
            }
        }
        return termianl;
    }
    //获取开始符号
    public String getStart(){
        start = "";
        if(nontermianl.size() == 1){
            start = nontermianl.get(0);
        }else {
            for(String vn : nontermianl){
                boolean temp = true;
                for(String gra : grammer){
                    String[] str = gra.split("->");
                    List<String> strlist = disassemble(str[1]);
                    if(strlist.contains(vn)){
                        temp = false;
                    }
                }
                if(temp)
                    start = vn;

            }
        }
                return start;
    }
    //将字符串拆分（考虑到可能存在S'）
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
