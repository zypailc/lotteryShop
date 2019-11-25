package com.didu.lotteryshop;

import org.junit.Test;

import java.util.Random;

public class Test1Lotter {
    public String randomLuckNum(int num){
        Random random = new Random();
        String luckNum = "";
        for(int i=0;i < num ; i++){
            luckNum += random.nextInt(10);
        }
        return luckNum;
    }

    public void test(){
        String luckNum = randomLuckNum(3);
        int rs = 50;
        String[] buyNumArrar = new String[rs];
        for(int i= 0;i < rs;i++){
            buyNumArrar[i] = randomLuckNum(3);
        }
        String aA = "";
        int xxA = 0;
        for(String buyNum : buyNumArrar) {
            if(luckNum.equals(buyNum)){
                aA +=","+buyNum;
                xxA++;
                continue;
            }
        }
        System.out.println("幸运号码："+luckNum);
        System.out.println("中奖："+xxA+";["+ aA+"]");

    }

    @Test
    public void test1(){
        for(int i = 0; i < 30; i++) {
            test();
        }
    }
}
