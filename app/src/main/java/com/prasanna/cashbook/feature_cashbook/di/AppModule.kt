package com.prasanna.cashbook.feature_cashbook.di

import android.app.Application
import androidx.room.Room
import com.prasanna.cashbook.feature_cashbook.data.data_source.CashbookDatabase
import com.prasanna.cashbook.feature_cashbook.data.repository.CashbookRepositoryImpl
import com.prasanna.cashbook.feature_cashbook.data.repository.TransactionRepositoryImpl
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCashbookDatabase(app: Application): CashbookDatabase{
        return Room.databaseBuilder(
            app,
            CashbookDatabase::class.java,
            CashbookDatabase.CASHBOOK_DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCashbookRepository(db:CashbookDatabase):CashbookRepository{
        return CashbookRepositoryImpl(db.cashbookDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db:CashbookDatabase):TransactionRepository{
        return TransactionRepositoryImpl(db.transactionDao)
    }

    @Provides
    @Singleton
    fun providesCashbookUseCases(repository: CashbookRepository,
                                 tRepository:TransactionRepository):CashbookUseCases{
        return CashbookUseCases(
            addCashbook = AddCashbook(repository),
            getCashbookById = GetCashbookById(repository),
            getCashbooks = GetCashbooks(repository),
            deleteCashbook = DeleteCashbook(repository),
            addTransaction = AddTransaction(tRepository),
            deleteTransaction = DeleteTransaction(tRepository),
            getTransactionByCashbookId = GetTransactionByCashbookId(tRepository),
            getTransactionsInBin = GetTransactionsInBin(tRepository),
            addTransactions = AddTransactions(tRepository)
        )
    }
}