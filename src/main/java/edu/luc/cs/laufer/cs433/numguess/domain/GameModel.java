package edu.luc.cs.laufer.cs433.numguess.domain;

/**
 * An interface for the business logic of this web application.
 */
public interface GameModel {

	/**
	 * Submits a guess to this model instance.
	 * 
	 * @param guess
	 *            the guess
	 * @return the result of this guess
	 */
	public GuessResult guess(int guess);

	/**
	 * Resets this model instance to begin a new game.
	 * 
	 * @param min
	 *            the minimum number allowed
	 * @param max
	 *            the maximum number allowed
	 */
	public void reset(int min, int max);
}
