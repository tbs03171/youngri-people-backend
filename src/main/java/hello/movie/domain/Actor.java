package hello.movie.domain;

import jakarta.persistence.*;

@Entity
public class Actor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTOR_ID")
    private Long id;

    private String name;

    private String profilePath;
}
