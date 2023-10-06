package com.tencent.wxcloudrun.controller;

import com.zbh.advertising_service.model.AfterTaxSalaryEntiy;
import com.zbh.advertising_service.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * counter控制器
 */
@RestController

public class CounterController {

  final CounterService counterService;
  final Logger logger;

  public CounterController(@Autowired CounterService counterService) {
    this.counterService = counterService;
    this.logger = LoggerFactory.getLogger(CounterController.class);
  }


  /**
   * 获取当前计数
   * @return API response json
   */
  @GetMapping(value = "/api/count")
  ApiResponse get() {
    logger.info("/api/count get request");
    Optional<Counter> counter = counterService.getCounter(1);
    Integer count = 0;
    if (counter.isPresent()) {
      count = counter.get().getCount();
    }

    return ApiResponse.ok(count);
  }

  @GetMapping(value = "/api/after_tax_salary")
  ApiResponse afterTaxSalary() {
    float money = 44444.f;
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
    ApiResponse apiResponse = ApiResponse.ok(afterTaxSalary);
    return apiResponse;
  }


  /**
   * 更新计数，自增或者清零
   * @param request {@link CounterRequest}
   * @return API response json
   */
  @PostMapping(value = "/api/count")
  ApiResponse create(@RequestBody CounterRequest request) {
    logger.info("/api/count post request, action: {}", request.getAction());

    Optional<Counter> curCounter = counterService.getCounter(1);
    if (request.getAction().equals("inc")) {
      Integer count = 1;
      if (curCounter.isPresent()) {
        count += curCounter.get().getCount();
      }
      Counter counter = new Counter();
      counter.setId(1);
      counter.setCount(count);
      counterService.upsertCount(counter);
      return ApiResponse.ok(count);
    } else if (request.getAction().equals("clear")) {
      if (!curCounter.isPresent()) {
        return ApiResponse.ok(0);
      }
      counterService.clearCount(1);
      return ApiResponse.ok(0);
    } else {
      return ApiResponse.error("参数action错误");
    }
  }
  
}