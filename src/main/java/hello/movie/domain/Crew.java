package hello.movie.domain;

import jakarta.persistence.*;

@Entity
public class Crew {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREW_ID")
    private Long id;

    private String name;

    private String profilePath;
}
