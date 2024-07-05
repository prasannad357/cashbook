package com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.prasanna.cashbook.feature_cashbook.common.MainDispatcherRule
import com.prasanna.cashbook.feature_cashbook.domain.model.Cashbook
import com.prasanna.cashbook.feature_cashbook.domain.repository.CashbookRepository
import com.prasanna.cashbook.feature_cashbook.domain.repository.TransactionRepository
import com.prasanna.cashbook.feature_cashbook.domain.use_case.AddCashbook
import com.prasanna.cashbook.feature_cashbook.domain.use_case.AddTransaction
import com.prasanna.cashbook.feature_cashbook.domain.use_case.AddTransactions
import com.prasanna.cashbook.feature_cashbook.domain.use_case.CashbookUseCases
import com.prasanna.cashbook.feature_cashbook.domain.use_case.DeleteCashbook
import com.prasanna.cashbook.feature_cashbook.domain.use_case.DeleteTransaction
import com.prasanna.cashbook.feature_cashbook.domain.use_case.GetCashbookById
import com.prasanna.cashbook.feature_cashbook.domain.use_case.GetCashbooks
import com.prasanna.cashbook.feature_cashbook.domain.use_case.GetTransactionByCashbookId
import com.prasanna.cashbook.feature_cashbook.domain.use_case.GetTransactionsInBin
import com.prasanna.cashbook.feature_cashbook.presentation.util.CashbookOrder
import com.prasanna.cashbook.feature_cashbook.presentation.util.OrderType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
class CashbookListViewModelTest() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var cashbookUseCases:CashbookUseCases

    @Before
    fun setUp(){
        cashbookUseCases = mockk()
    }

    @After
    fun tearDown(){

    }

    @Test
    fun getCashbooks_returnsCashbooks() = runTest{
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
        coEvery { cashbookUseCases.addCashbook(any()) } returns Unit
        coEvery{
//            cashbookUseCases.getCashbooks(CashbookOrder.LastEdit(OrderType.Descending))
            cashbookUseCases.getCashbooks(any())
        }returns flow{emit(listOf(cashbook, cashbook2))}

        val sut = CashbookListViewModel(cashbookUseCases)

        coVerify { cashbookUseCases.getCashbooks(any()) } //This verifies if the method was called

        Assert.assertEquals(true, sut.state.value.cashbooks.contains(cashbook))
        Assert.assertEquals(true, sut.state.value.cashbooks.contains(cashbook2))
        Assert.assertEquals(2, sut.state.value.cashbooks.size)
    }

    @Test
    fun repeatTransactionsBookPreexist_repeatTransactionsBookNotCreated() = runTest{
        val cashbook = Cashbook(
            name = "Repeat Transactions",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 1
        )

        coEvery {
            cashbookUseCases.getCashbooks(any())
        }returns flow { emit(listOf(cashbook)) }
        val sut = CashbookListViewModel(cashbookUseCases)
        coVerify(exactly = 0) {
            //Add cashbook should be called 0 times
            //if repeatTransactionsBook already exist
            cashbookUseCases.addCashbook(any()) }
    }

    @Test
    fun repeatTransactionsBookDoesntExist_CreatesRepeatTransactionsBook()= runTest{
        val cashbook = Cashbook(
            name = "Cashbook1",
            tags = emptyList(),
            createdDate = LocalDate.now(),
            createdTimeStamp = 1L,
            lastEditTimeStamp = 2L,
            id = 2
        )
        coEvery { cashbookUseCases.getCashbooks(any()) } returns flow {
            emit(listOf(cashbook))
        }
        coEvery { cashbookUseCases.addCashbook(any()) } returns Unit

        val sut = CashbookListViewModel(cashbookUseCases)
        coVerify(exactly = 1) {
            //Repeat cashbook doesn't exist and app is being launched for the first time after installation
            //Creates repeat cashbook. Add cashbook is called only once
            sut.cashbookUseCases.addCashbook(any()) }
    }
}