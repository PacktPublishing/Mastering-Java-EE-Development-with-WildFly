/******************************************************************************
 * Vige, Home of Professional Open Source Copyright 2010, Vige, and           *
 * individual contributors by the @authors tag. See the copyright.txt in the  *
 * distribution for a full listing of individual contributors.                *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0        *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package it.vige.businesscomponents.persistence;

public interface User {
	
	String INFO_USER_LAST_LOGIN_DATE = "INFO_USER_LAST_LOGIN_DATE";
	
	String INFO_USER_EMAIL_REAL = "INFO_USER_EMAIL_REAL";
	
	String INFO_USER_NAME_GIVEN = "INFO_USER_NAME_GIVEN";
	
	String INFO_USER_NAME_FAMILY = "INFO_USER_NAME_FAMILY";
	
	String GUEST_USER = "N/A";
	
	String getUserName();

	void setUserName(String userName);
	
	String getId();

	void setId(String id);
}
