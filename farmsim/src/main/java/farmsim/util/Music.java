package farmsim.util;

import farmsim.ui.FarmSimController;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.slf4j.LoggerFactory;
import org.staccato.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Music implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Music.class);

    public void run() {
        try {
            MidiSystem.getSequencer().open();
        } catch (MidiUnavailableException e1) {
            LOGGER.error("Could not open sequencer", e1);
        }
        String s = "";
        try {
            Pattern pattern = MidiFileManager.loadPatternFromMidi(new File(getClass().getResource("/music/moonlight2.mid").toURI()));
            s = pattern.toString();
        } catch (URISyntaxException | IOException | InvalidMidiDataException e) {
            LOGGER.error(e.toString());
        }
        s = removeTempo(s);

        Player player = new Player();
        ManagedPlayer mp = player.getManagedPlayer();
        Pattern p = new Pattern(s);
        p.setTempo(200);
        p = changeVolume(p, (int) FarmSimController.getSoundEffectsVolume());

        Sequence seq = player.getSequence(p);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                mp.start(seq);
            } catch (InvalidMidiDataException | MidiUnavailableException e) {
                LOGGER.error(e.toString());
            }

            while (!mp.isFinished() && !Thread.currentThread().isInterrupted()) {
                //wait until track is finished
            }
        }

        mp.finish();
    }

    private Pattern changeVolume(Pattern pattern, int volume) {
        String result = "";

        for (String note : pattern.toString().split("\\s+")) {
            if (NoteSubparser.getInstance().matches(note)) {
                String volumeString = "";
                for (int i = note.length(); i > 0; i--) {
                    if (note.substring(i - 1, i).matches("[0-9]")) {
                        volumeString += note.substring(i - 1, i);
                    } else {
                        result += note.substring(0, i);
                        break;
                    }
                }

                if (volumeString.length() > 0) {
                    result += volume;
                }

                result += " ";
            } else {
                result += note + " ";
            }
        }
        return new Pattern(result);
    }

    private String removeTempo(String string) {
        String result = "";

        for (String note : string.split("\\s+")) {
            if (!Objects.equals(note.substring(0, 1), "T")) {
                result += note + " ";
            }
        }
        return result;
    }
}