package com.itlike.domain;

public class Test {
    public static void PrintToMaxofDigita(int n){
        if(n<=0){
            return ;
        }
        char[] number=new char[n+1];
        for(int i=0;i<n;i++){
            number[i]='0';

        }
        number[n]='\0';
        while(!increment(number)){
            printNumber(number);
        }
    }
    //实现在表示数字的字符串number上增加1
    public static  boolean increment(char[] number){
        boolean isOverflow=false;
        int nTakeOver=0;//代表进位
       for(int i=number.length-1;i>=0;i--){
           int nSum=(number[i]-'0')+nTakeOver;//当前位置数字
           if(i==number.length-1){
               nSum++;
           }
           if(nSum>=10){
               if(i==0){
                  isOverflow= true;//标识溢出
               }else{
                   nSum-=10;
                   nTakeOver=1;
                   number[i]=(char)(nSum+'0');
               }
               }else {
               number[i] = (char) (nSum + '0');
        break;

    }
}

return  isOverflow;

    }
    public static  void printNumber(char[]number){
        boolean isBegining=true;
        for(int i=0;i<number.length;i++){
            if(number[i]-'0'!=0){
                isBegining=false;
                break;
            }
            if(!isBegining){
                System.out.println(number[i]);
            }
        }
        System.out.println();


    }

    public static void main(String[] args) {
        PrintToMaxofDigita(3);
        System.out.println("哈哈");

    }
}
