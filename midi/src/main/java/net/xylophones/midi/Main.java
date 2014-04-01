package net.xylophones.midi;

import javax.sound.midi.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
        DeviceManager manager = new DeviceManager();
        List<MidiDevice> devices = manager.getMidiOutDevices();
        for (MidiDevice device: devices) {
            System.out.println(device.getDeviceInfo().getName() + " r:" + device.getMaxReceivers() + ", t:" + device.getMaxTransmitters());
            device.open();

            try {
                device.getTransmitter();
                System.out.println("OPENED!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                device.close();
            }
        }


        /*
        MidiDevice device = devices.get(0);

        final Receiver midiOut = device.getReceiver();
        final Transmitter midiIn = device.getTransmitter();
        //midiIn.setReceiver(new MYReceiver());
        */

        /*
        byte[] singleDumpRequest = {(byte) 0xF0,00,20,33,01,10,30,00,00,(byte) 0xF7};
        SysexMessage sysexMessage = new SysexMessage(singleDumpRequest, singleDumpRequest.length);
        midiOut.send(sysexMessage, -1);
        */

        /*
        //transmitter
        ShortMessage myMsg = new ShortMessage();
        myMsg.setMessage(ShortMessage.NOTE_ON, 1, 60, 93);
        long timeStamp = -1;
        midiOut.send(myMsg, timeStamp);


        device.close();

        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */


    }


}
