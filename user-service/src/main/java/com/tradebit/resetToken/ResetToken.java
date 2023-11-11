package com.tradebit.resetToken;

import com.tradebit.user.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class ResetToken {
    // 15 minutes
    public static final int EXPIRATION = 15;

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String token;
    private Date expiryDate;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    public Date calculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public ResetToken(User user, String token){
        this.user = user;
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public ResetToken(){this.expiryDate = calculateExpiryDate(EXPIRATION);}

    public boolean isTokenValid(){
        return getExpiryDate().after(new Date());
    }
}