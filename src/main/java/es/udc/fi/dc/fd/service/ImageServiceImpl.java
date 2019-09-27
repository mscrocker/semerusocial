package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.InvalidImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {
	
	private final ImageRepository imageRepository;
	
	private PermissionChecker permissionChecker;
	
	@Autowired
	public ImageServiceImpl(final ImageRepository imageRepository, final PermissionChecker permissionChecker){
		super();
		
		this.imageRepository = checkNotNull(imageRepository,
                "Received a null pointer as imageRepository in ImageServiceImpl");
		
		this.permissionChecker = checkNotNull(permissionChecker,
                "Received a null pointer as permissionChecker in ImageServiceImpl");
	}

	@Override
	public ImageImpl addImage(ImageImpl image, Long userId) throws InstanceNotFoundException {
		UserImpl user = permissionChecker.checkUserByUserId(userId);
		
		image.setUser(user);

		return getImageRepository().save(image);
	}
	
	@Override
	public ImageImpl editImage(ImageImpl image, Long imageId, Long userId) throws InstanceNotFoundException, InvalidImageException {
		permissionChecker.checkUserByUserId(userId);
		
		Optional<ImageImpl> resultImage = getImageRepository().findById(imageId);
		
		//Si la imagen no existe
		if (!resultImage.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId="+image.getImageId()+" doesn't exist", resultImage);
		}
		
		image.setImageId(imageId);
		
		//Comprobamos que el user el mismo que el que viene en la imagen
		if (resultImage.get().getUser().getId()!=userId) {
			throw new InvalidImageException();
		}
		
		return getImageRepository().save(image);
	}

	@Override
	public void removeImage(ImageImpl image, Long userId) throws InstanceNotFoundException, InvalidImageException {
		permissionChecker.checkUserByUserId(userId);
		
		Optional<ImageImpl> i = getImageRepository().findById(image.getImageId());
		
		if (!i.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId="+image.getImageId()+" doesn't exist", i);
		}
		
		if (i.get().getUser().getId()!=userId) {
			throw new InvalidImageException();
		}
		
		getImageRepository().delete(image);
	}

	@Override
	public Block<ImageImpl> getImagesByUserId(Long userId, int page) throws InstanceNotFoundException {
		if (userId == null) {
			throw new InstanceNotFoundException("User not found", userId);
		}

		permissionChecker.checkUserExists(userId);
		
		Slice<ImageImpl> images = getImageRepository().findByUserId(userId, PageRequest.of(page, 10));
		
		return new Block<>(images.getContent(), images.hasNext());
	}
	
	public ImageRepository getImageRepository() {
		return imageRepository;
	}
	
}
