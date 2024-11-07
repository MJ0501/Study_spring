package toby.thirdspring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.thirdspring.exrate.CachedExRateProvider;
import toby.thirdspring.payment.ExRateProvider;
import toby.thirdspring.exrate.WebApiExRateProvider;
import toby.thirdspring.payment.PaymentService;

import java.time.Clock;

@Configuration
public class PaymentConfig {
    @Bean
    public PaymentService paymentService() {
        return new PaymentService(cachedExRateProvider(),clock());
    }
    @Bean
    public ExRateProvider cachedExRateProvider() {
        return new CachedExRateProvider(exRateProvider());
    }
    @Bean
    public ExRateProvider exRateProvider() {
        return new WebApiExRateProvider();
    }
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    };
}
