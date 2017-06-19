package it.vige.businesscomponents.persistence;

import static javax.persistence.CascadeType.REMOVE;
import static org.hibernate.search.annotations.Index.YES;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

@NamedQueries({
		@NamedQuery(name = "findAnnouncements", query = "select t from Topic as t where t.forum=:forumid and "
				+ "t.type = :type order by t.lastPostDate"),
		@NamedQuery(name = "findPoll", query = "select t.poll from Topic as t where t.id=:topicid"),
		@NamedQuery(name = "findTopics", query = "select t from Topic as t"),
		@NamedQuery(name = "findTopicsTypeasc", query = "select t from Topic as t " + "join fetch t.poster "
				+ "where t.forum = :forumid " + "and t.type = :type " + "order by t.lastPostDate asc"),
		@NamedQuery(name = "findTopicsTypedesc", query = "select t from Topic as t " + "join fetch t.poster "
				+ "where t.forum = :forumid " + "and t.type = :type " + "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsForumasc", query = "select t from Topic as t " + "join fetch t.poster "
				+ "where t.forum = :forumid " + "order by t.lastPostDate asc"),
		@NamedQuery(name = "findTopicsForumdesc", query = "select t from Topic as t " + "join fetch t.poster "
				+ "where t.forum = :forumid " + "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsHot", query = "select t from Topic as t where t.replies > :replies "
				+ "order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsByLatestPosts", query = "select t from Topic as t order by t.lastPostDate desc"),
		@NamedQuery(name = "findTopicsHottest", query = "select t from Topic as t where t.lastPostDate > :after "
				+ "order by t.replies desc"),
		@NamedQuery(name = "findTopicsMostViewed", query = "select t from Topic as t where t.lastPostDate > :after "
				+ "order by t.viewCount desc"),
		@NamedQuery(name = "findTopicsForumNoOrder", query = "select t from Topic as t where t.forum = :forumid"),
		@NamedQuery(name = "findPostsFromForumasc", query = "select p from Topic as t join t.posts as p where "
				+ "t.forum.id = :forumId order by p.createDate asc"),
		@NamedQuery(name = "findPostsFromForumdesc", query = "select p from Topic as t join t.posts as p where "
				+ "t.forum.id = :forumId order by p.createDate desc") })
@Entity
@Table(name = "JBP_FORUMS_TOPICS")
@Indexed(index = "indexes/topics")
public class Topic implements Serializable, Comparable<Topic> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3426875789016150344L;

	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	@OneToMany(mappedBy = "topic", cascade = REMOVE)
	private List<Post> posts;

	@ManyToOne
	@JoinColumn(name = "JBP_FORUM_ID")
	@IndexedEmbedded(includeEmbeddedObjectId = true, targetElement = Forum.class)
	private Forum forum;

	@Column(name = "JBP_VIEW_COUNT")
	private int viewCount;

	@Column(name = "JBP_REPLIES")
	private int replies;

	/**
	 * this field is cached but was left as it's easier to sort topics with HQL
	 * having such column
	 * 
	 */
	@Column(name = "JBP_LAST_POST_DATE")
	private Date lastPostDate;

	@ManyToOne
	@JoinColumn(name = "JBP_POSTER")
	private Poster poster;

	@Column(name = "JBP_TYPE")
	private TopicType type;

	@Column(name = "JBP_STATUS")
	private int status;

	@Field(index = YES)
	@Column(name = "JBP_SUBJECT")
	private String subject;

	@ManyToMany
	@JoinTable(name = "JBP_FORUMS_TOPICSWATCH", joinColumns = @JoinColumn(name = "JBP_TOPIC_ID"), inverseJoinColumns = @JoinColumn(name = "JBP_ID"))
	private List<Watch> watches;

	@ManyToOne(cascade = REMOVE)
	@JoinColumn(name = "JBP_POLL")
	private Poll poll;

	/**
	 * Creates a new {@link Topic} object.
	 */
	public Topic() {
		setPosts(new ArrayList<Post>());
	}

	public Topic(String subject) {
		this();
		this.subject = subject;
	}

	public Topic(Forum forum, String subject) {
		this(subject);
		this.forum = forum;
	}

	public Topic(Forum forum, String subject, List<Post> posts) {
		this(forum, subject);
		this.posts = posts;
	}

	public Topic(Forum forum, String subject, List<Post> posts, TopicType type, Poll poll) {
		this(forum, subject, posts);
		this.type = type;
		this.poll = poll;
	}

	/**
	 * we are implementing comparable to be able to sort topics by last post
	 * date without to have a column in db.
	 * 
	 * @param comp
	 *            the topic to compare
	 * @return the result of the comparation. -1 minor, 0 equal, 1 major
	 */
	public int compareTo(Topic comp) {
		Date thisDate = getLastPostDate();
		Date thatDate = comp.getLastPostDate();
		if (thisDate != null && (thatDate != null)) {
			return thisDate.compareTo(thatDate);
		} else if (thisDate == null && (thatDate != null)) {
			return -1;
		} else if (thisDate != null && (thatDate == null)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @return the id of the topic
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param id
	 *            DOCUMENT_ME
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the posts of the topic
	 */
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value
	 *            DOCUMENT_ME
	 */
	public void setPosts(List<Post> value) {
		posts = value;
	}

	/**
	 * @return the forum of the topic
	 */
	public Forum getForum() {
		return forum;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param forum
	 *            DOCUMENT_ME
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}

	/**
	 * @return the count of visitors
	 */
	public int getViewCount() {
		return viewCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param viewCount
	 *            DOCUMENT_ME
	 */
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the number of replies
	 */
	public int getReplies() {
		return replies;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param replies
	 *            DOCUMENT_ME
	 */
	public void setReplies(int replies) {
		this.replies = replies;
	}

	/**
	 * @return the last post date
	 */
	public Date getLastPostDate() {
		return lastPostDate;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param lastPostDate
	 *            DOCUMENT_ME
	 */
	public void setLastPostDate(Date lastPostDate) {
		this.lastPostDate = lastPostDate;
	}

	/**
	 * @return the poster
	 */
	public Poster getPoster() {
		return poster;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param poster
	 *            DOCUMENT_ME
	 */
	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	/**
	 * @return the topic type. It can be NORMAL, IMPORTANT or ADVICE
	 */
	public TopicType getType() {
		return type;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param type
	 *            DOCUMENT_ME
	 */
	public void setType(TopicType type) {
		this.type = type;
	}

	/**
	 * @return the status of the topic
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param status
	 *            DOCUMENT_ME
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the subject of the topic
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
	 * @return the watches list of the topic
	 */
	public List<Watch> getWatches() {
		return watches;
	}

	public void setWatches(List<Watch> watches) {
		this.watches = watches;
	}

	/**
	 * @return the poll of the topic
	 */
	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}