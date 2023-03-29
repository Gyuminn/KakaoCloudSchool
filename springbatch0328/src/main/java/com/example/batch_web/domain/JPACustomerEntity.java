package com.example.batch_web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="customer")
public class JPACustomerEntity {
    @Id
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "middleInitial")
    private String middleInitial;

    @Column(name = "lastName")
    private String lastName;

    private String address;
    private String city;
    private String state;

    @Column(name = "zipCode")
    private String zipCode;
}
