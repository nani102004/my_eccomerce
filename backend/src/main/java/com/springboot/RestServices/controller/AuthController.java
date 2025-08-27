package com.springboot.RestServices.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.springboot.RestServices.request.LoginRequest;
import com.springboot.RestServices.response.AuthResponse;
import com.springboot.RestServices.exception.UserException;
import com.springboot.RestServices.model.User;
import com.springboot.RestServices.repository.UserRepository;
import com.springboot.RestServices.config.JwtTokenProvider;
import com.springboot.RestServices.service.CartService;
import com.springboot.RestServices.service.CustomUserDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider jwtTokenProvider;
	private CustomUserDetailsService customUserDetails;
	private CartService cartService;
	
	public AuthController(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtTokenProvider jwtTokenProvider,CustomUserDetailsService customUserDetails,CartService cartService) {
		
		System.out.println("auth Constructor");
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.jwtTokenProvider=jwtTokenProvider;
		this.customUserDetails=customUserDetails;
		this.cartService=cartService;
		
		System.out.println(userRepository);
		System.out.println(passwordEncoder);
		System.out.println(customUserDetails);
		System.out.println(cartService);
//		System.out.prinln(jwtTokenProvider);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody User user) throws UserException{
		
			System.out.println("entered");
		  	String email = user.getEmail();
	        String password = user.getPassword();
	        String firstName=user.getFirstName();
	        String lastName=user.getLastName();
	        String role=user.getRole();
	        
	        User isEmailExist=userRepository.findByEmail(email);

	        // Check if user with the given email already exists
	        if (isEmailExist!=null) {
	        	
	            throw new UserException("Email Is Already Used With Another Account");
	        }

	        // Create new user
			User createdUser= new User();
			createdUser.setEmail(email);
			createdUser.setFirstName(firstName);
			createdUser.setLastName(lastName);
	        createdUser.setPassword(passwordEncoder.encode(password));
	        createdUser.setRole(role);
	        
	        System.out.println(email);
	        User savedUser= userRepository.save(createdUser);
	        
	        cartService.createCart(savedUser);

	        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        
	        String token = jwtTokenProvider.generateToken(authentication);

	        AuthResponse authResponse= new AuthResponse();
	        authResponse.setJwt(token);
	        authResponse.setStatus(true);
			
	        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
		
	}
	
	@PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        System.out.println(username +" ----- "+password);
        
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        
        String token = jwtTokenProvider.generateToken(authentication);
        AuthResponse authResponse= new AuthResponse();
		
		authResponse.setStatus(true);
		authResponse.setJwt(token);
		
        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
    }
	
	private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        
        System.out.println("sign in userDetails - "+userDetails);
        
        if (userDetails == null) {
        	System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        	System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}