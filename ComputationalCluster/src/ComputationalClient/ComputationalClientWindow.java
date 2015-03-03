package ComputationalClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GenericCommonClasses.GenericWindowGui;

/**
 * Last modification: 03/03/2015
 * 
 * <p>
 * Class that provides ready to use GUI interface for Computational Client
 * component.
 * </p>
 * 
 * 
 * @author Anna Zawadzka
 * 
 */

public class ComputationalClientWindow extends GenericWindowGui
{
	/******************/
	/* VARIABLES */
	/******************/
	private static final long serialVersionUID = -1254898218440155506L;

	/******************/
	/* FUNCTIONS */
	/******************/
	public ComputationalClientWindow()
	{
		super("Computational Client", new ComputationalClientActionListener());
		this.add(createLabelAndButton("Upload file", "Choose file"));
		this.add(createButton("Send"));
		this.add(createLabelAndTextField("Computation status", "unknown", false));

		this.pack();
		this.setLocationRelativeTo(null);
	}
}
