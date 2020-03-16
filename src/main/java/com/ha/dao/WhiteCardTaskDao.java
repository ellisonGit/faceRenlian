package main.java.com.ha.dao;

import main.java.com.ha.pojo.WhiteCardTask;

import java.util.List;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-29
 * Time: 14:37
 * Modified:
 */
public interface WhiteCardTaskDao {


    List<WhiteCardTask> selectNoWhite ();

    List<WhiteCardTask> selectNoWhiteDel ();

    List<WhiteCardTask>  selectWhiteDel();

    int updateState( WhiteCardTask whiteCardTask);




}
