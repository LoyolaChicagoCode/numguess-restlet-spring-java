package edu.luc.cs.laufer.cs433.numguess.restlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

import edu.luc.cs.laufer.cs433.numguess.domain.Games;
import edu.luc.cs.laufer.cs433.numguess.domain.GuessResult;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * A resource for a single game.
 *
 * GET: guess form for this game.
 *
 * POST: submit and process a guess (returns representation of game depending on
 * game state).
 */
public class GameResource extends Resource {

	@Override
	public void init(final Context context, final Request request,
			final Response response) {
		super.init(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.APPLICATION_XHTML_XML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
		id = Integer.parseInt((String) getRequest().getAttributes().get(
				"gameid"));
		if (!games.hasGame(id))
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
	}

	/**
	 * The unique ID of this game. This ID is the last part of the game's URI.
	 */
	private int id;

	/**
	 * Returns the unique ID of this game.
	 *
	 * @return the unique ID of this game
	 */
	protected int getId() {
		return id;
	}

	/**
	 * The collection of games that this game belongs to.
	 */
	private Games games;

	/**
	 * Sets the collection of games that this game belongs to.
	 *
	 * @param games
	 *            the collection of games
	 */
	public void setGames(final Games games) {
		this.games = games;
	}

	/**
	 * The Freemarker configuration for this resource.
	 */
	private Configuration freemarkerConfig;

	/**
	 * Sets the Freemarker configuration for this resource.
	 *
	 * @param freemarkerConfig
	 *            the Freemarker configuration
	 */
	public void setFreemarkerConfig(final Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restlet.resource.Resource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(final Variant variant) {
		if (variant == null)
			return null;

		if (variant.getMediaType().equals(MediaType.APPLICATION_XHTML_XML)) {
			try {
				final Template template = freemarkerConfig
						.getTemplate("guess.ftl");
				// pass map as data model to Freemarker template:
				// min and max are map entries
				final Map<String, Object> data = new HashMap<String, Object>();
				data.put("guess_min", 1);
				data.put("guess_max", 100);
				return new TemplateRepresentation(template, data,
						MediaType.TEXT_HTML);
			} catch (IOException ex) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
	 */
	@Override
	public void acceptRepresentation(final Representation entity) {
		final Form form = new Form(entity);
		Logger.getRootLogger().info(this + ": form " + form);
		final int guess = Integer.parseInt(form.getFirstValue("guess"));
		final GuessResult result = games.getGame(getId()).guess(guess);
		Logger.getRootLogger().info(
				this + ": form parameters " + form.getQueryString());
		if (getPreferredVariant().getMediaType().equals(MediaType.APPLICATION_XHTML_XML)) {
			try {
				final Template template = freemarkerConfig
						.getTemplate("result.ftl");
				getResponse().setStatus(Status.SUCCESS_CREATED);
				// pass POJO as data model to Freemarker template:
				// guess result object
				getResponse().setEntity(
						new TemplateRepresentation(template, result,
								MediaType.TEXT_HTML));
			} catch (IOException ex) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} else {
			getResponse().setEntity(new JsonRepresentation(result));
		}
	}
}
