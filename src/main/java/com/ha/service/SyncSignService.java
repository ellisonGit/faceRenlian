package main.java.com.ha.service;

import main.java.com.ha.pojo.SyncSign;

/**
 * Description:
 * User: Ellison
 * Date: 2019-08-17
 * Time: 11:39
 * Modified:
 */
public interface SyncSignService {

    SyncSign getSyncSign();
    SyncSign getSyncSignToo();
    SyncSign getSyncSignTr();

    int updateSyncSign(SyncSign syncSign);
}
