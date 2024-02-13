package uk.co.jkinc.Vaultier;

import org.bukkit.entity.Player;

import java.io.Serializable;

public class Transaction implements Serializable {
    public enum Status {
        PENDING,
        SUCCESS,
        REJECTED,
        TIMEDOUT,
    }
    public Status state = Status.PENDING;
    public int value;
    public Player Payer;
    public Player Payee;
}
