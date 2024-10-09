package com.example.platformerfx;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Platform extends RigidBody {
	
	

	public Platform(int x, int y, int hitWidth, int hitHeight) {
		// TODO Auto-generated constructor stub
		
		xPos = x;
		yPos = y;
		hitBox = new Rectangle(xPos, yPos, hitWidth, hitHeight);
		gravity = 0;
	}
	public Shape getNode()
	{
		hitBox = new Rectangle(xPos, yPos, hitBox.getBoundsInParent().getWidth(), hitBox.getBoundsInParent().getHeight());
		//hitBox.setLayoutY(yPos);
		
		return hitBox;
	}

}
