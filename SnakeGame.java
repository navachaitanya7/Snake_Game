package com.Games;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
	private final int TILE_SIZE = 25;
	private final int WIDTH = 600;
	private final int HEIGHT = 600;
	private final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);

	private int[] x = new int[TOTAL_TILES];
	private int[] y = new int[TOTAL_TILES];
	private int snakeSize = 3;
	private int appleX, appleY;

	private char direction = 'R';
	private boolean running = false;
	private Timer timer;

	public SnakeGame() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				changeDirection(e);
			}
		});

		startGame();
	}

	public void startGame() {
		spawnApple();
		running = true;
		timer = new Timer(100, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);

			for (int i = 0; i < snakeSize; i++) {
				if (i == 0) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(new Color(45, 180, 0));
				}
				g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
			}
		} else {
			gameOver(g);
		}
	}

	public void spawnApple() {
		Random random = new Random();
		appleX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
		appleY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
	}

	public void move() {
		for (int i = snakeSize; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] -= TILE_SIZE;
			break;
		case 'D':
			y[0] += TILE_SIZE;
			break;
		case 'L':
			x[0] -= TILE_SIZE;
			break;
		case 'R':
			x[0] += TILE_SIZE;
			break;
		}
	}

	public void checkApple() {
		if (x[0] == appleX && y[0] == appleY) {
			snakeSize++;
			spawnApple();
		}
	}

	public void checkCollisions() {
		for (int i = snakeSize; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}
		if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void changeDirection(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (direction != 'R')
				direction = 'L';
			break;
		case KeyEvent.VK_RIGHT:
			if (direction != 'L')
				direction = 'R';
			break;
		case KeyEvent.VK_UP:
			if (direction != 'D')
				direction = 'U';
			break;
		case KeyEvent.VK_DOWN:
			if (direction != 'U')
				direction = 'D';
			break;
		}
	}

	public void gameOver(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Snake Game");
		SnakeGame game = new SnakeGame();
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}


