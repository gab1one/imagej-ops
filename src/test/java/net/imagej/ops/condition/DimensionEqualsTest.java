package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class DimensionEqualsTest extends AbstractOpTest {
 
    @Test
    public void testDimensions()
    {
    	long item = (long) 3;
     Long[] test = {item, item, item};
     
     Boolean result = (Boolean) ops.run(DimensionEqualCondition.class, 2, test, 3.0);
 	assertSame(result, true);

 	
    }
}