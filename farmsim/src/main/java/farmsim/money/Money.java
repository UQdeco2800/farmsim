package farmsim.money;

import common.client.FarmClient;
import common.exception.UnauthenticatedUserException;

import java.util.Observable;

/**
 * Basic implementation for storing how much money you have.
 *
 * @author wondertroy
 *
 */
public class Money extends Observable {
    private long decoCoins;
    private  FarmClient client;

    public Money(FarmClient client, long defaultBalance) {
        this.client = client;
        this.decoCoins = defaultBalance;
    }

    public Money(FarmClient client) {
        this(client, 1000);
    }

    /**
     * Update balance from farm client.
     */
    public void update() {
        try {
            decoCoins = client.getWallet();
            handleObservers();
        } catch (UnauthenticatedUserException e) {
        }
    }

    /**
     * Set the amount of Deco Coins that you have
     */
    public void setAmount(long amount) {
        decoCoins = amount;
        try {
            client.setWallet(amount);
        } catch (UnauthenticatedUserException e) {
        }
        handleObservers();
    }

    /**
     * returns the amount of deco coins you have
     *
     * @return int value
     */
    public long getAmount() {
        return decoCoins;
    }

    /**
     * Add an amount of deco coins
     */
    public void addAmount(long amount) {
        setAmount(decoCoins + amount);
    }

    /**
     * Attempts to subtract amount of deco coins. Fails if attempting to
     * subtract more than you have.
     *
     * @param amount
     * @return True if successful, false if not enough balance
     */
    public Boolean subtractAmount(long amount) {
        if (decoCoins - amount >= 0) {
            setAmount(decoCoins - amount);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Notifies the wallet observer that the amount of money has changed and must be
     * updated
     */
    public void handleObservers() {
        setChanged();
        notifyObservers();
    }
}
