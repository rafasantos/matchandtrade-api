package com.matchandtrade.persistence.facade;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.repository.MembershipRepository;
import com.matchandtrade.persistence.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TradeRepositoryFacade {

	@Autowired
	private MembershipRepository membershipRepository;
	@Autowired
	private TradeRepository tradeRepository;

	@Transactional
	public void delete(Integer tradeId) {
		List<MembershipEntity> memberships = membershipRepository.findByTrade_TradeId(tradeId);
		membershipRepository.delete(memberships);
		tradeRepository.delete(tradeId);
	}

	public TradeEntity find(Integer tradeId) {
		return tradeRepository.findOne(tradeId);
	}
	
	public void save(TradeEntity entity) {
		tradeRepository.save(entity);
	}

}
