/*
 * @author - Vinodh K
 * Class for initializing objects for k shortest path implementation
 */
package code;
import java.util.ArrayList;
import java.util.List;


public class Data {

	List<String> path = new ArrayList<String>();
	double pathlength;
	public Data(List<String> a, double b)
	{
		path = a;
		pathlength = b;
	}
	
}
