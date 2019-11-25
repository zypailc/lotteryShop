package com.didu.lotteryshop;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class TestLottery {
    public String randomLuckNum(int num){
        Random random = new Random();
        String luckNum = "";
        for(int i=0;i < num ; i++){
            luckNum += random.nextInt(10);
        }
        return luckNum;
    }
    @Test
    public void test(){
        //85864
        //85864
      //  String luckNum = "17104";//;randomLuckNum(5);
        String luckNum = randomLuckNum(5);
       // System.out.println("幸运号码："+ luckNum);
        int rs = 100000;
        String[] buyNumArrar = new String[rs];
        for(int i= 0;i < rs;i++){
            buyNumArrar[i] = randomLuckNum(5);
        }
//        String[] buyNumArrar = new String[4];
//        buyNumArrar[0] ="17104";
//        buyNumArrar[1] ="17105";
//        buyNumArrar[2] ="12134";
//        buyNumArrar[3] ="11234";

        String aA = "";
        int xxA = 0;
        String bB = "";
        int xxB = 0;
        String cC = "";
        int xxC = 0;
        String dD = "";
        int xxD = 0;
        String dE = "";
        int xxE = 0;
        for(String buyNum : buyNumArrar){

            //一等奖
            if(luckNum.equals(buyNum)){
                aA +=","+buyNum;
                xxA++;
                continue;
            }
            int zjs = 0;
            StringBuffer  aa = new StringBuffer(luckNum);
            StringBuffer bb = new StringBuffer(buyNum);
            for(int j = 0;j < luckNum.length(); j++){
                if(zjs == 4){
                    break;
                }
                if(aa.charAt(j) == bb.charAt(j)){
                    aa.replace(j,j+1,"A");
                    bb.replace(j,j+1,"B");
                    zjs++;
                }
            }
            if(zjs == 4){
                bB +=","+buyNum;
                xxB++;
                continue;
            }
            if(zjs == 3){
                cC +=","+buyNum;
                xxC++;
                continue;
            }
            if(zjs == 2 ){
                String aas = aa.toString();
               String bbs = bb.toString();
               aas = aas.replaceAll("A","");
                bbs = bbs.replaceAll("B","");
                for(int i = 0 ; i < bbs.length();i++){
                   if(aas.indexOf(bbs.charAt(i)) >= 0 ){
                        dD +=","+buyNum;
                        xxD++;
              //  continue;
                       break;
                   }
                }
            }
//            if(zjs == 1 ){
//                dE +=","+buyNum;
//                xxE++;
//                continue;
//            }
        }
//        aA = aA.replaceFirst(",","");
//        bB = bB.replaceFirst(",","");
//        cC = cC.replaceFirst(",","");
//        dD = dD.replaceFirst(",","");
//        System.out.println("一等奖："+xxA+";["+ aA+"]");
//        System.out.println("二等奖："+xxB+";["+ bB+"]");
//        System.out.println("三等奖："+xxC+";["+ cC+"]");
//        System.out.println("四等奖："+xxD+";["+ dD+"]");
        dj1 += xxA;
        dj2 += xxB;
        dj3 += xxC;
        dj4 += xxD;
       // dj5 += xxE;

    }
    
    
    private Integer dj1 = 0;
    private Integer dj2 = 0;
    private Integer dj3 = 0;
    private Integer dj4 = 0;
    private Integer dj5 = 0;
    @Test
    public void testxx(){
        int qs = 1000;
        for(int i = 0;i < qs;i++){
            this.test();
        }
        System.out.println("一等奖概率："+dj1+";"+(dj1/qs));
        System.out.println("二等奖概率："+dj2+";"+(dj2/qs));
        System.out.println("三等奖概率："+dj3+";"+(dj3/qs));
        System.out.println("四等奖概率："+dj4+";"+(dj4/qs));
        //System.out.println("五等奖概率："+(dj5));
    }
    @Test
    public void testxx1(){
        int rs = 100000;
        String[] luckNumArrar = new String[rs];
        for(int i= 0;i < rs;i++){
            luckNumArrar[i] = randomLuckNum(5);
        }
        String aA = "";
        int xxA = 0;
        String bB = "";
        int xxB = 0;
        String cC = "";
        int xxC = 0;
        String dD = "";
        int xxD = 0;
        String eE = "";
        int xxE = 0;
        for(String luckNum : luckNumArrar){
            boolean bool = true;
            for(int i = 0; i < luckNum.length();i++){
                int u = Integer.parseInt(String.valueOf(luckNum.charAt(i)))+1;
                if( luckNum.length()-1 > i && ( u >= 10 || (String.valueOf(u).charAt(0) != luckNum.charAt(i+1)))){
                    bool = false;
                    break;
                }
            }
            if(bool){
                aA +=","+luckNum;
                xxA++;
                continue;
            }
            int yyy = 0;
            for(int i = 0; i < luckNum.length();i++){
//                if(luckNum.length()-1 > i && luckNum.charAt(i) != luckNum.charAt(i+1)){
//                    yyy++;
//                    if(yyy >= 2){
//                        break;
//                    }
//                }
                if(i <= 1){
                    yyy = getSubCount_2(luckNum,String.valueOf(luckNum.charAt(i)));
                    if(yyy >= 4){
                        break;
                    }
                }else{
                    yyy = 0;
                }
            }
            if(yyy == 5){
                bB +=","+luckNum;
                xxB++;
                continue;
            }
            if(yyy == 4){
                cC +=","+luckNum;
                xxC++;
                continue;
            }

            int axxx = dglx(luckNum);
            if(axxx == 3){
                dD +=","+luckNum;
                xxD++;
                continue;
            }
            if(axxx == 2){
                eE +=","+luckNum;
                xxE++;
                continue;
            }
        }

        aA = aA.replaceFirst(",","");
        bB = bB.replaceFirst(",","");
        cC = cC.replaceFirst(",","");
        dD = dD.replaceFirst(",","");
        System.out.println("顺子："+xxA+";["+ aA+"]；" +(xxA/rs));
        System.out.println("豹子："+xxB+";["+ bB+"]；" +(xxB/rs));
        System.out.println("龙角："+xxC+";["+ cC+"]；" +(xxC/rs));
        System.out.println("幅面："+xxD+";["+ dD+"]；" +(xxD/rs));
        System.out.println("两对："+xxE+";["+ eE+"]；" +(xxE/rs));
    }

    public int dglx(String luckNum){
        int xx = 0;
        for(int i = 0; i < luckNum.length();i++){
            int j = getSubCount_2(luckNum,String.valueOf(luckNum.charAt(i)));
            if(i > 1) break;
            if(j == 3 && i <= 1){
                if(i == 0){
                    if(luckNum.length()>3 ){
                         return dglx(luckNum.replaceAll(String.valueOf(luckNum.charAt(i)),""));
                    }else{
                        xx = 3;
                    }
                }
            }
            if(j == 2 && i <= 1){
                if(luckNum.length()>3 && i <= 1){
                    return dglx(luckNum.replaceAll(String.valueOf(luckNum.charAt(i)),""));
                }else{
                    if(luckNum.length() == 2){
                        xx = 3;
                    }else{
                        xx = 2;
                    }
                }
            }
        }
        return xx;
    }

    @Test
    public void testxx2(){
//        String x = "11122";
//////        for(int i = 0; i < x.length();i++) {
//////          System.out.println( getSubCount_2(x,String.valueOf(x.charAt(i))));
//////        }
        System.out.println(dglx("11122"));
        System.out.println(dglx("10122"));
        System.out.println(dglx("12122"));
        System.out.println(dglx("11222"));
        System.out.println(dglx("01122"));
        System.out.println(dglx("01322"));


    }

    public static int getSubCount_2(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count++;
        }
        return count;
    }

}
