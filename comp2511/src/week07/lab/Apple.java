package week07.lab;

public class Apple implements Fruit
{
	public Apple(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
			return name;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Apple o = (Apple)obj;
		return o.getName().equals(getName());
	}
	
	@Override
	public String toString() {
		return getName();
	}
	private String name;
}
