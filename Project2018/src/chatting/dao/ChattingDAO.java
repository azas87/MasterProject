package chatting.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import chatting.data.Log;

public class ChattingDAO {
	private static SqlSessionFactory factory = MybaticsConfig.getSqlSessionFactory();
	
	public boolean insertLog(Log l)
	{
		SqlSession session = null;
		
		int cnt = 0;
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			cnt = mapper.insertLog(l);	
			session.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(session != null)
			{
				session.close();
			}
		}
		if(cnt > 0)
		{
			return true;
		}
		
		
		return false;
	}
	
	
	public ArrayList<Log> listLogs()
	{
		
		SqlSession session = null;
		ArrayList<Log> list = null;
				
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			list = mapper.listLog();
			session.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(session != null)
			{
				session.close();
			}
		}
		
		return list;
	}
	
	public ArrayList<Log> findLog(String name) {
		SqlSession session = null;
		ArrayList<Log> result = new ArrayList<>();
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			result = mapper.findLog(name);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( session != null ) session.close();
		}
		
		return result;
	}
	
	public Log getStdNo(String name) {
		SqlSession session = null;
		Log result = null;
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			result = mapper.getStdNo(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( session != null ) session.close();
		}
		
		return result;
	}
	
	public int logCount() {
		SqlSession session = null;
		int result = 0;
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			result = mapper.logCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( session != null ) session.close();
		}
		
		return result;
	}
	
	public Log findClass(String name) {
		SqlSession session = null;
		Log result = null;
		
		try {
			session = factory.openSession();
			ChattingMapper mapper = session.getMapper(ChattingMapper.class);
			result = mapper.findClass(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( session != null ) session.close();
		}
		
		return result;
	}
	
//	
//	
//	public static void main(String[] args) {
//		System.out.println("½ÇÇà");
//		ChattingDAO dao = new ChattingDAO();
//		
//		
//		for(Log l : dao.listLogs())
//		{
//			System.out.println(l);
//		}
//		
//	}
}
