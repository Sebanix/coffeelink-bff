package com.coffeelink.bff.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "contraseña_hash")
    private String passwordHash;

    @Column(name = "rol")
    private String rol;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;
}