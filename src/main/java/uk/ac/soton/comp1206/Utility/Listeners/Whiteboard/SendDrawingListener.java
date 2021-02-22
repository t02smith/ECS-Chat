package uk.ac.soton.comp1206.Utility.Listeners.Whiteboard;

import uk.ac.soton.comp1206.Network.PaintMessage;

public interface SendDrawingListener {
    public void send(PaintMessage pm);
}
