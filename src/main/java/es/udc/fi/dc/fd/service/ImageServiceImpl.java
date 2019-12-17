package es.udc.fi.dc.fd.service;

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

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PermissionChecker permissionChecker;


	@Override
	public ImageImpl addImage(ImageImpl image, Long userId) throws InstanceNotFoundException {
		final UserImpl user = permissionChecker.checkUserByUserId(userId);

		image.setUser(user);

		return getImageRepository().save(image);
	}

	@Override
	public void removeImage(Long imageId, Long userId) throws InstanceNotFoundException, ItsNotYourImageException {
		permissionChecker.checkUserExists(userId);

		final Optional<ImageImpl> i = getImageRepository().findById(imageId);

		if (!i.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId=" + imageId + " doesn't exist", i);
		}

		if (!i.get().getUser().getId().equals(userId)) {
			throw new ItsNotYourImageException("You can't remove a image that doesn't belong to you.");
		}

		getImageRepository().delete(i.get());
	}

	@Override
	@Transactional(readOnly = true)
	public Block<ImageImpl> getImagesByUserId(Long userId, int page) throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);

		final Slice<ImageImpl> images = getImageRepository().findByUserId(userId, PageRequest.of(page, 10));

		return new Block<>(images.getContent(), images.hasNext());
	}

	@Override
	@Transactional(readOnly = true)
	public BlockImageByUserId<ImageImpl> getImageByUserId(Long imageId, Long userId)
			throws InstanceNotFoundException, ItsNotYourImageException {
		permissionChecker.checkUserExists(userId);

		final Optional<ImageImpl> image = getImageRepository().findById(imageId);

		if (!image.isPresent()) {
			throw new InstanceNotFoundException("Image with imageId=" + imageId + " doesn't exist", image);
		}

		if (!image.get().getUser().getId().equals(userId)) {
			throw new ItsNotYourImageException("You can't access to image that doesn't belong to you.");
		}
		final List<ImageImpl> images = getImageRepository().findByUserId(userId);

		final List<Long> ids = new ArrayList<>();
		for (final ImageImpl image2 : images) {
			ids.add(image2.getImageId());
		}

		Long prevId;
		Long nextId;
		final int position = ids.indexOf(imageId);

		try {
			prevId = ids.get(position - 1);
		} catch (final IndexOutOfBoundsException e) {
			prevId = null;
		}
		try {
			nextId = ids.get(position + 1);
		} catch (final IndexOutOfBoundsException e) {
			nextId = null;
		}

		return new BlockImageByUserId<>(image.get(), prevId, nextId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getFirstImageIdByUserId(Long userId) throws InstanceNotFoundException {
		permissionChecker.checkUserExists(userId);

		final List<ImageImpl> images = getImageRepository().findByUserId(userId);

		if (images.size() == 0) {
			return null;
		}
		return images.get(0).getImageId();
	}

	@Override
	@Transactional(readOnly = true)
	public Block<ImageImpl> getAnonymousCarrusel(String city, int page) {
		final Slice<ImageImpl> images = getImageRepository().findAnonymousCarrusel(city, page, 10);

		return new Block<>(images.getContent(), images.hasNext());
	}

	public ImageRepository getImageRepository() {
		return imageRepository;
	}

}
