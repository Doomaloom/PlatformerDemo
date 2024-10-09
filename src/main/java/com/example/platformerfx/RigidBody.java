package com.example.platformerfx;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public abstract class RigidBody {
	public final int X = 0;
	public final int Y = 1;
	
	double xPos, yPos;
	double gravity = 0.5;
	double friction = 0.1;
	double terminalVelocity = 15;
	double[] velocity;
	Image imgPlayer;
	ImageView iviewPlayer;
	Shape hitBox, moveBox, wallLeft, wallRight, ceiling, ground;
	ArrayList<Shape> colliders = new ArrayList<Shape>();
	boolean onGround, onWallLeft, onWallRight, onCeiling, isEllipse;	
	
	Label lblXPos, lblYPos, lblVelocityX, lblVelocityY, lblOnWallRight, lblOnWallLeft, lblOnGround, lblOnCeiling, lblColliders;
	Font f = new Font("Brittanica Bold", 18);

	public RigidBody() {
		
	}
	public void showDebug()
	{
		FlowPane p = new FlowPane(Orientation.VERTICAL, 10, 10);
		Scene scene = new Scene(p, 400, 400);
		Stage secondaryStage = new Stage();
		
		lblXPos = new Label("X position: " + Double.toString(xPos));
		lblXPos.setFont(f);
		lblYPos = new Label("Y position: " + Double.toString(yPos));
		lblYPos.setFont(f);
		lblVelocityX = new Label("X velocity: " + Double.toString(velocity[X]));
		lblVelocityX.setFont(f);
		lblVelocityY = new Label("Y velocity: " + Double.toString(velocity[Y]));
		lblVelocityY.setFont(f);
		lblOnWallRight = new Label("onWallRight: " + onWallRight);
		lblOnWallRight.setFont(f);
		lblOnWallLeft = new Label("onWallLeft: " + onWallLeft);
		lblOnWallLeft.setFont(f);
		lblOnGround = new Label("onGround: " + onGround);
		lblOnGround.setFont(f);
		lblOnCeiling = new Label("onCeiling: " + onCeiling);
		lblOnCeiling.setFont(f);
		lblColliders = new Label("Colliders: " + colliders.toString());
		lblColliders.setFont(f);
		
		p.getChildren().addAll(lblXPos, lblYPos, lblVelocityX, lblVelocityY, lblOnWallRight, lblOnWallLeft, lblOnGround, lblOnCeiling, lblColliders);
		
		secondaryStage.setScene(scene);
		secondaryStage.setX(0);
		secondaryStage.show();
	}
	public void updateValues()
	{
		lblXPos.setText("X position: " + Double.toString(xPos));
		lblYPos.setText("Y position: " + Double.toString(yPos));
		lblVelocityX.setText(Double.toString(velocity[X]));
		lblVelocityY.setText(Double.toString(velocity[Y]));
		lblOnWallRight.setText("onWallRight: " + onWallRight);
		lblOnWallLeft.setText("onWallLeft: " + onWallLeft);
		lblOnGround.setText("onGround: " + onGround);
		lblOnCeiling.setText("onCeiling: " + onCeiling);
		lblColliders.setText("Colliders: " + colliders.toString());
	}
	public Bounds getMoveBox()
	{
		moveBox.resize(moveBox.getLayoutBounds().getHeight() ,hitBox.getLayoutBounds().getWidth() + Math.abs(velocity[X]));
		moveBox.resize(hitBox.getLayoutBounds().getHeight() + Math.abs(velocity[Y]), moveBox.getLayoutBounds().getWidth());
		
		moveBox.setLayoutX(Math.abs(hitBox.getLayoutBounds().getMinX()));
		
		//X velocity movebox
		//moving right
		if (velocity[X] > 0)
		{
			moveBox.setLayoutX(hitBox.getLayoutX());
		}
		//moving left
		else
		{
			moveBox.setLayoutX(hitBox.getLayoutX() + velocity[X]); // + velocity[Y]);
		}
		
		//moving down
		if (velocity[Y] > 0)
		{
			moveBox.setLayoutY(hitBox.getLayoutY());
		}
		//moving up
		else
		{
			moveBox.setLayoutY(hitBox.getLayoutY() + velocity[Y]); // + velocity[Y]);
		}
		
		return moveBox.getBoundsInParent();
	}
	public void setX(double d)
	{
		
		xPos = d;
		hitBox.setLayoutX(xPos);
	}
	public void setY(double y)
	{
		yPos = y;
		hitBox.setLayoutY(yPos);
	}
	public double getX()
	{
		return xPos;
	}
	public double getY()
	{
		return yPos;
	}
	public void setGravity(int g)
	{
		gravity = g;
	}
	public double[] getVelocity()
	{
		return velocity;
	}
	public void setVelocity(double x, double y)
	{
		velocity[X] = x;
		velocity[Y] = y;
		
		xPos += velocity[X];
		yPos += velocity[Y];
	}
	public void setVelocityX(double vel)
	{
		if (!onWallLeft && !onWallRight)
		{
			velocity[X] = vel;
			//System.out.println("1");
		}
		else if (onWallLeft && vel < 0)
		{
			velocity[X] = 0;
			if (isEllipse)
			{
				setX(wallLeft.getLayoutBounds().getMaxX() - hitBox.getBoundsInParent().getWidth() * 1.5);
			}
			else
			{
				setX(wallLeft.getLayoutBounds().getMaxX() - hitBox.getBoundsInParent().getWidth() * 2);
			}
		}
		if (onWallRight && vel > 0)
		{
			velocity[X] = 0;
			if (isEllipse)
			{
				if (hitBox.getBoundsInParent().getMaxY() <= wallRight.getBoundsInParent().getMaxY() && hitBox.getBoundsInParent().getMinY() >= wallRight.getBoundsInParent().getMinY())
				{
					setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 2.5);
				}
				else if (hitBox.getBoundsInParent().getMinY() + hitBox.getLayoutBounds().getHeight() / 2 >= wallRight.getBoundsInParent().getMaxY())
				{
					
					double calcY = wallRight.getBoundsInParent().getMaxY() - hitBox.getBoundsInParent().getMinY();
					double calcRad = hitBox.getBoundsInParent().getHeight() / 2;
					xPos = Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcY, 2));
					setX(wallRight.getLayoutBounds().getMinX() - xPos - hitBox.getBoundsInParent().getWidth() * 2 + 4);
				}
				else if (hitBox.getLayoutBounds().getMaxY() - hitBox.getLayoutBounds().getHeight() / 2 <= wallRight.getBoundsInParent().getMinY())
				{
					double calcRad = hitBox.getBoundsInParent().getHeight() / 2;
					double calcY = wallRight.getBoundsInParent().getMinY() - hitBox.getBoundsInParent().getMinY() - calcRad;
					
					xPos = Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcY, 2));
					setX(wallRight.getLayoutBounds().getMinX() - xPos - hitBox.getBoundsInParent().getWidth() * 2 + 4);
					//onGround = true;
					//onWallRight = false;
					//ground = wallRight;
					//wallRight = null;
				}
				//setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 2.5);
			}
			else
			{
				setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 3);
			}
		}
		else
		{
	 		velocity[X] = vel;
		}
	}
	public void setVelocityY(double y)
	{
		velocity[Y] = y;
		
		yPos += velocity[Y];
	}
	public void gravity()
	{
		if (!onGround)
		{
			if (velocity[Y] < terminalVelocity)
			{
				velocity[Y] += gravity;
			}
		}
		else
		{
			velocity[Y] = 0;
			if (isEllipse)
			{
				if (hitBox.getBoundsInParent().getMinX() + hitBox.getBoundsInParent().getWidth() / 2 >= ground.getBoundsInParent().getMinX() && hitBox.getBoundsInParent().getMinX() <=ground.getBoundsInParent().getMaxX())
				{
					yPos = ground.getLayoutBounds().getMinY() - (hitBox.getLayoutBounds().getHeight() * 1.5);
				}
				else
				{

					double calcX = ground.getBoundsInParent().getMinX() - hitBox.getBoundsInParent().getMinX();
					double calcRad = hitBox.getBoundsInParent().getWidth() / 2;
					yPos = ground.getLayoutBounds().getMinY() - Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcX / 2, 2)) - hitBox.getBoundsInParent().getHeight() - 4;
				}
				if (hitBox.getBoundsInParent().getMinX() + hitBox.getBoundsInParent().getWidth() / 2>= ground.getBoundsInParent().getMaxX())
				{
					double calcX = hitBox.getBoundsInParent().getMaxX() - ground.getBoundsInParent().getMaxX();
					double calcRad = hitBox.getBoundsInParent().getWidth() / 2;
					yPos = ground.getLayoutBounds().getMinY() - Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcX / 2, 2)) - hitBox.getBoundsInParent().getHeight() - 4;
				}
			}
			else
			{
				yPos = ground.getLayoutBounds().getMinY() - (hitBox.getLayoutBounds().getHeight() * 2);
			}
		}
		if (onCeiling)
		{
			velocity[Y] = 0;
			if (isEllipse)
			{
				yPos = ceiling.getLayoutBounds().getMinY() + hitBox.getLayoutBounds().getHeight() / 2 + 1;
			}
			else
			{
				yPos = ceiling.getLayoutBounds().getMinY() + 1;
			}
		}
		
		
		xPos += velocity[X];
		yPos += velocity[Y];
		
	}
	public void friction()
	{
		if (onGround)
		{
			if (velocity[X] != 0)
			{
				if (velocity[X] > 0)
				{
					if (velocity[X] < friction)
					{
						velocity[X] = 0;
					}
					else
					{
						setVelocityX(velocity[X] - friction);
					}
				}
				else if (velocity[X] < 0)
				{
					if (velocity[X] > -friction)
					{
						velocity[X] = 0;
					}
					else
					{
						setVelocityX(velocity[X] + friction);
					}
				}
			}
		}
	}
	public void collide()
	{
		for (int i = 0; i < colliders.size(); i++)
		{
			if (colliders.get(i) == wallRight || colliders.get(i) == wallLeft || colliders.get(i) == ground || colliders.get(i) == ceiling)
			{
				continue;
			}
			if (isEllipse) 
			
			{
				// colliders.get(i).getBoundsInParent().getMinX()
				if (colliders.get(i).getBoundsInParent().getMinY() > hitBox.getBoundsInParent().getMaxY() - 10)
				{
					onGround = true;
					ground = colliders.get(i);
				} 
				else if (colliders.get(i).getBoundsInParent().getMinY() + colliders.get(i).getBoundsInParent().getHeight() - 2 < hitBox.getBoundsInParent().getMinY()) 
				{
					onCeiling = true;
					ceiling = colliders.get(i);
				} 
				else if (colliders.get(i).getBoundsInParent().getMinX() > hitBox.getBoundsInParent().getMinX()) 
				{
					onWallRight = true;
					wallRight = colliders.get(i);
				} 
				else if (colliders.get(i).getBoundsInParent().getMaxX() < hitBox.getBoundsInParent().getMinX() + 2) 
				{
					onWallLeft = true;
					wallLeft = colliders.get(i);
				}
			}
			
		}
		
		if (onWallRight && velocity[X] > 0)
		{
			//System.out.println("poo");
			velocity[X] = 0;
			if (isEllipse)
			{
				if (hitBox.getBoundsInParent().getMaxY() <= wallRight.getBoundsInParent().getMaxY() && hitBox.getBoundsInParent().getMinY() >= wallRight.getBoundsInParent().getMinY())
				{
					setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 2.5);
				}
				else if (hitBox.getBoundsInParent().getMinY() + (hitBox.getLayoutBounds().getHeight() / 2) >= wallRight.getBoundsInParent().getMaxY())
				{
					System.out.println("ppop");
					double calcY = wallRight.getBoundsInParent().getMaxY() - hitBox.getBoundsInParent().getMinY();
					double calcRad = hitBox.getBoundsInParent().getHeight() / 2;
					xPos = Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcY, 2));
					setX(wallRight.getLayoutBounds().getMinX() - xPos - hitBox.getBoundsInParent().getWidth() * 2 + 4);
				}
					//setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 2.5);
				else if (hitBox.getLayoutBounds().getMaxY() - (hitBox.getLayoutBounds().getHeight() / 2) <= wallRight.getBoundsInParent().getMinY())
				{
					double calcRad = hitBox.getBoundsInParent().getHeight() / 2;
					double calcY = wallRight.getBoundsInParent().getMinY() - hitBox.getBoundsInParent().getMinY() - calcRad;
					
					xPos = Math.sqrt(Math.pow(calcRad, 2) - Math.pow(calcY, 2));
					setX(wallRight.getLayoutBounds().getMinX() - xPos - hitBox.getBoundsInParent().getWidth() * 2 + 4);
					//onGround = true;
					//onWallRight = false;
					//ground = wallRight;
					//wallRight = null;
				}
			}
			else
			{
				setX(wallRight.getLayoutBounds().getMinX() - hitBox.getBoundsInParent().getWidth() * 3);
			}
		}
		if (onWallLeft && velocity[X] < 0)
		{
			velocity[X] = 0;
			if (isEllipse)
			{
				setX(wallLeft.getLayoutBounds().getMaxX() - hitBox.getBoundsInParent().getWidth() * 1.5);
			}
			else
			{
				setX(wallLeft.getLayoutBounds().getMaxX() - hitBox.getBoundsInParent().getWidth() * 2);
			}
		}
	}
	public void addForce(double[] force)
	{
		velocity[X] += force[X];
		velocity[Y] += force[Y];
		
		xPos += velocity[X];
		yPos += velocity[Y];
	}
	public void addCollider(Shape collider)
	{
		colliders.add(collider);
	}
	public void removeCollider(Shape collider)
	{
		if (wallRight == collider) {wallRight = null; onWallRight = false;}
		if (wallLeft == collider) {wallLeft = null; onWallLeft = false;}
		if (ground == collider) {ground = null; onGround = false;}
		if (ceiling == collider) {ceiling = null; onCeiling = false;}
		colliders.remove(collider);
	}
	public ArrayList<Shape> getCollider()
	{
		return colliders;
	}

}