Feature: AddAgreementItem API 

  Scenario: Calling API feature file
    #Operation 1
    When def apiDetails = call read(PATH_API_OPS+'AddAgreementItem/callAddAgreementItemTC7.feature')
    #Request
    * json apiRequest = apiDetails.response
    #Status
    * def apiStatus = apiDetails.responseStatus
    #Validation
    ##Then match apiStatus == 500
