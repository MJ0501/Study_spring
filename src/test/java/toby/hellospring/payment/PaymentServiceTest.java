package toby.hellospring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {
    Clock clock;
    @BeforeEach
    void beforeEach(){this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());}
    @Test
    // @DisplayName("prepare method가 요구사항 3가지(적용환율,환산,시간) 잘 충족했는지 검증")
    void convertedAmount() throws IOException {
        testAmount(valueOf(500),valueOf(5000), this.clock);
        testAmount(valueOf(1000),valueOf(10000), this.clock);
        testAmount(valueOf(10000),valueOf(100000), this.clock);
    }
    @Test
    void validUntil() throws IOException {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(valueOf(1000)), clock);
        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);
        // validUntil 이 prepare() 30min 보다 뒤로 설정되었는가? 검증
        //현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }
    private static void testAmount(BigDecimal exRate, BigDecimal convertedAmount, Clock clock) throws IOException {
        PaymentService paymentService = new PaymentService(new ExRateProviderStub(exRate), clock);
        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);

        // 환율정보 가지고옴
        assertThat(payment.getExRate()).isEqualByComparingTo(exRate);
        //원화환산금액 계산 assertEquals(expected,actual)
        assertThat(payment.getConvertedAmount()).isEqualByComparingTo(convertedAmount);
    }
}