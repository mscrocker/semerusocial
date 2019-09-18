package es.udc.fi.dc.fd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.User;
import es.udc.fi.dc.fd.repository.UserRepository;
/*
public class UserServiceImpl implements UserService {
	@Autowired
	private PermissionChecker permissionChecker;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	//---------- DAOS ----------
		@Autowired
		private UserRepository userDao;
		
		//---------- CASOS DE USO ----------
		
		//1. Registro de usuarios
		@Override
		public void signUp(User user) {
			
			if (userDao.existsByUserName(user.getUserName())) {
				throw new DuplicateInstanceException("project.entities.user", user.getUserName());
			}
				
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(User.RoleType.USER);
			
			userDao.save(user);
			
		}
		
		//2. Autenticación y salida
		@Override
		@Transactional(readOnly=true)
		public User login(String userName, String password) {
			
			Optional<User> user = userDao.findByUserName(userName);
			
			if (!user.isPresent()) {
				throw new IncorrectLoginException(userName, password);
			}
			
			if (!passwordEncoder.matches(password, user.get().getPassword())) {
				throw new IncorrectLoginException(userName, password);
			}
			
			return user.get();
			
		}

}
*/
