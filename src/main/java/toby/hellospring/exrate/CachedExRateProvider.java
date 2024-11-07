package toby.hellospring.exrate;

import toby.hellospring.payment.ExRateProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CachedExRateProvider implements ExRateProvider {
    private final ExRateProvider target;
    private BigDecimal cachedExRate;
    //초기화 하지않음. 처음값은 읽어서 받아와야하므로
    private LocalDateTime cacheExpiryTime;

    public CachedExRateProvider(ExRateProvider target) {
        this.target = target;
    }

    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        if (cachedExRate == null || cacheExpiryTime.isBefore(LocalDateTime.now()))   {
            cachedExRate = this.target.getExRate(currency);
            cacheExpiryTime = LocalDateTime.now().plusSeconds(3);
            System.out.println("Cached Updated");
        }
        return cachedExRate;
    }
}
