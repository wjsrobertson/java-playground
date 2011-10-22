package net.xylophones.midifinder.model;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name="url")
@NamedQueries( 
	{ 
		@NamedQuery(
			name = "Url.findAll", 
			query = "SELECT url FROM Url url"
		),
		@NamedQuery(
			name = "Url.findUnlockedByStatus", 
			query = "SELECT url FROM Url url " +
					" WHERE status=:status AND lockedDate=null " +
					" ORDER BY depth ASC "
		),
		@NamedQuery(
			name = "Url.findUnlockedByStatusAndMimeType", 
			query = "SELECT url FROM Url url " +
					" WHERE status=:status AND mimeType=:mimeType AND lockedDate=null AND statusCode=200 " +
					" ORDER BY depth ASC "
		),
		@NamedQuery(
			name = "Url.exists", 
			query = "SELECT COUNT(*) FROM Url url WHERE host=:host AND path=:path AND query=:query"
		)
	}
)
public class Url implements Serializable {
	
	/**
	 * Serializable field
	 */
	private static final long serialVersionUID = 1L;
	
	public Url() {
	}
	
	public Url(String scheme, String host, Integer port, String path, String query) {
		this.scheme = scheme;
		this.host   = host;
		this.port   = port;
		this.path   = path;
		this.query  = query;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id = -1;
	
	private String scheme;
	
	private String host;
	
	private Integer port;
	
	@Column(name="status_code")
	private Integer statusCode;
	
	private String path;

	private String query = "".intern();
	
	@Column(name="anchor_text", length=4)
	private String anchorText;
	
	@Column(name="mime_type")
	private String mimeType;
	
	@Column(name="data_size")
	private int dataSize;
	
	@Enumerated(EnumType.STRING)
	private UrlStatus status = UrlStatus.NEW;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="locked_date")
	private Date lockedDate;
	
	@Column(name="last_request_date")
	private Date lastRequestDate;
	
	@ManyToOne(fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="source_id")
	private Url parent;

	private Integer depth;
	
	public Url getParent() {
		return parent;
	}

	public void setParent(Url parent) {
		this.parent = parent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public UrlStatus getStatus() {
		return status;
	}

	public void setStatus(UrlStatus status) {
		this.status = status;
	}
	
	public Date getLastRequestDate() {
		return lastRequestDate;
	}

	public void setLastRequestDate(Date lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}
	
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getFullUrl() {
		String path  = (getPath()!=null)  ? getPath() : "";
		String query = (getQuery()!=null && ! getQuery().equals("")) ?  "?" + getQuery() : "";
		
		return getScheme() + "://" + getHost() + path + query;
	}
	
	@Override
	public String toString() {
		return getFullUrl();
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
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
		Url other = (Url) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		return true;
	}
	
	public String getAnchorText() {
		return anchorText;
	}
	
	public void setAnchorText(String anchorText) {
		this.anchorText = anchorText;
	}
	
	@PrePersist
	public void prePersist() {
		clean();
		createDate = new Date();
	}
	
	@PreUpdate
	public void preUpdate() {
		clean();
	}
	
	/**
	 * Clean the fields to be the appropriate length
	 * 
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    private void clean() {
    	for (Field field: Url.class.getDeclaredFields()) {
    		if (! field.isAnnotationPresent(javax.persistence.Column.class)) {
    			continue;
    		}
    		
    		Column annotation = field.getAnnotation(javax.persistence.Column.class);
    		int length = annotation.length();
    		if (length > 0) {
    			try {
	    			if (field.getType().equals(String.class)) {
	    				String value = (String) field.get(this);
	    				if (value != null) {
	    					if (value.length() > length) {
	    						value = value.substring(0,length);
	    						field.set(this, value);
	    					}
	    				}
	    			}
    			} catch (IllegalAccessException e) {
    			} catch (IllegalArgumentException e) {
    			}
    		}
     	}
	}
}
