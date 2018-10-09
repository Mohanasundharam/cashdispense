package com.wns.cashdispense;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.wns.cashdispense.controller.CashController;
import com.wns.cashdispense.exception.CurrencyCombinationException;
import com.wns.cashdispense.util.Note;

public class CashControllerTest {

	private CashController cashController;

	@Before
	public void setup() {
		cashController = new CashController();
	}

	@Test
	public void whenAddThenMoneyAdded() {
		cashController.add(Note.FIFTY, 26);
		Assert.assertEquals(26, cashController.getCount(Note.FIFTY));
		cashController.add(Note.FIFTY, 1);
		Assert.assertEquals(27, cashController.getCount(Note.FIFTY));
		cashController.add(Note.TWENTY, 3);
		Assert.assertEquals(3, cashController.getCount(Note.TWENTY));
	}

	@Test(expected = IllegalStateException.class)
	public void whenWithdrawOnNotInitialisedThenIllegalStateException() throws Exception {
		cashController.withdraw(new BigDecimal(100));
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenWithdrawNegativeThenIllegalArgumentException() throws Exception {
		cashController.add(Note.FIFTY, 26);
		cashController.withdraw(new BigDecimal(-100));
	}

	@Test
	public void whenGetCountThenReturnAmount() {
		Assert.assertEquals(0, cashController.getCount(Note.FIFTY));
		Assert.assertEquals(0, cashController.getCount(Note.TWENTY));
		cashController.add(Note.FIFTY, 1);
		cashController.add(Note.TWENTY, 3);
		Assert.assertEquals(1, cashController.getCount(Note.FIFTY));
		Assert.assertEquals(3, cashController.getCount(Note.TWENTY));
	}

	@Test
	public void whenWithdrawThenWithdrawCorrectAmount() throws Exception {
		cashController.add(Note.FIFTY, 100);
		cashController.add(Note.TWENTY, 100);
		cashController.withdraw(new BigDecimal(20));
		Assert.assertEquals(99, cashController.getCount(Note.TWENTY));
		cashController.withdraw(new BigDecimal(40));
		Assert.assertEquals(97, cashController.getCount(Note.TWENTY));
		cashController.withdraw(new BigDecimal(50));
		Assert.assertEquals(99, cashController.getCount(Note.FIFTY));
		cashController.withdraw(new BigDecimal(70));
		Assert.assertEquals(98, cashController.getCount(Note.FIFTY));
		Assert.assertEquals(96, cashController.getCount(Note.TWENTY));
		cashController.withdraw(new BigDecimal(100));
		Assert.assertEquals(96, cashController.getCount(Note.FIFTY));
		cashController.withdraw(new BigDecimal(150));
		Assert.assertEquals(93, cashController.getCount(Note.FIFTY));
	}

	@Test
	public void whenWithdrawUnavailableOnFirstDenomThenWithdrawCorrectOnOther() throws Exception {
		cashController.add(Note.FIFTY, 0);
		cashController.add(Note.TWENTY, 10);
		cashController.withdraw(new BigDecimal(200));
		Assert.assertEquals(0, cashController.getCount(Note.TWENTY));
		Assert.assertEquals(0, cashController.getCount(Note.FIFTY));
	}

	@Test(expected = CurrencyCombinationException.class)
	public void whenCantMakeAmountThenCurrencyComboException() throws Exception {
		cashController.add(Note.FIFTY, 10);
		cashController.add(Note.TWENTY, 10);
		cashController.withdraw(new BigDecimal(30));
	}

}
