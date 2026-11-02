package task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repository.ScheduleRepository;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleCleanupTask {

    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanUpOldSchedules() {

        LocalDateTime oneYearAgo = now().minusYears(1);
        log.info("Запуск очистки расписания старше {}", oneYearAgo);

        int deletedCount = scheduleRepository.deleteOlderThan(oneYearAgo);
        log.info("Удалено записей расписания: {}", deletedCount);
    }
}