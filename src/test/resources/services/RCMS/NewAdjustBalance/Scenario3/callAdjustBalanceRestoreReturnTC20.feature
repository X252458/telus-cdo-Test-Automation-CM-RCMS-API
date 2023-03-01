@Ignore
Feature: RCMS New Adjust Reward Amount API - TC20 Perform adjustBalance  with reasonCode=RESTORE_RETURN (amount=100) 

Background: Configuration - Set up the authentication, Headers, and params
    #Configure the xml payload
    * def auth_token = karate.properties['karate.auth_token_management']
    * def subID = karate.properties['karate.subID']
    #* def subNum = karate.properties['karate.subNum']
    * def payload = read(PATH_API_PAYLOAD + 'NewAdjustBalance/Scenario3/TC20_Telus_DF_AF_adjustBalance_RESTORE_RETURN.json')
    * header Authorization = 'Bearer ' + auth_token
    * header Content-Type = 'application/json'
    * header Env = karate.properties['karate.apiEnv']
    * header X-System = 'WLS'
    
Scenario: Retrieve SPID from the LSMS
    #Set endpoint url
    Given url ENDPOINT_NEW_ADJUST_BALANCE
    #Request XML passed for the operation and printing the same for verification
    And request payload
    #		When REST operation post
    When method patch