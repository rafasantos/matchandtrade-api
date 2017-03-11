package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationCallbakUT {
	
	@Autowired
	private AuthenticationCallback authenticationCallbakServlet;
	@Autowired
	private MockFactory mockFactory;
	
	@Test
	public void doGetAtiForgeryTokenNegative() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("state", "differentState");
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.authenticate(request, response);
		assertEquals(401, response.getStatus());
		assertNull(request.getSession(false));
	}
	
	@Test
	public void doGetAtiForgeryTokenPositive() throws ServletException, IOException {
		UserAuthentication sessionUserAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest request = mockFactory.getHttpRequestWithAuthenticatedUser(sessionUserAuthentication);
		request.setParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString(), AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString());
		request.getSession().setAttribute(AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString(), AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString());
		AuthenticationOAuth authenticationOAuthMock = Mockito.mock(AuthenticationOAuth.class);
		Mockito.when(authenticationOAuthMock.obtainAccessToken(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("accessTokenMock");
		Mockito.when(authenticationOAuthMock.obtainUserInformation("accessTokenMock")).thenReturn(sessionUserAuthentication);
		authenticationCallbakServlet.setAuthenticationOAuth(authenticationOAuthMock);
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.authenticate(request, response);
		String authenticationHeader = response.getHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString());
		assertEquals("accessTokenMock", authenticationHeader);
	}
	
}
