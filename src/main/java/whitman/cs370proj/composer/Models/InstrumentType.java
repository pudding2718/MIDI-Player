package whitman.cs370proj.composer.Models;

import javafx.util.Pair;

public enum InstrumentType {
    Piano(new Pair<>(0, 1)),
    Harpsichord(new Pair<>(1, 7)),
    Marimba(new Pair<>(2, 13)),
    ChurchOrgan(new Pair<>(3, 20)),
    Accordion(new Pair<>(4, 22)),
    Guitar(new Pair<>(5, 25)),
    Violin(new Pair<>(6, 41)),
    FrenchHorn(new Pair<>(7, 61));

    private final Pair<Integer, Integer> value;

    InstrumentType(Pair<Integer, Integer> value) {
        this.value = value;
    }

    public Pair<Integer, Integer> getValue() {
        return value;
    }
}
