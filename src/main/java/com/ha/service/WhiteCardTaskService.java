package main.java.com.ha.service;


import main.java.com.ha.pojo.WhiteCardTask;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:43
 * Modified:
 */
public interface WhiteCardTaskService {

    List<WhiteCardTask> selectNoWhite ();

    List<WhiteCardTask> selectNoWhiteDel ();

    List<WhiteCardTask>  selectWhiteDel();

    int updateState( WhiteCardTask whiteCardTask);
}
