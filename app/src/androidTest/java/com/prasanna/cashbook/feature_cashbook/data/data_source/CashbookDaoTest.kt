package com.prasanna.cashbook.feature_cashbook.data.data_source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.prasanna.cashbook.feature_cashbook.common.MainDispatcherRule
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
class CashbookDaoTest {

    lateinit var db:CashbookDatabase
    lateinit var dao:CashbookDao

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CashbookDatabase::class.java).allowMainThreadQueries().build()
        dao = db.cashbookDao
    }

    @After
    fun tearDown(){
        db.close()
    }


    @Test
    fun addCashbook_addsTwo_returnsSize() = runTest{
        val cashbook = Cashbook(
            name = "TestBook",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )
        val cashbook2 = Cashbook(
            name = "TestBook2",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 2
        )
        dao.addCashbook(cashbook)
        dao.addCashbook(cashbook2)
        dao.getCashbooks().test{
            val result = awaitItem()
            Assert.assertEquals(2, result.size)
            Assert.assertEquals(true, result.contains(cashbook))
            Assert.assertEquals(true, result.contains(cashbook2))
            cancel()
        }

    }

    @Test
    fun getCashbookById_whenIdPassed_returnsCashbook() = runTest{
        val cashbook = Cashbook(
            name = "TestBook",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )
        dao.addCashbook(cashbook)
        val result = dao.getCashbookById(cashbook.id!!)
        Assert.assertEquals(cashbook, result)
    }

    @Test
    fun getCashbookById_whenInvalidId_returnsNull() = runTest {
        val cashbook = Cashbook(
            name = "TestBook",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )
        dao.addCashbook(cashbook)

        val result = dao.getCashbookById(2)
        Assert.assertEquals(null, result)
    }

    @Test
    fun deleteCashbook_whenValidCashbook_deletesCashbook() = runTest {
        val cashbook = Cashbook(
            name = "TestBook",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )
        val cashbook2 = Cashbook(
            name = "TestBook2",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 2
        )
        dao.addCashbook(cashbook)
        dao.addCashbook(cashbook2)

        dao.deleteCashbook(cashbook)

        dao.getCashbooks().test {
            val result = awaitItem()
            Assert.assertEquals(false, result.contains(cashbook))
            Assert.assertEquals(1, result.size)
            cancel()
        }
    }

    @Test
    fun deleteCashbook_invalidCashbook_nothingDeleted() = runTest {
        val cashbook = Cashbook(
            name = "TestBook",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )
        val cashbook2 = Cashbook(
            name = "TestBook2",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 2
        )
        dao.addCashbook(cashbook)
        dao.deleteCashbook(cashbook2)
        dao.getCashbooks().test{
            val result = awaitItem()
            Assert.assertEquals(1, result.size)
            Assert.assertEquals(true, result.contains(cashbook))
            cancel()
        }
    }
}