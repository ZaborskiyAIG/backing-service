package com.backing.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "accountNumber"})
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String accountNumber;

    @Column(nullable = false, length = 4)
    private String pinCode;

    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    public BankAccount(String pinCode, Beneficiary beneficiary, String accountNumber) {
        this.pinCode = pinCode;
        this.beneficiary = beneficiary;
        this.accountNumber = accountNumber;
    }

    public void plusBalance(BigDecimal price) {
        balance = balance.add(price);
    }

    public void minusBalance(BigDecimal price) {
        balance = balance.subtract(price);
    }
}