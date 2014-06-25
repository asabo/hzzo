/**
 * DbConnectionBroker.
 * @version 1.0.13 3/12/02
 * @author Marc A. Mnich
 */
package biz.sunce.dao;

import java.io.*;
import java.sql.*;
import java.util.Date;


/**
 * DbConnectionBroker
 * A servlet-based broker for database connections.
 * Creates and manages a pool of database connections.
 * @version 1.0.13 3/12/02
 * @author Marc A. Mnich
 */
public class ConnectionBroker implements Runnable {
    private Thread runner;

    private Connection[] connPool;
    private int[] connStatus;

    private long[] connLockTime, connCreateDate;
    private String[] connID;
    private String dbDriver, dbServer, dbLogin, dbPassword, logFileString;
    private int currConnections, connLast, minConns, maxConns, maxConnMSec,
	maxCheckoutSeconds, debugLevel;

    //available: set to false on destroy, checked by getConnection()
    private boolean available=true;

    private PrintWriter log;
    private SQLWarning currSQLWarning;
    private String pid;

    private final int DEFAULTMAXCHECKOUTSECONDS=60;
    private final int DEFAULTDEBUGLEVEL=2;

    private java.io.PrintWriter izlaz=null;

    public static final java.io.OutputStream derbyStream=
        	new OutputStream() {			
    			@Override
    			public void write(int b) throws IOException {
    			}
    		};
    		
    static{
    	 System.setProperty("derby.stream.error.field", "biz.sunce.dao.ConnectionBroker.derbyStream");
    }
    
    /**
     * Creates a new Connection Broker<br>
     * dbDriver:        JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'<br>
     * dbServer:        JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'<br>
     * dbLogin:         Database login name.  e.g. 'Scott'<br>
     * dbPassword:      Database password.    e.g. 'Tiger'<br>
     * minConns:        Minimum number of connections to start with.<br>
     * maxConns:        Maximum number of connections in dynamic pool.<br>
     * logFileString:   Absolute path name for log file. e.g. 'c:/temp/mylog.log' <br>
     * maxConnTime:     Time in days between connection resets. (Reset does a basic cleanup)<br>
     * logAppend:       Append to logfile (optional)<br>
     * maxCheckoutSeconds:       Max time a connection can be checked out before being recycled. Zero value turns option off, default is 60 seconds.
     * debugLevel:      Level of debug messages output to the log file.  0 -> no messages, 1 -> Errors, 2 -> Warnings, 3 -> Information
     */
    public ConnectionBroker(String dbDriver, String dbServer, String dbLogin,
        String dbPassword, int minConns, int maxConns,
            String logFileString, double maxConnTime) throws IOException {

	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns,
		    maxConns, logFileString, maxConnTime, false,
		    DEFAULTMAXCHECKOUTSECONDS, DEFAULTDEBUGLEVEL);
    }

    /*
     * Special constructor to handle logfile append
     */
    public  ConnectionBroker(String dbDriver, String dbServer, String dbLogin,
        String dbPassword, int minConns, int maxConns,
            String logFileString, double maxConnTime, boolean logAppend)
	throws IOException {


	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns,
		    maxConns, logFileString, maxConnTime, logAppend,
		    DEFAULTMAXCHECKOUTSECONDS, DEFAULTDEBUGLEVEL);
    }

    public  ConnectionBroker(String dbDriver, String dbServer, String dbLogin,
    String dbPassword, int minConns, int maxConns,
        String logFileString, double maxConnTime, boolean logAppend, int maxCheckoutSeconds, java.io.PrintWriter izlaz)
    throws IOException {

   this.izlaz=izlaz;

    setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns,
                maxConns, logFileString, maxConnTime, logAppend,
                DEFAULTMAXCHECKOUTSECONDS, DEFAULTDEBUGLEVEL);
     }


    /*
     * Special constructor to handle connection checkout expiration
     */
    public  ConnectionBroker(String dbDriver, String dbServer, String dbLogin,
        String dbPassword, int minConns, int maxConns,
            String logFileString, double maxConnTime, boolean logAppend,
			      int maxCheckoutSeconds, int debugLevel)
	throws IOException {

	setupBroker(dbDriver, dbServer, dbLogin, dbPassword, minConns,
		    maxConns, logFileString, maxConnTime, logAppend,
		    maxCheckoutSeconds, debugLevel);
    }



    private void setupBroker(String dbDriver, String dbServer, String dbLogin,
        String dbPassword, int minConns, int maxConns,
            String logFileString, double maxConnTime, boolean logAppend,
			     int maxCheckoutSeconds, int debugLevel)
    {

        connPool = new Connection[maxConns];
        connStatus = new int[maxConns];
        connLockTime = new long[maxConns];
        connCreateDate = new long[maxConns];
        connID = new String[maxConns];
        currConnections = minConns;
        this.maxConns = maxConns;
        this.dbDriver = dbDriver;
        this.dbServer = dbServer;
        this.dbLogin = dbLogin;
        this.dbPassword = dbPassword;
        this.logFileString = logFileString;
	this.maxCheckoutSeconds = maxCheckoutSeconds;
	this.debugLevel = debugLevel;
        maxConnMSec = (int)(maxConnTime * 86400000.0);  //86400 sec/day
        if(maxConnMSec < 30000) {  // Recycle no less than 30 seconds.
            maxConnMSec = 30000;
        }



	        // Initialize the pool of connections with the mininum connections:
        // Problems creating connections may be caused during reboot when the
        //    servlet is started before the database is ready.  Handle this
        //    by waiting and trying again.  The loop allows 5 minutes for
        //    db reboot.
        boolean connectionsSucceeded=false;
        int dbLoop=20;


        try {
            for(int i=1; i < dbLoop; i++) {
                try {
                    for(int j=0; j < currConnections; j++) {
                        createConn(j);
                    }
                    connectionsSucceeded=true;
                    break;
                } catch (SQLException e){
                  System.out.println("SQL iznimka u brokeru: "+e);
                  e.printStackTrace();
                }
            }
            if(!connectionsSucceeded) { // All attempts at connecting to db exhausted
		if(debugLevel > 0) {

		}
              throw new IOException("Nema spoja na bazu:"+this.dbServer);
            }
        } catch (Exception e) {
          System.out.println("Greska pri setupu brokera: "+e);
          e.printStackTrace();
          this.destroy();
          return;
        }

        // Fire up the background housekeeping thread

        runner = new Thread(this);
        runner.setName("ConnectionBroker");
        runner.start();

    }//End DbConnectionBroker()


    	

    /**
     * Housekeeping thread.  Runs in the background with low CPU overhead.
     * Connections are checked for warnings and closure and are periodically
     * restarted.
     * This thread is a catchall for corrupted
     * connections and prevents the buildup of open cursors. (Open cursors
     * result when the application fails to close a Statement).
     * This method acts as fault tolerance for bad connection/statement programming.
     */
    public void run() {

        boolean forever = true;
        Statement stmt=null;
        String currCatalog=null;
        long maxCheckoutMillis = maxCheckoutSeconds * 1000;
        
       

         while (forever)
         {

	    // Get any Warnings on connections and print to event file
	    for(int i=0; i < currConnections; i++) {
	    	Thread.yield();
		try {
		    currSQLWarning = connPool[i].getWarnings();
		    if(currSQLWarning != null) {
			if(debugLevel > 1) {
			}
			connPool[i].clearWarnings();
		    }
		} catch(SQLException e) {
		    if(debugLevel > 1) {
			//log.println("Cannot access Warnings: " + e);
		    }
		}

	    }

	    for(int i=0; i < currConnections; i++) { // Do for each connection
		long age = System.currentTimeMillis() - connCreateDate[i];


		try {  // Test the connection with createStatement call
		    synchronized(connStatus) {
			if(connStatus[i] > 0) { // In use, catch it next time!

			    // Check the time it's been checked out and recycle
			    long timeInUse = System.currentTimeMillis() -
				connLockTime[i];
			    if(debugLevel > 2) {
			    }
			    if(maxCheckoutMillis != 0) {
				if(timeInUse > maxCheckoutMillis) {
				    if(debugLevel > 1) {
				    }
				    throw new SQLException();
				}
			    }

			    continue;
			}
			connStatus[i] = 2; // Take offline (2 indicates housekeeping lock)
		    }


		    if(age > maxConnMSec) {  // Force a reset at the max conn time
			throw new SQLException();
		    }

		    stmt = connPool[i].createStatement();
		    connStatus[i] = 0;  // Connection is O.K.
		    //log.println("Connection confirmed for conn = " +
		    //             String.valueOf(i));

		    // Some DBs return an object even if DB is shut down
		    if(connPool[i].isClosed()) {
			throw new SQLException();
		    }


		    // Connection has a problem, restart it
		} catch(SQLException e) {

		    if(debugLevel > 1) {
		    }

		    try {
			connPool[i].close();
		    } catch(SQLException e0) {
                      System.out.println("SQL greska: "+e0);
                       e0.printStackTrace();
		    }

		    try {
			createConn(i);
		    } catch(SQLException e1) {

                       System.out.println("SQL greska: "+e1);
                       e1.printStackTrace();


			connStatus[i] = 0;  // Can't open, try again next time
		    }
		} finally {
		    try{if(stmt != null) {stmt.close();}} catch(SQLException e1){};
		}

	    }
	    Thread.yield();
	    try { Thread.sleep(30000); }  // Wait 20 seconds for next cycle

	    catch(InterruptedException e) {
		// Returning from the run method sets the internal
		// flag referenced by Thread.isAlive() to false.
		// This is required because we don't use stop() to
		// shutdown this thread.
		return;
	    }

         } // while forever

    } // End run

    /**
     * This method hands out the connections in round-robin order.
     * This prevents a faulty connection from locking
     * up an application entirely.  A browser 'refresh' will
     * get the next connection while the faulty
     * connection is cleaned up by the housekeeping thread.
     *
     * If the min number of threads are ever exhausted, new
     * threads are added up the the max thread count.
     * Finally, if all threads are in use, this method waits
     * 2 seconds and tries again, up to ten times.  After that, it
     * returns a null.
     */
    public Connection getConnection() {

        Connection conn=null;

        if(available){
            boolean gotOne = false;

            for(int outerloop=1; outerloop<=10; outerloop++) {

                try  {
                    int loop=0;
                    int roundRobin = connLast + 1;
                    if(roundRobin >= currConnections) roundRobin=0;

                    do {
                         synchronized(connStatus) {
                            if((connStatus[roundRobin] < 1) &&
			       (! connPool[roundRobin].isClosed())) {
                                    conn = connPool[roundRobin];
                                    connStatus[roundRobin]=1;
                                    connLockTime[roundRobin] =
					System.currentTimeMillis();
                                    connLast = roundRobin;
                                    gotOne = true;
                                    break;
                            } else {
                                loop++;
                                roundRobin++;
                                if(roundRobin >= currConnections) roundRobin=0;
                            }
                         }
                    }
                    while((gotOne==false)&&(loop < currConnections));

                }
                catch (SQLException e1) {
		}

                if(gotOne) {
                    break;
                } else {
                    synchronized(this) {  // Add new connections to the pool
                        if(currConnections < maxConns) {

                            try {
                                createConn(currConnections);
				currConnections++;
                          } catch(SQLException e) {
                            System.out.println("SQL greska: (linija 369)"+e);
                            e.printStackTrace();
                            }
                        }
                    }

                    try { Thread.sleep(20); } // treba odrediti vrijeme spavanja preciznije
                    catch(InterruptedException e) {
                      System.out.println("Intrerrupt: (linija 377)" + e);
                      e.printStackTrace();
                    }

                }

            } // End of try 10 times loop

        } else {
	    if(debugLevel > 0) {

	    }
        } // End if(available)

	if(debugLevel > 2) {
	}

        return conn;

    }

    /**
     * Returns the local JDBC ID for a connection.
     */
    public int idOfConnection(Connection conn) {
        int match;
        String tag;

        try {
            tag = conn.toString();
        }
        catch (NullPointerException e1) {
            tag = "none";
        }

        match=-1;

        for(int i=0; i< currConnections; i++) {
            if(connID[i].equals(tag)) {
                match = i;
                break;
            }
        }
        return match;
    }

    /**
     * Frees a connection.  Replaces connection back into the main pool for
     * reuse.
     */
    public String freeConnection(Connection conn) {
        String res="";

        int thisconn = idOfConnection(conn);
        if(thisconn >= 0) {
            connStatus[thisconn]=0;
            res = "freed " + conn.toString();
            //log.println("Freed connection " + String.valueOf(thisconn) +
            //            " normal exit: ");
        } else {
	    if(debugLevel > 0) {

	    }
        }

        return res;

    }

    /**
     * Returns the age of a connection -- the time since it was handed out to
     * an application.
     */
    public long getAge(Connection conn) { // Returns the age of the connection in millisec.
        int thisconn = idOfConnection(conn);
        return System.currentTimeMillis() - connLockTime[thisconn];
    }

    private void createConn(int i)

        throws SQLException {

        Date now = new Date();

        try {

              Class.forName(dbDriver).newInstance();

            connPool[i] = DriverManager.getConnection
                          (dbServer,dbLogin,dbPassword);

            connStatus[i]=0;
            connID[i]=connPool[i].toString();
            connLockTime[i]=0;
            connCreateDate[i] =  now.getTime();


        } catch (ClassNotFoundException e2) {

            System.out.println("GRESKA! ClassNotFound Exception: "+e2);
              e2.printStackTrace();
	}
        catch (InstantiationException ex) {
          System.out.println("GRESKA! InstantiationException: "+ex);
          ex.printStackTrace();

           }
           catch (IllegalAccessException ex) {
             System.out.println("GRESKA! IllegalAccessException: "+ex);
          ex.printStackTrace();
           }
    } // createConn

    /**
     * Shuts down the housekeeping thread and closes all connections
     * in the pool. Call this method from the destroy() method of the servlet.
     */

    /**
     * Multi-phase shutdown.  having following sequence:
     * <OL>
     * <LI><code>getConnection()</code> will refuse to return connections.
     * <LI>The housekeeping thread is shut down.<br>
     *    Up to the time of <code>millis</code> milliseconds after shutdown of
     *    the housekeeping thread, <code>freeConnection()</code> can still be
     *    called to return used connections.
     * <LI>After <code>millis</code> milliseconds after the shutdown of the
     *    housekeeping thread, all connections in the pool are closed.
     * <LI>If any connections were in use while being closed then a
     *    <code>SQLException</code> is thrown.
     * <LI>The log is closed.
     * </OL><br>
     * Call this method from a servlet destroy() method.
     *
     * @param      millis   the time to wait in milliseconds.
     * @exception  SQLException if connections were in use after
     * <code>millis</code>.
     */
    public void destroy(int millis) throws SQLException {

        // Checking for invalid negative arguments is not necessary,
        // Thread.join() does this already in runner.join().

        // Stop issuing connections
        available=false;

        // Shut down the background housekeeping thread
        if (runner!=null)
        runner.interrupt();

        // Wait until the housekeeping thread has died.
       if (runner!=null)
       try { runner.join(millis); }
        catch(InterruptedException e){} // ignore

        // The housekeeping thread could still be running
        // (e.g. if millis is too small). This case is ignored.
        // At worst, this method will throw an exception with the
	// clear indication that the timeout was too short.

        long startTime=System.currentTimeMillis();

        // Wait for freeConnection() to return any connections
        // that are still used at this time.
        int useCount;
        while((useCount=getUseCount())>0 && System.currentTimeMillis() - startTime <=  millis) {
            try { Thread.sleep(500); }
            catch(InterruptedException e) {} // ignore
        }

        // Close all connections, whether safe or not
        for(int i=0; i < currConnections; i++) {
            try {
                if (connPool[i]!=null)
                 connPool[i].close();
            } catch (SQLException e1) {
		if(debugLevel > 0) {

		}
            }
        }

        if(useCount > 0) {
            //bt-test successful
            String msg="Unsafe shutdown: Had to close "+useCount+
		" active DB connections after "+millis+"ms";

            // Close all open files

            // Throwing following Exception is essential because servlet authors
            // are likely to have their own error logging requirements.
            throw new SQLException(msg);
        }

        // Close all open files


    }//End destroy()


    /**
     * Less safe shutdown.  Uses default timeout value.
     * This method simply calls the <code>destroy()</code> method
     * with a <code>millis</code>
     * value of 10000 (10 seconds) and ignores <code>SQLException</code>
     * thrown by that method.
     * @see     #destroy(int)
     */
    public void destroy() {
        try {
            destroy(10000);
        }
        catch(SQLException e) {}
        catch(Error e){}
    }



    /**
     * Returns the number of connections in use.
     */
    // This method could be reduced to return a counter that is
    // maintained by all methods that update connStatus.
    // However, it is more efficient to do it this way because:
    // Updating the counter would put an additional burden on the most
    // frequently used methods; in comparison, this method is
    // rarely used (although essential).
    public int getUseCount() {
        int useCount=0;
        synchronized(connStatus) {
            for(int i=0; i < currConnections; i++) {
                if(connStatus[i] > 0) { // In use
                    useCount++;
                }
            }
        }
        return useCount;
    }//End getUseCount()

    /**
     * Returns the number of connections in the dynamic pool.
     */
    public int getSize() {
        return currConnections;
    }//End getSize()

}// End class


