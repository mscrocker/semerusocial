package es.udc.fi.dc.fd.dtos;

import es.udc.fi.dc.fd.model.persistence.ImageImpl;

public class ImageConversor {

	private ImageConversor() {
	}

	public final static ImageCreationDto toImageCreationDto(ImageImpl image) {

		return new ImageCreationDto(image.getUser(), image.getImage(), image.getAge(), image.getSex(), image.getCity(),image.getDescription());

	}
	
	public final static ImageImpl toImageImpl(ImageCreationDto image) {

		return new ImageImpl(image.getUser(), image.getImage(), image.getAge(), image.getSex(), image.getCity(),image.getDescription());

	}
}
