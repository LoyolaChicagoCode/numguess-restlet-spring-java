package edu.luc.cs.laufer.cs433.numguess.domain;

/**
 * An implementation of the shared data for this application. It is intended to
 * be stored as an attribute in the application scope (shared between all
 * sessions).
 */
public class DefaultSharedData implements SharedData {

	private int bestScore = Integer.MAX_VALUE;

	/* (non-Javadoc)
	 * @see numguess.SharedData#getBestScore()
	 */
	@Override
	public synchronized int getBestScore() {
		return bestScore;
	}

	/* (non-Javadoc)
	 * @see numguess.SharedData#setIfBestScore(int)
	 */
	@Override
	public synchronized boolean setIfBestScore(int bestScore) {
		boolean result = bestScore < this.bestScore;
		if (result) {
			this.bestScore = bestScore;
		}
		return result;
	}
}