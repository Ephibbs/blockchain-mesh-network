import java.util.Random;

public class Location {
	public int x = 0;
	public int y = 0;
	public int size = 10;
	Random rn = new Random();
	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Location() {
		//createRandomLocation();
	}
	public Location createRandomLocation() {
		return new Location(rn.nextInt(800 - 2 * size) + size, rn.nextInt(800 - 2 * size) + size);
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public void setX(int newX){
		this.x = newX;
	}
	public void setY(int newY){
		this.y = newY;
	}
	public void printLocation(){
		System.out.println("(" + this.x + ", " + this.y + ")");
	}
}
