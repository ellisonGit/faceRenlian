package main.java.com.ha.service;


import main.java.com.ha.pojo.ClockWhiteCard;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:43
 * Modified:
 */
public interface ClockWhiteCardService {
    int insertClockWhite(ClockWhiteCard clockWhiteCard);

    int delClockWhite(ClockWhiteCard clockWhiteCard);

    List<ClockWhiteCard> selectIsexist(ClockWhiteCard clockWhiteCard);

}
