Feature: ApplyPromotionForSubList API 

  Scenario: Calling API feature file
    #Operation 1
    When def apiDetails = call read(PATH_API_OPS+'ApplyPromotionForSubList/callApplyPromoTC2.feature')
    #Request
    * json apiRequest = apiDetails.payload
    #Status
    * def apiStatus = apiDetails.responseStatus
    #Validation
    #Then match apiStatus == 200
