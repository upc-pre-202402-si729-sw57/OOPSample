package com.acme.sample.sales.domain.model.aggregates;

import com.acme.sample.shared.domain.model.valueobjects.Address;

import java.util.List;
import java.util.UUID;

public class SalesOrder {
    private UUID internalId;
    private Address shippingAddress;
    private SalesOrderStatus status;
    private List<SalesOrderItem> items;
    private double paymentAmount;

    public SalesOrder() {
        this.internalId = generateInternalId();
        this.status = SalesOrderStatus.CREATED;
        paymentAmount = 0.0;
    }


    public void addItem(Long productId, int quantity, double unitPrice) {
        if (status == SalesOrderStatus.APPROVED)
            throw new IllegalStateException("Cannot add item to an approved order");
        items.add(new SalesOrderItem(productId, quantity, unitPrice));
    }

    public double calculateTotalPrice() {
        return items.stream().mapToDouble(SalesOrderItem::calculateItemPrice).sum();
    }

    public void verifyIfReadyForDispatch() {
        if (status == SalesOrderStatus.APPROVED) return;
        if (paymentAmount == calculateTotalPrice()) this.status = SalesOrderStatus.APPROVED;
    }

    public void addPayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Payment amount must be greater than zero");
        if (amount > calculateTotalPrice() - paymentAmount)
            throw new IllegalArgumentException("Payment amount exceeds the total price");
        paymentAmount += amount;
        verifyIfReadyForDispatch();
    }

    public void dispatch(String street, String number, String city, String state, String zipCode, String country) {
        verifyIfReadyForDispatch();
        if (status != SalesOrderStatus.APPROVED)
            throw new IllegalStateException("Cannot dispatch an order that is not approved");
        status = SalesOrderStatus.IN_PROGRESS;
        shippingAddress = new Address(street, number, city, state, zipCode, country);
        items.forEach(SalesOrderItem::dispatch);
    }

    public boolean isDispatched() {
        return items.stream().allMatch(SalesOrderItem::isDispatched);
    }

    public void verifyIfItemsAreDispatched() {
        if (isDispatched()) status = SalesOrderStatus.SHIPPED;
    }

    public void completeDelivery() {
        verifyIfItemsAreDispatched();
        if (status == SalesOrderStatus.SHIPPED) status = SalesOrderStatus.DELIVERED;
    }

    public void cancel() {
        if (status == SalesOrderStatus.DELIVERED)
            throw new IllegalStateException("Cannot cancel a delivered order");
        status = SalesOrderStatus.CANCELLED;
    }

    private static UUID generateInternalId() {
        return UUID.randomUUID();
    }
}
