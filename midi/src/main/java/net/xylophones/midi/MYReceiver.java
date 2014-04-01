package net.xylophones.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MYReceiver implements Receiver {
    @Override
    public void send(MidiMessage message, long timeStamp) {
        System.out.println(message.getClass());
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
