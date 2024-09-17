package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private final PetService petService;
    @Autowired
    private final UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        String name = petDTO.getName();
        PetType type = petDTO.getType();
        Long ownerId = petDTO.getOwnerId();
        LocalDate birthDate = petDTO.getBirthDate();
        String notes = petDTO.getNotes();
        Optional<Customer> customerOptional = userService.getUser(ownerId);
        Pet pet = new Pet(name, type, customerOptional.orElse(new Customer()), birthDate, notes);
        long petDTOId = petDTO.getId();
        if (petDTOId != 0) {
            pet = petService.getPet(petDTOId);
        }

        Long id = petService.save(pet);
        petDTO.setId(id);

        return petDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPet(petId);

        return getPetDTO(pet);
    }

    private PetDTO getPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setName(pet.getName());
        petDTO.setNotes(pet.getNotes());
        petDTO.setOwnerId(pet.getOwner().getId());
        petDTO.setType(pet.getType());
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<Pet> pets = petService.getPets();

        return pets.stream().map(this::getPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        return pets.stream().map(this::getPetDTO).collect(Collectors.toList());
    }

    @PostMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId, @RequestBody PetDTO petDTO) {
        Pet pet = petService.getPet(petId);
        pet.setNotes(petDTO.getNotes());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setType(petDTO.getType());
        Optional<Customer> customerOptional = userService.getUser(petDTO.getOwnerId());
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            petDTO.setOwnerId(customer.getId());
        }
        petService.save(pet);
        petDTO.setId(petId);

        return petDTO;
    }
}