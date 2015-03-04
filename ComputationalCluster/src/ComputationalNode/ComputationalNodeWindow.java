package ComputationalNode;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

import GenericCommonClasses.GenericWindowGui;

/**
 * 
 * @author Monika ¯urkowska
 * @version 0.9
 * 
 *          <p>
 *          Class providing GUI for Computational Node
 *          </p>
 */

public final class ComputationalNodeWindow extends GenericWindowGui
{
	private static final long serialVersionUID = -6523200028537699606L;
	private JTextField currentTaskField;
	private JProgressBar currentProgressBar;

	public ComputationalNodeWindow()
	{
		super("Computational Node", new ComputationalNodeActionListener());

		this.add(createTwoHorizontalComponentsPanel(new JLabel("Current task"),
				currentTaskField = createTextField("none", false)));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder("Progress...");
		progressBar.setBorder(border);

		this.add(progressBar);

		this.pack();
		this.setLocationRelativeTo(null);
	}
}
