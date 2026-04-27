package com.bigdata.second.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    @NotBlank(message = "Telephone cannot be blank")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Telephone must be a valid phone number (10-15 digits)")
    private String telephone;
}