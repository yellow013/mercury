package io.mercury.library.sqlite;

import static io.mercury.common.file.FileUtil.mkdirInHome;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;

public final class SqliteUtil {

	private static final Logger log = CommonLoggerFactory.getLogger(SqliteUtil.class);

	public static final String JdbcProtocol = "jdbc:sqlite:";

	public static final String getSqliteUrlInHome(@Nonnull String dir, @Nonnull String dbName) {
		if (!dbName.endsWith(".db")) {
			dbName = dbName + ".db";
		}
		mkdirInHome(dir);
		return JdbcProtocol + StringUtil.fixPath(SysProperties.USER_HOME) + StringUtil.fixPath(dir) + dbName;
	}

	public static final <T> List<T> query(@Nonnull Connection connection, @Nonnull String sql,
			@Nonnull ResultSetProcessor processor, Class<T> type) throws SQLException {
		Assertor.nonNull(connection, "connection");
		try (// create a database connection
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			return processor.toBeanList(rs, type);
		} catch (SQLException e) {
			log.error("error message -> ", e.getMessage(), e);
			throw e;
		}
	}

	private SqliteUtil() {
	}

	public static void main(String[] args) {

		System.out.println(SqliteUtil.getSqliteUrlInHome("sqlite-file", "example"));

	}

}
