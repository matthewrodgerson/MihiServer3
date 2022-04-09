package com.mjr.mihiserver

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.println
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

object SendToDatabase {
    internal var conn: Connection? = null
//    internal var username = "admin"
//    internal var password = "RFriMZSNyDJeNL22pS57"
    internal var username = "appuser"
    internal var password = "appuserpassword"
    lateinit var resultSet: ResultSet

    fun getConnection() {
        val connectionProperties = Properties()
        connectionProperties.put("user", username)
        connectionProperties.put("password", password)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance()
            conn = DriverManager.getConnection(
//                "jdbc:mysql://localhost/mihi",
                "jdbc:mysql://mihi.cik2jd7ugmvb.eu-west-2.rds.amazonaws.com/mihi",
                connectionProperties
            )
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun writeData(organisation:String, data: String) {
        var stmt: Statement? = conn!!.createStatement()
        val sqlText = "insert into sessions (idSessions, organisation, sessionData) value(0, \"$organisation\", \"$data\");"
        try {
            stmt!!.execute(sqlText)
        } catch (e:Exception) {
            println(e.localizedMessage)
        }
    }

    fun getData(): ResultSet {
        var stmt: Statement? = conn!!.createStatement()
        val sqlText = "select * from sessions;"
        resultSet =
        try {
            stmt!!.executeQuery(sqlText)
        } catch (e:Exception) {
            println(e.localizedMessage)
            stmt = conn!!.createStatement()
            stmt!!.executeQuery("select 1 where false")
        }
        return resultSet

    }
}