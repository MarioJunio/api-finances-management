package br.com.mj.token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessor implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		if (req.getRequestURI().equalsIgnoreCase("/oauth/token") && req.getParameter("grant_type").equals("refresh_token")
				&& req.getCookies() != null) {

			// obtem refresh_token cookie
			req = new RefreshTokenServletWrapper(req, refreshTokenCookie(req.getCookies()));
		}

		chain.doFilter(req, response);

	}

	private String refreshTokenCookie(Cookie[] cookies) {

		for (Cookie cookie : cookies) {

			if (cookie.getName().equals("refresh-token")) {
				return cookie.getValue();
			}
		}

		return null;
	}

	class RefreshTokenServletWrapper extends HttpServletRequestWrapper {

		private String refreshToken;

		public RefreshTokenServletWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> params = new HashMap<>(super.getParameterMap());
			params.put("refresh_token", new String[] { refreshToken });

			return params;
		}

	}

}
