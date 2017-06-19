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

@NamedQueries( {
		@NamedQuery(name = "findForumWatchByUser", query = "select f from ForumWatch as f where f.poster.userId = :userId"),
		@NamedQuery(name = "findForumWatchedByUser", query = "select f "
				+ "from Forum as f where f.id IN ( "
				+ "select fw.forum.id from ForumWatch fw "
				+ "where fw.poster.userId = :userId )"),
		@NamedQuery(name = "findForumWatchById", query = "select f from ForumWatch as f where f.id = :forumWatchId"),
		@NamedQuery(name = "findForumWatches", query = "select fw.forum.id , fw "
				+ "from ForumWatch fw "
				+ "where fw.poster.userId = :userId"),
		@NamedQuery(name = "findForumWatchByUserAndForum", query = "select fw from ForumWatch as fw "
				+ "where fw.poster.userId = :userId "
				+ "and fw.forum.id= :forumId") })
@Entity
@Table(name = "JBP_FORUMS_FORUMSWATCH")
public class ForumWatch extends Watch implements Serializable {

	@ManyToOne
	@JoinColumn(name = "JBP_FORUM_ID")
	private Forum forum;

	private static final long serialVersionUID = 1L;

	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

}