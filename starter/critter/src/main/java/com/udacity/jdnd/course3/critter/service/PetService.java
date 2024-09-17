package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    // petService constructor
    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    // save pet
    public Pet save(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer owner = pet.getOwner();

        // null check
        if (owner != null) {
            owner.addPet(savedPet);
            customerRepository.save(owner);
        }

        return savedPet;
    }

    public Pet getPet(long petId) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        return petOptional.orElseGet(Pet::new);
    }

    // get pets by owner id
    public List<Pet> getPetsByOwner(long ownerId) {
        Optional<Customer> customerOptional = customerRepository.findById(ownerId);
        return customerOptional.map(Customer::getPets).orElse(new ArrayList<>());
    }

    // find pets by id
    public Optional<Pet> findById(Long petId) {
        return petRepository.findById(petId);
    }

    // get all pets in list
    public List<Pet> getPets() {
        Iterable<Pet> petIterable = petRepository.findAll();
        List<Pet> pets = new ArrayList<>();

        // add pets to pet list
        for (Pet pet : petIterable) {
            pets.add(pet);
        }

        return pets;
    }
}