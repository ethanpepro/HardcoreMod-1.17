package com.ethanpepro.hardcoremod.api.temperature;

// TODO: Ought to be able to dynamically modify these ranges during runtime, the rest of the code works for whatever values these are
public enum TemperatureRange {
    FREEZING(-7, -5),
    CHILLY(-4, -2),
    TEMPERATE(-1, 1),
    HOT(2, 4),
    BURNING(5, 7);

    private final int lowerBound;
    private final int upperBound;

    TemperatureRange(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public boolean isInRange(int temperature) {
        return (temperature >= this.lowerBound && temperature <= this.upperBound);
    }

    public int getLowerBound() {
        return this.lowerBound;
    }

    public int getMiddle() {
        return (this.lowerBound + this.upperBound) / 2;
    }

    public int getUpperBound() {
        return this.upperBound;
    }
}
