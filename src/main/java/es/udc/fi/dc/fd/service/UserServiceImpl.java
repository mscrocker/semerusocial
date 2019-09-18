package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.jwt.JwtGenerator;
import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl( final UserRepository repository){
		super();

		userRepository = checkNotNull(repository,
                "Received a null pointer as service");
		
	}
	// ---------- CASOS DE USO ----------

	// 1. Registro de usuarios
	@Override
	public void signUp(UserImpl user) throws DuplicateInstanceException {
		System.out.println("----" + user.getUserName());
		if (getUserRepository().existsByUserName(user.getUserName())) {
			throw new DuplicateInstanceException("project.entities.user", user.getUserName());
		}

		user.setPassword((user.getPassword()));

		getUserRepository().save(user);

	}

	// 2. Autenticación y salida
	@Override
	@Transactional(readOnly = true)
	public UserImpl login(String userName, String password) throws IncorrectLoginException {

		Optional<UserImpl> user = getUserRepository().findByUserName(userName);

		if (!user.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}

		if (!password.equals( user.get().getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}

		return user.get();

	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

}
