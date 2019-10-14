package au.com.metriculous.scanner.scan_implementations.blame;

import au.com.metriculous.scanner.domain.Pair;
import au.com.metriculous.scanner.domain.Person;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

/**
 * Created by stephenbatty on 11/07/2018.
 */
public class BlameResultDataStore {

    private final Map<Person, AtomicLong> prodLineCountByPerson = new ConcurrentHashMap<>();
    private final Map<Person, Map<Integer, Integer>> prodLineCountByTime = new ConcurrentHashMap<>();
    private final Map<RevCommit, AtomicInteger> prodLineCountByCommit = new ConcurrentHashMap<>();
    private final Map<Person, Map<String, Integer>> lineCountByPersonAndFile = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicInteger> lineCountByTimeAll = new ConcurrentHashMap<>();
    private final Map<String, Map<Person, Long>> authorsPerFile = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> numberOfAuthorsPerFile = new ConcurrentHashMap<>();
    private final Map<String, Map<Integer, AtomicLong>> lineCountTimeFile = new ConcurrentHashMap<>();
    private final Map<String, Integer> fileSize = new ConcurrentHashMap<>();
    private final Map<String, Map<Person, Set<Pair<Long, String>>>> commitStoreByFilePerson = new ConcurrentHashMap<>();
    private final NavigableSet<Pair<Integer, String>> commitSet = new TreeSet<>(new Comparator<Pair<Integer, String>>() {
        @Override
        public int compare(Pair<Integer, String> pair, Pair<Integer, String> otherPair) {
            return pair.getLeft().compareTo(otherPair.getLeft());
        }
    });
    private final AtomicInteger totalCount = new AtomicInteger(0);

    public void increment(RevCommit revCommit, String filePathStr) {
        AtomicInteger count = prodLineCountByCommit.get(revCommit);
        if (count == null) {
            count = new AtomicInteger();
            prodLineCountByCommit.put(revCommit, count);
        }
        count.incrementAndGet();
        Person person = Person.fromIdent(revCommit.getAuthorIdent());
        increment(person);
        incrementFileCount(person, filePathStr);
        incrementCommitTimeCount(revCommit.getCommitTime(), person);
        incrementCommitTimeCount(revCommit.getCommitTime());
        incrementCommitTimeFileCount(revCommit.getCommitTime(), filePathStr);
        incrementAuthorsPerFile(filePathStr, person);
        persistCommitRef(filePathStr, person, revCommit);
        totalCount.getAndIncrement();
    }

    private void persistCommitRef(String filePathStr, Person person, RevCommit revCommit) {
//        Map<Person, List<String>> map = commitStoreByFilePerson.get(filePathStr);
//        if (map == null) {
//            map = new HashMap<>(1);
//        }
        Map<Person, Set<Pair<Long, String>>> map = commitStoreByFilePerson.merge(filePathStr, new HashMap<>(1), new BiFunction<Map<Person, Set<Pair<Long, String>>>, Map<Person, Set<Pair<Long, String>>>, Map<Person, Set<Pair<Long, String>>>>() {
            @Override
            public Map<Person, Set<Pair<Long, String>>> apply(Map<Person, Set<Pair<Long, String>>> existing, Map<Person, Set<Pair<Long, String>>> empty) {

                return existing == null ? empty : existing;
            }
        });

        Set<Pair<Long, String>> set = new HashSet<>();
        set.add(new Pair(revCommit.getCommitTime(), revCommit.getName()));

        map.merge(person, set, new BiFunction<Set<Pair<Long, String>>, Set<Pair<Long, String>>, Set<Pair<Long, String>>>() {
            @Override
            public Set<Pair<Long, String>> apply(Set<Pair<Long, String>> current, Set<Pair<Long, String>> additional) {
                if (current == null) {
                    return additional;

                }
                current.addAll(additional);
                return current;
            }
        });

        commitSet.add(new Pair<>(revCommit.getCommitTime(), revCommit.getName()));
    }

    private void incrementAuthorsPerFile(String filePathStr, Person person) {
        Map<Person, Long> personWithOneCount = new HashMap<>();
        personWithOneCount.put(person, 1L);
        authorsPerFile.merge(filePathStr, personWithOneCount, new BiFunction<Map<Person, Long>, Map<Person, Long>, Map<Person, Long>>() {
            @Override
            public Map<Person, Long> apply(Map<Person, Long> personLongMap, Map<Person, Long> personWithOneCount) {
                Long count = personLongMap.get(person);
                if (count == null) {
                    count = 1L;
                } else {
                    count++;
                }
                personLongMap.put(person, count);
                return personLongMap;
            }
        });

        Map<Person, Long> countMap = authorsPerFile.get(filePathStr);
        numberOfAuthorsPerFile.put(filePathStr, new AtomicInteger(countMap.keySet().size()));
    }

    private void increment(Person person) {
        AtomicLong count = prodLineCountByPerson.get(person);
        if (count == null) {
            count = new AtomicLong();
            prodLineCountByPerson.put(person, count);
        }
        count.incrementAndGet();
    }

    private void incrementCommitTimeCount(int commitTime) {
        AtomicInteger count = lineCountByTimeAll.get(commitTime);
        if (count == null) {
            count = new AtomicInteger();
            lineCountByTimeAll.put(commitTime, count);
        }
        count.incrementAndGet();
    }

    private void incrementCommitTimeCount(int commitTime, Person person) {
        Map<Integer, Integer> timeMap = prodLineCountByTime.get(person);
        if (timeMap == null) {
            timeMap = new ConcurrentHashMap<>();
            prodLineCountByTime.put(person, timeMap);
        }
        Integer count = timeMap.get(commitTime);
        if (count == null) {
            count = 0;
        }
        count++;
        timeMap.put(commitTime, count);
        prodLineCountByTime.put(person, timeMap);
    }

    private void incrementCommitTimeFileCount(int commitTime, String filename) {
        Map<Integer, AtomicLong> oneCount = new ConcurrentHashMap<>();
        oneCount.put(commitTime, new AtomicLong(1L));

        lineCountTimeFile.merge(filename, oneCount, new BiFunction<Map<Integer, AtomicLong>, Map<Integer, AtomicLong>, Map<Integer, AtomicLong>>() {
            @Override
            public Map<Integer, AtomicLong> apply(Map<Integer, AtomicLong> currentMap, Map<Integer, AtomicLong> oneCountMap) {
                AtomicLong count = currentMap.get(commitTime);
                if (count == null) {
                    count = new AtomicLong(1L);
                } else {
                    count.getAndIncrement();
                }

                currentMap.put(commitTime, count);
                return currentMap;
            }
        });
    }

    private void incrementFileCount(Person person, String filePathStr) {
        Map<String, Integer> fileLineCountMap = lineCountByPersonAndFile.get(person);
        if (fileLineCountMap == null) {
            fileLineCountMap = new ConcurrentHashMap<>();
        }
        Integer count = fileLineCountMap.get(filePathStr);
        if (count == null) {
            count = 0;
        }
        count++;
        fileLineCountMap.put(filePathStr, count);
        lineCountByPersonAndFile.put(person, fileLineCountMap);
    }


    public Map<Person, AtomicLong> getProdLineCountByPerson() {
        return prodLineCountByPerson;

    }

    public Map<String, AtomicInteger> getNumberOfAuthorsPerFile() {

        return numberOfAuthorsPerFile;
    }

    public Map<Integer, AtomicInteger> getLineCountByTimeAll() {
        return lineCountByTimeAll;
    }

    public Map<Person, Map<String, Integer>> getLineCountByPersonAndFile() {
        return lineCountByPersonAndFile;
    }

    public Map<Person, Map<Integer, Integer>> getProdLineCountByTime() {
        return prodLineCountByTime;
    }

    public Map<String, Map<Person, Long>> getAuthorsPerFile() {
        return authorsPerFile;
    }

    public Map<String, Map<Integer, AtomicLong>> getLineCountTimeFile() {
        return lineCountTimeFile;
    }

    public Map<String, Map<Person, Set<Pair<Long, String>>>> getCommitStoreByFilePerson() {
        return commitStoreByFilePerson;
    }

    public NavigableSet<Pair<Integer, String>> getCommitSet() {
        return commitSet;
    }

    public void setFileSize(String filePathStr, int numberOfLinesInSourceFile) {
        fileSize.put(filePathStr, numberOfLinesInSourceFile);
    }

    public Map<String, Integer> getFileSize() {
        return new HashMap<>(fileSize);
    }
}


