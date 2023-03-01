package com.telus.rcms.jsonPathLibrary;

public class StatusChange {

	public static String relatedPartySubId = null, relatedPartyCharSubNum = null,
			relatedPartyCharPerviousStautsCd = null, relatedPartyCharCurrentStatusCd = null, relatedPartyAccId = null,
			relatedPartyOrgId = null, relatedPartyCharTransId = null, relatedPartyCharChnlOrgId = null,
			relatedPartyCharSalesRepId = null, relatedPartyCharTeamMemberId = null;

	public StatusChange() {

		relatedPartySubId = "$.relatedParty[0].id";
		relatedPartyCharSubNum = "$.relatedParty[0].characteristic[0].value";
		relatedPartyCharPerviousStautsCd = "$.relatedParty[0].characteristic[1].value";
		relatedPartyCharCurrentStatusCd = "$.relatedParty[0].characteristic[2].value";
		relatedPartyAccId = "$.relatedParty[1].id";
		relatedPartyOrgId = "$.relatedParty[2].id";
		relatedPartyCharTransId = "$.relatedParty[2].characteristic[0].value";
		relatedPartyCharChnlOrgId = "$.relatedParty[2].characteristic[1].value";
		relatedPartyCharSalesRepId = "$.relatedParty[2].characteristic[2].value";
		relatedPartyCharTeamMemberId = "$.relatedParty[2].characteristic[3].value";
	}

}
