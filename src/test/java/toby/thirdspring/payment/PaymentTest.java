package toby.thirdspring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.math.BigDecimal.valueOf;

public class PaymentTest {
    @Test
    void createPrepared(){
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(
              1L, "USD", BigDecimal.TEN,valueOf(1000), LocalDateTime.now(clock)
        );
        Assertions.assertThat(payment.getConvertedAmount()).isEqualByComparingTo(valueOf(10000));
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(LocalDateTime.now(clock).plusMinutes(30));
    }
    @Test
    void isValid(){
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Payment payment = Payment.createPrepared(
                1L, "USD", BigDecimal.TEN,valueOf(1000), LocalDateTime.now(clock)
        );
        Assertions.assertThat(payment.isValid(clock)).isTrue();
        // 시간을 강제로 30분 뒤로 가게 하고~ true,false 여부.
        Assertions.assertThat(payment.isValid(Clock.offset(clock, Duration.of(30, ChronoUnit.MINUTES)))).isFalse();

    }
}
