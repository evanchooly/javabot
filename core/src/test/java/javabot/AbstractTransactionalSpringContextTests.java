/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javabot;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Convenient superclass for tests that should occur in a transaction, but normally
 * will roll the transaction back on the completion of each test.
 *
 * <p>This is useful in a range of circumstances, allowing the following benefits:
 * <ul>
 * <li>Ability to delete or insert any data in the database, without affecting other tests
 * <li>Providing a transactional context for any code requiring a transaction
 * <li>Ability to write anything to the database without any need to clean up.
 * </ul>
 *
 * <p>This class is typically very fast, compared to traditional setup/teardown scripts.
 *
 * <p>If data should be left in the database, call the <code>setComplete()</code>
 * method in each test. The "defaultRollback" property, which defaults to "true",
 * determines whether transactions will complete by default.
 *
 * <p>It is even possible to end the transaction early; for example, to verify lazy
 * loading behavior of an O/R mapping tool. (This is a valuable away to avoid
 * unexpected errors when testing a web UI, for example.)  Simply call the
 * <code>endTransaction()</code> method. Execution will then occur without a
 * transactional context.
 *
 * <p>The <code>startNewTransaction()</code> method may be called after a call to
 * <code>endTransaction()</code> if you wish to create a new transaction, quite
 * independent of the old transaction. The new transaction's default fate will be to
 * roll back, unless <code>setComplete()</code> is called again during the scope of the
 * new transaction. Any number of transactions may be created and ended in this way.
 * The final transaction will automatically be rolled back when the test case is
 * torn down.
 *
 * <p>Transactional behavior requires a single bean in the context implementing the
 * PlatformTransactionManager interface. This will be set by the superclass's
 * Dependency Injection mechanism. If using the superclass's Field Injection mechanism,
 * the implementation should be named "transactionManager". This mechanism allows the
 * use of this superclass even when there's more than one transaction manager in the context.
 *
 * <p><i>This superclass can also be used without transaction management, if no
 * PlatformTransactionManager bean is found in the context provided. Be careful about
 * using this mode, as it allows the potential to permanently modify data.
 * This mode is available only if dependency checking is turned off in
 * the AbstractDependencyInjectionSpringContextTests superclass. The non-transactional
 * capability is provided to enable use of the same subclass in different environments.</i>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.1.1
 */
public abstract class AbstractTransactionalSpringContextTests extends AbstractDependencyInjectionSpringContextTests {

	/** The transaction manager to use */
	protected PlatformTransactionManager transactionManager;

	/** Should we roll back by default? */
	private boolean defaultRollback = true;

	/** Should we commit the current transaction? */
	private boolean complete = false;

	/** Number of transactions started */
	private int transactionsStarted = 0;

	/**
	 * Default transaction definition is used.
	 * Subclasses can change this to cause different behaviour.
	 */
	private TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();

	/**
	 * TransactionStatus for this test. Typical subclasses won't need to use it.
	 */
	protected TransactionStatus transactionStatus;


	/**
	 * Default constructor for AbstractTransactionalSpringContextTests.
	 */
	public AbstractTransactionalSpringContextTests() {
	}

	/**
	 * Specify the transaction manager to use. No transaction management will be available
	 * if this is not set. (This mode works only if dependency checking is turned off in
	 * the AbstractDependencyInjectionSpringContextTests superclass.)
	 * <p>Populated through dependency injection by the superclass.
	 */
	public void setTransactionManager(PlatformTransactionManager ptm) {
		transactionManager = ptm;
	}

	/**
	 * Subclasses can set this value in their constructor to change
	 * default, which is always to roll the transaction back.
	 */
	public void setDefaultRollback(boolean rollback) {
		defaultRollback = rollback;
	}


	/**
	 * Call in an overridden <code>runBare()</code> method to prevent transactional execution.
	 */
	protected void preventTransaction() {
		transactionDefinition = null;
	}

	/**
	 * Override the transaction attributes that will be used.
	 * Call in an overridden <code>runBare()</code> method so that
	 * <code>setUp()</code> and <code>tearDown()</code> behavior is modified.
	 * @param customDefinition custom definition to override with
	 */
	protected void setTransactionDefinition(TransactionDefinition customDefinition) {
		transactionDefinition = customDefinition;
	}


	/**
	 * This implementation creates a transaction before test execution.
	 * Override <code>onSetUpBeforeTransaction()</code> and/or
	 * <code>onSetUpInTransaction()</code> to add custom set-up behavior.
	 * @see #onSetUpBeforeTransaction()
	 * @see #onSetUpInTransaction()
	 */
	@Override
    protected final void onSetUp() throws Exception {
		complete = !defaultRollback;

		if (transactionManager == null) {
			logger.info("No transaction manager set: test will NOT run within a transaction");
		}
		else if (transactionDefinition == null) {
			logger.info("No transaction definition set: test will NOT run within a transaction");
		}
		else {
			onSetUpBeforeTransaction();
			startNewTransaction();
			try {
				onSetUpInTransaction();
			}
			catch (Exception ex) {
				endTransaction();
				throw ex;
			}
		}
	}

	/**
	 * Subclasses can override this method to perform any setup operations,
	 * such as populating a database table, <i>before</i> the transaction
	 * created by this class. Only invoked if there <i>is</i> a transaction:
	 * that is, if <code>preventTransaction()</code> has not been invoked in
	 * an overridden <code>runTest()</code> method.
	 * @throws Exception simply let any exception propagate
	 * @see #preventTransaction()
	 */
	protected void onSetUpBeforeTransaction() throws Exception {
	}

	/**
	 * Subclasses can override this method to perform any setup operations,
	 * such as populating a database table, <i>within</i> the transaction
	 * created by this class.
	 * <p><b>NB:</b> Not called if there is no transaction management, due to no
	 * transaction manager being provided in the context.
	 * <p>If any {@link Throwable} is thrown, the transaction that has been started
	 * prior to the execution of this method will be {@link #endTransaction() ended}
	 * (or rather an attempt will be made to {@link #endTransaction() end it gracefully});
	 * The offending {@link Throwable} will then be rethrown.
	 * @throws Exception simply let any exception propagate
	 */
	protected void onSetUpInTransaction() throws Exception {
	}


	/**
	 * This implementation ends the transaction after test execution.
	 * Override <code>onTearDownInTransaction()</code> and/or
	 * <code>onTearDownAfterTransaction()</code> to add custom tear-down behavior.
	 * <p>Note that <code>onTearDownInTransaction()</code> will only be called
	 * if a transaction is still active at the time of the test shutdown.
	 * In particular, it will <code>not</code> be called if the transaction has
	 * been completed with an explicit <code>endTransaction()</code> call before.
	 * @throws Exception simply let any exception propagate
	 * @see #onTearDownInTransaction()
	 * @see #onTearDownAfterTransaction()
	 * @see #endTransaction()
	 */
	@Override
    protected final void onTearDown() throws Exception {
		// Call onTearDownInTransaction and end transaction if the transaction is still active.
		if (transactionStatus != null && !transactionStatus.isCompleted()) {
			try {
				onTearDownInTransaction();
			}
			finally {
				endTransaction();
			}
		}
		// Call onTearDownAfterTransaction if there was at least one transaction,
		// even if it has been completed early through an endTransaction() call.
		if (transactionsStarted > 0) {
			onTearDownAfterTransaction();
		}
	}

	/**
	 * Subclasses can override this method to run invariant tests here.
	 * The transaction is <i>still active</i> at this point, so any changes
	 * made in the transaction will still be visible. However, there is no need
	 * to clean up the database, as a rollback will follow automatically.
	 * <p><b>NB:</b> Not called if there is no actual transaction, for example
	 * due to no transaction manager being provided in the application context.
	 * @throws Exception simply let any exception propagate
	 */
	protected void onTearDownInTransaction() throws Exception {
	}

	/**
	 * Subclasses can override this method to perform cleanup after a transaction
	 * here. At this point, the transaction is <i>not active anymore</i>.
	 * @throws Exception simply let any exception propagate
	 */
	protected void onTearDownAfterTransaction() throws Exception {
	}


	/**
	 * Cause the transaction to commit for this test method,
	 * even if default is set to rollback.
	 * @throws IllegalStateException if the operation cannot be set to
	 * complete as no transaction manager was provided
	 */
	protected void setComplete() throws UnsupportedOperationException {
		if (transactionManager == null) {
			throw new IllegalStateException("No transaction manager set");
		}
		complete = true;
	}

  /**
   * Cause the transaction to rollback for this test method,
   * even if default is set to complete.
   * @throws IllegalStateException if the operation cannot be set to
   * complete as no transaction manager was provided
   */
  protected void setRollback() throws UnsupportedOperationException {
    if (transactionManager == null) {
      throw new IllegalStateException("No transaction manager set");
    }
    complete = false;
  }

  /**
	 * Immediately force a commit or rollback of the transaction,
	 * according to the complete flag.
	 * <p>Can be used to explicitly let the transaction end early,
	 * for example to check whether lazy associations of persistent objects
	 * work outside of a transaction (that is, have been initialized properly).
	 * @see #setComplete()
	 */
	protected void endTransaction() {
		if (transactionStatus != null) {
			try {
				if (!complete) {
					transactionManager.rollback(transactionStatus);
					logger.info("Rolled back transaction after test execution");
				}
				else {
					transactionManager.commit(transactionStatus);
					logger.info("Committed transaction after test execution");
				}
			}
			finally {
				transactionStatus = null;
			}
		}
	}

	/**
	 * Start a new transaction. Only call this method if <code>endTransaction()</code>
	 * has been called. <code>setComplete()</code> can be used again in the new transaction.
	 * The fate of the new transaction, by default, will be the usual rollback.
	 * @see #endTransaction()
	 * @see #setComplete()
	 */
	protected void startNewTransaction() throws TransactionException {
		if (transactionStatus != null) {
			throw new IllegalStateException("Cannot start new transaction without ending existing transaction: " +
					"Invoke endTransaction() before startNewTransaction()");
		}
		if (transactionManager == null) {
			throw new IllegalStateException("No transaction manager set");
		}

		transactionStatus = transactionManager.getTransaction(transactionDefinition);
		++transactionsStarted;
		complete = !defaultRollback;

		if (logger.isInfoEnabled()) {
			logger.info("Began transaction (" + transactionsStarted + "): transaction manager [" +
					transactionManager + "]; default rollback = " + defaultRollback);
		}
	}

}
