package Problems.DVRPProblem;

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
