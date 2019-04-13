package hr.foi.diplomski.central.configuration;

import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional
public class TestModelConfing {

    private final BeaconRepository beaconRepository;
    private final DeviceRepository deviceRepository;
    private final RecordRepository recordRepository;
    @Bean
    public void napraviNesto() {
        System.out.println("nesto");
    }
}
