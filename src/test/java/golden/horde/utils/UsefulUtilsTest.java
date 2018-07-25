package golden.horde.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsefulUtilsTest {

	@Test
	public void test() {
		assertEquals(UsefulUtils.trim("фис.", "."), "фис");
	}
	
	@Test
	public void testOne() {
		System.out.println(UsefulUtils.checkTypos("великийхан[];'./"));
		System.out.println(UsefulUtils.checkTypos("dtkbrbq[fyхъжэю."));
	}
	
	
}
