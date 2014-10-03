package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class EqualsTest extends AbstractOpTest {
 
    @Test
    public void testEquals()
    {

    	 Boolean result = (Boolean) ops.run(EqualsCondition.class, 5.0, 5.0);
    	assertSame(result, true);
    	
    	Boolean result1 = (Boolean) ops.run(EqualsCondition.class, 5.0, 6.0);
    	assertSame(result1, false);
       
    }
}