package main.java.com.ha.service.impl;

import main.java.com.ha.dao.SyncSignDao;
import main.java.com.ha.pojo.SyncSign;
import main.java.com.ha.service.SyncSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: Ellison
 * Date: 2019-08-17
 * Time: 11:39
 * Modified:
 */
@Service
public class SyncSignServiceImpl implements SyncSignService {

    @Autowired
    private SyncSignDao syncSignDao;

    @Override
    public SyncSign getSyncSign() {
        return this.syncSignDao.getSyncSign();
    }

    @Override
    public SyncSign getSyncSignToo() {
        return this.syncSignDao.getSyncSignToo();
    }

    @Override
    public SyncSign getSyncSignTr() {
        return this.syncSignDao.getSyncSignTr();
    }
    @Override
    public int updateSyncSign(SyncSign syncSign) {
        try{
            this.syncSignDao.updateSyncSign(syncSign);
            return 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }
}
