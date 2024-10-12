package hhplus.tdd.concert.domain.repository.userqueue;

import hhplus.tdd.concert.domain.entity.userqueue.UserQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQueueRepository extends JpaRepository<UserQueue, Long> {
}
