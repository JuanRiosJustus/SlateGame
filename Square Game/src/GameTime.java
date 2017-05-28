import java.util.Timer;
import java.util.TimerTask;

public class GameTime {
	private int secondsPassed = 0;
	private int setTime;
	
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed = secondsPassed + 1;
			setTime = setTime - 1;
			System.out.println("Seconds passed:" + secondsPassed);
			System.out.println("Seconds passed:" + setTime);
		}
	};
	
	public GameTime(int setTime) {
		this.setTime = setTime;
		timer.scheduleAtFixedRate(task,1000,1000);
	}
	
	public int getTimerTime() {
		return setTime;
	}
	
	public void resetTimer(int time) {
		setTime = time;
	}
	
	public void moreTime() {
		setTime = setTime + 10;
	}
}