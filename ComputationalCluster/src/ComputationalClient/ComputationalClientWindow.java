package ComputationalClient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

public class ComputationalClientWindow extends GenericWindowGui implements
		ActionListener
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
		super("Computational Client");
		this.add(createLabelAndButton("Upload file", "Choose file"));
		this.add(createButton("Send"));
		this.add(createLabelAndTextField("Computation status", "unknown", false));

		this.pack();
	}

	private JButton createButton(String buttonString)
	{
		JButton button = new JButton(buttonString);
		// TODO: Add action listener
		button.addActionListener(this);

		return button;
	}

	private JPanel createLabelAndTextField(String labelString,
			String textFieldString, boolean isTextFieldEditable)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		Label label = new Label(labelString);
		JTextField textField;

		if (textFieldString == null)
		{
			textField = new JTextField();
		} else
		{
			textField = new JTextField(textFieldString);
		}
		textField.setEditable(isTextFieldEditable);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setAlignmentY(CENTER_ALIGNMENT);
		textField.setPreferredSize(new Dimension(100, 30));
		// TODO: Set text aligment!!!

		panel.add(label);
		panel.add(textField);

		return panel;
	}

	private JPanel createLabelAndButton(String labelString, String buttonString)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout());

		Label label = new Label(labelString);
		JButton button = createButton(buttonString);

		panel.add(label);
		panel.add(button);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		// TODO Auto-generated method stub
		JButton button = (JButton) actionEvent.getSource();
		if (button.getText().equals("Send"))
		{
			JOptionPane.showMessageDialog(new JFrame(), "SENT", "Dialog",
			        JOptionPane.INFORMATION_MESSAGE);
		}
		if (button.getText().equals("Choose file"))
		{
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				button.setText(file.getName());
			}
		}
	}

}
