package toby.hellospring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.hellospring.payment.ExRateProvider;
import toby.hellospring.payment.ExRateProviderStub;
import toby.hellospring.payment.PaymentService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static java.math.BigDecimal.valueOf;

@Configuration
// @ComponentScan
public class TestPaymentConfig {
    @Bean
    public PaymentService paymentService() {
        return new PaymentService(exRateProvider(),clock());
    }

    @Bean
    public ExRateProvider exRateProvider() {
        return new ExRateProviderStub(valueOf(1000));
    }
    @Bean
    public Clock clock(){
        return Clock.fixed(Instant.now(), ZoneId.systemDefault());
    };
}
