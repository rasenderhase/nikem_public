package de.nikem.ipub.jersey;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.json.JSONWriter;

public abstract class JsonOutput implements StreamingOutput {
	@Override
	public void write(OutputStream output) throws IOException, WebApplicationException {
		OutputStreamWriter writer = new OutputStreamWriter(output);
		JSONWriter w = new JSONWriter(writer);
		write(w);
		writer.flush();
	}
	protected abstract void write(JSONWriter w);
}