import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;

public class Yard extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ROWS = 30;
	public static final int COLS = 30;
	public static final int BLOCK_SIZE = 15;
	private int score = 0;
	Snake s = new Snake(this);
	Egg e =new Egg();
	Image offScreenImage = null;
	private Font fontGameOver = new Font("宋体",Font.BOLD,50);
	private Font fontScore = new Font("宋体",Font.BOLD,30);
	public boolean gameOver = s.gameOver;
	PaintThread paintThread = new PaintThread();
	
	public void launch() {
		this.setLocation(200,200);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setVisible(true);
		this.addKeyListener(new KeyMonitor());
		new Thread(paintThread).start();
	}
	
	
	
	public static void main(String[] args) {
		new Yard().launch();

	}
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		g.setColor(Color.DARK_GRAY);
		//画出横线
		for(int i=1; i<ROWS;i++) {
		g.drawLine(0, BLOCK_SIZE * i, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
		}
		//画出竖线
		for(int i=1; i<COLS;i++) {
			g.drawLine( BLOCK_SIZE * i, 0,i * BLOCK_SIZE, BLOCK_SIZE * ROWS);
			}
		g.setColor(Color.GREEN);
		g.setFont(fontScore);
		g.drawString("分数：" + score, 10, 430);
		
		if(s.gameOver) {
			g.setColor(Color.RED);
			g.setFont(fontGameOver);
			g.drawString("游戏结束", 135, 250);
		}
		
		g.setColor(c);
		s.eat(e);
		e.draw(g);
		s.draw(g);
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage,0,0, null);
	}
	
	private class PaintThread implements Runnable {
		private boolean running=true;
		public void run() {
			
			while(running) {
				 repaint();
				try {
					Thread.sleep(100);
				} catch	(InterruptedException e) {
					e.printStackTrace();
				}		
			}
			
			
		}
		
		//public void pause() {
		//	this.pause = true;
		//}
		
		
	}
	
	public void reStart() {
		s = new Snake(Yard.this);
		score = 0;
		
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_X) {
				reStart();
			}
			s.keyPressed(e);
		}}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
