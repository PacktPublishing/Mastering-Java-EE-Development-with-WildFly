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

import static org.hibernate.search.annotations.Index.YES;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Field;

/**
 * A DVC object that groups the message informations for a post.
 */
@Embeddable
public class Message implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6746986706193433125L;

	@Column(name = "JBP_SUBJECT")
	private String subject = "";

	@Field(index = YES)
	@Column(name = "JBP_TEXT")
	private String text = "";
	@Transient
	private boolean BBCodeEnabled = true;
	@Transient
	private boolean HTMLEnabled = true;
	@Transient
	private boolean smiliesEnabled = false;
	@Transient
	private boolean signatureEnabled = true;

	public Message() {

	}

	public Message(String text) {
		this.text = text;
	}

	/**
	 * @return the subject of the message
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param subject
	 *            DOCUMENT_ME
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the text of the message
	 */
	public String getText() {
		return text;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param text
	 *            DOCUMENT_ME
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	public boolean getBBCodeEnabled() {
		return BBCodeEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param BBCodeEnabled
	 *            DOCUMENT_ME
	 */
	public void setBBCodeEnabled(boolean BBCodeEnabled) {
		this.BBCodeEnabled = BBCodeEnabled;
	}

	public boolean getHTMLEnabled() {
		return HTMLEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param HTMLEnabled
	 *            DOCUMENT_ME
	 */
	public void setHTMLEnabled(boolean HTMLEnabled) {
		this.HTMLEnabled = HTMLEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	public boolean getSmiliesEnabled() {
		return smiliesEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param smiliesEnabled
	 *            DOCUMENT_ME
	 */
	public void setSmiliesEnabled(boolean smiliesEnabled) {
		this.smiliesEnabled = smiliesEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @return DOCUMENT_ME
	 */
	public boolean getSignatureEnabled() {
		return signatureEnabled;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param signatureEnabled
	 *            DOCUMENT_ME
	 */
	public void setSignatureEnabled(boolean signatureEnabled) {
		this.signatureEnabled = signatureEnabled;
	}
}