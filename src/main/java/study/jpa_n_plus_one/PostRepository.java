package study.jpa_n_plus_one;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 베이스 쿼리
    @Query("select p from Post p")
    List<Post> findAllPosts();

    // Fetch Join
    @Query("select distinct p from Post p join fetch p.comments")
    List<Post> findAllWithFetchJoin();

    // EntityGraph
    @EntityGraph(attributePaths = {"comments"})
    @Query("select p from Post p")
    List<Post> findAllWithEntityGraph();
}
