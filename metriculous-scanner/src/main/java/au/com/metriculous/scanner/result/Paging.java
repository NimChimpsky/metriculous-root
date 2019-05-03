package au.com.metriculous.scanner.result;

import java.util.List;

public interface Paging {

    Integer start();

    Integer end();

    default <T> List<T> getSubList(List<T> resultList) {
        int end = end() > resultList.size() ? resultList.size() : end();
        return resultList.subList(start(), end);
    }
}
