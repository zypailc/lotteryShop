package com.didu.lotteryshop.lotteryb.utils;
/**
 * 统计任三出现的最多的几率的组合
 *
 * @author wangmingjie
 * @date 2009-1-1下午01:22:19
 */
/**
 * 从n个数里取出m个数的排列或组合算法实现
 * @author chengesheng
 * @date 2016年9月28日 下午3:18:34
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationUtil {

    public static void main(String[] args) {
 //     A(4, 2) = 12
//      arrangementSelect(new String[] {
//                "1", "2", "3", "4"
//      }, 2);
//    C(5, 3) = 10
        combinationSelect(new String[] {
                "1", "2","3","4"
        }, 3);
    }

    /**
     * 排列选择（从列表中选择n个排列）
     * @param dataList 待选列表
     * @param n 选择个数
     */
    public static void arrangementSelect(String[] dataList, int n) {
        System.out.println(String.format("A(%d, %d) = %d", dataList.length, n, arrangement(dataList.length, n)));
        arrangementSelect(dataList, new String[n], 0);
    }

    /**
     * 排列选择
     * @param dataList 待选列表
     * @param resultList 前面（resultIndex-1）个的排列结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void arrangementSelect(String[] dataList, String[] resultList, int resultIndex) {
        int resultLen = resultList.length;
        if (resultIndex >= resultLen) { // 全部选择完时，输出排列结果
            System.out.println(Arrays.asList(resultList));
            return;
        }

        // 递归选择下一个
        for (int i = 0; i < dataList.length; i++) {
            // 判断待选项是否存在于排列结果中
            boolean exists = false;
            for (int j = 0; j < resultIndex; j++) {
                if (dataList[i].equals(resultList[j])) {
                    exists = true;
                    break;
                }
            }
            if (!exists) { // 排列结果不存在该项，才可选择
                resultList[resultIndex] = dataList[i];
                arrangementSelect(dataList, resultList, resultIndex + 1);
            }
        }
    }

    /**
     * 组合选择（从列表中选择n个组合）
     * @param dataList 待选列表
     * @param n 选择个数
     */
    public static List<String> combinationSelect(String[] dataList, int n) {
        System.out.println(String.format("C(%d, %d) = %d", dataList.length, n, combination(dataList.length, n)));
        List<String> list = new ArrayList<>();
        list = combinationSelect(dataList, 0, new String[n], 0,list);
        for (String s : list) {
            System.out.println(s);
        }
        return list;
    }

    /**
     * 组合选择
     * @param dataList 待选列表
     * @param dataIndex 待选开始索引
     * @param resultList 前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     */
    private static List<String> combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex, List<String> list) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            list.add(Arrays.asList(resultList).toString().replace("[","").replace("]","").replace(" ",""));
            return list;
        }
        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1,list);
        }
        return list;
    }

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     * @param n
     * @return
     */
    public static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     * @param n
     * @param m
     * @return
     */
    public static long arrangement(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) : 0;
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     * @param n
     * @param m
     * @return
     */
    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }
}