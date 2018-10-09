package com.wns.cashdispense.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.wns.cashdispense.exception.CurrencyCombinationException;
import com.wns.cashdispense.exception.InsufficientFundsException;
import com.wns.cashdispense.util.Money;

public class CashController {

	/* Store all the currency value & count */
	private Map<Money, Integer> count;

	public CashController() {
		this.count = new HashMap<>();
	}

	public CashController(Map<Money, Integer> count) {
		this.count = count;
	}

	/* Add Currency and count to bank fund */
	public void add(Money denomination, int amount) {
		if (count.containsKey(denomination)) {
			Integer total = count.get(denomination) + amount;
			count.put(denomination, total);
		} else {
			count.put(denomination, amount);
		}
	}

	/* Withdraw money from available bank fund */
	public void withdraw(BigDecimal withdrawAmount) throws IllegalStateException, IllegalArgumentException,
			CurrencyCombinationException, InsufficientFundsException {
		if (count.isEmpty()) {
			throw new IllegalStateException("Must be initialised with money before a withdraw can occur");
		}
		if (withdrawAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Cannot withdraw a negative amount");
		}
		Map<Money, Integer> toWithdraw = calculateWithdraw(withdrawAmount.intValue(), false);
		deduct(toWithdraw);
	}

	private Map<Money, Integer> calculateWithdraw(int withdrawAmount, boolean combination)
			throws InsufficientFundsException, CurrencyCombinationException {
		Map<Money, Integer> toWithdraw = new HashMap<>();
		List<Money> denominations = count.entrySet().stream().filter(entry -> entry.getValue() > 0)
				.map(Map.Entry::getKey).collect(Collectors.toList());
		// reverse sort denominations
		if (combination == false) {
			denominations.sort(Comparator.comparing(Money::getValue).reversed());
		}

		int amount = withdrawAmount;

		for (int i = 0; i < denominations.size(); i++) {
			int countVal = count.get(denominations.get(i));
			if (count.get(denominations.get(i)) > 0) {
				// Withdraw amount divided by each money sets on above
				int c = (amount / denominations.get(i).getValue().intValue());
				// a reminder will set again withdrawal amount.
				amount = amount % denominations.get(i).getValue().intValue();
				if (countVal < c) {
					amount += ((c - countVal) * denominations.get(i).getValue().intValue());
					c = c - (c - countVal);
				}
				if (countVal >= c) {
					toWithdraw.put(denominations.get(i), c);
				}
			}
		}
		if (!fundsAvailable(toWithdraw)) {
			throw new InsufficientFundsException("Insufficient funds to make withdrawal");
		} else if (!getTotal(toWithdraw).equals(new BigDecimal(withdrawAmount))) {
			if (combination == false) {
				calculateWithdraw(withdrawAmount, true);
			}
			throw new CurrencyCombinationException("Cannot make amount with available denominations");
		}
		return toWithdraw;
	}

	// Check fund availability in Bank
	private boolean fundsAvailable(Map<Money, Integer> toWithdraw) {
		for (Map.Entry<Money, Integer> entry : toWithdraw.entrySet()) {
			if (!count.containsKey(entry.getKey()))
				return false;
			if (count.get(entry.getKey()) < entry.getValue())
				return false;
		}
		return true;
	}

	// Deduct money from Bank fund after withdraw condition check
	private void deduct(Map<Money, Integer> toWithdraw) {
		toWithdraw.entrySet().forEach(entry -> {
			Integer deduction = count.get(entry.getKey()) - entry.getValue();
			count.put(entry.getKey(), deduction);
		});
	}

	public int getCount(Money denomination) {
		return count.containsKey(denomination) ? count.get(denomination) : 0;
	}

	public int getTotalCount() {
		int sum = 0;
		for (Map.Entry<Money, Integer> entry : count.entrySet()) {
			sum += getCount(entry.getKey());
		}
		return sum;
	}

	public void report(Money denomination) {
		System.out.printf(" %s Notes Count  %d\n", denomination.toString(), getCount(denomination));
	}

	public void reportAll() {
		count.keySet().forEach(this::report);
	}

	private BigDecimal getTotal(Map<Money, Integer> count) {
		BigDecimal sum = BigDecimal.ZERO;
		for (Map.Entry<Money, Integer> entry : count.entrySet()) {
			sum = sum.add(entry.getKey().getValue().multiply(new BigDecimal(entry.getValue().intValue())));
		}
		return sum;
	}

	public BigDecimal getTotal() {
		return getTotal(count);
	}

}
