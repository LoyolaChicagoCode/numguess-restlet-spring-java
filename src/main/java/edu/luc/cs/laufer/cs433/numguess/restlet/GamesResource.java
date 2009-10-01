package edu.luc.cs.laufer.cs433.numguess.restlet;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Variant;

import edu.luc.cs.laufer.cs433.numguess.domain.Games;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * A resource for initiating new games.
 * 
 * GET: welcome page (with button to start game).
 * 
 * POST: create and start game (with redirect to representation of the new game).
 */
public class GamesResource extends Resource {

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

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#init(org.restlet.Context, org.restlet.data.Request, org.restlet.data.Response)
	 */
	@Override
	public void init(final Context context, final Request request,
			final Response response) {
		super.init(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_HTML));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#represent(org.restlet.resource.Variant)
	 */
	@Override
	public Representation represent(final Variant variant) {
		if (variant == null)
			return null;

		if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
			try {
				final Template template = freemarkerConfig
						.getTemplate("welcome.ftl");
				// pass a null data model to Freemarker template
				return new TemplateRepresentation(template, null,
						MediaType.TEXT_HTML);
			} catch (IOException ex) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
	 */
	@Override
	public void acceptRepresentation(final Representation entity) {
		final int index = games.createGame();
		getResponse().setStatus(Status.SUCCESS_CREATED);
		final Reference ref = getRequest().getResourceRef();
		ref.addSegment(Integer.toString(index));
		if (getPreferredVariant().getMediaType().equals(MediaType.TEXT_HTML)) {
			getResponse().redirectPermanent(ref);
		} else {
			JSONObject result = new JSONObject();
			try {
				result.put("href", ref.toString());
				getResponse().setEntity(new JsonRepresentation(result));
			} catch (JSONException ex) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		}
	}
}
