package com.apogeu.engine.utils;
import javax.swing.*;
import java.io.File;

public class FileChooser {
    public static File selectFile() {
        // Create a file chooser instance
        JFileChooser fileChooser = new JFileChooser();

        // Set the default directory to the program's current working directory
        String defaultDir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(defaultDir));

        // Set the file selection mode (files only, not directories)
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Optionally set a file filter (e.g., for .obj files)
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("OBJ Files", "obj"));

        // Show the dialog and get the result
        int result = fileChooser.showOpenDialog(null);

        // Check if a file was selected
        if (result == JFileChooser.APPROVE_OPTION) {
            // Return the selected file
            return fileChooser.getSelectedFile();
        } else {
            // No file was selected or the dialog was cancelled
            return null;
        }
    }
}
