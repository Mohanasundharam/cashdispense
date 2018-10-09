package com.wns.cashdispense;

import org.junit.Assert;
import org.junit.Test;

import com.wns.cashdispense.util.Note;

import static com.wns.cashdispense.util.Constants.AUD;

import java.math.BigDecimal;
import java.util.Currency;

public class NoteTest {

    @Test
    public void whenGetCurrencyThenCorrectCurrency() {
        Currency expectedCurrency = Currency.getInstance(AUD);
        Assert.assertEquals(expectedCurrency, Note.TWENTY.getCurrency());
        Assert.assertEquals(expectedCurrency, Note.FIFTY.getCurrency());
    }

    @Test
    public void whenGetValueThenCorrectValue() {
        Assert.assertEquals(new BigDecimal(20), Note.TWENTY.getValue());
        Assert.assertEquals(new BigDecimal(50), Note.FIFTY.getValue());
    }

}
