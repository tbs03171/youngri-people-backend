package hello.movie.domain;

import jakarta.persistence.*;

@Entity
public class Genre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GENRE_ID")
    private Long id;

    private String name;
}
