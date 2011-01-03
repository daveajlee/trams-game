package trams.messages;

/**
 * Class representing a message in TraMS.
 * @author Dave Lee
 */
public abstract class Message {

    protected String theSubject;
    protected String theText;
    protected String theSender;
    protected String theFolder;
    protected String theDate;

    /**
     * Create a new message.
     * @param subject a <code>String</code> with the subject.
     * @param text a <code>String</code> with the message text.
     * @param sender a <code>String</code> with the sender.
     * @param folder a <code>String</code> with the folder.
     * @param date a <code>String</code> with the date.
     */
    public Message ( String subject, String text, String sender, String folder, String date ) {
        theSubject = subject;
        theText = text;
        theSender = sender;
        theFolder = folder;
        theDate = date;
    }

    /**
     * Get the subject of this message.
     * @return a <code>String</code> with the subject.
     */
    public String getSubject ( ) {
        return theSubject;
    }

    /**
     * Get the text of this message.
     * @return a <code>String</code> with the text of the message.
     */
    public String getText ( ) {
        return theText;
    }

    /**
     * Get the sender of this message.
     * @return a <code>String</code> with the sender.
     */
    public String getSender ( ) {
        return theSender;
    }

    /**
     * Get the folder which this message belongs in.
     * @return a <code>String</code> with the folder.
     */
    public String getFolder ( ) {
        return theFolder;
    }

    /**
     * Get the date of this message.
     * @return a <code>String</code> with the date.
     */
    public String getDate ( ) {
        return theDate;
    }

    /**
     * Get the type of this message.
     * @return a <code>String</code> with the type.
     */
    public abstract String getMessageType ( );

}
