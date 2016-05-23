package com.russmiles.antifragilesoftware.samples.rps;

/**
 * Created by gtarrant-fisher on 12/05/2016.
 */
public enum Move {
    rock, paper, scissor;

    public boolean defeats(Move other) {
        switch (this) {
            case rock:
                return other == scissor;
            case paper:
                return other == rock;
            case scissor:
                return other == paper;
        }
        return false;
    }
}
