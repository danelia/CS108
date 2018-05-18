package Objects;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	
	private Map<Product, Integer> cart;
	
	public Cart(){
		cart = new HashMap<Product, Integer>();
	}
	
	public void changeCart(Product product, int count){
		if(count == 0)
			cart.remove(product);
		else
			cart.put(product, count);
	}
	
	public int get(Product product){
		return cart.containsKey(product) ? cart.get(product) : 0;
	}
	
	public double getTotal(){
		double count = 0;
		for(Product product : cart.keySet())
			count += cart.get(product) * product.getPrice();
		
		return count;
	}
	
}
