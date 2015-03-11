package TaskManager;

import javax.swing.JLabel;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowGui;

/**
 * @author Filip Turkot
 * @version 1.0
 * 
 *          <p>
 *          Class providing an user interface for a person running task manager.
 *          </p>
 */
public final class TaskManagerWindow extends GenericWindowGui
{
	private static final long serialVersionUID = -8228437767612168880L;
	private JTextField currentJobField;

	public TaskManagerWindow()
	{
		super("Task manager", null); //new TaskManagerWindowActionListener()
		
		this.add(createTwoHorizontalComponentsPanel(new JLabel("Current job"),
				currentJobField = createTextField("none", false)));
		
		this.pack();
		this.setLocationRelativeTo(null);
	}

}
