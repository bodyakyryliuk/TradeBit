package com.tradebit.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date")
    private Date timeStamp;

    @Column(name = "user_id")
    private String userId;

    @PrePersist
    @PostLoad
    private void generateId() {
        if (this.userId != null && this.timeStamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            this.id = this.userId + "_" + sdf.format(this.timeStamp);
        }
    }
}


