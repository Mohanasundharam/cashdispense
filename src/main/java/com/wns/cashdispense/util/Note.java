package com.wns.cashdispense.util;

 
import static com.wns.cashdispense.util.Constants.AUD;

import java.math.BigDecimal;
import java.util.Currency;

public enum Note implements Money {
	 FIFTY(AUD, 50),
	 TWENTY(AUD, 20); 
 
    private Currency currency;
    private BigDecimal value;

    Note(String currencyCode, int value) {
        this.currency = Currency.getInstance(currencyCode);
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", getCurrency().getCurrencyCode(), getValue());
    }

}
