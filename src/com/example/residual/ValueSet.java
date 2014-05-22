package com.example.residual;

public class ValueSet implements Cloneable {
	public double mileageRate = 0.3;
	
	public double preparation;
	public double workingTime;
	public double distance;
	public double oneWayJourneyTime;
	public double daysAtCustomerSite;
	
	public double residual;
	public double nominalFee;
	public double totalAmount;
	
	@Override
	public ValueSet clone() {
		try {
			return (ValueSet) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}
}
