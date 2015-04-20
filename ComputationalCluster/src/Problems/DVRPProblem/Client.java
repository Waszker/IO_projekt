package Problems.DVRPProblem;

public class Client implements IGraphNode
{
	public double x,y; //position
	public double time; //time, when order becomes available
	public double unld; //time required to unload the cargo
	public double size; //size of the cargo
	
	public Client(double x, double y, double time, double unld, double size)
	{
		this.x = x;
		this.y = y;
		this.time = time;
		this.unld = unld;
		this.size = size;
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
