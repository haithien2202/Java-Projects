package nz.ac.vuw.ecs.swen225.gp21.app;

import nz.ac.vuw.ecs.swen225.gp21.domain.Game;

/**
 * The Class Countdown.
 */
public class Countdown {	
	
	/** Start counting. */
	private static long startTime;
	
	/** Total time to finish the game. */
	private static int totalTime = 50;
	
	/** Total pause time. */
	private static int totalPauseTime = 0;
	
	/** Current pause time. */
	private static int pauseTime = 0;
	
	/** Start of pausing time. */
	private static long startPausing = 0;
	
	/** Extra time. */
	private static int extraTime = 0;
	
	/**
	 * return extra time added.
	 *
	 * @param e the new extra time
	 */
	public static void setExtraTime(int e) {
		extraTime = e;
	}
	
	/**
	 * get count down time.
	 *
	 * @param g the g
	 * @return the count down
	 */
	public static void getCountDown(Game g) {
		g.setTimeLeft(Countdown.getTimeLeft(startTime, totalTime, totalPauseTime));
		if (g.isPaused()) {
			pauseTime = Countdown.getPauseTime(startPausing);
		}
	}
	
	
	/**
	 * Set time.
	 *
	 * @param t the new timer
	 */
	public static void setTimer(int t) {
		totalTime = t;
	}
	
	/**
	 * Start pausing.
	 */
	public static void setInitPauseTime() {
		 startPausing = System.currentTimeMillis();
	}
	
	
	/**
	 * Finish pausing, add the total pausing time.
	 */
	public static void updateTotalPauseTime() {
		totalPauseTime += pauseTime;
	}
	

	/**
	 * Set start game.
	 */
	public static void setStartTime() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * Reset the countdown.
	 */
	public static void resetCountDown() {
		startTime = System.currentTimeMillis()+1000;
		totalPauseTime = 0;
		extraTime = 0;
	}
	
	/**
	 * Get time left to finish the level.
	 *
	 * @param startTime the start time
	 * @param totalTime the total time
	 * @param totalPause the total pause
	 * @return the time left
	 */
	private static int getTimeLeft(long startTime, int totalTime, int totalPause) {
		int timeleft = (int) (totalTime - (System.currentTimeMillis() - startTime)/1000 + totalPause + extraTime);
		return timeleft;
	}
	

	/**
	 * return total pausing time.
	 *
	 * @param startPause the start pause
	 * @return the pause time
	 */
	private static int getPauseTime(long startPause) {
		int pause = (int) (System.currentTimeMillis() - startPause)/1000 +1;
		return pause;
	}
}
