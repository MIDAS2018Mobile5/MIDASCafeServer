package com.midas2018mobile5.serverapp.Model.External.Account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(nullable = false, length = 20)
    private String userid;

    @Column(nullable = false, length = 500)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format("User[id='%s', username='%s']", userid, username);
    }
}
