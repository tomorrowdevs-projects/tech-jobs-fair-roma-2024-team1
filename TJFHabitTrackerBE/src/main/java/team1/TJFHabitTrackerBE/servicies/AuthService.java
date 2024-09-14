package team1.TJFHabitTrackerBE.servicies;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team1.TJFHabitTrackerBE.entities.User;
import team1.TJFHabitTrackerBE.exceptions.UnauthorizedException;
import team1.TJFHabitTrackerBE.payload.UsersDTO.UserLoginDTO;
import team1.TJFHabitTrackerBE.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bCrypt;

    public String authenticateUtenteAndGenerateToken(UserLoginDTO payload) {
        User user = this.userService.findByEmail(payload.email());
        if (bCrypt.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non corrette");
        }
    }
}
