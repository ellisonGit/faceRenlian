package main.java.com.ha.dao;

import main.java.com.ha.pojo.ClockWhiteCard;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:37
 * Modified:
 */
public interface ClockWhiteCardDao {

int insertClockWhite(ClockWhiteCard clockWhiteCard);

int delClockWhite(ClockWhiteCard clockWhiteCard);

List<ClockWhiteCard> selectIsexist(ClockWhiteCard clockWhiteCard);





}
