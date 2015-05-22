package TaskManager;

import javax.swing.JLabel;
import javax.swing.JTextField;

import GenericCommonClasses.GenericWindowActionListener;
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

	public TaskManagerWindow(TaskManager component)
	{
		super("Task manager", component); //new TaskManagerWindowActionListener()
		
		this.add(createTwoHorizontalComponentsPanel(new JLabel("Current job"),
				currentJobField = createTextField("none", false)));
		
		this.pack();
		this.setLocationRelativeTo(null);
		
		currentJobField.toString();
	}

	@Override
	public GenericWindowActionListener createActionListener()
	{
		return new TaskManagerWindowActionListener(this);
	}

}
