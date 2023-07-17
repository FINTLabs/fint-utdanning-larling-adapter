package no.fintlabs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

    private static final int PROCESS_CLEAR_COUNT = 3;

    private final RestUtil restUtil;
    private final AtomicInteger processCount = new AtomicInteger(0);
    private RequestData requestData;

    public void finishProcess() {
        int currentCount = processCount.incrementAndGet();
        if (currentCount >= PROCESS_CLEAR_COUNT) {
            clearCache();
        }
    }

    public RequestData get() {
        synchronized (this) {
            if (requestData == null) {
                fillCache();
            }
        }
        return requestData;
    }

    public void handleProcessingError() {
        finishProcess();
    }

    private void clearCache() {
        log.debug("Clearing Cache...");
        requestData = null;
        processCount.set(0);
    }

    private void fillCache() {
        requestData = restUtil.getRequestData();
    }

}
