package com.midas2018mobile5.serverapp.Model.External;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @Column(nullable = false, length = 20)
    public String username;

    @Column(nullable = false)
    public String password;

    protected Member() { }

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User[id='%d', name='%s']", Id, username);
    }
}
