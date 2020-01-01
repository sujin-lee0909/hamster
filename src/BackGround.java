import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class BackGround extends JFrame implements Runnable, KeyListener{
	Image background_img;
	Image image;
	Image gh;
	Image rock_img;
	Image jh;
	Image end;
	
	int cx[] = {0,0,0}; //배경 좌표
	int bx=0; //배경 이동 
	int f_width, f_height; //배경 크기
	
	int re = 0;
	
	int x, y; //캐릭터 좌표
	int count; //count 변수
	
	int l;//
	
	int h_w, h_h;
	int g_w, g_h;
	int r_w, r_h;
	
	//score용 변수
	long cnt;
	long sc;
	int score;
	
	//thread
	Thread t1 = new Thread(this);

	boolean KeyUP=false;
	
	//buff
	Image buffImage;
	Graphics buffg;
	
	ArrayList Ghost_list = new ArrayList();
	ArrayList Rock_list = new ArrayList();
	
	Ghost ghost;
	Rock rock;
	
	//Toolkit tk = Toolkit.getDefaultToolkit();
	
	public void init() {
		//image 생성
		Sound("R.wav",true);
		background_img = new ImageIcon("BackGround.png").getImage();
		rock_img = new ImageIcon("rock.png").getImage();
		gh = Toolkit.getDefaultToolkit().createImage("Ghost.gif");
		image = Toolkit.getDefaultToolkit().createImage("hamm.gif");
		jh = Toolkit.getDefaultToolkit().createImage("jumpham.gif");
		x=80;
		y=220;
		
		h_w = ImageWidthValue("hamm.gif")-15;
		h_h = ImageHeightValue("hamm.gif")-15;
		
		g_w = ImageWidthValue("ghost.gif")-15;
		g_h = ImageHeightValue("ghost.gif")-15;
		
		r_w = ImageWidthValue("rock.png")-15;
		r_h = ImageHeightValue("rock.png")-15;
	}
	
	public void Sound(String file, boolean Loop) {
		Clip clip;
		try {
		AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
		clip = AudioSystem.getClip();
		clip.open(ais);
		clip.start();
		if (Loop) clip.loop(-1);
		} catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	public void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		cnt = System.currentTimeMillis();
		t1.start();
	}
	
	public void run() {
		try {
			while(re==0) {
				score=(int) ((int) (sc-cnt)/100.0);
				KeyProcess();
				GhostProcess();
				RockProcess();
				repaint();
				t1.sleep(10);
				count ++;	
			}
			System.out.println("finish");
			buffg.clearRect(0, 0, 960, 350);
			buffg.drawImage(background_img, 0, 0, null);
			Draw_End();
			t1.sleep(100000);
		}catch(Exception e) {};
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) 
			//System.out.println("UP");
			KeyUP=true;
	}
	
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_UP)
			KeyUP= false;
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void KeyProcess() throws InterruptedException{
		//System.out.println("KeyProcess");
		int up=0;
		
		if (KeyUP==true) {
			//System.out.println("UP true");
			y-=5;
		}
	}
	
	public void Draw_Background(){
		buffg.clearRect(0, 0, 960, 350); //화면 지우

		if ( bx > - 2880){
			buffg.drawImage(background_img, bx, 0, this);
			bx -= 1;
		}	else { bx = 0; }
	}
	
	
	public void Draw_Hamster() {
		buffg.drawImage(image, x, y, this);
		if(y<220) {
			y++;
		}
		if(y<=4) {
			y=4;
		}
	}
	

	public void Draw_Score() {
		buffg.setFont(new Font("Defualt", Font.BOLD, 20));
		//System.out.println("score: "+score);
		buffg.drawString("Score"+score, x, y-5);
	}
	
	
	public void GhostProcess() throws InterruptedException {
		for(int i = 0; i<Ghost_list.size(); ++i) {
			ghost = (Ghost)(Ghost_list.get(i));
			ghost.move(score);
			if(ghost.x<-960) {
				Ghost_list.remove(i);
			}
		}
	
		if(score>700) {
			if(count%80==0) {
				ghost = new Ghost(f_width+100,y);
				Ghost_list.add(ghost);
			}
		}
		
		else if(score>300) {
			if(count%100==0) {
				ghost = new Ghost(f_width+100,y);
				Ghost_list.add(ghost);
			}
		}
		
		else if(score>150) {
			if(count%200==0) {
				ghost = new Ghost(f_width+100,y);
				Ghost_list.add(ghost);
			}
		}
		else {
			if(count%300==0) {
				ghost = new Ghost(f_width+100,y);
				Ghost_list.add(ghost);
			}
		}
		
		for(int j = 0 ; j< Ghost_list.size(); ++j) {
			//System.out.println(Ghost_list.size());
			ghost = (Ghost)Ghost_list.get(j);
			if(Crash(x,y,ghost.x,ghost.y,h_w,h_h,g_w,g_h)) {
				re++;
			}
		}
	}
	
	public void Draw_End() {
		buffg.drawImage(background_img, 0, 0, null);
		buffg.drawString("The End", f_width/2, f_height/2);
/*		
		end= new ImageIcon("End.png").getImage();
		JButton button = new JButton();
		button.setText("Start");
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		add(button);
		
		ActionListener listener = new ActionListener() {
			int start=0;
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start");	
				start++;
				if(start==1) {
					button.setVisible(false);
					init();
					start();
				}
			}
		};
		button.addActionListener(listener);
*/	}
	
	public void Draw_Ghost() {
		for(int i =0; i<Ghost_list.size();++i) {
			//System.out.println(Ghost_list.get(i));
			ghost = (Ghost)(Ghost_list.get(i));
			buffg.drawImage(gh, ghost.x, ghost.y, this);
		}
	}
	
	public void RockProcess() {
		for(int i =0; i<Rock_list.size(); ++i) {
			rock = (Rock)(Rock_list.get(i));
			rock.move();
			if(rock.x<-960) {
				Rock_list.remove(i);
			}
		}
		
		if(count%300==0) {
			rock = new Rock(f_width+100, 245);
			Rock_list.add(rock);
		}
		
		for(int j = 0 ; j< Rock_list.size(); ++j) {
			//System.out.println(Rock_list.size());
			rock = (Rock)Rock_list.get(j);
			if(Crash(x,y,rock.x,rock.y,h_w,h_h,r_w,r_h)) {
				//setVisible(false);
				t1.interrupt();
				Draw_End();
				System.out.println("end");
				t1.notify();
			}
		}
	}
	
	public void Draw_Rock() {
		for(int i=0; i<Rock_list.size();++i) {
			rock = (Rock)(Rock_list.get(i));
			buffg.drawImage(rock_img, rock.x, rock.y, this);
		}
	}
	
	public void paint(Graphics g) {
		buffImage = createImage(f_width, f_height); 
		buffg = buffImage.getGraphics();
		update(g);

	}
	
	public void update(Graphics g) {
		sc = System.currentTimeMillis();
		Draw_Background();
		Draw_Ghost();
		Draw_Rock();
		Draw_Hamster();
		Draw_Score();
		
		g.drawImage(buffImage, 0, 0, this);

	}
	
	public int ImageWidthValue(String file) {
		int x = 0;
		try {
			File f = new File(file);
			BufferedImage bi = ImageIO.read(f);
			x=bi.getWidth();
		}catch(Exception e) {}
		return x;
	}
	
	public int ImageHeightValue(String file) {
		int y =0 ;
		try {
			File f = new File(file);
			BufferedImage bi = ImageIO.read(f);
			y=bi.getHeight();
		}catch(Exception e) {}
		return y;
	}
	
	public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2) {
		boolean check = false;
		if(Math.abs((x1+w1/2)-(x2+w2/2))<(w2/2+w1/2)&&Math.abs((y1+h1/2)-(y2+h2/2))<(h2/2+h1/2)) {
			check = true;
		}else{check = false;}
		return check;
	}
	
	public BackGround() {
		ImageIcon intro_img = new ImageIcon("Intro.png");  //start display
		f_width = 960;
		f_height = 351;
		setSize(f_width, f_height);
		setFocusable(true);

		JButton button = new JButton(intro_img);
		button.setText("Start");
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(false);
		add(button);
		
		ActionListener listener = new ActionListener() {
			int start=0;
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start");	
				start++;
				if(start==1) {
					button.setVisible(false);
					init();
					start();
				}
			}
		};
		button.addActionListener(listener);
		
		//pack();
		setResizable(false);
		setVisible(true);
		
		System.out.println("?");

	}
	
	
    public static void main(String[] args) {
    		new BackGround();
    }

}