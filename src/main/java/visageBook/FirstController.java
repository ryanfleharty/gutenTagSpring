package visageBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class FirstController {

    @Autowired
    private PostRepository postRepository;

    // Get All
    @GetMapping("/posts")
    public Iterable<Post> postsIndex(){
        return postRepository.findAll();
    }

    // Add Item Route POST
    @PostMapping("/posts")
    public Post newString(@RequestBody Post post){
        Post newPost = postRepository.save(post);
        return newPost;
    }

    // Show Route GET one /:id
    @GetMapping("/posts/{id}")
    public Post show (@PathVariable("id") Long id) throws Exception{
        Optional<Post> response = postRepository.findById(id);
        if(response.isPresent()){
            return response.get();
        }
        throw new Exception("no such post");
    }

    // Delete Route DELETE /:id
    @DeleteMapping("/posts/{id}")
    public String delete(@PathVariable("id") Long id){
        postRepository.deleteById(id);
        return("delete route hit, deleted" + id );
    }

    // Edit Item Route PUT /:id
    @PutMapping("/posts/{id}")
    public Post edit(@PathVariable("id") Long id, @RequestBody Post formData) throws Exception{
        Optional<Post> response = postRepository.findById(id);
        if(response.isPresent()){
            Post post = response.get();
            post.setText(formData.getText());
            return postRepository.save(post);
        }
        throw new Exception("no such post");
    }

}
