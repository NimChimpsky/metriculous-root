package au.com.metriculous.scanner.api.blame;

public interface BlameApiResult {

    RawResult raw();

    PersonResult people();

    FileResult file();

    TimeResult time();

    CommitResult commit();

}
