package edu.luc.cs.laufer.cs433.numguess.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A collection of game instances representing ongoing games.
 */
public class Games {

	/**
	 * The internal collection that holds the game model instances.
	 */
	private List<DefaultGameModel> games = new ArrayList<DefaultGameModel>();

	/**
	 * Creates a new game and adds it to this collection.
	 * 
	 * @return the index of the new game
	 */
	public synchronized int createGame() {
		final DefaultGameModel newGame = new DefaultGameModel();
		games.add(newGame);
		newGame.setSharedData(sharedData);
		newGame.reset(1, 100);
		final int index = games.size() - 1;
		Logger.getRootLogger().info(this + ": appended game " + index);
		return index;
	}

	/**
	 * Returns the game instance corresponding to the given index.
	 * 
	 * @param index
	 *            the index of the desired game instance
	 * @return the game instance
	 */
	public synchronized GameModel getGame(int index) {
		return games.get(index);
	}

	/**
	 * Indicates whether there is a game instance corresponding to the given
	 * index.
	 * 
	 * @param index
	 *            the index of the desired game instance
	 * @return whether the game instance exists
	 */
	public synchronized boolean hasGame(int index) {
		return index < games.size();
	}

	/**
	 * The shared data for the best score.
	 */
	private SharedData sharedData;

	/**
	 * Injects the dependency on the shared data instance.
	 * 
	 * @param sharedData
	 *            the shared data instance
	 */
	public void setSharedData(SharedData sharedData) {
		this.sharedData = sharedData;
	}
}
