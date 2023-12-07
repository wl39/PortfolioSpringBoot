package com.wl39.portfolio.user;

import jakarta.persistence.*;

import java.security.MessageDigest;
import java.util.Arrays;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String username;
    @Column(columnDefinition="BINARY(16) NOT NULL")
    private byte[] password;

    public User() {

    }

    public User(String username, byte[] password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + Arrays.toString(password) + '\'' +
                '}';
    }
}
