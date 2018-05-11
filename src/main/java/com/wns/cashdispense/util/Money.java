package com.wns.cashdispense.util;

import java.math.BigDecimal;
import java.util.Currency;

public interface Money {

    BigDecimal getValue();

    Currency getCurrency();

}
