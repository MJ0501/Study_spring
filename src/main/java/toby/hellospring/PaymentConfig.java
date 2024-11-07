package toby.hellospring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.hellospring.exrate.CachedExRateProvider;
import toby.hellospring.payment.ExRateProvider;
import toby.hellospring.exrate.WebApiExRateProvider;
import toby.hellospring.payment.PaymentService;

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
