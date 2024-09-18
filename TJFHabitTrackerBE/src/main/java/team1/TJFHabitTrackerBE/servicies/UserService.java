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
import team1.TJFHabitTrackerBE.security.JwtTool;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTool jwtTool;




    public Page<User> getAllUsers(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public String saveUser(UserDTO body) {
        if(userRepository.findById(body.id()).isPresent()){
            User user = findById(body.id());
            return jwtTool.createToken(user);
        }
        User user = new User(body.id(), body.email(), body.createdAt(), body.updatedAt());
        userRepository.save(user);
        return jwtTool.createToken(user);
    }


    public User findById(String id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findUserByIdAndDelete(String id) {
        User found = this.findById(id);
        this.userRepository.delete(found);
    }



    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("The user with email: " + email + ", already exist."));
    }





    }

