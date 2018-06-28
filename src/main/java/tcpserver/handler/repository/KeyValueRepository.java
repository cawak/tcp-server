package tcpserver.handler.repository;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class KeyValueRepository {

    private final ReadWriteLock lock;

    public KeyValueRepository() {
        lock = new ReentrantReadWriteLock();
    }

    public void setByKey(String key, List<String> values){
        lock.writeLock().lock();

        try {

        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<String> getByKey(String key){

        return new ArrayList<>();
    }

    public ReadWriteLock getLock() {
        return lock;
    }
}
