package de.davelee.trams.gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.controllers.ControllerHandler;

/**
 * This class represents two dialogs which will be shown to either choose a file to load or a file to save.
 * @author Dave Lee
 */
public class FileDialog {

    /**
     * Create a new dialog to load a file.
     * @param currentFrame a <code>JFrame</code> object containing the parent frame of this dialog.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     * @return a <code>boolean</code> which is true iff a file was selected and loaded successfully.
     */
	public boolean createLoadFileDialog (final JFrame currentFrame, final ControllerHandler controllerHandler) {
		JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Load Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showOpenDialog(currentFrame);
        //Check if user submitted file and print coming soon.
        boolean validFile = true;
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            CompanyResponse companyResponse = controllerHandler.getFileController().loadFile(fileDialog.getSelectedFile());
            if ( companyResponse != null ) {
                currentFrame.dispose();
                ControlScreen controlScreen = new ControlScreen(controllerHandler, companyResponse.getName(), companyResponse.getPlayerName());
                controlScreen.displayScreen("", 0, 4, false);
                //Set control screen.
                controlScreen.setVisible(true);
                //Finally, run simulation
                controllerHandler.getSimulationController().resumeSimulation(controlScreen);
                return true;
            } else {
                return false;
            }
        }
        if ( !validFile ) {
            JOptionPane.showMessageDialog(currentFrame,"The selected file is not compatible with this version of TraMS.\nYou may want to check the TraMS website for a convertor at https://www.davelee.de/trams/\nPlease either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
        }
        return false;
	}

    /**
     * Create a new dialog to save a file.
     * @param currentFrame a <code>JFrame</code> object containing the parent frame of this dialog.
     * @param controllerHandler a <code>ControllerHandler</code> object allowing access to Controllers.
     */
	public void createSaveFileDialog ( final JFrame currentFrame, final ControllerHandler controllerHandler ) {
		//Create file dialog box.
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Save Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showSaveDialog(currentFrame);
        //Check if user submitted file.
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            if ( controllerHandler.getFileController().saveFile(fileDialog.getSelectedFile()) ) {
                String fileName = fileDialog.getSelectedFile().getPath();
                if ( !fileName.endsWith(".tms") ) { fileName += ".tms"; }
                JOptionPane.showMessageDialog(currentFrame, "The current simulation has been successfully saved to " + fileName, "File Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
            }
            JOptionPane.showMessageDialog(currentFrame, "The file could not be saved. Please try again later.", "ERROR: File Could Not Be Saved", JOptionPane.ERROR_MESSAGE);
        }
	}

}
