package at.ac.fhcampuswien.fhmdb.models;

public enum Rating {
    RATING_0(0),
    RATING_1(1),
    RATING_2(2),
    RATING_3(3),
    RATING_4(4),
    RATING_5(5),
    RATING_6(6),
    RATING_7(7),
    RATING_8(8),
    RATING_9(9);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
