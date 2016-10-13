package calculator;
import java.util.Scanner;

public class calculator{
    public static class Command{
        String cmd_line;
        String[] cmd_group;
        Scanner scanner=new Scanner(System.in);
        String exp_bk;
        String[] exp_group;
        int var[]=new int [256];
        String exp_str;
        int exp_int;
        String dao_str;
        int dao_int;

        boolean is_num(char ch){
            if(ch>=48 && ch<=57)
                return true;
            else
                return false;
        }

        boolean is_letter(char ch){
            if((ch>=65 && ch<=90)||(ch>=97 && ch<=122))
                return true;
            else
                return false;
        }

        boolean is_cal(char ch){
            if(ch=='+'||ch=='*')
                return true;
            else
                return false;
        }

        boolean is_nextvalid(char ch,char next_ch){
            if(is_num(ch))
                if(is_num(next_ch)||is_cal(next_ch))
                    return true;
                else
                    return false;
            else if(is_letter(ch))
                if(is_cal(next_ch))
                    return true;
                else
                    return false;
            else if(is_cal(ch))
                if(is_num(next_ch)||is_letter(next_ch))
                    return true;
                else
                    return false;
            else
                return false;
        }

        void var_clear(){
            for(int i=0;i<256;i++)
                var[i]=0;
        }

        void input(){
            cmd_line=scanner.nextLine();
            if(cmd_line.equals("quit"))
                System.exit(0);
            cmd_group=cmd_line.split(" ");
        }

        boolean read_exp(String cmd_line){
            int exp_len=cmd_line.length();
            char exp_first=cmd_line.charAt(0);

            if(is_letter(exp_first))
                var[exp_first]=-1;
            else if(!is_num(exp_first))
                return false;

            for(int i=0;i<exp_len-1;i++){
                char cur=cmd_line.charAt(i);
                char next=cmd_line.charAt(i+1);

                if(is_nextvalid(cur,next)){
                    if(is_letter(next)&&var[next]==0)
                        var[next]=-1;
                }else
                    return  false;
            }
            exp_bk=cmd_line;
            return true;
        }

        String cha_str(String str){
            int len=str.length();
            char ch=str.charAt(len-1);

            if(ch=='+'||ch=='*')
                str=str.substring(0,len-1);
            return str;
        }

        boolean cal_exp(){
            int cmdgrp_len=cmd_group.length;
            int expgrp_len=exp_group.length;

            for(int i=1;i<cmdgrp_len;i++){
                String item=cmd_group[i];
                int item_len=item.length();
                int item_value=0;

                if(var[item.charAt(0)]==0 || item.charAt(1)!='=')
                    return false;
                for(int j=2;j<item_len;j++){
                    if(is_num(item.charAt(j)))
                        item_value=10*item_value+(item.charAt(j)-48);
                    else
                        return false;
                }
                var[item.charAt(0)]=item_value;
            }

            for(int i=0;i<expgrp_len;i++){
                String item=exp_group[i];
                int item_int=1;
                int item_len=item.length();
                String item_str="";

                for(int j=0;j<item_len;j++){
                    int num=0;

                    while(j<item_len&&is_num(item.charAt(j))){
                        num=num*10+(item.charAt(j)-48);
                        j++;
                    }
                    if(num!=0)
                        item_int*=num;
                    if(j<item_len){
                        if(is_letter(item.charAt(j)))
                            if(var[item.charAt(j)]!=-1)
                                item_int*=var[item.charAt(j)];
                            else
                                item_str=item_str.concat(String.valueOf(item.charAt(j))).concat("*");
                    }
                }
                if(item_str.equals(""))
                    exp_int+=item_int;
                else{
                    item_str=cha_str(item_str);
                    if(item_int!=1)
                        item_str=String.valueOf(item_int).concat("*").concat(item_str);
                    exp_str=exp_str.concat(item_str).concat("+");
                }
            }
            if(!exp_str.equals("")){
                exp_str=cha_str(exp_str);
                if(exp_int!=0)
                    exp_str=String.valueOf(exp_int).concat("+").concat(exp_str);
            }
            return true;
        }

        boolean is_in(char ch,String str){
            int len=str.length();

            for(int i=0;i<len;i++)
                if(str.charAt(i)==ch)
                    return true;
            return false;
        }

        void qiudao(char ch){
            int expgrp_len=exp_group.length;

            for(int i=0;i<expgrp_len;i++){
                String item=exp_group[i];
                int item_int=1;
                String item_str="";
                int item_len=item.length();

                if(!is_in(ch,item))
                    continue;
                for(int j=0;j<item_len;j++){
                    int num=0;

                    while(j<item_len && is_num(item.charAt(j))){
                        num=num*10+(item.charAt(j)-48);
                        j++;
                    }
                    if(num!=0)
                        item_int*=num;
                    if(j<item_len&&is_letter(item.charAt(j))&&item.charAt(j)!=ch)
                        item_str=item_str.concat(String.valueOf(item.charAt(j))).concat("*");
                }
                if(item_str.equals(""))
                    dao_int+=item_int;
                else{
                    item_str=cha_str(item_str);
                    if(item_int!=1)
                        item_str=String.valueOf(item_int).concat("*").concat(item_str);
                    dao_str=dao_str.concat(item_str).concat("+");
                }
            }
            if(!dao_str.equals("")){
                dao_str=cha_str(dao_str);
                if(dao_int!=0)
                    dao_str=String.valueOf(dao_int).concat("+").concat(dao_str);
            }
        }

        boolean exec_cmd(){
            char cmd_flag=cmd_line.charAt(0);

            if(cmd_flag!='!'){
                var_clear();
                if(read_exp(cmd_line)){
                    exp_group = cmd_line.split("\\+");
                    System.out.println(cmd_line);
                }else{
                    var_clear();
                    read_exp(exp_bk);
                    return false;
                }
            }else if(cmd_group[0].equals("!simplify")){
                exp_int=0;
                exp_str="";
                var_clear();
                read_exp(exp_bk);
                if(!cal_exp()){
                    var_clear();
                    read_exp(exp_bk);
                    return false;
                }else {
                    if (exp_str.equals(""))
                        System.out.println(exp_int);
                    else
                        System.out.println(exp_str);
                }
            }else if(cmd_group[0].startsWith("!d/d")&&cmd_group[0].length()==5&&
                    is_letter(cmd_group[0].charAt(4))&&var[cmd_group[0].charAt(4)]!=0){
                dao_int=0;
                dao_str="";
                qiudao(cmd_group[0].charAt(4));
                if(dao_str.equals(""))
                    System.out.println(dao_int);
                else
                    System.out.println(dao_str);
            }else
                return false;
            return true;
        }

    }

    public static void main(String[] args){
        Command cmd=new Command();

        while(true) {
            System.out.print('>');
            cmd.input();
            if(!cmd.exec_cmd())
                System.out.println("输入错误");
        }
    }

}
