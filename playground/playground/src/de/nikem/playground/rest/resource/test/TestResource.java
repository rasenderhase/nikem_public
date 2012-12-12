package de.nikem.playground.rest.resource.test;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.MultiPart;

@Path("/customers")
public class TestResource {

	@POST
	public Response test(MultiPart mimeMultipartData) {
		System.out.println("TEST");
		System.out.println(mimeMultipartData);
		
		for (BodyPart bodyPart : mimeMultipartData.getBodyParts()) {
			BodyPartEntity entity = (BodyPartEntity) bodyPart.getEntity();
			System.out.println(entity);
			
			InputStream is = entity.getInputStream();
			byte[] buffer = new byte[1024];
			try {
				int read = is.read(buffer);
				System.out.println(new String(buffer, 0, read));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return Response.ok().type("multipart/mixed").build();
	}
}
