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
    //将输入框中的内容保存
    public void readGrammer(JTextArea textArea_input, ArrayList<String> grammer){
        grammer.clear();
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
    }
    //获取非终结符
    public void spiltToNonterminal(ArrayList<String> grammer,ArrayList<String> nontermianl){
        nontermianl.clear();
        for(String gra : grammer){
            String[] str = gra.split("->");
            if (!nontermianl.contains(str[0])){
                nontermianl.add(str[0]);
            }
        }
    }
    //获取终结符
    public void spiltToTerminal(ArrayList<String> grammer,ArrayList<String> termianl,ArrayList<String> nontermianl){
        termianl.clear();
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
    }
    public String getStart(ArrayList<String> grammer,ArrayList<String> nontermianl){
        String start = "";
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
        return start;
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
