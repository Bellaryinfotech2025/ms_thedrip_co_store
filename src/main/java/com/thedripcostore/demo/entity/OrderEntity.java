package com.thedripcostore.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "drip_co_orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_id", unique=true, nullable=false, length=40)
    private String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

    @Column(name="user_id", nullable=false)
    private String userId;

    @Column(name="address_id", nullable=false)
    private String addressId;

    @Column(name="product_id", nullable=false)
    private String productId;

    @Column(name="product_title", nullable=false)
    private String productTitle;

    @Column(name="product_category")
    private String productCategory;

    @Column(name="product_size")
    private String productSize;

    @Column(name="unit_price", nullable=false)
    private Double unitPrice;

    @Column(nullable=false)
    private Integer quantity = 1;

    @Column(nullable=false)
    private Double subtotal;

    @Column(nullable=false)
    private String status = "PENDING";

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Helper to calculate subtotal
    public void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            this.subtotal = unitPrice * quantity;
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    
}
