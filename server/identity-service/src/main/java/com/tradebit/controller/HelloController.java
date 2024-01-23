package com.tradebit.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/public/hello")
	public ResponseEntity<String> helloPublic() {
		return ResponseEntity.ok("Hello public user");
	}
	
	@GetMapping("/secured/hello")
	public ResponseEntity<String> helloMember() {
		return ResponseEntity.ok("Hello authenticated user");
	}
	@RolesAllowed("ADMIN")
	@GetMapping("/admin/hello")
	public ResponseEntity<String> helloAdmin() {
		return ResponseEntity.ok("Hello admin");
	}

}