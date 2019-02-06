package co.l3co.configuration

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

object Test : IntIdTable() {
    val name = varchar("name", 100)
}

private fun <T> runWithinTransaction(db: Database, block: () -> T): T {
    return transaction(
        transactionIsolation = TransactionManager.manager.defaultIsolationLevel,
        repetitionAttempts = 0,
        db = db
    ) {
        addLogger(Slf4jSqlDebugLogger)
        warnLongQueriesDuration = 2000
//        currentStatement!!.queryTimeout = 60
        block()
    }
}

fun main() {

    val database = Database.connect(hikariDataSource("test"))


    runWithinTransaction(database) {
        SchemaUtils.create(Test)

        Test.insert {
            it[name] = "Test"
        }

        val start = System.currentTimeMillis()

        val conn = TransactionManager.current().connection
        val statement = conn.createStatement()
        val query = "SELECT pg_sleep(50)"
        statement.execute(query)

        val end = System.currentTimeMillis()

        println(end - start)
    }
}