package com.shared;

public class GenericMove<T> {
    public Cord from;
    public Cord to;
    public T Player;
    public Stack<Cord> conversions;

    public GenericMove(Cord from, Cord to){
        this.to = to;
        this.from = from;
    }

    public GenericMove(Cord from, Cord to, T player, Stack<Cord> conversions){
        this.to = to;
        this.from = from;
        this.Player = player;
        this.conversions  = conversions;
    }
}
