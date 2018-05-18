package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, s1;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	@Test
	public void testSample3(){
		assertEquals(6, b.getHeight());
		assertEquals(3, b.getWidth());
		
		s1 = new Piece(Piece.STICK_STR);
		
		int placing;
		
		boolean cought = false;
		try{
			placing = b.place(s1, 0, 0);
		}catch(RuntimeException e){
			cought = true;
		}
		
		assertTrue(cought);
		
		b.commit();
		
		String boardToStr = b.toString();
		
		placing = b.place(s1, 0, 0);
		
		assertEquals(placing, b.PLACE_BAD);
		b.undo();
		assertEquals(b.toString(), boardToStr);
		
		placing = b.place(s1, -1, -1);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
		
		placing = b.place(s1, 2, 6);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
		
		placing = b.place(s1, 6, 6);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
		
		placing = b.place(s1, 0, -1);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
		
		placing = b.place(s1, -1, 0);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
		
		placing = b.place(s1, 1, 5);
		assertEquals(placing, b.PLACE_OUT_BOUNDS);
		b.undo();
	}
	
	@Test
	public void testSample4(){
		b.commit();
	
		int placing;
		
		placing = b.place(pyr3, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, placing);
		
		int cleared = b.clearRows();
		assertEquals(2, cleared);
		
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(-1, b.getColumnHeight(-1));
		assertEquals(-1, b.getColumnHeight(3));
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(0, b.getRowWidth(4));
		assertEquals(0, b.getRowWidth(5));
		assertEquals(-1, b.getRowWidth(-1));
		assertEquals(-1, b.getRowWidth(6));
		assertEquals(2, b.getMaxHeight());
		
		assertEquals(-1, b.dropHeight(pyr1, -1));
		assertEquals(-1, b.dropHeight(pyr1, 6));
		assertEquals(2, b.dropHeight(pyr1, 0));
		assertEquals(2, b.dropHeight(pyr1, 1));
		assertEquals(0, b.dropHeight(pyr1, 2));
	}
	
	@Test
	public void testSample5(){
		b.commit();
		
		assertTrue(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 0));
		assertTrue(b.getGrid(1, 1));
		assertTrue(b.getGrid(2, 0));
		assertTrue(b.getGrid(-1, 1));
		assertTrue(b.getGrid(1, 7));
		
		assertFalse(b.getGrid(2, 1));
		assertFalse(b.getGrid(2, 2));
		assertFalse(b.getGrid(0, 5));
		assertFalse(b.getGrid(0, 1));
		assertFalse(b.getGrid(0, 2));
	}
	
}
