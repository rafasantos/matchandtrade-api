package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.repository.AttachmentRepository;

import java.util.Optional;

@Repository
public class AttachmentRepositoryFacade {
	@Autowired
	private AttachmentRepository attachmentRepository;

	// TODO: Optional?
	public AttachmentEntity findByAttachmentId(Integer attachmentId) {
		Optional<AttachmentEntity> entity = attachmentRepository.findById(attachmentId);
		return  entity.get();
	}

	public void save(AttachmentEntity entity) {
		attachmentRepository.save(entity);
	}

	public void delete(Integer fileId) {
		attachmentRepository.deleteById(fileId);
	}

	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		Pageable pageable = PersistenceUtil.buildPageable(1, 10);
		Page<AttachmentEntity> page = attachmentRepository.findAttachmentsByArticleId(articleId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}
}
