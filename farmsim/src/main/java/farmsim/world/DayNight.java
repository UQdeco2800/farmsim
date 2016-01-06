package farmsim.world;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import farmsim.util.Tickable;
import javafx.application.Platform;
import farmsim.ui.Notification;

/**
* An implementation that keeps track of time, the date and the day night status
**/
public class DayNight extends Observable implements Tickable {
	private int counter = 0;
	private boolean isDay = true;
	private int days = 0;
	private int hours = 8;
	private int weeks = 0;
	private int month = 1;
	private int years = 0;
	private ArrayList<TimeListener> dayListeners = new ArrayList<TimeListener>();
	private ArrayList<TimeListener> weekListeners = new ArrayList<TimeListener>();
	private ArrayList<TimeListener> hourListeners = new ArrayList<TimeListener>();
	private ArrayList<TimeListener> monthListeners = new ArrayList<TimeListener>();
	private ArrayList<TimeListener> yearListeners = new ArrayList<TimeListener>();

	// How many real world seconds it takes for 1 in game hour to pass.
	private int secondsInHour = 5;

	// how many ticks it takes for 1 in game hour to pass.
	private int hourLength = 20 * secondsInHour;
	/**
	* runs every tick to update time status
	*/
	@Override
	public void tick() {
		counter++;
		World world = WorldManager.getInstance().getWorld();
		for (int y = 0; y < world.getHeight(); y++) {
			for (int x = 0; x < world.getWidth(); x++) {
				world.getTile(x, y).tick();
			}
		}
		// increment hours
		if (counter == hourLength) {
			hours++;
			hourTick(hours);
			counter = 0;
		}

		// switch to night time
		if (hours == 12) {
			isDay = false;
		}

		// Increment day
		if (hours == 24) {
			days++;
			hours = 0;
			isDay = true;
			dayTick(days);
			// Increment Week
			if (days % 7 == 0 && days != 0) {
				weeks++;
				weekTick(weeks);
			}
			// Increment Month
			if (weeks % 4 == 0 && days % 7 == 0) {
				month++;
				monthTick(month);
				// Increment Year
				if (month % 12 == 0 && month != 0) {
					years++;
					month = 0;
				}
			}
		}
	}
	
	/**
	* called at start of every month to call methods that
	* need to be called monthly. put methods to be called
	* monthly here.
	*/
	private void monthTick(int month) {
		// put methods you want to be called monthly here.
		World world = WorldManager.getInstance().getWorld();
		for(TimeListener listener:monthListeners) {
			listener.notifyListener();
		}

	}
	
	/**
	* handles methods that are called weekly. Put methods
	* to be called weekly here.
	*/
	private void weekTick(int weeks) {
		// put methods you want to be called weekly in here.
		for(TimeListener listener:weekListeners) {
			listener.notifyListener();
		}
		WorldManager.getInstance().getWorld().resetAvailableContracts();
		Platform.runLater(() -> {
			Notification.makeNotification("Contracts", "New contracts have become available");
        });

	}
	
	/**
	* handles methods that are called hourly. Put methods
	* to be called hourly here.
	*/
	private void hourTick(int hours) {
		// put methods you want to be called hourly in here.
		handleObservers();
		for(TimeListener listener:hourListeners) {
			listener.notifyListener();
		}
	}

	/**
	 * Updates external classes on a daily basis.
	 */
	private void dayTick(int days) {
		World world = WorldManager.getInstance().getWorld();
		if (days == 1) {
			world.resetAvailableContracts();
			Platform.runLater(() -> {
				Notification.makeNotification("Contracts", "New contracts have become available");
	        });
		}
		world.getActiveContracts().checkContracts();
		for(TimeListener listener:dayListeners) {
			listener.notifyListener();
		}
		
		

	}

	/**
	 * @return True if it is day time, False if it is night time
	 */
	public boolean isDay() {
		return isDay;
	}

	/**
	 * @return int number of hours passed.
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @return int number of days passed.
	 */
	public int getDays() {
		int day = days + 1;
		return day;
	}

	/**
	 * @return int number of weeks passed.
	 */
	public int getWeeks() {
		return weeks;
	}

	/**
	 * @return int number of months passed.
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return int number of years passed.
	 */
	public int getYears() {
		return years;
	}

	/**
	 * Notifies the DayNum observer that number of days has changed and must be
	 * updated
	 */
	public void handleObservers() {
		setChanged();
		notifyObservers();
	}
	
	/**
	* Adds listener to be notified on a daily basis.
	*/
	public void registerDailyTask(TimeListener listener) {
		dayListeners.add(listener);
	}
	
	/**
	* Adds listener to be notified on a hourly basis.
	*/
	public void registerHourlyTask(TimeListener listener) {
		hourListeners.add(listener);
	}
	
	/**
	* Adds listener to be notified on a weekly basis.
	*/
	public void registerWeeklyTask(TimeListener listener) {
		weekListeners.add(listener);
	}
	
	/**
	* Adds listener to be notified on a monthly basis.
	*/
	public void registerMonthlyTask(TimeListener listener) {
		monthListeners.add(listener);
	}
	
	/**
	* Adds listener to be notified on a yearly basis.
	*/
	public void registerYearlyTask(TimeListener listener){
   	 yearListeners.add(listener);
   }
	
	/**
	* notifies listener.
	*/
	public interface TimeListener {
		public void notifyListener();
	}
	
	/**
	 * for debugging only -> speeds up game a lot
	 */
	public void setSpeed() {
		secondsInHour = 1;
	}
}
