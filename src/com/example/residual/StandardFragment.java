package com.example.residual;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class StandardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    private int calculationAnchor = Calculator.CALCULATION_ANCHOR_RESIDUAL;
    
    private View rootView;
    
    private int[] editTextViewIds = new int[] {
    		R.id.preparation_value,
    		R.id.working_time_value,
    		R.id.distance_value,
    		R.id.one_way_journey_time_value,
    		R.id.days_value,
    		R.id.residual_value,
    		R.id.nominal_fee_value,
    		R.id.total_amount_value
    };
    
    private int[] editTextCalculationFocus = new int[] {
    		Calculator.CALCULATION_ANCHOR_NONE,
    		Calculator.CALCULATION_ANCHOR_NONE,
    		Calculator.CALCULATION_ANCHOR_NONE,
    		Calculator.CALCULATION_ANCHOR_NONE,
    		Calculator.CALCULATION_ANCHOR_NONE,
    		Calculator.CALCULATION_ANCHOR_RESIDUAL,
    		Calculator.CALCULATION_ANCHOR_NOMINAL_FEE,
    		Calculator.CALCULATION_ANCHOR_TOTAL_AMOUNT
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_standard,
				container, false);
		registerListeners();
        return rootView;
    }
    
    private void registerListeners() {
		for (int i = 0; i < editTextViewIds.length ; i++) {
			TextView editTextView = 
					(TextView) rootView.findViewById(editTextViewIds[i]);
			int associatedCalculationFocus = editTextCalculationFocus[i];
			editTextView.setOnFocusChangeListener(
					getOnFocusChangeListener(associatedCalculationFocus));
			editTextView.addTextChangedListener(
					getTextWatcher(associatedCalculationFocus));
		}
    }
    
    private ValueSet getValueSet() {
    	ValueSet vs = new ValueSet();
    	vs.preparation = getEditTextValue(R.id.preparation_value);
    	vs.workingTime = getEditTextValue(R.id.working_time_value);
    	vs.distance = getEditTextValue(R.id.distance_value);
    	vs.oneWayJourneyTime = getEditTextValue(R.id.one_way_journey_time_value);
    	vs.daysAtCustomerSite = getEditTextValue(R.id.days_value);
    	vs.residual = getEditTextValue(R.id.residual_value);
    	vs.nominalFee = getEditTextValue(R.id.nominal_fee_value);
    	vs.totalAmount = getEditTextValue(R.id.total_amount_value);
    	return vs;
    }
    
    private double getEditTextValue(int resId) {
    	EditText et = getEditText(resId);
    	if (et != null) {
        	String strValue = et.getText().toString();
        	if (strValue != null) {
        		try {
        			return Double.valueOf(strValue);
        		} catch (NumberFormatException e) {
        		}
        	}
    	}
    	return 0;
    }
    
	private OnFocusChangeListener getOnFocusChangeListener(
			final int associatedCalculationAnchor) {
    	return new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && associatedCalculationAnchor
							!= Calculator.CALCULATION_ANCHOR_NONE) {
					calculationAnchor = associatedCalculationAnchor;
				}
			}
		};
    }
	
	private TextWatcher getTextWatcher(final int associatedCalculationAnchor) {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (associatedCalculationAnchor == calculationAnchor
						|| associatedCalculationAnchor == 
								Calculator.CALCULATION_ANCHOR_NONE) {
					recalculate();
				}
			}
		};
	}
	
	private EditText getEditText(int resId) {
		return (EditText) rootView.findViewById(resId);
	}
	
	private void setEditTextValue(int resId, double value) {
		getEditText(resId).setText(Double.toString(Math.round(value * 100) / 100));
	}
	
	private void recalculate() {
		ValueSet result = Calculator.calculate(getValueSet(), calculationAnchor);
		if (calculationAnchor != Calculator.CALCULATION_ANCHOR_RESIDUAL) {
			setEditTextValue(R.id.residual_value, result.residual);
		}
		if (calculationAnchor != Calculator.CALCULATION_ANCHOR_NOMINAL_FEE) {
			setEditTextValue(R.id.nominal_fee_value, result.nominalFee);
		}
		if (calculationAnchor != Calculator.CALCULATION_ANCHOR_TOTAL_AMOUNT) {
			setEditTextValue(R.id.total_amount_value, result.totalAmount);
		}
	}
}

