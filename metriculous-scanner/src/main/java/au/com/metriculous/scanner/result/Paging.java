package au.com.metriculous.scanner.result;

import java.util.List;

public interface Paging {

    Integer start();

    Integer end();

    default <T> List<T> getSubList(List<T> resultList) {
        int end = end() > resultList.size() ? resultList.size() : end();
        return resultList.subList(start(), end);
    }

    static Paging create(int start, int end) {
        return new Paging() {
            @Override
            public Integer start() {
                return start;
            }

            @Override
            public Integer end() {
                return end;
            }
        };
    }
}
