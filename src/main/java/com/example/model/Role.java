package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType roleType;

    @OneToMany(mappedBy = "role")
    private List<User> users=new ArrayList<>();
}
