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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Luca Stancapiano
 */

@Entity
@Table(name = "JBP_FORUMS_POLL_OPTION")
public class PollOption implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1866410296669954729L;

	@Id
	@GeneratedValue
	@Column(name = "JBP_POLL_OPTION_POSITION")
	private int pollOptionPosition;

	@Column(name = "JBP_VOTES")
	private int votes;

	@Column(name = "JBP_QUESTION")
	private String question;

	@ManyToOne
	@JoinColumn(name = "JBP_POLL_ID")
	private Poll poll;

	public PollOption() {
	}

	public PollOption(Poll poll) {
		this.poll = (Poll) poll;
	}

	public PollOption(String question) {
		this.question = question;
	}

	public int getPollOptionPosition() {
		return pollOptionPosition;
	}

	public void setPollOptionPosition(int pollOptionPosition) {
		this.pollOptionPosition = pollOptionPosition;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	/**
	 * @return the votes number of the poll option
	 */
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	/**
	 * the question of the poll option
	 * 
	 * @return the question of the poll option
	 */
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void incVotes() {
		votes++;
	}

}
