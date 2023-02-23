package com.shared;

public class GenericMove<T> {
    Cord from;
    Cord to;
    T Player;

    public GenericMove(Cord from, Cord to){
        this.to = to;
        this.from = from;
    }

    public GenericMove(Cord from, Cord to, T player){
        this.to = to;
        this.from = from;
        this.Player = player;
    }
}
