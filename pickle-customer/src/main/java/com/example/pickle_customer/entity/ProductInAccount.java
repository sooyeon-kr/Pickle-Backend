package com.example.pickle_customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInAccount {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int productId;

        private String productName;

        private String productCode;

        private int heldQuantity;

        private long purchaseAmount;

        private long evaluationAmount;

        private long profitAmount;

        private double profitMargin;

        private String themeName;

        private double ratioInCategory;

        private String categoryName;


        @ManyToOne
        @JoinColumn(name = "account_id", referencedColumnName = "accountId")
        private Account account;

    }
