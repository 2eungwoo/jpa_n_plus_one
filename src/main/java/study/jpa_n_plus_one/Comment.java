package study.jpa_n_plus_one;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }
}
