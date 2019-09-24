package es.udc.fi.dc.fd.service;


import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.controller.exception.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.ImageImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.ImageRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
public class ImageServiceImpl implements ImageService {
	
	private PermissionChecker permissionChecker;
	private final ImageRepository imageRepository;
	private final UserRepository userRepository;
	
	@Autowired
	public ImageServiceImpl(final ImageRepository imageRepository, final PermissionChecker permissionChecker, 
			final UserRepository userRepository){
		super();
		
		this.imageRepository = checkNotNull(imageRepository,
                "Received a null pointer as service imageRepository");
		
		this.permissionChecker = checkNotNull(permissionChecker,
                "Received a null pointer as service permissionChecker");
		
		this.userRepository = checkNotNull(userRepository,
                "Received a null pointer as service userRepository");
	}

	@Override
	public ImageImpl addImage(ImageImpl image, Long userId) throws InstanceNotFoundException {
		
		UserImpl user = permissionChecker.checkUser(userRepository.getOne(userId).getUserName());
		
		image.setUser(user);
		
		ImageImpl i = imageRepository.save(image);
		
		return i;
	}

	@Override
	public void removeImage(ImageImpl image, Long userId) throws InstanceNotFoundException {
		
		permissionChecker.checkUser(userRepository.getOne(userId).getUserName());
		
		imageRepository.delete(image);
		
	}

}
