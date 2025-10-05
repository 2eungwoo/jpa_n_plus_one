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
    def "1. Batch Size 테스트 (application.yml 설정)"() {
        given:
        println "\n\n--- [실행] 1. Batch Size 테스트 ---"
        println "(주의: application.yml의 default_batch_fetch_size=100 설정이 적용됩니다.)"

        when:
        println "--- Post 조회 (1번의 쿼리) ---"
        List<Post> posts = postRepository.findAllPosts()

        then:
        println "--- 각 Post의 Comment 접근 시, IN 절을 사용해 한번에 조회 (1번의 추가 쿼리) ---"
        posts.each { post ->
            println "[로그] Post: ${post.getTitle()}, 댓글 수: ${post.getComments().size()}"
        }
        println "--- [종료] 1. Batch Size 테스트 ---"
    }

    @Transactional
    def "2. Fetch Join 테스트"() {
        given:
        println "\n\n--- [실행] 2. Fetch Join 테스트 ---"

        when:
        println "--- Post와 Comment를 한번에 조회 (1번의 쿼리) ---"
        List<Post> posts = postRepository.findAllWithFetchJoin()

        then:
        println "--- 이미 로딩된 Comment 사용 (추가 쿼리 없음) ---"
        posts.each { post ->
            println "[로그] Post: ${post.getTitle()}, 댓글 수: ${post.getComments().size()}"
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
        println "--- 이미 로딩된 Comment 사용 (추가 쿼리 없음) ---"
        posts.each { post ->
            println "[로그] Post: ${post.getTitle()}, 댓글 수: ${post.getComments().size()}"
        }
        println "--- [종료] 3. EntityGraph 테스트 ---"
    }
}