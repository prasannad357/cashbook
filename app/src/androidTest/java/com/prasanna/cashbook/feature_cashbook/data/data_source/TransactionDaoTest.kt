package com.prasanna.cashbook.feature_cashbook.data.data_source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.prasanna.cashbook.feature_cashbook.common.MainDispatcherRule
import com.prasanna.cashbook.feature_cashbook.domain.model.Transaction
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@SmallTest
class TransactionDaoTest {
    lateinit var db:CashbookDatabase
    lateinit var dao:TransactionDao

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CashbookDatabase::class.java).allowMainThreadQueries().build()
        dao = db.transactionDao
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun addTransaction_returnsTransactions() = runTest {
        val transaction = Transaction(
            id = 1,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        dao.addTransaction(transaction)
        dao.getTransactionsByCashbookId(transaction.id!!).test {
            val result = awaitItem()
            Assert.assertEquals(true, result.contains(transaction))
            Assert.assertEquals(1, result.size)
            cancel()
        }
    }

    @Test
    fun addTransactions_returnsTransactions() = runTest {
        val transaction = Transaction(
            id = 1,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        val transaction2 = Transaction(
            id = 2,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        val transactionsList = listOf(transaction, transaction2)

        dao.addTransactions(transactionsList)
        dao.getTransactionsByCashbookId(1).test{

            val result = awaitItem()
            Assert.assertEquals(transactionsList.size, result.size)
            Assert.assertEquals(true, result.contains(transaction))
            Assert.assertEquals(true, result.contains(transaction2))
            cancel()
        }
    }

    @Test
    fun addTransactions_emptyList_returnsEmptyList() = runTest{
        dao.addTransactions(emptyList())
        dao.getTransactionsByCashbookId(1).test{
            val result = awaitItem()
            Assert.assertEquals(0, result.size)
            cancel()
        }
    }

    @Test
    fun getTransactionsByCashbookId_invalidId_returnsEmptyList() = runTest{
        dao.getTransactionsByCashbookId(1).test{
            val result = awaitItem()
            Assert.assertEquals(0, result.size)
            cancel()
        }
    }

    @Test
    fun deleteTransaction_returnsListAfterDeletion() = runTest{
        val transaction = Transaction(
            id = 1,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        val transaction2 = Transaction(
            id = 2,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        val transactionsList = listOf(transaction, transaction2)

        dao.addTransactions(transactionsList)

        dao.deleteTransaction(transaction)

        dao.getTransactionsByCashbookId(1).test{
            val result = awaitItem()
            Assert.assertEquals(1, result.size)
            Assert.assertEquals(false, result.contains(transaction))
            Assert.assertEquals(true, result.contains(transaction2))
            cancel()
        }
    }

    @Test
    fun getTransactionsInBin_returnsBinTransactions() = runTest{
        val transaction = Transaction(
            id = 1,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = true
        )

        val transaction2 = Transaction(
            id = 2,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = true
        )

        val transaction3 = Transaction(
            id = 3,
            date = LocalDate.now(),
            amount = 0.toBigDecimal(),
            isCredit = true,
            createdAt = 1L,
            updatedAt = 1L,
            cashbookId = 1,
            cashbookName = "cashbook",
            tags = emptyList(),
            remark = "",
            inBin = false
        )

        val transactionsList = listOf(transaction, transaction2, transaction3)

        dao.addTransactions(transactionsList)

        dao.getTransactionsInBin(cashbookId = 1, inBin = true).test {
            val result = awaitItem()
            Assert.assertEquals(2, result.size)
            Assert.assertEquals(true, result.contains(transaction))
            Assert.assertEquals(true, result.contains(transaction))
            Assert.assertEquals(false, result.contains(transaction3))
            cancel()
        }
    }
}