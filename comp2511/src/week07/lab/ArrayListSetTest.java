package week07.lab;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ArrayListSetTest {

	@Test
	void ConstructorTest() {
		ArrayListSet<Apple> als = new ArrayListSet<Apple>();
		assertTrue(als != null);
		Set<Apple> s = new ArrayListSet<Apple>(als);
		assertTrue(s != null);
	}
	@Test
	void SizeChangingTest() {
		Set<Apple> s = new ArrayListSet<Apple>();
		
		Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("c");
		
		s.add(a0);
		assertTrue(s.size()==1);
		
		s.add(a0);
		assertTrue(s.size() == 1);
		
		s.add(a1);
		assertTrue(s.size() == 2);
		
		s.add(a2);
		assertTrue(s.size() == 3);
		
		s.remove(a1);
		assertTrue(s.size() == 2);
		
		s.remove(a1);
		assertTrue(s.size() == 2);
	}
	
	@Test
	void UnionTest() {
		Set<Apple> s1 = new ArrayListSet<Apple>();
		Set<Apple> s2 = new ArrayListSet<Apple>();
		Set<Apple> s3 = s1.union(s2);
		assertTrue(s3.size() == 0);
		
		Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("c");
		
		s1.add(a0);
		s2.add(a1);
		s2.add(a2);
		
		s3 = s1.union(s2);
		assertTrue(s3.size() == 3);
		
	}
	
	@Test
	void IntersectionTest() {
		Set<Apple> s1 = new ArrayListSet<Apple>();
		Set<Apple> s2 = new ArrayListSet<Apple>();
		Set<Apple> s3 = s1.intersection(s2);
		assertTrue(s3.size() == 0);
		
		Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("b");
		
		s1.add(a0);
		s1.add(a1);
		s2.add(a2);
		
		s3 = s1.intersection(s2);
		assertTrue(s3.size() == 1);
		
	}
	
	@Test
	void ContainTest() {
		Set<Apple> s1 = new ArrayListSet<Apple>();
		Set<Apple> s2 = new ArrayListSet<Apple>();
		
		Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("b");
		Apple a3 = new Apple ("c");
		
		s1.add(a0);
		s1.add(a1);
		s2.add(a2);
		//s2.add(a3);
		
		assertTrue(s1.contains(a0));
		assertTrue(s1.contains(a1));
		assertTrue(s2.contains(a1));
		assertTrue (s2.contains(a2));
		assertFalse(s2.contains(a3));
	}
	
	@Test
	void EqualTest() {
		Set<Apple> s1 = new ArrayListSet<Apple>();
		Set<Apple> s2 = new ArrayListSet<Apple>();
		
		//Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("b");
		Apple a3 = new Apple ("c");
		assertTrue(s1.equals(s2));
		assertFalse(s1.equals(a1));
		
		s1.add(a1);
		s2.add(a2);
		assertTrue(s1.equals(s2));
		
		s2.add(a3);
		assertFalse(s1.equals(s2));
	}
	
	@Test
	void ToStringTest() {
		Set<Apple> s1 = new ArrayListSet<Apple>();
		Set<Apple> s2 = new ArrayListSet<Apple>();
		
		assertTrue(s1.toString().equals("Apples:"));
		
		Apple a0 = new Apple("a");
		Apple a1 = new Apple ("b");
		Apple a2 = new Apple ("b");
		Apple a3 = new Apple ("c");
		
		s1.add(a0);
		assertTrue(s1.toString().equals ("Apples: a"));
		
		s1.add(a1);
		assertTrue(s1.toString().equals("Apples: a b"));
		
		s1.add(a2);
		assertTrue(s1.toString().equals("Apples: a b"));
		
		s1.remove(a2);
		assertTrue(s1.toString().equals("Apples: a"));
		
	}
	
	
}
