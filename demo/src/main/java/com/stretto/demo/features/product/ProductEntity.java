package com.stretto.demo.features.product;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long ProductId;

private String name;



}
