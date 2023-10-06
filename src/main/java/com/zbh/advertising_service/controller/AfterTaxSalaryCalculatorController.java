package com.zbh.advertising_service.controller;

import com.zbh.advertising_service.controller.common.BaseResponse;
import com.zbh.advertising_service.model.AfterTaxSalaryEntiy;
import com.zbh.advertising_service.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 税后计算器相关接口
 */
@Controller
@RequestMapping("/api/after_tax_salary")
public class AfterTaxSalaryCalculatorController {
    final Logger logger;

    public AfterTaxSalaryCalculatorController() {
        this.logger = LoggerFactory.getLogger(AfterTaxSalaryCalculatorController.class);
    }

    @ResponseBody
    @GetMapping("/calculatorIndividualIncomeTax")
    public BaseResponse<AfterTaxSalaryEntiy> calculatorIndividualIncomeTax(@RequestParam(value = "money") float money) {
        logger.info("/api/after_tax_salary");
        AfterTaxSalaryEntiy afterTaxSalary = new AfterTaxSalaryEntiy();
        afterTaxSalary.setPreTaxIncome(money);
        afterTaxSalary.setPersonPayEndowmentInsurance((float) (money * 0.08));// 计算养老保险,税率为8%
        afterTaxSalary.setPersonPayMedicalInsurance((float) (money * 0.02));// 计算医保保险，税率为2%
        afterTaxSalary.setPersonPayUnemploymentInsurance((float) (money * 0.002));// 计算失业保险，税率为0.2%
        afterTaxSalary.setPersonPayHousingFund(money * 0.07f);// 计算住房公积金，税率为7%
        afterTaxSalary.setCompanyPayEndowmentInsurance((float) (money * 0.2));// 公司养老保险,税率为20%
        afterTaxSalary.setCompanyPayMedicalInsurance((float) (money * 0.11));// 公司交医保保险，税率为11%
        afterTaxSalary.setCompanyPayUnemploymentInsurance((float) (money * 0.015));// 公司交失业保险，税率为1.5%
        afterTaxSalary.setCompanyPayMploymentInjuryInsurance((float) (money * 0.05));// 公司交工伤保险，税率为0.5%
        afterTaxSalary.setCompanyPayMaternityInsurance((float) (money * 0.015));// 公司交生育保险，税率为1.5%
        afterTaxSalary.setCompanyPayHousingFund(money * 0.07f);// 公司住房公积金，税率为7%
        float total = afterTaxSalary.getPersonPayEndowmentInsurance() +
                afterTaxSalary.getPersonPayMedicalInsurance() +
                afterTaxSalary.getPersonPayUnemploymentInsurance()+
        afterTaxSalary.getPersonPayHousingFund();
        afterTaxSalary.setIndividualIncomeTax(CommonUtil.calculatorIndividualIncomeTax(money - total));
        afterTaxSalary.setAfterTaxIncome(afterTaxSalary.getPreTaxIncome() - afterTaxSalary.getIndividualIncomeTax());
        BaseResponse<AfterTaxSalaryEntiy> response = BaseResponse.success(afterTaxSalary);
        return response;
    }


}
