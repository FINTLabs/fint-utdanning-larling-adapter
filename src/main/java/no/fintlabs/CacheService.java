package no.fintlabs;

import lombok.RequiredArgsConstructor;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.RequestData;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

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
        requestData = null;
        processCount.set(0);
    }

    private void fillCache() {
        requestData = restUtil.getRequestData();
    }

}
