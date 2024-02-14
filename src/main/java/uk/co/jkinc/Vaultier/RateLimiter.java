package uk.co.jkinc.Vaultier;

import java.util.Date;

public class RateLimiter {
    private static final int maxReqs = 60; // 1 every 30 seconds
    public static boolean ProcessRequest(String ApiKey) {
        Integer requestsThisPeriod = Vaultier.database.db.RateLimits.get(ApiKey);
        if (requestsThisPeriod == null) {
            requestsThisPeriod = 0;
        }

        long currentPeriod = new Date().getTime() / (1000 * 60 * 30);

        if (Vaultier.database.db.currentTimePeriod != currentPeriod) {
            Vaultier.database.db.RateLimits.clear();
            return true;
        }

        if (requestsThisPeriod > maxReqs) {
            return false;
        } else {
            requestsThisPeriod+=1;
            Vaultier.database.db.RateLimits.put(ApiKey, requestsThisPeriod);
            return true;
        }
    }


}
