import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/*
 * Subclass of Node, simulation specific
 * Parameters: ID string
 */

/* TODO
 * When I am sending a message, I should try and verify the header of the message
 * but I am not sure if that would work or not because then someone could
 * possible alter the contents of it without having to alter the signature.
 */

public class SimulationNode extends Node {

	// Variables
	public int xCoordinate = 0;
	public int yCoordinate = 0;
	public Color color = Color.BLUE;
	public int WIDTH = 0;

	// Constructor
	public SimulationNode(String id) throws NoSuchAlgorithmException, NoSuchProviderException {
		super(id);
	}

	// Accessors
	public Color getColor() {
		return this.color;
	}
	public int getXCoord() {
		return this.xCoordinate;
	}
	public int getYCoord() {
		return this.yCoordinate;
	}
	public int getWidth() {
		return this.WIDTH;
	}

	// Mutators
	public void setNodeValues(int xVal, int yVal, Color myColor, int width)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		this.xCoordinate = xVal;
		this.yCoordinate = yVal;
		this.color = myColor;
		this.WIDTH = width;
	}
	public void setColor(Color myColor) {
		this.color = myColor;
	}
	public void moveNode(int maxSize, int offset, int movement, Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(this.xCoordinate, this.yCoordinate, this.WIDTH, this.WIDTH);

		int randomX = rand.nextInt(movement);
		int randomY = rand.nextInt(movement);
		int direction = rand.nextInt(4);
		int maxChecker = maxSize - offset;
		if ((this.xCoordinate + randomX) > maxChecker) {
			this.xCoordinate = this.xCoordinate - 40;
			// System.out.println("I got here");
			// this.xCoordinate = this.xCoordinate +
			// (randomX-(maxChecker-this.xCoordinate));
		}
		if ((this.xCoordinate - randomX) < 20) {
			this.xCoordinate = this.xCoordinate + 25;
			// System.out.println("I got here");
			// this.xCoordinate = this.xCoordinate - (randomX-(this.xCoordinate
			// - offset));
		}
		if ((this.yCoordinate + randomY) > maxChecker) {
			System.out.println("I got here");
			this.yCoordinate = this.yCoordinate - 40;
			// this.yCoordinate = this.yCoordinate +
			// (randomY-(maxChecker-this.yCoordinate));
		}
		if ((this.yCoordinate + randomY) < 100) {
			System.out.println("I got here");
			this.yCoordinate = this.yCoordinate + 25;
			// this.yCoordinate = this.yCoordinate - (randomY-(this.yCoordinate
			// - offset));
		} else {
			if (direction == 0) {
				this.xCoordinate = this.xCoordinate + randomX;
				this.yCoordinate = this.yCoordinate + randomY;
			} else if (direction == 1) {
				this.xCoordinate = this.xCoordinate - randomX;
				this.yCoordinate = this.yCoordinate + randomY;
			} else if (direction == 2) {
				this.xCoordinate = this.xCoordinate + randomX;
				this.yCoordinate = this.yCoordinate - randomY;
			} else {
				this.xCoordinate = this.xCoordinate - randomX;
				this.yCoordinate = this.yCoordinate - randomY;
			}
		}
	}

	// Utility
	public void Draw(Graphics g) {
		g.setColor(this.color);
		g.fillOval(this.xCoordinate, this.yCoordinate, this.WIDTH, this.WIDTH);
	}
	public void drawLinesToFriends(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < this.networkNodes.size(); i++) {
			SimulationNode friend = (SimulationNode) networkNodes.get(i);
			g.drawLine(this.xCoordinate + this.WIDTH / 2, this.yCoordinate + this.WIDTH / 2,
					friend.getXCoord() + this.WIDTH / 2, friend.getYCoord() + this.WIDTH / 2);
		}
	}
}
