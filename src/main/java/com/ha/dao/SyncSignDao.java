package main.java.com.ha.dao;

import main.java.com.ha.pojo.SyncSign;

/**
 * Description:
 * User: Ellison
 * Date: 2019-08-17
 * Time: 11:31
 * Modified:
 */
public interface SyncSignDao {

    SyncSign getSyncSign();
    SyncSign getSyncSignToo();
    SyncSign getSyncSignTr();
    void updateSyncSign(SyncSign syncSign);
}
