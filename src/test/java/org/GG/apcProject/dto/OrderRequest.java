package org.GG.apcProject.dto;

import java.util.List;

public class OrderRequest {

    private Long userId; // ID of the user placing the order
    private List<OrderItemRequest> items; // List of products + quantity
    private String paymentMethod; // e.g., "CARD", "UPI", "COD"

    // ---------------- Getters and Setters ----------------
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
