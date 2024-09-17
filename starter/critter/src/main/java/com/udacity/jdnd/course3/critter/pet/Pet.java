package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor()
@Builder
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private PetType type;

    @ManyToOne
    private Customer owner;

    private LocalDate birthDate;
    private String notes;

    public Pet(String name, PetType type, Customer owner, LocalDate birthDate, String notes) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.birthDate = birthDate;
        this.notes = notes;
    }

}