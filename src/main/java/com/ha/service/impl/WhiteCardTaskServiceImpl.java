package main.java.com.ha.service.impl;



import main.java.com.ha.dao.WhiteCardTaskDao;
import main.java.com.ha.pojo.WhiteCardTask;
import main.java.com.ha.service.EmployeeService;
import main.java.com.ha.service.WhiteCardTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Description:
 * User: Ellison
 * Date: 2019-11-07
 * Time: 14:47
 * Modified:
 */
@Service
public class WhiteCardTaskServiceImpl implements WhiteCardTaskService {

    @Autowired
    private WhiteCardTaskDao whiteCardTaskDao;
    @Autowired
    private EmployeeService employeeService;



    @Override
    public List<WhiteCardTask> selectNoWhite( ) {
        return whiteCardTaskDao. selectNoWhite();
    }


    @Override
    public List<WhiteCardTask> selectNoWhiteDel( ) {
        return whiteCardTaskDao. selectNoWhiteDel();
    }


    @Override
    public List<WhiteCardTask> selectWhiteDel( ) {
        return whiteCardTaskDao. selectNoWhiteDel();
    }

    @Override
    public     int updateState( WhiteCardTask whiteCardTask){
        return this.whiteCardTaskDao.updateState(whiteCardTask);
    }



}
