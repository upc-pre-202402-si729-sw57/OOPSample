package com.acme.sample.sales.domain.model.aggregates;

import java.util.UUID;

public class SalesOrderItem {
    private UUID itemId;
    private Long productId;
    private int quantity;
    private double unitPrice;
    private boolean dispatched;

    public SalesOrderItem(Long productId, int quantity, double unitPrice) {
        if (productId == null || productId <= 0) throw new IllegalArgumentException("Product Id must be greater than 0");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        if (unitPrice <= 0) throw new IllegalArgumentException("Unit Price must be greater than 0");

        this.itemId = generateItemId();
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.dispatched = false;
    }

    public double calculateItemPrice() {
        return quantity * unitPrice;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public void dispatch() {
        this.dispatched = true;
    }

    private static UUID generateItemId() {
        return UUID.randomUUID();
    }



}
