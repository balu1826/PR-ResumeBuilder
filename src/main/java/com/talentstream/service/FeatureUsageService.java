package com.talentstream.service;
 
import javax.transaction.Transactional;
 
import org.springframework.stereotype.Service;
 
import com.talentstream.dto.AnalyticsEventRequest;
import com.talentstream.entity.UserFeatureUsage;
import com.talentstream.repository.UserFeatureUsageRepository;
 
@Service
public class FeatureUsageService {
 
    private final UserFeatureUsageRepository repo;
 
    public FeatureUsageService(UserFeatureUsageRepository repo) {
        this.repo = repo;
    }
 
    @Transactional
    public void recordFeatureEvent(AnalyticsEventRequest request) {
 
        long userId = request.getUserId();
        String feature = request.getFeature();
 
        UserFeatureUsage usage = repo.findByUserId(userId).orElse(null);

        if (usage == null) {
            usage = new UserFeatureUsage(userId);
            updateCount(usage,feature);
            repo.save(usage);
            return;
        }
       updateCount(usage,feature);
    }

     private void updateCount(UserFeatureUsage usage,String feature) {
      if(feature.indexOf("MENTOR")!=-1&&feature.indexOf("MOBILE")!=-1) {
    	 String date=feature.substring(feature.lastIndexOf(" ") + 1);
		 if(usage.getMobileLatestSessionDate()!=null&&usage.getMobileLatestSessionDate().equals(date)) {
			 return;
		 }
		 usage.incrementMobileMentorSessions();
		 usage.setMobileLatestSessionDate(date);
		 return;
    }
      else if(feature.indexOf("MENTOR")!=-1) {
    		 String date=feature.substring(feature.lastIndexOf(" ") + 1);
    		 if(usage.getLatestSessionDate()!=null&&usage.getLatestSessionDate().equals(date)) {
    			 return;
    		 }
    		 usage.incrementMentorSessions();
    		 usage.setLatestSessionDate(date);
    		 return;
    	 }
    	switch (feature) {
        case "BLOGS":
            usage.incrementBlogs();
            break;
        case "SHORTS":
            usage.incrementShorts();
            break;
        case "HACKATHONS":
            usage.incrementHackathons();
            break;
         case "ASK NEWTON":
    	    usage.incrementAskNewton();
        	break;
         case "RESUME UPLOAD":
    	    usage.incrementResumeUpload();
        	break;
         case "MOBILE-BLOGS":
             usage.incrementMobileBlogs();
             break;
         case "MOBILE-SHORTS":
             usage.incrementMobileShorts();
             break;
         case "MOBILE-HACKATHONS":
             usage.incrementMobileHackathons();
             break;
          case "MOBILE-ASK NEWTON":
     	    usage.incrementMobileAskNewton();
         	break;
          case "MOBILE-RESUME UPLOAD":
     	    usage.incrementMobileResumeUpload();
         	break;
        default:
            throw new IllegalArgumentException(
                "Invalid feature: " + feature
            );
    }
    }
}
