package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Comment;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

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
    public void testCreateNewComments(){
        int before = commentRepository.findAll().size();
        User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
        Post randomPost = DatabaseUtils.generateRandomObjFromDb(postRepository,postRepository.findAll().iterator().next().getPostId());

        String content = NumberStringUtils.generateRandomString(15,false,true,true,true);
        Comment commentOne = new Comment().builder()
                .commenter(randomUsr)
                .content(content)
                .post(randomPost)
                .createdOn(new Date())
                .build();
        Comment commentTwo = new Comment().builder()
                .commenter(randomUsr)
                .content(content)
                .post(randomPost)
                .createdOn(new Date())
                .build();
        commentRepository.saveAll(List.of(commentOne,commentTwo));

        randomPost.addComment(commentOne);
        randomPost.addComment(commentTwo);
        int after = commentRepository.findAll().size();
        assertThat(before).isEqualTo(after-2);
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
