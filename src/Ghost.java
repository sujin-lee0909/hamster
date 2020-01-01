
public class Ghost {
	int x, y;
	
	Ghost(int x, int y ){
		this.x=x;
		this.y=y;
	}
	
	//score에 따라 속도 조절해주기
	public void move(int score) {
		if (score>30) {
			x-=3;
		}
		else if(score>80) {
			x-=4;
		}
		else if(score>150)
			x=x-5;
		else if(score>200)
			x=x-6;
		else
			x=x-3;
		
		//System.out.println("score"+score+"x"+x);
	}
}
