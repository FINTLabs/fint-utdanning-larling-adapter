package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.restutil.RestUtil;
import no.fintlabs.restutil.model.Contract;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CacheService {

    private static final int PROCESS_CLEAR_COUNT = 3;

    private final RestUtil restUtil;
    private final AtomicInteger processCount = new AtomicInteger(0);
    private final Map<String, List<Contract>> cache;

    public CacheService(RestUtil restUtil) {
        this.restUtil = restUtil;
        this.cache = new HashMap<>();
    }

    public void finishProcess() {
        int currentCount = processCount.incrementAndGet();
        if (currentCount >= PROCESS_CLEAR_COUNT) {
            clearCache();
        }
    }

    public List<Contract> getContracts(String bedriftsNummer) {
        return cache.containsKey(bedriftsNummer) ? cache.get(bedriftsNummer) : new ArrayList<>();
    }

    public List<Contract> getContracts() {
        synchronized (this) {
            if (cache.isEmpty()) {
                fillCache();
            }
        }
        return cache.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void handleProcessingError() {
        finishProcess();
    }

    private void clearCache() {
        log.debug("Clearing Cache...");
        cache.clear();
        processCount.set(0);
    }

    private void fillCache() {
        log.debug("Filling Cache...");
        restUtil.getRequestData()
                .getKontrakter()
                .forEach(this::addToCache);
    }

    private void addToCache(Contract contract) {
        cache.computeIfAbsent(contract.getBedriftsNummer(), key -> new ArrayList<>()).add(contract);
    }

}
