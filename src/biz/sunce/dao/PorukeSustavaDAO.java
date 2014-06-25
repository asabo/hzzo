/*
 * Project opticari
 *
 */
package biz.sunce.dao;

/**
 * datum:2006.02.23
 * @author asabo
 *
 */
public interface PorukeSustavaDAO extends DAO
{
public static final String[] RAZINE={"","informacija","upozorenje","nadzor sustava","kritièno"};
public static final int DEFAULT_LOG_LEVEL=1;
public static final int INFO=1;
public static final int WARNING=2;
public static final int DEBUG=3;
public static final int FATAL=4;

}
