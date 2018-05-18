package Objects;

public class Product {
	
	private String id;
	private String name;
	private String img;
	private double price;
	
	public Product(String id, String name, String img, String price){
		this.id = id;
		this.name = name;
		this.img = img;
		this.price = Double.parseDouble(price);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImg() {
		return img;
	}

	public double getPrice() {
		return price;
	}

}
