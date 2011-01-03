package trams.messages;

/**
 * Class representing a message from the council in TraMS.
 * @author Dave Lee
 */
public class CouncilMessage extends Message {

    /**
     * Create a new council message.
     * @param subject a <code>String</code> with the subject.
     * @param text a <code>String</code> with the message text.
     * @param sender a <code>String</code> with the sender.
     * @param folder a <code>String</code> with the folder.
     * @param date a <code>String</code> with the date.
     */
    public CouncilMessage ( String subject, String text, String sender, String folder, String date ) {
        super(subject, text, sender, folder, date);
    }

    /**
     * Get the type of this message.
     * @return a <code>String</code> with the type.
     */
    public String getMessageType ( ) {
        return "Council";
    }

}
