package main.java.com.ha.service.impl;

import main.java.com.ha.dao.ClockWhiteCardDao;
import main.java.com.ha.pojo.ClockWhiteCard;
import main.java.com.ha.service.ClockWhiteCardService;
import main.java.com.ha.service.EmployeeService;
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
public class ClockWhiteCardServiceImpl implements ClockWhiteCardService {

    @Autowired
    private ClockWhiteCardDao clockWhiteCardDao;


    @Override
    public int insertClockWhite(ClockWhiteCard clockWhiteCard) {
        return this.clockWhiteCardDao.insertClockWhite(clockWhiteCard);
    }

    @Override
    public int delClockWhite(ClockWhiteCard clockWhiteCard) {
        return this.clockWhiteCardDao.delClockWhite(clockWhiteCard);
    }

    @Override
    public List<ClockWhiteCard> selectIsexist(ClockWhiteCard clockWhiteCard) {
        return this.clockWhiteCardDao.selectIsexist(clockWhiteCard);
    }








}
