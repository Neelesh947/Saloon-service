package com.keycloak.utils;

import java.util.List;

public final class Constants {

	public static final String MASTER_REALM = "master";
	public static final String GRANT_TYPE = "grant_type";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String CLIENT_ID = "client_id";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String APPLICATION_JSON = "application/json";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String CLIENT_CREDENTIALS = "client_credentials";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String ENABLED = "enabled";
	public static final String ID = "id";
	public static final String GRANT_TYPE_PASSWORD = "password";
	public static final String TYPE = "type";
	public static final String VALUE = "value";
	public static final String TEMPORARY = "temporary";
	public static final String ROLES = "roles";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String COMPANY_NAME = "companyName";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String POSTAL_CODE = "postalCode";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String CUSTOMER_SUPPORT_NUMBER = "customerSupportNumber";
	public static final String ASSIGNED_SENDERS = "assignedSenders";
	public static final String COUNTRY_CODE = "countryCode";
	public static final String CREATED_BY = "created_by";

	public static final String REALM_ACCESS_CLAIM = "realm_access";
	public static final String ROLES_CLAIM = "roles";
	public static final List<String> ROLES_TO_SKIP = List.of("offline_access", "default-roles-smartbox",
			"uma_authorization", "default-roles-c2c");
	
	public static final Object LOCATION_HEADER = "location";
	public static final String USER_ID_REGEX = ".*/users/([a-fA-F0-9\\-]+)$";
	
	public static final String EMAIL = "email";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String PHONENUMBER = "phonenumber";
	public static final String COMPANYNAME = "companyname";
}
