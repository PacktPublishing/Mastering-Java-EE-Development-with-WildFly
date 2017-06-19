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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "findTopicWatchedByUser", query = "select t "
				+ "from Topic as t where t.id IN ( "
				+ "select tw.topic.id from TopicWatch tw "
				+ "where tw.poster.userId = :userId ) "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicWatchedByUserCreateDate", query = "select t "
				+ "from Topic as t where t.id IN ( "
				+ "select tw.topic.id from TopicWatch tw "
				+ "where tw.poster.userId = :userId ) "
				+ "and (t.lastPostDate > :datePoint) "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicWatches", query = "select tw.topic.id , tw "
				+ "from TopicWatch tw "
				+ "where tw.poster.userId = :userId"),
		@NamedQuery(name = "findTopicWatchByUserAndTopic", query = "select tw from TopicWatch as tw "
				+ "where tw.poster.userId = :userId "
				+ "and tw.topic.id= :topicId"),
		@NamedQuery(name = "findTopicWatchById", query = "select f from TopicWatch as f where f.id = :topicWatchId") })
@Entity
@Table(name = "JBP_FORUMS_TOPICSWATCH")
public class TopicWatch extends Watch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -588159949173481044L;

	@ManyToOne
	@JoinColumn(name = "JBP_TOPIC_ID")
	private Topic topic;

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
