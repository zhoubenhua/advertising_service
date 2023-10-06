package com.tencent.wxcloudrun.util;

public class CommonUtil {
    /**
     * 计算个人所得税
     * @param money
     * @return
     */
    public static float calculatorIndividualIncomeTax(float money) {
        float one =0,two=0,three=0,four=0,five=0,six=0;
        one = (8000-5000) * 0.03f;//第一档满额缴费
        two = (17000-8000) * 0.1f; //第二档满额缴费
        three = (30000-17000) * 0.2f; //第三档满额缴费
        four = (40000-30000) * 0.25f; //第四档满额缴费
        five = (60000-40000) * 0.3f; //第五档满额缴费
        six = (85000-60000) * 0.35f; //第六档满额缴费
        float tax = 0;//税率
        if (money <=5000) {
            tax = 0;//级别1税率
        } else if (money <= 8000) {
            tax = (money - 5000) * 0.03f;  //级别2税率
        }  else if (money <= 17000) {
            tax = (money - 8000) * 0.1f + one;  //级别3税率
        } else if (money <= 30000) {
            tax = (money - 17000) * 0.2f + two + one;  //级别4税率
        } else if (money <= 40000) {
            tax = (money - 30000) * 0.25f + three + two + one;  //级别5税率
        } else if (money <= 60000) {
            tax = (money - 40000) * 0.3f + four + three + two + one;  //级别6税率
        } else if (money <= 85000) {
            tax = (money - 60000) * 0.35f + five + four + three + two + one;  //级别7税率
        } else {
            tax = (money - 85000) * 0.45f + six + five + four +three + two + one;  //级别8税率
        }
        return tax;

    }
}
