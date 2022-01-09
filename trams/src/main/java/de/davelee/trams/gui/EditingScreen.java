package de.davelee.trams.gui;

import javax.swing.*;

/**
 * This is an interface which combines the <code>VehicleDepotPanel</code> and <code>ViewDriverPanel</code> to reduce
 * code duplication.
 * @author Dave Lee
 */
public interface EditingScreen {

    /**
     * Create a new <code>EditingScreen</code> panel and display it to the user.
     * @param name a <code>String</code> object containing the name of the driver or vehicle to show.
     * @param controlScreen a <code>ControlScreen</code> object with the control screen that the user can use to control the game.
     * @return a <code>JPanel</code> object which can be displayed to the user.
     */
    JPanel createPanel (final String name, final ControlScreen controlScreen );
}
