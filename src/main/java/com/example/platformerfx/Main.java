package com.example.platformerfx;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;


public class Main extends Application {
	Timeline t;
	KeyFrame kf;
	Button b;
	
	int[] n = new int[] {2, 5, 3, 9, 6};
	
	boolean right, left, slowmode, moveCam;
	
	double speed = 10;
	double jump[] = new double[] {0, -10};
	int delay = 100;
	double ogX, currentX, ogY, currentY, pivX, pivY;
	Scale scale = new Scale(1, 1);
	
	ArrayList<Shape> obstacles = new ArrayList<Shape>();

	@Override
	public void start(Stage primaryStage) {
		try {
			

			Arrays.sort(n);
			
			
			Pane root = new Pane();
			Scene scene = new Scene(root,1200,700);

			Player p = new Player(100, 50, 50, 50);
			
			Platform floor = new Platform(-100, 650, 32000, 50);
			obstacles.add(floor.getNode());
			Platform box = new Platform(500, 500, 400, 50);
			obstacles.add(box.getNode());
			Platform box2 = new Platform(1000, 635, 400, 50);
			obstacles.add(box2.getNode());
			
			Shape poop = new Rectangle(1000, 10, 20, 20);
			root.getChildren().add(poop);
			
			p.showDebug();

			kf = new KeyFrame(Duration.millis(25), e -> {
//					new EventHandler<ActionEvent>() {
//				public void handle(ActionEvent e) 
//				{
					if (slowmode)
					{
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					if (p.getMoveBox().intersects(poop.getBoundsInParent()))
					{
						System.out.println("hit");
					}
					
					//************************************* NEW COLLISION *****************************************
					for (int i = 0; i < obstacles.size(); i ++)
					{
						if (p.getMoveBox().intersects(obstacles.get(i).getBoundsInParent()))
						{
							int count = 0;
							for (int j = 0; j < p.getCollider().size(); j++)
							{
								if (p.getCollider().get(j) == obstacles.get(i))
								{
									count++;
								}
							}
							if (count == 0)
							{
								p.addCollider(obstacles.get(i));
							}
						}
						else
						{
							p.removeCollider(obstacles.get(i));
							
						}
					}
					
					
					
		
					if (right)
					{
						double vel = lerp(p.getVelocity()[p.X], speed, 0.1);
						//System.out.println(vel);
						p.setVelocityX(vel);
					}
					if (left)
					{
						double vel = lerp(p.getVelocity()[p.X], -speed, 0.1);
						p.setVelocityX(vel);
					}
					
					//if (MouseEvent.DRAG_DETECTED)
					
					if (moveCam)
					{
						currentX = MouseInfo.getPointerInfo().getLocation().getX();
						currentY = MouseInfo.getPointerInfo().getLocation().getY();
						root.translateXProperty().set(root.translateXProperty().get() - (ogX - currentX));
						root.translateYProperty().set(root.translateYProperty().get() - (ogY - currentY));
						ogX = currentX;
						ogY = currentY;
					}
					
					
					p.gravity();
					p.getMoveBox();
					p.collide();
					p.getNode();
					p.friction();
					p.updateValues();
					
					//root.translateXProperty().set(-(p.getX()) + (root.getWidth() / 2));
				}
			);
			t = new Timeline(kf);
			t.setCycleCount(Timeline.INDEFINITE);
			t.play();
			
			scene.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					
					moveCam = true;
					ogX = e.getScreenX();
					ogY = e.getScreenY();
					System.out.println("entered");
				}});
			scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					
					poop.setTranslateX(e.getX() - 1000);
					poop.setLayoutY(e.getY());
				}});
			scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					
					moveCam = false;
					System.out.println("exited");
					
				}});
			
			scene.setOnScroll(new EventHandler<ScrollEvent>() {
				public void handle(ScrollEvent e) {
					pivX = e.getX();
					pivY = e.getY();
					
					scale.setPivotX(pivX);
					scale.setPivotY(pivY);
					scale.setX(scale.getX() + (e.getDeltaY() / 1000));
					scale.setY(scale.getY() + (e.getDeltaY() / 1000));
					
					//root.translateXProperty().set(pivX + (root.getWidth() / 2));
					//root.translateYProperty().set(pivY + (root.getHeight() / 2));
					//System.out.println(e.getDeltaY());
				
				}});
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					if (e.getCode() == KeyCode.UP) 
					{
						if (p.onGround)
						{
							p.setVelocityY(0);
							p.addForce(jump);
							
							p.onGround = false;
						}
					} 
					
					if (e.getCode() == KeyCode.RIGHT) 
					{
						right = true;
					}
					if (e.getCode() == KeyCode.LEFT) 
					{
						left = true;
					}
					if (e.getCode() == KeyCode.DOWN) 
					{
						t.stop();
					}
					if (e.getCode() == KeyCode.P) 
					{
						t.play();
					}
					if (e.getCode() == KeyCode.Q) 
					{
						slowmode = true;
						delay += 100;
					}
				}
			});
			
			scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					if (e.getCode() == KeyCode.RIGHT) 
					{
						right = false;
					}
					if (e.getCode() == KeyCode.LEFT) 
					{
						left = false;
					}
				}
			});
			
			root.getChildren().addAll(p.getNode());
			root.getTransforms().add(scale);
			
			for (int i = 0; i < obstacles.size(); i ++)
			{
				root.getChildren().add(obstacles.get(i));
			}


			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(e -> {System.exit(0);});
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public double lerp(double a, double b, double f) 
	{
	    return (a * (1.0 - f)) + (b * f);
	}
}
