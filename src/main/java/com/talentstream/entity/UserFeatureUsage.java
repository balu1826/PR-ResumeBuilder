package com.talentstream.entity;
 
import javax.persistence.*;
 
@Entity
@Table(name = "feature_tracker")
public class UserFeatureUsage {
 
    @Id
    @Column(name = "user_id")
    private Long userId;
 
    @Column(name = "blogs_count", nullable = false,columnDefinition = "integer default 0")
    private int blogsCount = 0;
 
    @Column(name = "shorts_count", nullable = false,columnDefinition = "integer default 0")
    private int shortsCount = 0;
 
    @Column(name = "hackathons_count", nullable = false,columnDefinition = "integer default 0")
    private int hackathonsCount = 0;
    
    @Column(name = "ask_newton_count", nullable = false,columnDefinition = "integer default 0")
    private int askNewton = 0;

	@Column(name="resume_upload_count",nullable =false,columnDefinition = "integer default 0")
    private int resumeUpload=0;
	
	  @Column(name = "blogs_count_mobile", nullable = false,columnDefinition = "integer default 0")
	    private int mobileBlogsCount = 0;
	
	    @Column(name = "shorts_count_mobile", nullable = false,columnDefinition = "integer default 0")
	    private int mobileShortsCount = 0;
	
	    @Column(name = "hackathons_count_mobile", nullable = false,columnDefinition = "integer default 0")
	    private int mobileHackathonsCount = 0;
	    
	    @Column(name = "ask_newton_count_mobile", nullable = false,columnDefinition = "integer default 0")
	    private int mobileAskNewton = 0;
 
		@Column(name="resume_upload_count_mobile",nullable =false,columnDefinition = "integer default 0")
	    private int mobileResumeUpload=0;
	@Column(name = "mentor__sessions" ,nullable = false,
			    columnDefinition = "integer default 0")
	 private Integer mentorConnects=0 ;

	@Column(name="mobile_latest_session_date")
	private String mobileLatestSessionDate;
	
	@Column(name="latest_session_date")
	private String latestSessionDate;
	@Column(name = "mobile_mentor__sessions" ,nullable = false,
		    columnDefinition = "integer default 0")
private Integer mobileMentorConnects=0 ;
 
public String getMobileLatestSessionDate() {
		return mobileLatestSessionDate;
	}
 
	public void setMobileLatestSessionDate(String mobileLatestSessionDate) {
		this.mobileLatestSessionDate = mobileLatestSessionDate;
	}
 
 
    public int getResumeUpload() {
		return resumeUpload;
	}
 
	public void setResumeUpload(int resumeUpload) {
		this.resumeUpload = resumeUpload;
	}
	public void incrementResumeUpload() {
    	this.resumeUpload++;
    }
 
    public int getAskNewton() {
		return askNewton;
	}
 
	public void setAskNewton(int askNewton) {
		this.askNewton = askNewton;
	}
 
	protected UserFeatureUsage() {
        // JPA requirement
    }
 
    public UserFeatureUsage(Long userId) {
        this.userId = userId;
    }
    public void incrementBlogs() {
        this.blogsCount++;
    }
 
    public void incrementShorts() {
        this.shortsCount++;
    }
 
    public void incrementHackathons() {
        this.hackathonsCount++;
    }
    
    public void incrementAskNewton() {
        this.askNewton++;
    }
 
    public Long getUserId() {
        return userId;
    }
 
    public int getBlogsCount() {
        return blogsCount;
    }
 
    public int getShortsCount() {
        return shortsCount;
    }
 
    public int getHackathonsCount() {
        return hackathonsCount;
    }
	   public void incrementMobileBlogs() {
        this.mobileBlogsCount++;
    }
 
    public void incrementMobileShorts() {
        this.mobileShortsCount++;
    }
 
    public void incrementMobileHackathons() {
        this.mobileHackathonsCount++;
    }
    
    public void incrementMobileAskNewton() {
        this.mobileAskNewton++;
    }
    public void incrementMobileResumeUpload() {
    	this.mobileResumeUpload++;
    }
	   public void incrementMentorSessions() {
        this.mentorConnects++;
    }
     public void incrementMobileMentorSessions() {
	        this.mobileMentorConnects++;
	    }
   
    public String getLatestSessionDate() {
		return latestSessionDate;
	}
 
	public void setLatestSessionDate(String latestSessionDate) {
		this.latestSessionDate = latestSessionDate;
	}
}
