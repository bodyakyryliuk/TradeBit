package com.tradebit.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "total_balance")
public class TotalBalance {
    @Id
    @Column
    private String id;

    @Column(name = "balance")
    private Double totalBalance;

    @Column(name = "time_stamp")
    private Date timeStamp;

    @Column(name = "user_id", unique = true)
    private String userId;

    @PrePersist
    @PostLoad
    private void generateId() {
        if (this.userId != null && this.timeStamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            this.id = this.userId + "_" + sdf.format(this.timeStamp);
        }
    }


}


