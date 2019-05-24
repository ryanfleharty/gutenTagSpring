package visageBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/users")
    public Iterable<User> usersIndex(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public HashMap<String, Object> findUser(@PathVariable("id") Long id)throws Exception{
        Optional<User> response = userRepository.findById(id);
        if(response.isPresent()){
            User user = response.get();
            Iterable<Post> posts = postRepository.findByUser(user);
            HashMap<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("posts", posts);
            return result;
        }
        throw new Exception("no such user");
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User login, HttpSession session) throws Exception {
        User user = userRepository.findByUsername(login.getUsername());
        if(user ==  null){
            throw new Exception("Invalid Credentials");
        }
        boolean valid = bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword());
        if(valid){
            session.setAttribute("username", user.getUsername());
            return user;
        }else{
            throw new Exception("Invalid Credentials");
        }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userRepository.deleteById(id);
        return "deleted user " + id;
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User formData, @PathVariable("id") Long id) throws Exception{
        Optional<User> response = userRepository.findById(id);
        if(response.isPresent()){
            User user = response.get();
            user.setUsername(formData.getUsername());
            user.setPassword(formData.getPassword());
            return userRepository.save(user);
        }
        throw new Exception("no such user");
    }

}
