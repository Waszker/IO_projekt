package Problems.DVRP.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents vehicle in DVRP.
 * </p>
 * 
 * @author Piotr Waszkiewicz
 * @version 1.0
 * 
 */
public class Vehicle
{
	/******************/
	/* VARIABLES */
	/******************/
	public static int DEFAULT_VEHICLE_SPEED = 1;
	public static int DEFAULT_VEHICLE_CAPACITY = 10;
	private int speed, capacity;
	private List<Cargo> cargosInside;

	/******************/
	/* FUNCTIONS */
	/******************/
	/**
	 * <p>
	 * Creates vehicle with default speed and capacity.
	 * </p>
	 */
	public Vehicle()
	{
		this.speed = DEFAULT_VEHICLE_SPEED;
		this.capacity = DEFAULT_VEHICLE_CAPACITY;
		this.cargosInside = new ArrayList<>();
	}

	/**
	 * <p>
	 * Creates vehicle with given speed and capacity.
	 * </p>
	 * 
	 * @param speed
	 *            or DEFAULT_VEHICLE_SPEED if -1
	 * @param capacity
	 *            or DEFAULT_VEHICLE_CAPACITY if -1
	 */
	public Vehicle(int speed, int capacity)
	{
		this.speed = speed;
		this.capacity = capacity;
		this.cargosInside = new ArrayList<>();
	}

	/**
	 * <p>
	 * Returns speed of vehicle.
	 * </p>
	 * 
	 * @return vehicle's speed
	 */
	public int getSpeed()
	{
		return speed;
	}

	/**
	 * <p>
	 * Returns current vehicle capacity. Capacity can be lower than initial
	 * capacity because there could possibly some packages already loaded.
	 * </p>
	 * 
	 * @return
	 */
	public int getCapacity()
	{
		return capacity;
	}

	/**
	 * <p>
	 * Inserts cargo inside vehicle and decrements available size.
	 * </p>
	 * 
	 * @param cargo
	 * @throws ArrayStoreException
	 *             ifcargo is too big to fit inside vehicle.
	 */
	public void addCargo(Cargo cargo) throws ArrayStoreException
	{
		if (capacity < cargo.getSize())
			throw new ArrayStoreException("Cargo's size too big.");
		cargosInside.add(cargo);
		capacity -= cargo.getSize();
	}

	/**
	 * <p>
	 * Removes cargo of specified size from vehicle.
	 * </p>
	 * 
	 * @param size
	 * @return
	 */
	public Cargo removeCargo(int size)
	{
		Cargo removedCargo = null;

		for (int i = 0; i < cargosInside.size(); i++)
			if (cargosInside.get(i).getSize() == size)
			{
				removedCargo = cargosInside.get(i);
				capacity += removedCargo.getSize();
				break;
			}

		return removedCargo;
	}

}
