package ernest.zamelczyk.koleotask.di

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ernest.zamelczyk.koleotask.StationsDb
import ernest.zamelczyk.koleotask.di.qualifiers.AppPrefs
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideSqlDriver(
        @ApplicationContext context: Context
    ): SqlDriver = AndroidSqliteDriver(
        StationsDb.Schema,
        context,
        "stations.db",
        callback = object : AndroidSqliteDriver.Callback(StationsDb.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
            }
        })

    @Provides
    @Singleton
    fun provideDatabase(
        driver: SqlDriver
    ) = StationsDb(driver)

    @Provides
    @Singleton
    @AppPrefs
    fun provideAppPrefs(
        @ApplicationContext context: Context
    ) = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

}