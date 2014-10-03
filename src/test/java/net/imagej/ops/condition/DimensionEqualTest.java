package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class DimensionEqualTest extends AbstractOpTest {
 
    @Test
    public void testDimensions()
    {
    	ArrayList<Double> testing = new ArrayList<Double>();
    	
    	
    	for(int i = 0; i < 3; i++)
    	{
    		testing.add(3.0);
    	}
    	
    	 Boolean result = (Boolean) ops.run(DimensionEqualCondition.class, 3.0, 1, testing);
         assertSame(result, true);
         
         
         Boolean result2 = (Boolean) ops.run(DimensionEqualCondition.class, 4.0, 1, testing);
         assertSame(result2, false);
         
    }
}