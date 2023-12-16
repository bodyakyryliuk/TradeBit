package com.tradebit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "total_balance")
public class TotalBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "balance")
    private Double totalBalance;

    @Column(name = "time_stamp")
    private Date timeStamp;

    @Column(name = "user_id", unique = true)
    private String userId;


}


