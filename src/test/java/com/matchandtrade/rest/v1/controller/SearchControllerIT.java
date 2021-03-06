package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.ListingHelper;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class SearchControllerIT extends BaseControllerIT {
	@Autowired
	private ArticleHelper articleHelper;
	private ArticleEntity expectedArticle;
	private TradeEntity expectedTrade;
	@Autowired
	private ListingHelper listingHelper;
	@Autowired
	private MembershipHelper membershipHelper;

	@Transactional
	@Before
	public void before() {
		super.before();
		MembershipEntity expectedMembership = membershipHelper.createPersistedEntity(authenticatedUser);
		expectedArticle = articleHelper.createPersistedEntity(expectedMembership);
		expectedTrade = expectedMembership.getTrade();
		listingHelper.createPersisted(expectedArticle.getArticleId(), expectedMembership.getMembershipId());
	}

	@Test
	public void post_When_ArticleIsListedAndSearchingByTradeId_Then_Succeeds() throws Exception {
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe("ARTICLES");
		request.addCriterion("Trade.tradeId", expectedTrade.getTradeId());
		String requestBody = JsonUtil.toJson(request);
		String response = mockMvc
			.perform(
				post("/matchandtrade-api/v1/search/")
					.param("_pageNumber", "1")
					.param("_pageSize", "3")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		List<ArticleJson> actual = JsonUtil.fromArrayString(response, ArticleJson.class);
		ArticleTransformer articleTransformer = new ArticleTransformer();
		ArticleJson expected = articleTransformer.transform(expectedArticle);
		assertEquals(expected, actual.get(0));
	}
}
