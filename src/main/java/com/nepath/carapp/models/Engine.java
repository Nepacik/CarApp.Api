package com.nepath.carapp.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "engines")
@NoArgsConstructor
@Getter
@Setter
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "engine")
    private List<Car> cars;
}