package farmsim.money;

import java.util.Observable;

/**
 * Basic implementation for storing how much money you have.
 *
 * @author wondertroy
 *
 */
public class Money extends Observable {
    private long decoCoins;

    public Money(long defaultBalance) {
        this.decoCoins = defaultBalance;
    }

    public Money() {
        this(1000);
    }

    /**
     * Update balance from farm client.
     */
    public void update() {
        handleObservers();
    }

    /**
     * Set the amount of Deco Coins that you have
     */
    public void setAmount(long amount) {
        decoCoins = amount;
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
