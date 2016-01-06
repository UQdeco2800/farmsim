package farmsim.util;

import java.net.URL;
import farmsim.ui.FarmSimController;
import javafx.scene.media.AudioClip;

/**
 * A class for initializing a background sound effect.
 *
 * @author gjavi1 for Team Floyd
 *
 */
public class Sound implements Runnable{

    //Actual player object
    private AudioClip currentClip;
    //Mute is static and applied to all sounds
    private static boolean mute;
    //volume is static and applied to all sounds
    private static double vol = -1.0;

    /**
     * Initialization method, accepts a string which describes the name of
     * the sound that is requested to be played.
     *
     * @param fileName
     *  The name of the file located on the sounds folder under resources to
     *  be played.
     */
    public Sound(String fileName) {
        URL url = Sound.class.getResource("/sounds/" + fileName);
        currentClip = new AudioClip(url.toString());
    }

    /**
     *  Method for setting the value of the sound to be played, by default
     *  the initial sound relies on the main volume mixer.
     *
     * @param volume
     *  The volume of the sound to be played from 1.00 ro 100.00
     */
    public void setVolume(double volume) {
        vol = volume/100.00;
        currentClip.setVolume(vol);
    }

    /**
     * Method for changing the toggling the mute value of the current sound
     *
     * @param value
     *  The value to be set.
     */
    public void setMute(boolean value){
        mute = value;
    }

    /**
     * Method for getting the current mute status of the audio.
     *
     * @return
     *  Returns true if audio is muted false otherwise.
     */
    public boolean isMute(){
        return mute;
    }

    /**
     *  Set how many times the audio clip to be played.
     *
     * @param cycle
     *  The number of times it should be played.
     */
    public void setCycle(int cycle){
        currentClip.setCycleCount(cycle);
    }

    /**
     * Method for continuously playing the sound clip.
     *
     */
    public void playForever() {
        if(mute){
            currentClip.setVolume(0.0);
        } else {
            currentClip.setVolume(FarmSimController.getSoundEffectsVolume());
        }
        currentClip.setCycleCount(AudioClip.INDEFINITE);
        currentClip.play();
    }

    /**
     * Method for stopping the sound.
     */
    public void stop(){
        currentClip.stop();
    }

    /**
     * Method for playing the audio sound.
     */

    public void play() {
        mute = FarmSimController.soundEffectsStatus();
        if(mute) {
            vol = 0;
        } else if (vol == -1.0) {
            vol = FarmSimController.getSoundEffectsVolume();
        }
        currentClip.setVolume(vol);
        currentClip.play();
    }

    /**
     * Method for running the audio clip inside a thread.
     */
    @Override
    public void run() {
        play();
    }
}