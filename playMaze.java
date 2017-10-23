import java.awt.*;
import java.awt.event.*;

class stackcell{
	int x, y, size;
	stackcell tail; //first
	public stackcell(int a, int b, int s, stackcell t){
		y=a; x=b; tail=t; size = s;
	}
	
	//comparison
	public boolean equals(stackcell target){
		return x==target.x && y==target.y;
	}
	
	//cut function
	public void cut(){
		if(size<2){return;}
		stackcell candidate = null;
		int count = 0, add = 0;
		
		stackcell i = tail;		
		while(i!=null){
			if(i.equals(tail)){
				candidate = i; count = add;
			}
			i = i.tail; add++;
		}if(candidate != null){tail = candidate;
			size = size - count;
		}
	}//stack class
}

class playMaze extends mazedfs
{	
	public playMaze(int bh0, int mh0, int mw0) // don't change constructor
	{
		super(bh0,mh0,mw0); 
	}

	public void customize(){
//		showvalue = true;
	}
	
	public void digout(int y, int x)   // modify this function
	{
		// The following is a skeleton program that demonstrates the mechanics
		// needed for the completion of the program.

		// We always dig out two spaces at a time: we look two spaces ahead
		// in the direction we're trying to dig out, and if that space has
		// not already been dug out, we dig out that space as well as the
		// intermediate space.  This makes sure that there's always a wall
		// separating adjacent corridors.

		M[y][x] = 1;  // digout maze at coordinate y,x
		drawblock(y,x);  // change graphical display to reflect space dug out

		//Permutation of Directions
		//Randomize the directions taken
		int[] P = {0,1,2,3};
		for(int i=0; i<P.length; i++){
			int r = i+(int)(Math.random()*P.length-i); //r is between i and P.length-1
			int temp = P[i]; //swap each element with some random element
			P[i] = P[r];
			P[r] = temp;
		}

		//NESW -> 0,1,2,3
		int []dX = {0, 1, 0, -1};
		int []dY = {-1, 0, 1, 0};

		for(int dir=0; dir<4; dir++){
			int rand=P[dir];
			int dx = dX[rand], dy = dY[rand];
			int nx = x+dx*2, ny = y+dy*2;

			if (nx>=0 && nx<mw && ny>=0 && ny<mh && M[ny][nx]==0) // always check for maze boundaries //order matters 
			{
				M[y+dy][x+dx] = 1;
				drawblock(y+dy,x+dx);
				digout(ny,nx);
			}
		}//done!
	}//digout

	//Reprogrammed to generate the optimal path for comparison to the player's path.
	public void trace(){
		while(stack != null){
			M[stack.y][stack.x]=50; //Set ideal pathing by this number
			drawblock(stack.y,stack.x);
			idealMoves++; //generates ideal number of moves
			stack = stack.tail;
		}
	}

	stackcell stack = null; //Create a global solution stack
	int size = 0; //Implemented size var for cut function

	public void solve()
	{
		int x=1, y=1;
		int a,b,c,d;
		int min = 9999, minIdx = -1;
		size++;
		stack = new stackcell(y,x,size,stack); 
		while(y!=mh-2 || x!=mw-1){ //While we haven't reached the goal.	
			//Check if not a wall and value exists
			if(x+1>=0 && M[y][x+1]!=0){ a = M[y][x+1];}//right
			else{a = 127;}
		
			if(x-1>=0 && M[y][x-1]!=0){ b = M[y][x-1];}//left
			else{b = 127;}
	
			if(y+1>=0 && M[y+1][x]!=0){ c = M[y+1][x];}//up
			else{c = 127;}
	
			if(y-1>=0 && M[y-1][x]!=0){ d = M[y-1][x];}//down
			else{d = 127;}
			

			// Find the smallest value
			min = a;
			minIdx = 0;
			if(b>0 && min>b){min = b; minIdx = 1;}
			if(c>0 && min>c){min = c; minIdx = 2;}
			if(d>0 && min>d){min = d; minIdx = 3;}	
			
			if(minIdx==0){//Move through the maze
				M[y][x+1]++; // Update value
				size++; 
				stack = new stackcell(y,x+1,size,stack);
				stack.cut();
				x++;// increment M

			}else if(minIdx==1){
				M[y][x-1]++; // Update value
				size++;
				stack = new stackcell(y,x-1,size,stack);
				stack.cut();
				x--;
				
			}else if(minIdx==2){
				M[y+1][x]++; // Update value
				size++;
				stack = new stackcell(y+1,x,size,stack);
				stack.cut();
				y++;
				
			}else{
				M[y-1][x]++; // Update value
				size++;
				stack = new stackcell(y-1,x,size,stack);
				stack.cut();
				y--;				

			}
		}
	}//solve

	//This section of code serves no real purpose other than to troll. I might as well have fun with it.
	//The lab did say --> "appropriately denigrating messages" (Although 'appropriate' is debatable)
	//===============================================================================//
	public void insultCodex(int num, int mode){
		int x = num/2;
		if(x>9){
			x=9;
		}
		if(mode==0){
			String[] insults1 = {
				"You hit a wall.", "Wow. Really?",
				"Are you special?", "I was wrong. You ARE special.",
				"Achievement Unlocked: Brain Damage", "Do you need to be committed?", 
				"How is a toddler more intelligent than you?", "Obligatory mental hospital joke.",
				"I'm impressed. You're actually too stupid to insult.",
				"At this point you're either too stupid or really bored. Get a life. Preferably one far far away from me."
			};
			drawMessage(insults1[x]);
		}else if(mode==1){
			String[] insults2 = {
				"You're going the wrong way.", "Are you dumb? You've already been there.",
				"JESUS CHRIST, TURN AROUND!", 
				"I'm trying to help you here! You're going the wrong way!",
				"And you wonder why machines wanna SkyNet humans.",
				"Do you just love backtracking or something?",
				"Dude. Just turn around.",
				"I wasted processing power to give you this solution. Stop backtracking and turn around already!",
				"At this point, I honestly don't care if you do anymore.",
				"Keep going the wrong way. See if I care.",
			};
			drawMessage(insults2[x]);
		}else if(mode==2){
			String[] insults3 = {
				"That's the wrong way.", "Are you retarded? I said that's the wrong way.",
				"Are you even listening? You're WAY off course.", 
				"I've seen special ed kids with more intelligence.",
				"I didn't know you could get this stupid.", "Really? You're not even remotely close.",
				"Achievement Unlocked: Negative IQ",
				"How is it that I feel sorry for you? Just stop this.",
				"You're existence is God's failure. Please stop this.",
				"You know what? I don't care. Keep being bad at this."
			};
			drawMessage(insults3[x]);
		}else{
			float z = num/(float)idealMoves;
			if(z<.25){x=0;}
			else if(z>=.25 && z<.5){x=1;}
			else if(z>=.5 && z<.75){x=2;}
			else if(z>=.75 && z<.85){x=3;}
			else{x=4;}
			String[] patBack = {
				"You're on the right path, human.",
				"You're almost halfway through.",
				"You're halfway there. Keep going.",
				"You're almost there. Just a little further.",
				"Home stretch! Just a couple more steps!!!"
			};
			drawMessage(patBack[x]);
		}
	}
	//===============================================================================//

	//Keeps track of your 'score' vs the CPU's
	int yourMoves = 0, idealMoves = 0; 

	//key-->Keyboard inputs; backtrack-->If player has been there before; patBack-->Tells player how they're doing
	int key=0, backtrack=0, patBack=0;
	
	//Insult counters
	int bad0 = 0, bad1 = 0, bad2 = 0;
	
	public void evalMove(int y, int x){
		if(M[y][x]==50){
			if(bad2>0){drawMessage("Thank God. Now keep going.");}
			else{insultCodex(patBack,3);}
			
			patBack++;
			bad1=0; bad2=0;
			M[y][x]=25;
			backtrack = 0;

		}else if(M[y][x]==25){
			M[y][x]=50;
			patBack--;
			backtrack++;
			if(backtrack>=2){
				insultCodex(bad1,1);
				bad1++;
			}
		}else{
			if(M[y][x] != 10 && M[y][x] < 25){ //It'll only keep insulting you if you continue to go the wrong way.	
				insultCodex(bad2,2);
				bad2++;
				M[y][x]= 10;
			}else{
				drawMessage("Yes! Please! Now keep going back.");
				M[y][x]=1;
			}
		}
	}

	public void play(){
		autodelay = false;
		drawMessage("Try and solve this maze, human!");
		int x=1, y=1;
		while(y!=mh-2 || x!=mw-1){ //While we haven't reached the goal.	
			drawdot(y,x); //Start
			if(key==39){
				if(x+1>=0 && M[y][x+1]!=0){
					bad0=0;
					drawblock(y,x);
					drawdot(y,x+1);
					yourMoves++;
					x++;
					evalMove(y,x);
				}//right
				else{
					insultCodex(bad0,0);
					bad0++;
				}
			}else if(key==37){
				if(x-1>=0 && M[y][x-1]!=0){
					bad0=0;
					drawblock(y,x);
					drawdot(y,x-1);
					yourMoves++;
					x--;
					evalMove(y,x);
				}//left
				else{
					insultCodex(bad0,0);
					bad0++;
				}
			}else if(key==40){
				if(y+1>=0 && M[y+1][x]!=0){
					bad0=0;
					drawblock(y,x);
					drawdot(y+1,x);
					yourMoves++;
					y++;
					evalMove(y,x);
				}//up
				else{
					insultCodex(bad0,0);
					bad0++;
				}
			}else if(key==38){
				if(y-1>=0 && M[y-1][x]!=0){ 
					bad0=0;
					drawblock(y,x);
					drawdot(y-1,x);
					yourMoves++;
					y--;
					evalMove(y,x);
				}//down
				else{
					insultCodex(bad0,0);
					bad0++;
				}
			}key=0;
		}//end of while loop
		idealMoves--;
		if(yourMoves<=idealMoves){
			drawMessage("Congrats on not being trash. But you're still trash. You:CPU -- "+yourMoves+":"+idealMoves);
		}else{drawMessage("Wow. You're really bad at this. You:CPU -- "+yourMoves+":"+idealMoves);}
	}
	public void keyPressed(KeyEvent e){
		key = e.getKeyCode();
	}

	public void drawMessage(String m){
		g.setColor(Color.green);
		g.fillRect(0,yoff,bw*mw,bh);
		g.setColor(Color.blue); // erase line
		g.drawString(m,10,yoff+bh-4);	
	}
}//studentcode2 subclass

