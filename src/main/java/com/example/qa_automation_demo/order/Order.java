package com.example.qa_automation_demo.order;

import com.example.qa_automation_demo.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order() {
    }

    public Order(String product, Integer amount, User user) {
        this.product = product;
        this.amount = amount;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public Integer getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }
}