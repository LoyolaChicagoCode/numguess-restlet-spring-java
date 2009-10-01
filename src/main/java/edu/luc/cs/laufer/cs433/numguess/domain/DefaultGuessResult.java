package edu.luc.cs.laufer.cs433.numguess.domain;

/**
 * This bean is used to represent guess results going back from the game to the
 * user. It is an example of the ValueObject (TransferObject) pattern.
 */
public class DefaultGuessResult implements GuessResult {

	private int guess;
	private int comparison;
	private int guesses;
	private int bestScore;
	private boolean newBestScore;

	/* (non-Javadoc)
	 * @see numguess.GuessResult#getGuess()
	 */
	public int getGuess() {
		return guess;
	}

	public void setGuess(int guess) {
		this.guess = guess;
	}

	/* (non-Javadoc)
	 * @see numguess.GuessResult#getComparison()
	 */
	public int getComparison() {
		return comparison;
	}

	public void setComparison(int comparison) {
		this.comparison = comparison;
	}

	/* (non-Javadoc)
	 * @see numguess.GuessResult#getNumGuesses()
	 */
	public int getNumGuesses() {
		return guesses;
	}

	public void setNumGuesses(int guesses) {
		this.guesses = guesses;
	}

	/* (non-Javadoc)
	 * @see numguess.GuessResult#getBestScore()
	 */
	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	/* (non-Javadoc)
	 * @see numguess.GuessResult#isNewBestScore()
	 */
	public boolean isNewBestScore() {
		return newBestScore;
	}

	public void setNewBestScore(boolean newBestScore) {
		this.newBestScore = newBestScore;
	}
}