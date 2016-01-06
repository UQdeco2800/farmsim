package farmsim.world.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * A queue of upcoming weather events and relevant methods for populating and
 * accessing it. Used to control weather system, and makes the forecast feature
 * possible.
 */
public class WeatherQueue {
    private LinkedList<Element> queue; 
    private List<Element> forecast;
    private Weight weights;
    public static final int MIN_DURATION = 10; 
    public static final int MAX_DURATION = 30; 
    
    /**
     * Constructor for weather queue; creates a linked list to hold the weather
     * queue, adds a default weather, and calls methods to populate the initial
     * queue.
     */
    public WeatherQueue() {
        queue = new LinkedList<Element>();
        
        // Begin the game with Default weather
        queue.add(new Element(WeatherType.DEFAULT, selectDuration()));
        while (queue.size() < ((168 / MIN_DURATION) + 1)) {
            addNew();
        }
        forecast = buildForecast();
    }
    
    /**
     * Generate a weather to add to add on the queue, based on season and last 
     * queue. 
     */
    public Element generate() {
        Element result;
        WeatherType nextType;
        int nextDuration;
        Element previous;
        WeatherType prevType;
        
        previous = queue.getLast();
        prevType = previous.getWeather();
        weights = new Weight(prevType);
        nextType = selectWeather();
        nextDuration = selectDuration();
        
        result = new Element(nextType, nextDuration);
        return result;
    }
    
    /**
     * Perform a weighted random weather selection.
     * @return the selected weathertype
     */
    private WeatherType selectWeather() {
        WeatherType next;
        int result;
        int total;
        Random randomiser = new Random();
        
        total = weights.total();
        result = randomiser.nextInt(total) + 1;
        
        next = weights.selectWeather(result);
        
        return next;
    }
    
    /**
     * Perform a probabalistic duration selection.
     * As the modelled duration increases, so too does the chance of the event 
     * ending.
     * Can tweak algorithm to change the threshold scalar if desired. 
     * At a threshold scalar of 1.0, the 50%-point of the algorithm is at
     * halfway between min and max - this results in a heavy bias toward the 
     * lower half of the window. Increasing the scalar will increase the
     * liklihood of a longer duration being selected. 
     * @return selected duration.
     */
    /* This is more time-complex than a dumb random or a weighted random; can 
     * alter to either if necessary.
     */
    private int selectDuration() {
        int result;
        int window = MAX_DURATION - MIN_DURATION;
        int duration = MIN_DURATION;
        int threshold = 1;
        int thresholdScalar = 2; // Higher value biases toward longer duration
        Random randomiser = new Random();
        
        for (threshold = 1; threshold <= window; threshold++) {
            result = randomiser.nextInt(window) + 1;
            if (result <= threshold / thresholdScalar) {
                break;
            } else {
                duration++;
            }
        }
        return duration;
    }
    
    /**
     * Add a weather event to the end of the queue.
     */
    public void addNew() {
        queue.add(generate());
    }
    
    
    /**
     * remove and return the first element in queue.
     * @return first element of queue
     */
    public Element pop() {
        return queue.removeFirst();
    }
    
    /**
     * Get a member of the queue.
     * @require index < queue.size()
     * @param index index to access
     * @return element at index
     */
    public Element inspect(int index) {
        return queue.get(index);
    }
    
    /** 
     * Gets the forecast list.
     * @return the forecast list.
     */
    public List<Element> getForecast() {
        return this.forecast;
    }
    
    /**
     * Generate a list of the most dominant weather event each day for the 
     * next week (for the forecast). For each day of the next week, adds the 
     * weather with the longest duration in that day. If multiple weathers have
     * the same duration, adds the earliest.
     */
    public List<Element> buildForecast() {
        List<Element> newForecast = new LinkedList<Element>();
        
        for (int i = 1; i <= 7; i++) {
            newForecast.add(generateForecastMember(i));
        }
        return newForecast;
    }
    
    /**
     * Calculate a member to add to the forecast from queue.
     * @require 1 <= day <= 7
     * @param day the day to add a member for
     * @return the member to add
     */
    public Element generateForecastMember(int day) {
        int max;
        int startIndex;
        int endIndex;
        int carry;
        int remainder;
        List<Integer> durations;
        
        max = 0;
        durations = new ArrayList<>();
        startIndex = getIndexAtDay(day);
        endIndex = getIndexAtDay(day + 1);
        carry = (getSumToIndex(startIndex) 
                + queue.get(startIndex).getDuration()) 
                - (((getSumToIndex(startIndex) / 24) + 1) * 24);
        remainder = (((getSumToIndex(endIndex) / 24) + 1) * 24) 
                - getSumToIndex(endIndex);
        durations.add(carry);
        durations.add(remainder);
        if (carry + remainder < 24) {
            for (int j = startIndex + 1; j < endIndex; j++) {
                durations.add(queue.get(j).getDuration());
            }
        }
        max = Collections.max(durations);
        if (max == carry) {
            return queue.get(startIndex);
        } else if (max == remainder) {
            return queue.get(endIndex);
        } else {
            for (int k = startIndex + 1; k < endIndex; k++) {
                if (max == queue.get(k).getDuration()) {
                    return queue.get(k);
                }
            }
        }
        return null; //unreachable
    }
    
    /**
     * Updates the forecast. Removes the first element, and adds a new one.
     */
    public void updateForecast() {
        this.forecast.remove(0);
        this.forecast.add(generateForecastMember(7));
    }
    
    /**
     * Get the index of the first weather in Day X (1-7) of the forecast.
     * @require 1 <= day <= 7.
     * @param day day to access
     * @return the resulting index
     */
    private int getIndexAtDay(int day) {
        int hours = (day) * 24;
        int sum = 0;
        int index = 0;
        
        while (sum < hours) {
            sum += queue.get(index).getDuration();
            index++;
        }
        return index - 1;
    }
    
    
    /**
     * Sums the durations of all weathers in queue up to and excluding the 
     * weather at Index.
     * @param endIndex index at which to end sum
     * @return sum of durations before weather at index
     */
    private int getSumToIndex(int endIndex) {
        int index = 0;
        int sum = 0;
        while (index < endIndex) {
            sum += queue.get(index).getDuration();
            index++;
        }
        return sum;
    }
    
    /**
     * For the purpose of the forecast, queue needs to store weather duration 
     * as well as type - this class encapsulates those.  For use as member of 
     * the weather queue.
     */
    public class Element {
        private WeatherType weather;
        private int duration;
        
        /**
         * Constructor: encapsulates params WeatherType and Duration (hours).
         * @param weather weather event type
         * @param duration duration of weather event
         */
        public Element(WeatherType weather, int duration) {
            this.weather = weather;
            this.duration = duration;
        }
        
        /**
         * Get weathertype from Element.
         * @return Elements' weathertype
         */
        public WeatherType getWeather() {
            return this.weather;
        }
        
        /**
         * Get duration from Element. 
         * @return Element's duration (hours)
         */
        public int getDuration() {
            return this.duration;
        }
    }

}
