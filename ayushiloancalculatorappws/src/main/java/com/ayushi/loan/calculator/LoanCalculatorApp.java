package com.ayushi.loan.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.ayushi.loan.*;

@RestController
@EnableAutoConfiguration
public class LoanCalculatorApp {
	private static String inputLeft, inputRight, calcEquals, inputOperator;
	private static String air, numOfYears, loanAmount, lender, state, amortizeOnDate;
	interface IntegerMath{
		int operation(int a, int b);
	}

	@RequestMapping("/")
    	String home() {
	return "home";
    	}

	@RequestMapping(path="/calculateloan", produces="application/json")
    	Loan calculateloan(@RequestParam("airVal") String airVal,
		                @RequestParam("lender") String lndr,
				                @RequestParam("loanAmt") String loanAmtVal,
						                @RequestParam("state") String st,
								                @RequestParam("numOfYears") String numYrs
    	) {
			air = airVal;
			numOfYears = numYrs;
			loanAmount = loanAmtVal;
			lender = lndr;
			state = st;

			double periodicInterestRate = Double.valueOf(air)/(12*100);
			double addOne = (1 + periodicInterestRate);
			double loanAmt = Double.valueOf(loanAmount);
			double compoundingPeriods = Double.valueOf(numOfYears)*12;
			
			double monthly = 0;
			double total = 0, interest = 0;
			
			monthly = loanAmt * (((periodicInterestRate * Math.pow(addOne, compoundingPeriods))/(Math.pow(addOne,compoundingPeriods) - 1)));
			total = (compoundingPeriods) * monthly;

			interest = (loanAmt * Math.pow((1+periodicInterestRate),compoundingPeriods) - loanAmt)/compoundingPeriods; 
		return new Loan(monthly, loanAmt, total, lender, state, periodicInterestRate, Double.valueOf(air), Integer.valueOf(numOfYears), interest);    	
	}

	@RequestMapping(path="/amortizeloan", produces="application/json")
    	AmortizedLoan amortizeloan(@RequestParam("airVal") String airVal,
		                @RequestParam("lender") String lndr,
				                @RequestParam("loanAmt") String loanAmtVal,
						                @RequestParam("state") String st,
								                @RequestParam("numOfYears") String numYrs,
											@RequestParam("amortizeOn") String amortizeOn
    	) {
			air = airVal;
			numOfYears = numYrs;
			loanAmount = loanAmtVal;
			lender = lndr;
			state = st;
			amortizeOnDate = amortizeOn;

			double periodicInterestRate = Double.valueOf(air)/(12*100);
			double addOne = (1 + periodicInterestRate);
			double loanAmt = Double.valueOf(loanAmount);
			double compoundingPeriods = Double.valueOf(numOfYears)*12;
			
			double monthly = 0;
			double total = 0, interest = 0;
			
			monthly = loanAmt * (((periodicInterestRate * Math.pow(addOne, compoundingPeriods))/(Math.pow(addOne,compoundingPeriods) - 1)));
			total = (compoundingPeriods) * monthly;

			interest = (loanAmt * Math.pow((1+periodicInterestRate),compoundingPeriods) - loanAmt)/compoundingPeriods; 
		return  new AmortizedLoan(amortizeOnDate, monthly, loanAmt, total, lender, state, periodicInterestRate, Double.valueOf(air), Integer.valueOf(numOfYears), interest);    	
	}
	public LoanCalculatorApp(){
	}
	
	public LoanCalculatorApp(String inpLft, String inpRgt, String calcEq, String inpOp){
		inputLeft = inpLft;
		inputRight = inpRgt;
		calcEquals = calcEq;
		inputOperator = inpOp;
	}

	public static String calculate(){
		Float inpLeft = Float.valueOf(inputLeft);
		Float inpRight = Float.valueOf(inputRight);
		if(calcEquals != null && calcEquals.equals("+")){
			return String.valueOf(new Float(inpLeft + inpRight));
		}else if(calcEquals != null && calcEquals.equals("-")){
			return String.valueOf(new Float(inpLeft - inpRight));
		}else if(calcEquals != null && calcEquals.equals("*")){
			return String.valueOf(new Float(inpLeft * inpRight));
		}else if(calcEquals != null && calcEquals.equals("/")){
			return String.valueOf(new Float(inpLeft / inpRight));
		}else if(calcEquals != null && calcEquals.equals("%")){
			return String.valueOf(new Float(inpLeft * inpRight / 100));
		}

		return "No Value";
	}

	public int operateBinary(int a, int b, IntegerMath op){
		return op.operation(a, b);
	}

	public static void main(String... args) throws Exception {
		SpringApplication.run(LoanCalculatorApp.class, args);
		LoanCalculatorApp loanCalc = new LoanCalculatorApp();
		IntegerMath addition = (a, b) -> a + b;
		IntegerMath subtraction = (a, b) -> a - b;
		System.out.println("40 + 2 = " + loanCalc.operateBinary(40, 2, addition));
		System.out.println("20 - 10 = " + loanCalc.operateBinary(20, 10, subtraction));		
	 }
}
