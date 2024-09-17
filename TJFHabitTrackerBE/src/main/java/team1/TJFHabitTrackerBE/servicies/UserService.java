package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.exceptions.BadRequestException;
import team1.TJFHabitTrackerBE.exceptions.NotFoundException;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserDTO;
import team1.TJFHabitTrackerBE.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;





    public Page<User> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User saveUser(UserDTO body) {
        this.userRepository.findByEmail(body.email()).ifPresent(utente -> {
            throw new BadRequestException("The user with email: " + body.email() + ", already exist.");
        });

        User user = new User(body.id(), body.email(), body.createdAt(), body.updatedAt());

        return userRepository.save(user);
    }


    public User findById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findUserByIdAndDelete(UUID id) {
        User found = this.findById(id);
        this.userRepository.delete(found);
    }



    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("The user with email: " + email + ", already exist."));
    }


    public User changeEmail(UUID id, UserDTO body) {
        User found = this.findById(id);
        found.setEmail(body.email());
        System.out.println(body.email());
        return userRepository.save(found);
    }


    }

