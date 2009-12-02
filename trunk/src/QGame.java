
public class QGame {
	
	public int state = 0;
	
	long gameStartTime = 0;
	long gameTime;
	long lastMeasureTime;
	static long realTime= System.currentTimeMillis();
	
	public QGame() {
		gameTime = gameStartTime = realTime = System.currentTimeMillis();
	}
	
	public long getGameTime() {
		return gameTime;
	}
	
	static public long getRealTime() {
		return realTime;
	}
	
	public void update() {
		realTime = System.currentTimeMillis();
		gameTime = realTime - gameStartTime;
		//lastMeasureTime = realTime;
	}
	
}
