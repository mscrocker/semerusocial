package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.controller.exception.ItsNotYourImageException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.ImageRepository;

@Service
@Transactional
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
	public ImageImpl editImage(ImageImpl image, Long imageId, Long userId) throws InstanceNotFoundException, ItsNotYourImageException {
		permissionChecker.checkUserExists(userId);
		
		Optional<ImageImpl> resultImage = getImageRepository().findById(imageId);
		
		if (!resultImage.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId="+image.getImageId()+" doesn't exist", resultImage);
		}
		if (resultImage.get().getUser().getId()!=userId) {
			throw new ItsNotYourImageException("You can't edit a image that doesn't belong to you.");
		}
		
		resultImage.get().setData(image.getData());
		resultImage.get().setDescription(image.getDescription());
		
		return getImageRepository().save(resultImage.get());
	}

	@Override
	public void removeImage(Long imageId, Long userId) throws InstanceNotFoundException, ItsNotYourImageException {
		permissionChecker.checkUserExists(userId);
		
		Optional<ImageImpl> i = getImageRepository().findById(imageId);
		
		if (!i.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId="+imageId+" doesn't exist", i);
		}
		
		if (i.get().getUser().getId()!=userId) {
			throw new ItsNotYourImageException("You can't remove a image that doesn't belong to you.");
		}
		
		getImageRepository().delete(i.get());
	}

	@Override
	@Transactional(readOnly=true)
	public Block<ImageImpl> getImagesByUserId(Long userId, int page) throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);
		
		Slice<ImageImpl> images = getImageRepository().findByUserId(userId, PageRequest.of(page, 10));
		
		return new Block<>(images.getContent(), images.hasNext());
	}

	@Override
	@Transactional(readOnly=true)
	public BlockImageByUserId<ImageImpl> getImageByUserId(Long imageId, Long userId) throws InstanceNotFoundException, ItsNotYourImageException {
		permissionChecker.checkUserExists(userId);
		
		Optional<ImageImpl> image = getImageRepository().findById(imageId);
		
		if (!image.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId="+imageId+" doesn't exist", image);
		}
		
		if (image.get().getUser().getId()!=userId) {
			throw new ItsNotYourImageException("You can't access to image that doesn't belong to you.");
		}

		List<ImageImpl> images = getImageRepository().findByUserId(userId);
		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < images.size(); i++) {
			ids.add(images.get(i).getImageId());
		}
		
		Long prevId;
		Long nextId;
		int position=ids.indexOf(imageId);

		try {
			prevId=ids.get((int)(position-1));
		}
		catch (IndexOutOfBoundsException e) {
			prevId=null;
		}
		try {
			nextId=ids.get((int)(position+1));
		}
		catch (IndexOutOfBoundsException e) {
			nextId=null;
		}
				
		return new BlockImageByUserId<>(image.get(),prevId,nextId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Long getFirstImageIdByUserId(Long userId) throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);

		List<ImageImpl> images = getImageRepository().findByUserId(userId);
		
		if (images.size()==0) {
			return null;
		}	
		return images.get(0).getImageId();
	}
	
	public ImageRepository getImageRepository() {
		return imageRepository;
	}
	
}
