package farmsim.util;

import farmsim.ui.FarmSimController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for initializing the persistent animal sound effects on the
 * background.
 *
 * @author gjavi1 for Team Floyd
 *
 */
public class SoundEffects implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(SoundEffects.class);

    private static MediaPlayer player; // media player for the current track
    private static List<MediaPlayer> playList; // list of media players
    private int trackIndex = 0; // the track to be played

    /**
     *  Method for executing the agme's farm animal sound effects. To add a tack
     *  add the track's name on the Tracklist.
     */
    @Override
    public void run() {
        ArrayList<String> trackList = new ArrayList<>();
        trackList.add("cow.m4a");
        trackList.add("pig.m4a");
        trackList.add("cow2.m4a");
        trackList.add("sheep.m4a");
        trackList.add("pig2.m4a");
        trackList.add("cow.m4a");
        trackList.add("sheep2.m4a");
        trackList.add("cow3.m4a");
        trackList.add("sheep3.m4a");
        read(trackList);
        play();
    }

    /**
     *  Method for reading the contents of trackList and initializing them as
     *  MediaPlayer.
     *
     * @param songList
     *  The list containing the file names of each tract to be added to the
     *  playList
     */
    private void read(List<String> songList) {
        playList = new ArrayList<>();
        for (String song: songList) {
            try{
                URL url = SoundEffects.class.getResource("/sounds/" + song);
                Media media = new Media(url.toString());
                MediaPlayer player = new MediaPlayer(media);
                playList.add(player);
            } catch (RuntimeException e){
                LOGGER.error("Incorrect filename or it is not stored in the " +
                        "sounds folder under resources.", e);
            }
        }
    }

    /**
     *  Method for playing the MediaPlayer on the playList. Plays the
     *  playList in a loop unless the thread is interrupted/stop.
     */
    private void play() {
        player = playList.get(trackIndex);
        player.setVolume(FarmSimController.getSoundEffectsVolume()/100.0);
        player.play();
        player.setMute(FarmSimController.soundEffectsStatus());
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.stop();
                trackIndex++;
                if(trackIndex == playList.size()){
                    trackIndex = 0;
                }
                play();
            }
        });
    }
}

