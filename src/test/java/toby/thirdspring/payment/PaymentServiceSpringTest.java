package toby.thirdspring.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toby.thirdspring.TestPaymentConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestPaymentConfig.class)
class PaymentServiceSpringTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    ExRateProviderStub exRateProviderStub;
    @Autowired
    Clock clock;
    @Test
        // @DisplayName("prepare method가 요구사항 3가지(적용환율,환산,시간) 잘 충족했는지 검증")
    void convertedAmount() throws IOException {
        // exRate : 1000
        Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);
        assertThat(payment.getExRate()).isEqualByComparingTo(valueOf(1000));
        assertThat(payment.getConvertedAmount()).isEqualByComparingTo(valueOf(10000));
        // exRate = 500 인 경우
        exRateProviderStub.setExRate(valueOf(500));
        Payment payment2= paymentService.prepare(1L, "USD", BigDecimal.TEN);
        assertThat(payment2.getExRate()).isEqualByComparingTo(valueOf(500));
        assertThat(payment2.getConvertedAmount()).isEqualByComparingTo(valueOf(5000));

    }
    @Test
    void validUntil() throws IOException {
         Payment payment = paymentService.prepare(1L, "USD", BigDecimal.TEN);
        // validUntil 이 prepare() 30min 보다 뒤로 설정되었는가? 검증
        //현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now(this.clock);
        LocalDateTime expectedValidUntil = now.plusMinutes(30);
        Assertions.assertThat(payment.getValidUntil()).isEqualTo(expectedValidUntil);
    }
}

