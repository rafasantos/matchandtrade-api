package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.link.TradeLinkAssember;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;

@RestController
@RequestMapping(path="/rest/v1/trades")
public class TradeController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeRepository tradeRepository;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeService tradeService;
	@Autowired
	TradeTransformer tradeTransformer;

	@RequestMapping(path="/", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public TradeJson post(@RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeValidador.validatePost(requestJson);
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson);
		// Delegate to Service layer
		tradeService.create(tradeEntity, authenticationProvider.getAuthentication().getUser());
		// Transform the response
		TradeJson response = TradeTransformer.transform(tradeEntity);
		// Assemble links
		TradeLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.PUT)
	public TradeJson put(@PathVariable Integer tradeId, @RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setTradeId(tradeId);
		tradeValidador.validatePut(requestJson, authenticationProvider.getAuthentication().getUser());
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson);
		// Delegate to Service layer
		tradeService.update(tradeEntity);
		// Transform the response
		TradeJson response = TradeTransformer.transform(tradeEntity);
		// Assemble links
		TradeLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		tradeValidador.validateDelete(tradeId);
		// Delegate to Service layer
		tradeService.delete(tradeId);
	}

	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<TradeJson> get(String name, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeValidador.validateGet(_pageSize);
		// Delegate to Service layer
		SearchResult<TradeEntity> searchResult = tradeService.search(name, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<TradeJson> response = TradeTransformer.transform(searchResult);
		// Assemble links
		TradeLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.GET)
	public TradeJson get(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to Repository layer
		TradeEntity tradeEntity = tradeService.get(tradeId);
		// Transform the response
		TradeJson response = TradeTransformer.transform(tradeEntity);
		// Assemble links
		TradeLinkAssember.assemble(response);
		return response;
	}
}
