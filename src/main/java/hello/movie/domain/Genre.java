package hello.movie.domain;

public enum Genre {
    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    MUSIC(10402),
    MYSTERY(9648),
    ROMANCE(10749),
    SCIENCE_FICTION(878),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(32);

    private final long id;

    Genre(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    // id로 Genre 값 가져옴
    public static Genre fromId(long id) {
        for (Genre genre : Genre.values()) {
            if (genre.getId() == id) {
                return genre;
            }
        }
        return null;
    }

    // genre 문자열로 Genre 값 가져옴
    public static Genre fromString(String genre) {
        for (Genre g : Genre.values()) {
            if (g.name().equalsIgnoreCase(genre)) {
                return g;
            }
        }
        // 일치하는 Genre 없음
        return null;
    }
}
