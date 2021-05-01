package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Topic;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    @Test
    public void testCreateNewComment(){
        int before = commentRepository.findAll().size();
        User randomUsr = DatabaseUtils.generateRandomObjFromDb(userRepository,userRepository.findAll().iterator().next().getUserId());

        Post randomPost = postRepository.findById(2).get();
        //Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());
        String content = NumberStringUtils.generateRandomString(15,false,true,true,true);
        Comment comment = new Comment().builder()
                .commenter(randomUsr)
                .content(content)
                .post(randomPost)
                .createdOn(new Date())
                .build();
        commentRepository.save(comment);

        randomPost.addComment(comment);
        int after = commentRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }

//    @Test
//    public void testDeleteComment(){
//        int before = commentRepository.findAll().size();
//        Comment cmt = commentRepository.findById(3).get();
//        commentRepository.delete(cmt);
//        int after = commentRepository.findAll().size();
//        assertThat(before).isEqualTo(after+1);
//    }

}
