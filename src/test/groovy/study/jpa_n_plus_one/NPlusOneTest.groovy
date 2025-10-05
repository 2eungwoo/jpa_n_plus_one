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
        (1..5).each { i ->
            def post = new Post("Post " + i)
            postRepository.save(post)
            (1..3).each { j ->
                def comment = new Comment("Comment " + j + " for Post " + i, post)
                commentRepository.save(comment)
            }
        }
        em.flush()
        em.clear()
        println "--- setup() 종료 ---"
    }

    @Transactional
    def "1. Batch Size 테스트 (배치사이즈20)"() {
        given:
        println "\n\n--- [실행] 1. Batch Size 테스트 ---"

        when:
        println "--- findAll() 조회 ---"
        List<Post> posts = postRepository.findAllPosts()

        then:
        println "--- batch size 적용한 연관관계 접근 -> In절 사용---"
        posts.each { post ->
            post.getComments().size()
        }
        println "--- [종료] Batch Size 테스트 ---"
    }

    @Transactional
    def "2. Fetch Join 테스트"() {
        given:
        println "\n\n--- [실행] 2. Fetch Join 테스트 ---"

        when:
        println "--- findAll() 조회 ---"
        List<Post> posts = postRepository.findAllWithFetchJoin()

        then:
        println "--- 이미 로딩된 Comment 사용 ---"
        posts.each { post ->
            post.getComments().size()
        }
        println "--- [종료] 2. Fetch Join 테스트 ---"
    }

    @Transactional
    def "3. EntityGraph 테스트"() {
        given:
        println "\n\n--- [실행] 3. EntityGraph 테스트 ---"

        when:
        println "--- Post와 Comment를 한번에 조회 (1번의 쿼리) ---"
        List<Post> posts = postRepository.findAllWithEntityGraph()

        then:
        println "--- 이미 로딩된 Comment 사용  ---"
        posts.each { post ->
            post.getComments().size()
        }
        println "--- [종료] 3. EntityGraph 테스트 ---"
    }
}