package com.example.simplaws.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int age;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, columnDefinition = "varchar(8) default 'MALE'")
    private Gender gender;
}
