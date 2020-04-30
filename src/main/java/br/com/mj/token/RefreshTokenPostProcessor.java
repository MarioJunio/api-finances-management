package br.com.mj.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.mj.properties.AlgamoneyProperties;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private AlgamoneyProperties properties;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();

		// obtem o token para alteracao
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;

		// adiciona cookie refresh token na resposta
		adicionaRefreshTokenNoCookie(req, res, token.getRefreshToken().getValue());

		// remove refresh token do corpo de resposta
		token.setRefreshToken(null);

		return token;
	}

	private void adicionaRefreshTokenNoCookie(HttpServletRequest req, HttpServletResponse res, String value) {
		Cookie cookie = new Cookie("refresh-token", value);
		cookie.setHttpOnly(true);
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setSecure(properties.getSeguranca().isEnableHttps());
		cookie.setMaxAge(3600 * 24);

		res.addCookie(cookie);
	}

}
