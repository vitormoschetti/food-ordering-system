package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isGreaterThanZero() {
        return this.validateNotNull() && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(final Money money) {
        return this.validateNotNull() && this.validateNotNull(money) && this.amount.compareTo(money.getAmount()) > 0;
    }

    private boolean validateNotNull() {
        return this.amount != null;
    }

    private boolean validateNotNull(final Money money) {
        return money.getAmount() != null;
    }

    public Money add(final Money money) {
        return new Money(this.setScale(this.amount.add(money.getAmount())));
    }

    public Money subtract(final Money money) {
        return new Money(this.setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(final Integer value) {
        return new Money(this.setScale(this.amount.multiply(new BigDecimal(value))));
    }

    private BigDecimal setScale(final BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
