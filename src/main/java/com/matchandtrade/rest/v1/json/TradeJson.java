package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

import java.util.Objects;

public class TradeJson extends JsonLinkSupport {

	public enum State {
		SUBMITTING_ARTICLES,
		MATCHING_ARTICLES,
		ARTICLES_MATCHED,
		GENERATE_RESULTS,
		GENERATING_RESULTS,
		RESULTS_GENERATED,
		CANCELED
	}

	private String description;
	private String name;
	private Integer tradeId;
	private State state;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TradeJson tradeJson = (TradeJson) o;
		return Objects.equals(description, tradeJson.description) &&
			Objects.equals(name, tradeJson.name) &&
			Objects.equals(tradeId, tradeJson.tradeId) &&
			state == tradeJson.state;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public State getState() {
		return state;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getTradeId() {
		return tradeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, tradeId, state);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonInclude(value=Include.NON_NULL)
	public void setState(State state) {
		this.state = state;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

}
