package TaskManager;

import GenericCommonClasses.GenericWindowGui;

/**
 * @author Filip Turkot
 * @version 1.0
 * 
 * <p>
 * Class providing an user interface for a person running task manager.
 * </p>
 */
public class TaskManagerWindow extends GenericWindowGui
{
	private static final long serialVersionUID = -8228437767612168880L;

	public TaskManagerWindow()
	{
		super("Task manager", new TaskManagerWindowActionListener());
		this.add(createLabelAndTextField("Current job", "none", false));
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
}
