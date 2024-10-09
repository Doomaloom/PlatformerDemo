package com.example.platformerfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.TriangleMesh;

public class Player extends RigidBody {
		
	private Image imgPlayer;
	private ImageView iviewPlayer;
	
	
	public Player(int x, int y, int hitWidth, int hitHeight) {
		velocity = new double[] {0,0};
		xPos = x;
		yPos = y;
		hitBox = new Circle(x, y, hitWidth / 2);
		hitBox.setStyle("-fx-fill: red");
		moveBox = new Circle(x, y, hitWidth / 2);
		moveBox.setStyle("-fx-fill: gray");
		isEllipse = true;
	}
	public Shape getNode()
	{
		setX(xPos);
		setY(yPos);
		return hitBox;
	}

}
