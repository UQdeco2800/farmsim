package farmsim.ui;


import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class CreditsPopUp extends PopUpWindow {
    StringBuffer content;
    TextFlow textFlow;

    public CreditsPopUp() {
        super(600, 400, 450, 0, "Credits");
        content = new StringBuffer();

        textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);

        try {
            URL url = new URL("https://s4359041-decofarmcontribs.uqcloud.net/credits.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                addLine(line);
            }
        } catch (IOException e) {

        }

        addBlankLine();
        addLine("Music:");
        addBlankLine();

        addBlock(18);

        addLine("Sonata No. 14 \"Moonlight\"");
        addLine("L. V. Beethoven, through Berners and Winterberger");
        addLine("Creative Commons Attribution-ShareAlike 2.5");
        addLine("http://www.mutopiaproject.org/cgibin/piece-info.cgi?id=276");
        addBlankLine();

        addBlock(14);

        addBlankLine();
        addLine("No ducks were harmed in the making of this game");
        addBlankLine();
        addBlock(16);

        addLine("   _  ");
        addLine("__(.)=");
        addLine("\\___) ");

        addBlock(18, "Consolas");

        ScrollPane scrollPane = new ScrollPane(textFlow);
        scrollPane.setFitToWidth(true);

        this.setContent(scrollPane);
    }

    private void addLine(String line) {
        content.append(line);
        content.append(System.lineSeparator());
    }

    private void addBlankLine() {
        content.append(System.lineSeparator());
    }

    private void addBlock(int size) {
        Text text = new Text(content.toString());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font(size));
        textFlow.getChildren().add(text);
        content = new StringBuffer();
    }

    private void addBlock(int size, String font) {
        Text text = new Text(content.toString());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(new Font(font, size));
        textFlow.getChildren().add(text);
        content = new StringBuffer();
    }

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<>(
                (e1, e2) -> {
                    int res = e2.getValue().compareTo(e1.getValue());
                    return res != 0 ? res : 1;
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}