Feature: Get OAuth Token for RCMS API

  Scenario: Calling Management authentication token
    * def fieldValue = read(PATH_CONFIG + 'Auth_Token.json')
    * url 'https://apigw-st.tsl.telus.com/st/token'
    * def encryptionUtils = Java.type('com.telus.rcms.utils.GenericUtils')
    * def encryptionUtilsObj = new encryptionUtils()
    And form field grant_type = encryptionUtilsObj.decrypt(fieldValue.Management.grant_type)
    And form field client_id = encryptionUtilsObj.decrypt(fieldValue.Management.client_id)
    And form field client_secret = encryptionUtilsObj.decrypt(fieldValue.Management.client_secret)
    And form field scope = encryptionUtilsObj.decrypt(fieldValue.Management.scope)
    When method POST
