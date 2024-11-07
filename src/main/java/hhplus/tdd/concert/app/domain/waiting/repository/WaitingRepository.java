package hhplus.tdd.concert.app.domain.waiting.repository;

import hhplus.tdd.concert.app.domain.waiting.entity.Waiting;
import hhplus.tdd.concert.app.domain.waiting.model.ActiveToken;

import java.util.List;
import java.util.Set;

public interface WaitingRepository {

    void addWaitingToken(String key, String value, Long currentTime);

    Long getWaitingTokenScore(String key, String value);

    List<ActiveToken> getWaitingTokenRange(String key, int start, int end);

    void deleteWaitingToken(String key, String value);

    List<ActiveToken> getActiveToken(String key);

    List<ActiveToken> getWaitingToken(String key);

    Set<String> getAllTokens(String tokenKey);

    void deleteActiveToken(String key, String value);

    void addActiveToken(String key, String value);

    default Waiting findByTokenOrThrow(String waitigToken){
        return null;
    }


}