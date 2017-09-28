package cn.appsys.tools;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
	private static SqlSessionFactory sf = null;

	static {
		try {
			InputStream is = Resources
					.getResourceAsStream("mybatis-config.xml");
			sf = new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * µÃµ½SqlSession
	 * 
	 * @return
	 */
	public static SqlSession createSqlSession() {
		return sf.openSession(false);
	}

	/**
	 * ¹Ø±ÕSqlSession
	 * 
	 * @param sqlSession
	 */
	public static  void closeSqlSession(SqlSession sqlSession) {
		if (null != sqlSession)
			sqlSession.close();
	}
}
