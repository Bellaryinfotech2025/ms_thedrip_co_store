package com.thedripcostore.demo.dto;

public class OrderRequestDto {

    private String userId;
    private String addressId;
    private String productId;
    private String productTitle;
    private String productCategory;
    private String productSize;
    private Double unitPrice;
    private Integer quantity;

    // ===== Getters & Setters =====
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public String getProductSize() { return productSize; }
    public void setProductSize(String productSize) { this.productSize = productSize; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
