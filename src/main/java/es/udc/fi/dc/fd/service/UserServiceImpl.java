package es.udc.fi.dc.fd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.controller.exception.DuplicateInstanceException;
import es.udc.fi.dc.fd.controller.exception.IncorrectLoginException;
import es.udc.fi.dc.fd.model.persistence.UserImpl;
import es.udc.fi.dc.fd.repository.UserRepository;

public class UserServiceImpl implements UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

		//---------- DAOS ----------
		@Autowired
		private UserRepository userRepository;
		
		//---------- CASOS DE USO ----------
		
		//1. Registro de usuarios
		@Override
		public void signUp(UserImpl user) throws DuplicateInstanceException {
			
			if (getUserRepository().existsByUserName(user.getUserName())) {
				throw new DuplicateInstanceException("project.entities.user", user.getUserName());
			}
				
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			getUserRepository().save(user);
			
		}
		
		//2. Autenticación y salida
		@Override
		@Transactional(readOnly=true)
		public UserImpl login(String userName, String password) throws IncorrectLoginException {
			
			Optional<UserImpl> user = getUserRepository().findByUserName(userName);
			
			if (!user.isPresent()) {
				throw new IncorrectLoginException(userName, password);
			}
			
			if (!passwordEncoder.matches(password, user.get().getPassword())) {
				throw new IncorrectLoginException(userName, password);
			}
			
			return user.get();
			
		}
		
		public UserRepository getUserRepository() {
			return userRepository;
		}

}

