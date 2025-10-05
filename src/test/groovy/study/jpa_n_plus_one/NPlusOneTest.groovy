package study.jpa_n_plus_one

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@SpringBootTest
class NPlusOneTest extends Specification {

    @Autowired
    private PostRepository postRepository

    @Autowired
    private CommentRepository commentRepository

    @Autowired
    private EntityManager em

    def setup() {
        (1..10).each { i ->
            def post = new Post("Post " + i)
            postRepository.save(post)
            (1..3).each { j ->
                def comment = new Comment("Comment " + j + " for Post " + i, post)
                commentRepository.save(comment)
            }
        }
        em.flush()
        em.clear()
        println "--- Test data setup complete ---"
    }

    @Transactional
    def "N+1 문제 테스트"() {
        given:
        println "\n\n--- START: N+1 문제 테스트 ---"

        when:
        println "--- 모든 Post 조회 ---"
        List<Post> posts = postRepository.findAll()

        then:
        println "--- Post를 순회하며 Comment 지연 로딩 발생 ---"
        posts.each { post ->
            println "[로그] Post: ${post.getTitle()}, 댓글 수: ${post.getComments().size()}"
        }
        println "--- END: N+1 문제 테스트 ---"
        true
    }
}
