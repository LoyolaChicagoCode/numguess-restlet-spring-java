package edu.luc.cs.laufer.cs433.numguess.client.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;

public class TestClient {

	private static final int MIN = 1, MAX = 100;

	private static final String URI_GAMES = "http://localhost:3000/numguess-restlet/games/";

	/**
	 * curl --header 'Accept: application/json' -d ' ' \
	 * http://localhost:3000/numguess-restlet/games/
	 */
	protected JSONObject postGames() throws IOException, JSONException {
		final Client client = new Client(Protocol.HTTP);
		final Request request = new Request(Method.POST, URI_GAMES);
		request.getClientInfo().setAcceptedMediaTypes(
				Collections.singletonList(new Preference<MediaType>(
						MediaType.APPLICATION_JSON)));
		final String responseBody = client.handle(request).getEntity()
				.getText();
		return new JSONObject(responseBody);
	}

	/**
	 * curl --header 'Accept: application/json' -d 'guess=28' \
	 * http://localhost:3000/numguess-restlet/games/0
	 */
	protected JSONObject postGuess(final Reference uri, final int guess)
			throws IOException, JSONException {
		final Client client = new Client(Protocol.HTTP);
		final Request request = new Request(Method.POST, uri);
		request.getClientInfo().setAcceptedMediaTypes(
				Collections.singletonList(new Preference<MediaType>(
						MediaType.APPLICATION_JSON)));
		final Form form = new Form();
		form.add("guess", Integer.toString(guess));
		request.setEntity(form.getWebRepresentation());
		final String responseBody = client.handle(request).getEntity()
				.getText();
		return new JSONObject(responseBody);
	}

	@Test
	public void testNewGame() throws Exception {
		final Reference refExpected = new Reference(URI_GAMES);
		// start first new game
		final JSONObject r1 = postGames();
		assertTrue(r1.has("href"));
		final Reference refActual1 = new Reference(r1.getString("href"));
		// make sure link is original link plus game number
		assertEquals(refExpected, refActual1.getParentRef());
		// start second new game
		final JSONObject r2 = postGames();
		assertTrue(r2.has("href"));
		// make sure link is original link plus game number
		final Reference refActual2 = new Reference(r2.getString("href"));
		assertEquals(refExpected, refActual1.getParentRef());
		// make sure game numbers are consecutive
		final int index1 = Integer.parseInt(refActual1.getLastSegment()
				.toString());
		final int index2 = Integer.parseInt(refActual2.getLastSegment()
				.toString());
		assertEquals(index1 + 1, index2);
	}

	@Test
	public void testMultipleGuesses() throws Exception {
		// start new game
		final JSONObject r0 = postGames();
		final Reference refGame = new Reference(r0.getString("href"));
		// submit guess
		final JSONObject r1 = postGuess(refGame, 50);
		assertEquals(1, Integer.parseInt(r1.getString("numGuesses")));
		final JSONObject r2 = postGuess(refGame, 50);
		assertEquals(2, Integer.parseInt(r2.getString("numGuesses")));
		final JSONObject r3 = postGuess(refGame, 50);
		assertEquals(3, Integer.parseInt(r3.getString("numGuesses")));
	}

	@Test
	@SuppressWarnings("null")
	public void testComplexSequential() throws Exception {
		// start new game
		final JSONObject r0 = postGames();
		final Reference refGame = new Reference(r0.getString("href"));
		// submit successive guesses
		JSONObject result = null;
		for (int i = MIN; i <= MAX; i++) {
			result = postGuess(refGame, i);
			assertEquals(i, Integer.parseInt(result.getString("numGuesses")));
			if (Integer.parseInt(result.getString("comparison")) == 0)
				break;
		}
		assertEquals(0, Integer.parseInt(result.getString("comparison")));
	}

	@Test
	public void testComplexBinary() throws Exception {
		// start new game
		final JSONObject r0 = postGames();
		final Reference refGame = new Reference(r0.getString("href"));
		// submit successive guesses
		JSONObject result;
		int min = MIN, max = MAX;
		while (true) {
			final int middle = min + (max - min) / 2;
			result = postGuess(refGame, middle);
			final int comparison = Integer.parseInt(result
					.getString("comparison"));
			if (comparison == 0)
				break;
			else if (comparison > 0) {
				max = middle;
			} else {
				min = middle;
			}
		}
		assertTrue(Integer.parseInt(result.getString("numGuesses")) <= Math
				.ceil(Math.log(MAX - MIN) / Math.log(2)));
	}
}
