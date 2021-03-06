package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleControllerIT.class,
	AuthenticationControllerIT.class,
	ListingControllerIT.class,
	MembershipControllerIT.class,
	OfferControllerIT.class,
	SearchControllerIT.class,
	TradeControllerIT.class,
	TradeResultControllerIT.class,
	UserControllerIT.class,
	AttachmentControllerIT.class,
	ArticleAttachmentControllerIT.class
})
public class ControllerSuite {

}
