package trams.messages;

/**
 * Class representing a message from a vehicle in TraMS.
 * @author Dave Lee
 */
public class VehicleMessage extends Message {

    /**
     * Create a new vehicle message.
     * @param subject a <code>String</code> with the subject.
     * @param text a <code>String</code> with the message text.
     * @param sender a <code>String</code> with the sender.
     * @param folder a <code>String</code> with the folder.
     * @param date a <code>String</code> with the date.
     */
    public VehicleMessage ( String subject, String text, String sender, String folder, String date ) {
        super(subject, text, sender, folder, date);
    }

    /**
     * Get the type of this message.
     * @return a <code>String</code> with the type.
     */
    public String getMessageType ( ) {
        return "Vehicle";
    }

}
