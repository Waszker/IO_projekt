package Problems.DVRPProblem;

/**
 * <p>Depot class, used as data structure.</p>
 * @author Filip
 */
public class Depot implements IGraphNode
{
	public double x,y; //position
	public double startTime, endTime; //working hours
	
	public Depot(double x, double y, double startTime, double endTime)
	{
		this.x = x;
		this.y = y;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public double getX()
	{
		return x;
	}

	@Override
	public double getY()
	{
		return y;
	}
}
