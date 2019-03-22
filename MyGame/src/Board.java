
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel{
	Plane plane;
	private Timer timer;
	
	private final int DELAY_TIME = 10; 
	private ArrayList<Enemy> enemyList= new ArrayList<>();
	private final int MAX_ENEMY = 20;
	private boolean gameOver = false;
	private void prepareEnemy(){
		
		 int positionOfEnemies[][]={
				{700,30},{500,90},{600,130},{500,230},{1200,40},
				{1700,130},{700,430},{700,330},{600,100},{700,250},
				{700,109},{600,130},{500,330},{700,100},{500,230},
				{700,409},{600,230},{500,430},{700,440},{500,130}
				
		};
		for(int i = 0; i<positionOfEnemies.length ; i++){
			
			enemyList.add(new Enemy(positionOfEnemies[i][0], positionOfEnemies[i][1]));
			}
		
	}
	
	public Board(){
		this.setFocusable(true);
		this.setBackground(Color.CYAN);
		plane = new Plane();
		this.prepareEnemy();
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				plane.planeMove(e);
				//plane.move();
				//repaint();
			}
			@Override
			public void keyReleased(KeyEvent e){
				plane.planeDontMove();
			}
		});
		timer = new Timer(DELAY_TIME,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				plane.move();
				// Print Missile
				//for(int i = 0; i<plane.getMissileList().size(); i++){
					//Missile missile = plane.getMissileList().get(i);
				
				repaint();
				// TODO Auto-generated method stub
				
			}
		});
		timer.start();
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		Graphics2D twod = (Graphics2D) g;
		twod.drawImage(new ImageIcon(Board.class.getResource("sky.gif")).getImage(), 0, 0, this);
		twod.drawString("Enemy Left "+enemyList.size(), 10, 20);
		if(plane.isVisible()){
		twod.drawImage(plane.getImage(), plane.getX(), plane.getY(), this);
		}
		//twod.drawImage(img, x, y, observer)
		
		// Check Collision (Missile)
				for(int i = 0 ; i<plane.getMissileList().size();i++){
					for(int j = 0; j<enemyList.size();j++){
						Missile missile = plane.getMissileList().get(i);
						Enemy enemy = enemyList.get(j);
						if(missile.getRectangle().intersects(enemy.getRectangle())){
							missile.setVisible(false);
							enemy.setVisible(false);
							enemyList.remove(j);
						}
					}
				}
				
				for(int j = 0; j<enemyList.size();j++){
					
					Enemy enemy = enemyList.get(j);
					if(plane.getRectangle().intersects(enemy.getRectangle())){
						gameOver = true;
						plane.setVisible(false);
						enemy.setVisible(false);
						enemyList.remove(j);
						
					}
				}
		
		for(int i = 0 ; i<plane.getMissileList().size(); i++){
			Missile missile = plane.getMissileList().get(i);
		//for(Missile missile: plane.getMissileList()){
			if(missile.isVisible()){
				missile.moveMissile();
				twod.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
				//System.out.println("Plane "+plane);
			}
			else{
				plane.getMissileList().remove(i);
			}
		}
		if(enemyList.size()==0){
			gameOver = true;
		}
		if(gameOver){
			timer.stop();
			twod.setFont(new Font("Arial",Font.BOLD,40));
			twod.drawString("GAME OVER", 100, 100);
			enemyList.clear();
		}
		// Print Enemy
		for(int i = 0; i<enemyList.size();i++){
			Enemy enemyObject = enemyList.get(i);
			if(enemyObject.isVisible() ){
				
				enemyObject.move();
				twod.drawImage(enemyObject.getImage(), enemyObject.getVelX(), enemyObject.getVelY(), this);
			}
			else
			{
				//enemyList.remove(i);
			}
		}
		
		
		
		Toolkit.getDefaultToolkit().sync();
	}
}