package net.xylophones.midi;

import com.google.common.base.Function;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager {

    public List<MidiDevice> getMidiOutDevices() {
        return getDevicesMatchingPredicate(
            new Function<MidiDevice,Boolean>() {
                @Override
                public Boolean apply(MidiDevice midiDevice) {
                    return isMidiDeviceHardwareMidiPort(midiDevice) && isReceivingDevice(midiDevice);
                }
            }
        );
    }

    public List<MidiDevice> getMidiInDevices() {
        return getDevicesMatchingPredicate(
            new Function<MidiDevice,Boolean>() {
                @Override
                public Boolean apply(MidiDevice midiDevice) {
                    return isMidiDeviceHardwareMidiPort(midiDevice) && isTransmittingDevice(midiDevice);
                }
            }
        );
    }

    public List<MidiDevice> getMidiInAndOutDevices() {
        return getDevicesMatchingPredicate(
            new Function<MidiDevice,Boolean>() {
                @Override
                public Boolean apply(MidiDevice midiDevice) {
                    return isMidiDeviceHardwareMidiPort(midiDevice) && isTransmittingDevice(midiDevice) && isReceivingDevice(midiDevice);
                }
            }
        );
    }

    private List<MidiDevice> getDevicesMatchingPredicate(Function<MidiDevice,Boolean> predicate) {
        List<MidiDevice> devices = new ArrayList<MidiDevice>();

        MidiDevice.Info[] midiDeviceInfos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info: midiDeviceInfos) {
            try {
                MidiDevice midiDevice = MidiSystem.getMidiDevice(info);
                if (predicate.apply(midiDevice)) {
                    devices.add(midiDevice);
                }
            } catch (MidiUnavailableException e) {
                // TODO
                System.err.println("Error " + e.getMessage());
            }
        }

        return devices;
    }

    private boolean isMidiDeviceHardwareMidiPort(MidiDevice device) {
        return true;
        //return ! (device instanceof Sequencer) && ! (device instanceof Synthesizer);
    }

    private boolean isTransmittingDevice(MidiDevice device) {
        return device.getMaxTransmitters() != 0;
    }

    private boolean isReceivingDevice(MidiDevice device) {
        return device.getMaxReceivers() != 0;
    }

}
