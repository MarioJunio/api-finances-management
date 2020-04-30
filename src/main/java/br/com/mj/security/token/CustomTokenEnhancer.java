package br.com.mj.security.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import br.com.mj.security.user.UsuarioSistema;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		UsuarioSistema usuario = (UsuarioSistema) authentication.getPrincipal();
		DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;

		Map<String, Object> additionalInfos = new HashMap<>();
		additionalInfos.put("nome", usuario.getUsuario().getNome());

		defaultAccessToken.setAdditionalInformation(additionalInfos);

		return defaultAccessToken;
	}

}
