package br.com.mj.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.mj.properties.AlgamoneyProperties;

@RestController
@RequestMapping("/tokens")
public class TokenResource extends Resource {

	@Autowired
	private AlgamoneyProperties properties;

	@DeleteMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = new Cookie("refresh-token", "");
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setHttpOnly(true);
		cookie.setSecure(properties.getSeguranca().isEnableHttps());
		cookie.setMaxAge(0);

		response.addCookie(cookie);
	}

}
