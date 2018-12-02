package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class ArticleAttachmentValidator {

	@Autowired
	ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	AttachmentRepositoryFacade attachmentRepositoryFacade;
	@Autowired
	UserRepositoryFacade userRepositoryFacade;

	public void validateDelete(Integer userId, Integer articleId, Integer attachmentId) {
		verifyThatAttachmentExists(attachmentId);
		verifyThatUserOwnsArticle(userId, articleId);
	}

	@Transactional
	public void validatePost(Integer userId, Integer articleId) {
		verifyThatUserOwnsArticle(userId, articleId);
		verifyThatArticleHasLessThanTwoFiles(articleId);
	}

	private void verifyThatArticleHasLessThanTwoFiles(Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.findByArticleId(articleId);
		if (article.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 files.");
		}
	}

	private void verifyThatAttachmentExists(Integer attachmentId) {
		AttachmentEntity attachment = attachmentRepositoryFacade.findByAttachmentId(attachmentId);
		if (attachment == null) {
			throw new RestException(NOT_FOUND, String.format("Attachment.attachmentId: %s does not exist.", attachmentId));
		}
	}

	private void verifyThatUserOwnsArticle(Integer userId, Integer articleId) {
		UserEntity user = userRepositoryFacade.findByArticleId(articleId);
		if (user == null || !userId.equals(user.getUserId())) {
			throw new RestException(FORBIDDEN,
				String.format("User.userId: %s is not the owner of Article.articleId: %s", userId, articleId));
		}
	}

	public void validateGet(Integer attachmentId) {
		AttachmentEntity entity = attachmentRepositoryFacade.findByAttachmentId(attachmentId);
		if (entity == null) {
			throw new RestException(NOT_FOUND, String.format("Attachment.attachmentId: %s was not found", attachmentId));
		}
	}

	public void validateGetByArticleId(Integer articleId) {
	}
}
