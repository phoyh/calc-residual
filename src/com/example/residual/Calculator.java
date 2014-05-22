package com.example.residual;

public class Calculator {
	public static final int CALCULATION_ANCHOR_NONE = 0;
	public static final int CALCULATION_ANCHOR_RESIDUAL = 1;
	public static final int CALCULATION_ANCHOR_NOMINAL_FEE = 2;
	public static final int CALCULATION_ANCHOR_TOTAL_AMOUNT = 3;

	public static ValueSet calculate(ValueSet parameter, int calculationAnchor) {
		ValueSet result = parameter.clone();
		
		if (calculationAnchor == CALCULATION_ANCHOR_NOMINAL_FEE) {
			result.totalAmount = getTotalAmountBasedOnNominalFee(result);
		}
		if (calculationAnchor == CALCULATION_ANCHOR_NONE
				|| calculationAnchor == CALCULATION_ANCHOR_RESIDUAL) {
			result.totalAmount = getTotalAmountBasedOnResidual(result);
		}
		
		if (calculationAnchor != CALCULATION_ANCHOR_NONE
				&& calculationAnchor != CALCULATION_ANCHOR_RESIDUAL) {
			result.residual = getResidualBasedOnTotalAmount(result);
		}
		if (calculationAnchor != CALCULATION_ANCHOR_NOMINAL_FEE) {
			result.nominalFee = getNominalFeeBasedOnTotalAmount(result);
		}
		
		return result;
	}

	private static double getResidualBasedOnTotalAmount(ValueSet parameter) {
		if (getTotalTimeEffort(parameter) == 0) {
			return 0;
		}
		return (parameter.totalAmount - getMileageRefund(parameter))
				/ getTotalTimeEffort(parameter);
	}

	private static double getNominalFeeBasedOnTotalAmount(ValueSet parameter) {
		if (parameter.workingTime == 0) {
			return 0;
		}
		return (parameter.totalAmount - getMileageRefund(parameter))
				/ parameter.workingTime;
	}

	private static double getTotalAmountBasedOnResidual(ValueSet parameter) {
		return getTotalTimeEffort(parameter) * parameter.residual
				+ getMileageRefund(parameter);
	}
	
	private static double getTotalAmountBasedOnNominalFee(ValueSet parameter) {
		return parameter.workingTime * parameter.nominalFee
				+ getMileageRefund(parameter);
	}
	
	private static double getMileageRefund(ValueSet parameter) {
		return parameter.daysAtCustomerSite
				* parameter.distance * 2 
				* parameter.mileageRate;
	}

	private static double getTotalTimeEffort(ValueSet parameter) {
		return parameter.daysAtCustomerSite * 2 * parameter.oneWayJourneyTime
				+ parameter.preparation
				+ parameter.workingTime;
	}
}
