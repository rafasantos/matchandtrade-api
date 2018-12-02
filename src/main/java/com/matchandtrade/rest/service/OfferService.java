package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.OfferQueryBuilder;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private SearchService<OfferEntity> searchService;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;

	@Transactional
	public void create(Integer membershipId, OfferEntity offer) {
		MembershipEntity membership = membershipRepositoryFacade.findByMembershipId(membershipId);
		offerRepositoryFacade.save(offer);
		membership.getOffers().add(offer);
		membershipRepositoryFacade.save(membership);
	}

	@Transactional
	public void delete(Integer offerId) {
		MembershipEntity membership = membershipRepositoryFacade.findByOfferId(offerId);
		OfferEntity offer = offerRepositoryFacade.findByOfferId(offerId);
		membership.getOffers().remove(offer);
		membershipRepositoryFacade.save(membership);
		offerRepositoryFacade.delete(offerId);
	}

	public OfferEntity findByOfferId(Integer offerId) {
		return offerRepositoryFacade.findByOfferId(offerId);
	}

	// TODO: change to SearchResult?
	public List<OfferEntity> findByOfferedArticleId(Integer offeredArticleId) {
		return offerRepositoryFacade.findByOfferedArticleId(offeredArticleId);
	}

	public SearchResult<OfferEntity> findByMembershipIdOfferedArticleIdWantedArticleId(
			Integer membershipId,
			Integer offeredArticleId,
			Integer wantedArticleId,
			Integer pageNumber,
			Integer pageSize) {
		SearchCriteria criteria = new SearchCriteria(new Pagination(pageNumber, pageSize));
		if (membershipId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.MEMBERSHIP_ID, membershipId);
		}
		if (offeredArticleId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.OFFERED_ARTICLE_ID, offeredArticleId);
		}
		if (wantedArticleId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.WANTED_ARTICLE_ID, wantedArticleId);
		}
		return searchService.search(criteria, OfferQueryBuilder.class);
	}
}
