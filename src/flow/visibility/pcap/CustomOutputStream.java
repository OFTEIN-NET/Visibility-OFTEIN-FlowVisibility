package flow.visibility.pcap;

import java.io.IOException;

import java.io.OutputStream;

 

import javax.swing.JTextArea;

/**
 * The Class of CustomOutputStream.
 * This class extends from OutputStream to redirect output to a JTextArrea
 * 
 * @author www.codejava.net
 *
 * @edit/modified by Aris C. Risdianto
 * @edit/modified GIST NetCS
 * 
 * Adding package information
 */

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override

    public void write(int b) throws IOException {

        // redirects data to the text area

        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}